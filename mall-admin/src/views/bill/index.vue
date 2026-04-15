<template>
  <div class="bill-page">
    <el-card>
      <el-form :inline="true" class="search-form">
        <el-form-item label="账单编号">
          <el-input v-model="searchForm.billNo" placeholder="请输入账单编号" clearable />
        </el-form-item>
        <el-form-item label="订单编号">
          <el-input v-model="searchForm.orderNo" placeholder="请输入订单编号" clearable />
        </el-form-item>
        <el-form-item label="账单类型">
          <el-select v-model="searchForm.billType" placeholder="请选择类型" clearable>
            <el-option label="收入" :value="1" />
            <el-option label="支出" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="账单状态">
          <el-select v-model="searchForm.billStatus" placeholder="请选择状态" clearable>
            <el-option label="已确认" :value="1" />
            <el-option label="待确认" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchData">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
          <el-button type="success" @click="handleExport">导出Excel</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="billNo" label="账单编号" width="180" />
        <el-table-column prop="orderNo" label="订单编号" width="180" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="amount" label="账单金额" width="120">
          <template #default="{ row }">
            <span :style="{ color: row.billType === 1 ? '#67c23a' : '#f56c6c' }">
              {{ row.billType === 1 ? '+' : '-' }}¥{{ row.amount }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="billType" label="账单类型" width="100">
          <template #default="{ row }">
            <el-tag :type="row.billType === 1 ? 'success' : 'danger'">
              {{ row.billType === 1 ? '收入' : '支出' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="billStatus" label="账单状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.billStatus === 1 ? 'success' : 'warning'">
              {{ row.billStatus === 1 ? '已确认' : '待确认' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="150" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" fixed="right" width="150">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="openDialog(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" title="编辑账单" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="账单金额" prop="amount">
          <el-input-number v-model="form.amount" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="账单类型" prop="billType">
          <el-radio-group v-model="form.billType">
            <el-radio :label="1">收入</el-radio>
            <el-radio :label="2">支出</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="账单状态" prop="billStatus">
          <el-radio-group v-model="form.billStatus">
            <el-radio :label="1">已确认</el-radio>
            <el-radio :label="2">待确认</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getBillList, updateBill, deleteBill, exportBill } from '@/api/bill'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const formRef = ref(null)

const searchForm = reactive({
  billNo: '',
  orderNo: '',
  billType: null,
  billStatus: null
})

const form = reactive({
  id: null,
  amount: 0,
  billType: 1,
  billStatus: 1,
  remark: ''
})

const rules = {
  amount: [{ required: true, message: '请输入账单金额', trigger: 'blur' }],
  billType: [{ required: true, message: '请选择账单类型', trigger: 'change' }],
  billStatus: [{ required: true, message: '请选择账单状态', trigger: 'change' }]
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getBillList(searchForm)
    tableData.value = res.data
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const resetSearch = () => {
  searchForm.billNo = ''
  searchForm.orderNo = ''
  searchForm.billType = null
  searchForm.billStatus = null
  fetchData()
}

const openDialog = (row) => {
  Object.assign(form, {
    id: row.id,
    amount: row.amount,
    billType: row.billType,
    billStatus: row.billStatus,
    remark: row.remark
  })
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await updateBill(form)
        ElMessage.success('更新成功')
        dialogVisible.value = false
        fetchData()
      } catch (error) {
        console.error(error)
      }
    }
  })
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除该账单吗？', '提示', {
      type: 'warning'
    })
    await deleteBill(id)
    ElMessage.success('删除成功')
    fetchData()
  } catch (error) {
    console.error(error)
  }
}

const handleExport = async () => {
  try {
    const res = await exportBill(searchForm)
    const blob = new Blob([res], { type: 'application/vnd.ms-excel' })
    const link = document.createElement('a')
    link.href = URL.createObjectURL(blob)
    link.download = '账单列表.xlsx'
    link.click()
    URL.revokeObjectURL(link.href)
    ElMessage.success('导出成功')
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  fetchData()
})
</script>
