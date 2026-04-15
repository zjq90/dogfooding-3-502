<template>
  <div class="product-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>商品管理</span>
          <el-button type="primary" @click="handleAdd">新增商品</el-button>
        </div>
      </template>

      <el-form :model="queryParams" inline>
        <el-form-item label="商品名称">
          <el-input v-model="queryParams.name" placeholder="请输入商品名称" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="queryParams.categoryId" placeholder="请选择分类" clearable style="width: 150px">
            <el-option v-for="item in categoryList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 150px">
            <el-option label="上架" :value="1" />
            <el-option label="下架" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table v-loading="loading" :data="productList" border stripe>
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column prop="image" label="图片" width="100" align="center">
          <template #default="{ row }">
            <el-image v-if="row.image" :src="row.image" style="width: 50px; height: 50px" fit="cover" />
          </template>
        </el-table-column>
        <el-table-column prop="name" label="商品名称" align="center" />
        <el-table-column prop="categoryName" label="分类" align="center" />
        <el-table-column prop="price" label="价格" align="center" />
        <el-table-column prop="stock" label="库存" align="center" />
        <el-table-column prop="status" label="状态" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '上架' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" align="center" width="180" />
        <el-table-column label="操作" align="center" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button link :type="row.status === 1 ? 'warning' : 'success'" size="small" @click="handleStatus(row)">
              {{ row.status === 1 ? '下架' : '上架' }}
            </el-button>
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择分类" style="width: 100%">
            <el-option v-for="item in categoryList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input v-model.number="form.price" type="number" placeholder="请输入价格" />
        </el-form-item>
        <el-form-item label="原价" prop="originalPrice">
          <el-input v-model.number="form.originalPrice" type="number" placeholder="请输入原价" />
        </el-form-item>
        <el-form-item label="库存" prop="stock">
          <el-input v-model.number="form.stock" type="number" placeholder="请输入库存" />
        </el-form-item>
        <el-form-item label="图片" prop="image">
          <el-upload
            class="avatar-uploader"
            :action="uploadUrl"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
            :before-upload="beforeUpload"
          >
            <el-image v-if="form.image" :src="form.image" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :value="1">上架</el-radio>
            <el-radio :value="0">下架</el-radio>
          </el-radio-group>
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
import { Plus } from '@element-plus/icons-vue'
import request from '@/utils/request'

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增商品')
const formRef = ref(null)
const productList = ref([])
const categoryList = ref([])
const total = ref(0)

const uploadUrl = import.meta.env.VITE_API_BASE_URL + '/api/product/upload'
const uploadHeaders = {
  Authorization: 'Bearer ' + localStorage.getItem('token')
}

const queryParams = reactive({
  name: '',
  categoryId: null,
  status: null,
  page: 1,
  pageSize: 10
})

const form = reactive({
  id: null,
  name: '',
  categoryId: null,
  price: '',
  originalPrice: '',
  stock: '',
  image: '',
  description: '',
  status: 1
})

const rules = {
  name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  price: [{ required: true, message: '请输入价格', trigger: 'blur' }]
}

const handleQuery = async () => {
  loading.value = true
  try {
    const res = await request.get('/api/product/list', { params: queryParams })
    productList.value = res.data.list
    total.value = res.data.total
  } catch (error) {
    productList.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

const loadCategories = async () => {
  try {
    const res = await request.get('/api/product/categories')
    categoryList.value = res.data
  } catch (error) {
    categoryList.value = []
  }
}

const handleReset = () => {
  queryParams.name = ''
  queryParams.categoryId = null
  queryParams.status = null
  queryParams.page = 1
  handleQuery()
}

const handleAdd = () => {
  dialogTitle.value = '新增商品'
  form.id = null
  form.name = ''
  form.categoryId = null
  form.price = ''
  form.originalPrice = ''
  form.stock = ''
  form.image = ''
  form.description = ''
  form.status = 1
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑商品'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleStatus = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要${row.status === 1 ? '下架' : '上架'}该商品吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    const newStatus = row.status === 1 ? 0 : 1
    await request.put(`/api/product/${row.id}/status?status=${newStatus}`)
    ElMessage.success('状态更新成功')
    handleQuery()
  } catch {
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该商品吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete(`/api/product/${row.id}`)
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
          await request.put('/api/product', form)
          ElMessage.success('更新成功')
        } else {
          await request.post('/api/product', form)
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

const handleUploadSuccess = (response) => {
  form.image = response.data
  ElMessage.success('上传成功')
}

const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt10M = file.size / 1024 / 1024 < 10
  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt10M) {
    ElMessage.error('图片大小不能超过 10MB!')
    return false
  }
  return true
}

onMounted(() => {
  loadCategories()
  handleQuery()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  text-align: center;
}

.avatar {
  width: 100px;
  height: 100px;
  display: block;
}
</style>
