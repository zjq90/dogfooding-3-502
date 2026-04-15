<template>
  <div class="order-management">
    <el-card>
      <template #header>
        <span>订单管理</span>
      </template>

      <el-form :model="queryParams" inline>
        <el-form-item label="订单号">
          <el-input v-model="queryParams.orderNo" placeholder="请输入订单号" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="用户名">
          <el-input v-model="queryParams.username" placeholder="请输入用户名" clearable style="width: 150px" />
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select v-model="queryParams.orderStatus" placeholder="请选择状态" clearable style="width: 150px">
            <el-option label="待付款" :value="1" />
            <el-option label="待发货" :value="2" />
            <el-option label="已发货" :value="3" />
            <el-option label="已完成" :value="4" />
            <el-option label="已关闭" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table v-loading="loading" :data="orderList" border stripe>
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column prop="orderNo" label="订单号" align="center" />
        <el-table-column prop="username" label="用户" align="center" />
        <el-table-column prop="totalAmount" label="总金额" align="center" />
        <el-table-column prop="payAmount" label="实付金额" align="center" />
        <el-table-column prop="orderStatus" label="状态" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.orderStatus)">
              {{ getStatusText(row.orderStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="consignee" label="收货人" align="center" />
        <el-table-column prop="phone" label="手机号" align="center" />
        <el-table-column prop="createTime" label="创建时间" align="center" width="180" />
        <el-table-column label="操作" align="center" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleDetail(row)">详情</el-button>
            <el-button v-if="row.orderStatus !== 5" link type="warning" size="small" @click="handleClose(row)">关闭</el-button>
            <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="queryParams.page"
        v-model:page-size="queryParams.pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        background
        @size-change="handleQuery"
        @current-change="handleQuery"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>

    <el-dialog v-model="detailVisible" title="订单详情" width="700px">
      <el-descriptions :column="2" border v-if="orderDetail">
        <el-descriptions-item label="订单号">{{ orderDetail.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">
          <el-tag :type="getStatusType(orderDetail.orderStatus)">
            {{ getStatusText(orderDetail.orderStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="收货人">{{ orderDetail.consignee }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ orderDetail.phone }}</el-descriptions-item>
        <el-descriptions-item label="收货地址">{{ orderDetail.address }}</el-descriptions-item>
        <el-descriptions-item label="总金额">{{ orderDetail.totalAmount }}</el-descriptions-item>
        <el-descriptions-item label="实付金额">{{ orderDetail.payAmount }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ orderDetail.createTime }}</el-descriptions-item>
      </el-descriptions>
      <div v-if="orderDetail?.orderItems?.length > 0" style="margin-top: 20px">
        <h4>商品明细</h4>
        <el-table :data="orderDetail.orderItems" border stripe size="small">
          <el-table-column prop="productName" label="商品名称" />
          <el-table-column prop="price" label="单价" width="100" />
          <el-table-column prop="quantity" label="数量" width="100" />
          <el-table-column prop="totalAmount" label="小计" width="100" />
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const loading = ref(false)
const orderList = ref([])
const total = ref(0)
const detailVisible = ref(false)
const orderDetail = ref(null)

const queryParams = reactive({
  orderNo: '',
  username: '',
  orderStatus: null,
  page: 1,
  pageSize: 10
})

const getStatusType = (status) => {
  const typeMap = { 1: 'warning', 2: 'primary', 3: 'info', 4: 'success', 5: 'danger' }
  return typeMap[status] || 'info'
}

const getStatusText = (status) => {
  const textMap = { 1: '待付款', 2: '待发货', 3: '已发货', 4: '已完成', 5: '已关闭' }
  return textMap[status] || '未知'
}

const handleQuery = async () => {
  loading.value = true
  try {
    const res = await request.get('/api/order/list', { params: queryParams })
    orderList.value = res.data.list
    total.value = res.data.total
  } catch (error) {
    orderList.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  queryParams.orderNo = ''
  queryParams.username = ''
  queryParams.orderStatus = null
  queryParams.page = 1
  handleQuery()
}

const handleDetail = async (row) => {
  try {
    const res = await request.get(`/api/order/${row.id}`)
    orderDetail.value = res.data
    detailVisible.value = true
  } catch (error) {
    ElMessage.error('获取详情失败')
  }
}

const handleClose = async (row) => {
  try {
    await ElMessageBox.confirm('确定要关闭该订单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.put(`/api/order/${row.id}/close`)
    ElMessage.success('订单关闭成功')
    handleQuery()
  } catch {
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该订单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete(`/api/order/${row.id}`)
    ElMessage.success('删除成功')
    handleQuery()
  } catch {
  }
}

onMounted(() => {
  handleQuery()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
