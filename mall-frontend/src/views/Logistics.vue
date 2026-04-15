<template>
  <div class="logistics-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>物流管理</span>
          <el-button type="primary" @click="handleAdd">新增物流</el-button>
        </div>
      </template>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="订单编号">
          <el-input v-model="searchForm.orderNo" placeholder="请输入订单编号" clearable />
        </el-form-item>
        <el-form-item label="物流状态">
          <el-select v-model="searchForm.shippingStatus" placeholder="请选择状态" clearable>
            <el-option label="待发货" :value="1" />
            <el-option label="已发货" :value="2" />
            <el-option label="运输中" :value="3" />
            <el-option label="已签收" :value="4" />
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
        <el-table-column prop="shippingCompany" label="物流公司" />
        <el-table-column prop="shippingNo" label="物流单号" />
        <el-table-column prop="shippingStatus" label="物流状态">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.shippingStatus)">
              {{ getStatusText(row.shippingStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="订单ID" prop="orderId">
          <el-input-number v-model="form.orderId" :min="1" />
        </el-form-item>
        <el-form-item label="订单编号" prop="orderNo">
          <el-input v-model="form.orderNo" />
        </el-form-item>
        <el-form-item label="物流公司" prop="shippingCompany">
          <el-input v-model="form.shippingCompany" />
        </el-form-item>
        <el-form-item label="物流单号" prop="shippingNo">
          <el-input v-model="form.shippingNo" />
        </el-form-item>
        <el-form-item label="物流状态" prop="shippingStatus">
          <el-select v-model="form.shippingStatus">
            <el-option label="待发货" :value="1" />
            <el-option label="已发货" :value="2" />
            <el-option label="运输中" :value="3" />
            <el-option label="已签收" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="跟踪信息" prop="trackingInfo">
          <el-input v-model="form.trackingInfo" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
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
  name: 'Logistics',
  setup() {
    const loading = ref(false)
    const tableData = ref([])
    const dialogVisible = ref(false)
    const dialogTitle = ref('')
    const isEdit = ref(false)
    const submitLoading = ref(false)
    const formRef = ref(null)

    const searchForm = reactive({
      orderNo: '',
      shippingStatus: null
    })

    const form = reactive({
      id: null,
      orderId: null,
      orderNo: '',
      shippingCompany: '',
      shippingNo: '',
      shippingStatus: 1,
      trackingInfo: ''
    })

    const rules = {
      orderId: [{ required: true, message: '请输入订单ID', trigger: 'blur' }],
      orderNo: [{ required: true, message: '请输入订单编号', trigger: 'blur' }],
      shippingCompany: [{ required: true, message: '请输入物流公司', trigger: 'blur' }],
      shippingNo: [{ required: true, message: '请输入物流单号', trigger: 'blur' }]
    }

    const getStatusType = (status) => {
      const types = {
        1: 'info',
        2: 'primary',
        3: 'warning',
        4: 'success'
      }
      return types[status] || ''
    }

    const getStatusText = (status) => {
      const texts = {
        1: '待发货',
        2: '已发货',
        3: '运输中',
        4: '已签收'
      }
      return texts[status] || '-'
    }

    const loadData = async () => {
      loading.value = true
      try {
        const res = await api.logistics.getLogisticsList(searchForm)
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
      searchForm.shippingStatus = null
      loadData()
    }

    const handleAdd = () => {
      isEdit.value = false
      dialogTitle.value = '新增物流'
      Object.assign(form, {
        id: null,
        orderId: null,
        orderNo: '',
        shippingCompany: '',
        shippingNo: '',
        shippingStatus: 1,
        trackingInfo: ''
      })
      dialogVisible.value = true
    }

    const handleEdit = (row) => {
      isEdit.value = true
      dialogTitle.value = '编辑物流'
      Object.assign(form, {
        id: row.id,
        orderId: row.orderId,
        orderNo: row.orderNo,
        shippingCompany: row.shippingCompany,
        shippingNo: row.shippingNo,
        shippingStatus: row.shippingStatus,
        trackingInfo: row.trackingInfo
      })
      dialogVisible.value = true
    }

    const handleSubmit = async () => {
      const valid = await formRef.value.validate().catch(() => false)
      if (!valid) return

      submitLoading.value = true
      try {
        if (isEdit.value) {
          await api.logistics.updateLogistics(form)
          ElMessage.success('更新成功')
        } else {
          await api.logistics.addLogistics(form)
          ElMessage.success('添加成功')
        }
        dialogVisible.value = false
        loadData()
      } catch (error) {
        console.error(error)
      } finally {
        submitLoading.value = false
      }
    }

    const handleDelete = async (row) => {
      try {
        await ElMessageBox.confirm('确定要删除该物流信息吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        await api.logistics.deleteLogistics(row.id)
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
      dialogVisible,
      dialogTitle,
      isEdit,
      form,
      rules,
      formRef,
      submitLoading,
      getStatusType,
      getStatusText,
      handleSearch,
      handleReset,
      handleAdd,
      handleEdit,
      handleSubmit,
      handleDelete
    }
  }
}
</script>

<style scoped>
.logistics-page {
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
