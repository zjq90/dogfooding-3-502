<template>
  <div class="bill-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>账单管理</span>
          <el-button type="primary" @click="handleExport">导出Excel</el-button>
        </div>
      </template>

      <el-form :model="queryParams" inline>
        <el-form-item label="账单号">
          <el-input v-model="queryParams.billNo" placeholder="请输入账单号" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="订单号">
          <el-input v-model="queryParams.orderNo" placeholder="请输入订单号" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="账单类型">
          <el-select v-model="queryParams.billType" placeholder="请选择类型" clearable style="width: 130px">
            <el-option label="收入" :value="1" />
            <el-option label="支出" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="账单状态">
          <el-select v-model="queryParams.billStatus" placeholder="请选择状态" clearable style="width: 130px">
            <el-option label="待确认" :value="0" />
            <el-option label="已确认" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table v-loading="loading" :data="billList" border stripe>
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column prop="billNo" label="账单号" align="center" />
        <el-table-column prop="orderNo" label="订单号" align="center" />
        <el-table-column prop="username" label="用户" align="center" />
        <el-table-column prop="amount" label="金额" align="center" />
        <el-table-column prop="billType" label="类型" align="center">
          <template #default="{ row }">
            <el-tag :type="row.billType === 1 ? 'success' : 'warning'">
              {{ row.billType === 1 ? '收入' : '支出' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="billStatus" label="状态" align="center">
          <template #default="{ row }">
            <el-tag :type="row.billStatus === 1 ? 'success' : 'warning'">
              {{ row.billStatus === 1 ? '已确认' : '待确认' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" align="center" />
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form ref="formRef" :model="form" label-width="80px">
        <el-form-item label="账单号">
          <el-input v-model="form.billNo" disabled />
        </el-form-item>
        <el-form-item label="订单号">
          <el-input v-model="form.orderNo" />
        </el-form-item>
        <el-form-item label="金额">
          <el-input v-model.number="form.amount" type="number" />
        </el-form-item>
        <el-form-item label="账单类型">
          <el-radio-group v-model="form.billType">
            <el-radio :value="1">收入</el-radio>
            <el-radio :value="2">支出</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="账单状态">
          <el-radio-group v-model="form.billStatus">
            <el-radio :value="0">待确认</el-radio>
            <el-radio :value="1">已确认</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="3" />
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
const dialogTitle = ref('编辑账单')
const formRef = ref(null)
const billList = ref([])
const total = ref(0)

const queryParams = reactive({
  billNo: '',
  orderNo: '',
  billType: null,
  billStatus: null,
  page: 1,
  pageSize: 10
})

const form = reactive({
  id: null,
  billNo: '',
  orderNo: '',
  amount: '',
  billType: 1,
  billStatus: 0,
  remark: ''
})

const handleQuery = async () => {
  loading.value = true
  try {
    const res = await request.get('/api/bill/list', { params: queryParams })
    billList.value = res.data.list
    total.value = res.data.total
  } catch (error) {
    billList.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  queryParams.billNo = ''
  queryParams.orderNo = ''
  queryParams.billType = null
  queryParams.billStatus = null
  queryParams.page = 1
  handleQuery()
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑账单'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该账单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete(`/api/bill/${row.id}`)
    ElMessage.success('删除成功')
    handleQuery()
  } catch {
  }
}

const handleSubmit = async () => {
  submitLoading.value = true
  try {
    await request.put('/api/bill', form)
    ElMessage.success('更新成功')
    dialogVisible.value = false
    handleQuery()
  } finally {
    submitLoading.value = false
  }
}

const handleExport = () => {
  const params = new URLSearchParams()
  if (queryParams.billNo) params.append('billNo', queryParams.billNo)
  if (queryParams.orderNo) params.append('orderNo', queryParams.orderNo)
  if (queryParams.billType !== null) params.append('billType', queryParams.billType)
  if (queryParams.billStatus !== null) params.append('billStatus', queryParams.billStatus)
  
  const token = localStorage.getItem('token')
  const url = import.meta.env.VITE_API_BASE_URL + `/api/bill/export?${params.toString()}`
  
  const link = document.createElement('a')
  link.href = url
  link.target = '_blank'
  link.click()
  ElMessage.success('导出成功')
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
