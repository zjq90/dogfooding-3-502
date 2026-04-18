<template>
  <div class="order-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>订单管理</span>
        </div>
      </template>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="订单编号">
          <el-input v-model="searchForm.orderNo" placeholder="请输入订单编号" clearable />
        </el-form-item>
        <el-form-item label="用户名">
          <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select v-model="searchForm.orderStatus" placeholder="请选择状态" clearable>
            <el-option label="待付款" :value="1" />
            <el-option label="待发货" :value="2" />
            <el-option label="已发货" :value="3" />
            <el-option label="已完成" :value="4" />
            <el-option label="已关闭" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="orderNo" label="订单编号" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="totalAmount" label="订单金额">
          <template #default="{ row }">
            ¥{{ row.totalAmount }}
          </template>
        </el-table-column>
        <el-table-column prop="payType" label="支付方式">
          <template #default="{ row }">
            {{ row.payType === 1 ? '微信' : row.payType === 2 ? '支付宝' : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="orderStatus" label="订单状态">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.orderStatus)">
              {{ getStatusText(row.orderStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看</el-button>
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleClose(row)" v-if="row.orderStatus !== 5">关闭</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog title="订单详情" v-model="viewDialogVisible" width="700px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单编号">{{ currentOrder.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="用户名">{{ currentOrder.username }}</el-descriptions-item>
        <el-descriptions-item label="订单金额">¥{{ currentOrder.totalAmount }}</el-descriptions-item>
        <el-descriptions-item label="实付金额">¥{{ currentOrder.payAmount }}</el-descriptions-item>
        <el-descriptions-item label="支付方式">
          {{ currentOrder.payType === 1 ? '微信' : currentOrder.payType === 2 ? '支付宝' : '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="订单状态">
          {{ getStatusText(currentOrder.orderStatus) }}
        </el-descriptions-item>
        <el-descriptions-item label="收货人">{{ currentOrder.consignee }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ currentOrder.phone }}</el-descriptions-item>
        <el-descriptions-item label="收货地址" :span="2">{{ currentOrder.address }}</el-descriptions-item>
        <el-descriptions-item label="订单备注" :span="2">{{ currentOrder.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
      
      <h4 style="margin-top: 20px;">订单明细</h4>
      <el-table :data="currentOrder.orderItems" border size="small">
        <el-table-column prop="productName" label="商品名称" />
        <el-table-column prop="price" label="单价">
          <template #default="{ row }">
            ¥{{ row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="quantity" label="数量" />
        <el-table-column prop="totalAmount" label="小计">
          <template #default="{ row }">
            ¥{{ row.totalAmount }}
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <el-dialog title="编辑订单" v-model="editDialogVisible" width="600px">
      <el-form :model="form" ref="formRef" label-width="100px">
        <el-form-item label="收货人">
          <el-input v-model="form.consignee" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="收货地址">
          <el-input v-model="form.address" type="textarea" :rows="2" />
        </el-form-item>
        <el-form-item label="订单备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '../api'

export default {
  name: 'Order',
  setup() {
    const loading = ref(false)
    const tableData = ref([])
    const viewDialogVisible = ref(false)
    const editDialogVisible = ref(false)
    const submitLoading = ref(false)
    const formRef = ref(null)
    const currentOrder = ref({})

    const searchForm = reactive({
      orderNo: '',
      username: '',
      orderStatus: null
    })

    const form = reactive({
      id: null,
      consignee: '',
      phone: '',
      address: '',
      remark: ''
    })

    const getStatusType = (status) => {
      const types = {
        1: 'warning',
        2: 'primary',
        3: 'success',
        4: 'success',
        5: 'info'
      }
      return types[status] || ''
    }

    const getStatusText = (status) => {
      const texts = {
        1: '待付款',
        2: '待发货',
        3: '已发货',
        4: '已完成',
        5: '已关闭'
      }
      return texts[status] || '-'
    }

    const loadData = async () => {
      loading.value = true
      try {
        const res = await api.order.getOrderList(searchForm)
        tableData.value = res.data || []
      } catch (error) {
        console.error(error)
      } finally {
        loading.value = false
      }
    }

    const handleSearch = () => {
      loadData()
    }

    const handleReset = () => {
      searchForm.orderNo = ''
      searchForm.username = ''
      searchForm.orderStatus = null
      loadData()
    }

    const handleView = async (row) => {
      try {
        const res = await api.order.getOrderById(row.id)
        currentOrder.value = res.data || {}
        viewDialogVisible.value = true
      } catch (error) {
        console.error(error)
      }
    }

    const handleEdit = (row) => {
      Object.assign(form, {
        id: row.id,
        consignee: row.consignee,
        phone: row.phone,
        address: row.address,
        remark: row.remark
      })
      editDialogVisible.value = true
    }

    const handleSubmit = async () => {
      submitLoading.value = true
      try {
        await api.order.updateOrder(form)
        ElMessage.success('更新成功')
        editDialogVisible.value = false
        loadData()
      } catch (error) {
        console.error(error)
      } finally {
        submitLoading.value = false
      }
    }

    const handleClose = async (row) => {
      try {
        await ElMessageBox.confirm('确定要关闭该订单吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        await api.order.closeOrder(row.id)
        ElMessage.success('订单关闭成功')
        loadData()
      } catch (error) {
        if (error !== 'cancel') {
          console.error(error)
        }
      }
    }

    const handleDelete = async (row) => {
      try {
        await ElMessageBox.confirm('确定要删除该订单吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        await api.order.deleteOrder(row.id)
        ElMessage.success('删除成功')
        loadData()
      } catch (error) {
        if (error !== 'cancel') {
          console.error(error)
        }
      }
    }

    onMounted(() => {
      loadData()
    })

    return {
      loading,
      tableData,
      searchForm,
      viewDialogVisible,
      editDialogVisible,
      currentOrder,
      form,
      formRef,
      submitLoading,
      getStatusType,
      getStatusText,
      handleSearch,
      handleReset,
      handleView,
      handleEdit,
      handleSubmit,
      handleClose,
      handleDelete
    }
  }
}
</script>

<style scoped>
.order-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}
</style>
