<template>
  <div class="logistics-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>物流管理</span>
          <el-button type="primary" @click="handleAdd">新增物流</el-button>
        </div>
      </template>

      <el-form :model="queryParams" inline>
        <el-form-item label="订单号">
          <el-input v-model="queryParams.orderNo" placeholder="请输入订单号" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="发货状态">
          <el-select v-model="queryParams.shippingStatus" placeholder="请选择状态" clearable style="width: 150px">
            <el-option label="待发货" :value="0" />
            <el-option label="已发货" :value="1" />
            <el-option label="已收货" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table v-loading="loading" :data="logisticsList" border stripe>
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column prop="orderNo" label="订单号" align="center" />
        <el-table-column prop="shippingCompany" label="快递公司" align="center" />
        <el-table-column prop="shippingNo" label="快递单号" align="center" />
        <el-table-column prop="shippingStatus" label="发货状态" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.shippingStatus)">
              {{ getStatusText(row.shippingStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="trackingInfo" label="物流信息" align="center" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" align="center" width="180" />
        <el-table-column label="操作" align="center" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="550px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="订单号" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="请输入订单号" />
        </el-form-item>
        <el-form-item label="快递公司" prop="shippingCompany">
          <el-input v-model="form.shippingCompany" placeholder="请输入快递公司" />
        </el-form-item>
        <el-form-item label="快递单号" prop="shippingNo">
          <el-input v-model="form.shippingNo" placeholder="请输入快递单号" />
        </el-form-item>
        <el-form-item label="发货状态" prop="shippingStatus">
          <el-radio-group v-model="form.shippingStatus">
            <el-radio :value="0">待发货</el-radio>
            <el-radio :value="1">已发货</el-radio>
            <el-radio :value="2">已收货</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="物流信息" prop="trackingInfo">
          <el-input v-model="form.trackingInfo" type="textarea" :rows="3" placeholder="请输入物流信息" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增物流')
const formRef = ref(null)
const logisticsList = ref([])
const total = ref(0)

const queryParams = reactive({
  orderNo: '',
  shippingStatus: null,
  page: 1,
  pageSize: 10
})

const form = reactive({
  id: null,
  orderNo: '',
  shippingCompany: '',
  shippingNo: '',
  shippingStatus: 0,
  trackingInfo: ''
})

const rules = {
  orderNo: [{ required: true, message: '请输入订单号', trigger: 'blur' }]
}

const getStatusType = (status) => {
  const typeMap = { 0: 'warning', 1: 'primary', 2: 'success' }
  return typeMap[status] || 'info'
}

const getStatusText = (status) => {
  const textMap = { 0: '待发货', 1: '已发货', 2: '已收货' }
  return textMap[status] || '未知'
}

const handleQuery = async () => {
  loading.value = true
  try {
    const res = await request.get('/api/logistics/list', { params: queryParams })
    logisticsList.value = res.data.list
    total.value = res.data.total
  } catch (error) {
    logisticsList.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  queryParams.orderNo = ''
  queryParams.shippingStatus = null
  queryParams.page = 1
  handleQuery()
}

const handleAdd = () => {
  dialogTitle.value = '新增物流'
  form.id = null
  form.orderNo = ''
  form.shippingCompany = ''
  form.shippingNo = ''
  form.shippingStatus = 0
  form.trackingInfo = ''
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑物流'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该物流信息吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete(`/api/logistics/${row.id}`)
    ElMessage.success('删除成功')
    handleQuery()
  } catch {
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (form.id) {
          await request.put('/api/logistics', form)
          ElMessage.success('更新成功')
        } else {
          await request.post('/api/logistics', form)
          ElMessage.success('添加成功')
        }
        dialogVisible.value = false
        handleQuery()
      } finally {
        submitLoading.value = false
      }
    }
  })
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
