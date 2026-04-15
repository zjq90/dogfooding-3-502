<template>
  <div class="logistics-page">
    <el-card>
      <el-form :inline="true" class="search-form">
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
          <el-button type="primary" @click="fetchData">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
          <el-button type="success" @click="openDialog()">新增物流</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="orderNo" label="订单编号" width="180" />
        <el-table-column prop="shippingCompany" label="物流公司" width="120" />
        <el-table-column prop="shippingNo" label="物流单号" width="180" />
        <el-table-column prop="shippingStatus" label="物流状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.shippingStatus)">
              {{ getStatusText(row.shippingStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="trackingInfo" label="物流信息" min-width="200" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" fixed="right" width="180">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="openDialog(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="订单ID" prop="orderId">
          <el-input v-model.number="form.orderId" placeholder="请输入订单ID" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item label="订单编号" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="请输入订单编号" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item label="物流公司" prop="shippingCompany">
          <el-input v-model="form.shippingCompany" placeholder="请输入物流公司" />
        </el-form-item>
        <el-form-item label="物流单号" prop="shippingNo">
          <el-input v-model="form.shippingNo" placeholder="请输入物流单号" />
        </el-form-item>
        <el-form-item label="物流状态" prop="shippingStatus">
          <el-select v-model="form.shippingStatus" placeholder="请选择状态">
            <el-option label="待发货" :value="1" />
            <el-option label="已发货" :value="2" />
            <el-option label="运输中" :value="3" />
            <el-option label="已签收" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="物流信息">
          <el-input v-model="form.trackingInfo" type="textarea" :rows="3" placeholder="请输入物流信息" />
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
import { getLogisticsList, addLogistics, updateLogistics, deleteLogistics } from '@/api/logistics'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增物流')
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
  shippingNo: [{ required: true, message: '请输入物流单号', trigger: 'blur' }],
  shippingStatus: [{ required: true, message: '请选择物流状态', trigger: 'change' }]
}

const getStatusType = (status) => {
  const types = { 1: 'info', 2: 'primary', 3: 'warning', 4: 'success' }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = { 1: '待发货', 2: '已发货', 3: '运输中', 4: '已签收' }
  return texts[status] || '未知'
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getLogisticsList(searchForm)
    tableData.value = res.data
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const resetSearch = () => {
  searchForm.orderNo = ''
  searchForm.shippingStatus = null
  fetchData()
}

const openDialog = (row = null) => {
  dialogTitle.value = row ? '编辑物流' : '新增物流'
  if (row) {
    Object.assign(form, row)
  } else {
    Object.assign(form, {
      id: null,
      orderId: null,
      orderNo: '',
      shippingCompany: '',
      shippingNo: '',
      shippingStatus: 1,
      trackingInfo: ''
    })
  }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (form.id) {
          await updateLogistics(form)
          ElMessage.success('更新成功')
        } else {
          await addLogistics(form)
          ElMessage.success('添加成功')
        }
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
    await ElMessageBox.confirm('确定要删除该物流信息吗？', '提示', {
      type: 'warning'
    })
    await deleteLogistics(id)
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
