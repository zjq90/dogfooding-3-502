<template>
  <div class="order-page">
    <el-card>
      <el-form :inline="true" class="search-form">
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
          <el-button type="primary" @click="fetchData">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="orderNo" label="订单编号" width="180" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="totalAmount" label="订单金额" width="120">
          <template #default="{ row }">
            <span style="color: #f56c6c;">¥{{ row.totalAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="payAmount" label="实付金额" width="120">
          <template #default="{ row }">
            <span style="color: #f56c6c;">¥{{ row.payAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="payType" label="支付方式" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.payType === 1" type="success">微信</el-tag>
            <el-tag v-else-if="row.payType === 2" type="primary">支付宝</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="orderStatus" label="订单状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.orderStatus)">
              {{ getStatusText(row.orderStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="consignee" label="收货人" width="100" />
        <el-table-column prop="phone" label="联系电话" width="130" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" fixed="right" width="200">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="viewDetail(row)">详情</el-button>
            <el-button
              v-if="row.orderStatus !== 5"
              type="warning"
              size="small"
              @click="handleClose(row.id)"
            >关闭</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="detailVisible" title="订单详情" width="700px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单编号">{{ orderDetail.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="用户名">{{ orderDetail.username }}</el-descriptions-item>
        <el-descriptions-item label="订单金额">¥{{ orderDetail.totalAmount }}</el-descriptions-item>
        <el-descriptions-item label="实付金额">¥{{ orderDetail.payAmount }}</el-descriptions-item>
        <el-descriptions-item label="支付方式">
          {{ orderDetail.payType === 1 ? '微信' : orderDetail.payType === 2 ? '支付宝' : '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="订单状态">
          <el-tag :type="getStatusType(orderDetail.orderStatus)">
            {{ getStatusText(orderDetail.orderStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="收货人">{{ orderDetail.consignee }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ orderDetail.phone }}</el-descriptions-item>
        <el-descriptions-item label="收货地址" :span="2">{{ orderDetail.address }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ orderDetail.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
      
      <h4 style="margin: 20px 0 10px;">商品明细</h4>
      <el-table :data="orderDetail.orderItems" border>
        <el-table-column prop="productName" label="商品名称" />
        <el-table-column prop="price" label="单价" width="120">
          <template #default="{ row }">¥{{ row.price }}</template>
        </el-table-column>
        <el-table-column prop="quantity" label="数量" width="80" />
        <el-table-column prop="totalAmount" label="小计" width="120">
          <template #default="{ row }">¥{{ row.totalAmount }}</template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getOrderList, getOrderById, closeOrder, deleteOrder } from '@/api/order'

const loading = ref(false)
const tableData = ref([])
const detailVisible = ref(false)
const orderDetail = ref({})

const searchForm = reactive({
  orderNo: '',
  username: '',
  orderStatus: null
})

const getStatusType = (status) => {
  const types = { 1: 'warning', 2: 'primary', 3: 'info', 4: 'success', 5: 'danger' }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = { 1: '待付款', 2: '待发货', 3: '已发货', 4: '已完成', 5: '已关闭' }
  return texts[status] || '未知'
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getOrderList(searchForm)
    tableData.value = res.data
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const resetSearch = () => {
  searchForm.orderNo = ''
  searchForm.username = ''
  searchForm.orderStatus = null
  fetchData()
}

const viewDetail = async (row) => {
  try {
    const res = await getOrderById(row.id)
    orderDetail.value = res.data
    detailVisible.value = true
  } catch (error) {
    console.error(error)
  }
}

const handleClose = async (id) => {
  try {
    await ElMessageBox.confirm('确定要关闭该订单吗？', '提示', {
      type: 'warning'
    })
    await closeOrder(id)
    ElMessage.success('订单已关闭')
    fetchData()
  } catch (error) {
    console.error(error)
  }
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除该订单吗？', '提示', {
      type: 'warning'
    })
    await deleteOrder(id)
    ElMessage.success('删除成功')
    fetchData()
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  fetchData()
})
</script>
