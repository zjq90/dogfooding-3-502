<template>
  <div class="bill-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>账单管理</span>
          <el-button type="primary" @click="handleExport">导出Excel</el-button>
        </div>
      </template>

      <el-form :inline="true" :model="searchForm" class="search-form">
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
            <el-option label="待处理" :value="1" />
            <el-option label="已处理" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="billNo" label="账单编号" />
        <el-table-column prop="orderNo" label="订单编号" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="billType" label="账单类型">
          <template #default="{ row }">
            <el-tag :type="row.billType === 1 ? 'success' : 'danger'">
              {{ row.billType === 1 ? '收入' : '支出' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额">
          <template #default="{ row }">
            ¥{{ row.amount }}
          </template>
        </el-table-column>
        <el-table-column prop="billStatus" label="账单状态">
          <template #default="{ row }">
            <el-tag :type="row.billStatus === 1 ? 'warning' : 'success'">
              {{ row.billStatus === 1 ? '待处理' : '已处理' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="billDate" label="账单日期" width="180" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog title="编辑账单" v-model="dialogVisible" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="账单类型" prop="billType">
          <el-radio-group v-model="form.billType">
            <el-radio :label="1">收入</el-radio>
            <el-radio :label="2">支出</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="金额" prop="amount">
          <el-input-number v-model="form.amount" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="账单状态" prop="billStatus">
          <el-radio-group v-model="form.billStatus">
            <el-radio :label="1">待处理</el-radio>
            <el-radio :label="2">已处理</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" />
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
  name: 'Bill',
  setup() {
    const loading = ref(false)
    const tableData = ref([])
    const dialogVisible = ref(false)
    const submitLoading = ref(false)
    const formRef = ref(null)

    const searchForm = reactive({
      billNo: '',
      orderNo: '',
      billType: null,
      billStatus: null
    })

    const form = reactive({
      id: null,
      billType: 1,
      amount: 0,
      billStatus: 1,
      remark: ''
    })

    const rules = {
      billType: [{ required: true, message: '请选择账单类型', trigger: 'change' }],
      amount: [{ required: true, message: '请输入金额', trigger: 'blur' }],
      billStatus: [{ required: true, message: '请选择账单状态', trigger: 'change' }]
    }

    const loadData = async () => {
      loading.value = true
      try {
        const res = await api.bill.getBillList(searchForm)
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
      searchForm.billNo = ''
      searchForm.orderNo = ''
      searchForm.billType = null
      searchForm.billStatus = null
      loadData()
    }

    const handleExport = async () => {
      try {
        const res = await api.bill.exportBills(searchForm)
        const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const link = document.createElement('a')
        link.href = URL.createObjectURL(blob)
        link.download = '账单列表.xlsx'
        link.click()
        ElMessage.success('导出成功')
      } catch (error) {
        console.error(error)
        ElMessage.error('导出失败')
      }
    }

    const handleEdit = (row) => {
      Object.assign(form, {
        id: row.id,
        billType: row.billType,
        amount: row.amount,
        billStatus: row.billStatus,
        remark: row.remark
      })
      dialogVisible.value = true
    }

    const handleSubmit = async () => {
      const valid = await formRef.value.validate().catch(() => false)
      if (!valid) return

      submitLoading.value = true
      try {
        await api.bill.updateBill(form)
        ElMessage.success('更新成功')
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
        await ElMessageBox.confirm('确定要删除该账单吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        await api.bill.deleteBill(row.id)
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
      form,
      rules,
      formRef,
      submitLoading,
      handleSearch,
      handleReset,
      handleExport,
      handleEdit,
      handleSubmit,
      handleDelete
    }
  }
}
</script>

<style scoped>
.bill-page {
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
