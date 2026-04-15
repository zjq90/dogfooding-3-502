<template>
  <div class="product-page">
    <el-card>
      <el-form :inline="true" class="search-form">
        <el-form-item label="商品名称">
          <el-input v-model="searchForm.name" placeholder="请输入商品名称" clearable />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="searchForm.categoryId" placeholder="请选择分类" clearable>
            <el-option
              v-for="item in categoryList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="上架" :value="1" />
            <el-option label="下架" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchData">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
          <el-button type="success" @click="openDialog()">新增商品</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="image" label="图片" width="100">
          <template #default="{ row }">
            <el-image
              v-if="row.image"
              :src="row.image"
              style="width: 60px; height: 60px;"
              fit="cover"
            />
          </template>
        </el-table-column>
        <el-table-column prop="name" label="商品名称" min-width="150" />
        <el-table-column prop="categoryName" label="分类" width="120" />
        <el-table-column prop="price" label="价格" width="120">
          <template #default="{ row }">
            <span style="color: #f56c6c;">¥{{ row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="originalPrice" label="原价" width="120">
          <template #default="{ row }">
            <span style="text-decoration: line-through; color: #999;">¥{{ row.originalPrice }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" fixed="right" width="180">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="openDialog(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择分类">
            <el-option
              v-for="item in categoryList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number v-model="form.price" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="原价">
          <el-input-number v-model="form.originalPrice" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="库存" prop="stock">
          <el-input-number v-model="form.stock" :min="0" />
        </el-form-item>
        <el-form-item label="商品图片">
          <el-upload
            class="avatar-uploader"
            action="#"
            :show-file-list="false"
            :before-upload="beforeUpload"
            :http-request="handleUpload"
          >
            <el-image v-if="form.image" :src="form.image" style="width: 100px; height: 100px;" fit="cover" />
            <el-icon v-else class="avatar-uploader-icon" size="40"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="商品描述">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入商品描述" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">上架</el-radio>
            <el-radio :label="0">下架</el-radio>
          </el-radio-group>
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
import { getProductList, addProduct, updateProduct, updateProductStatus, deleteProduct, uploadImage } from '@/api/product'
import { getAllCategories } from '@/api/category'

const loading = ref(false)
const tableData = ref([])
const categoryList = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增商品')
const formRef = ref(null)

const searchForm = reactive({
  name: '',
  categoryId: null,
  status: null
})

const form = reactive({
  id: null,
  name: '',
  categoryId: null,
  price: 0,
  originalPrice: 0,
  stock: 0,
  image: '',
  description: '',
  status: 1
})

const rules = {
  name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  price: [{ required: true, message: '请输入价格', trigger: 'blur' }],
  stock: [{ required: true, message: '请输入库存', trigger: 'blur' }]
}

const fetchCategoryList = async () => {
  try {
    const res = await getAllCategories()
    categoryList.value = res.data
  } catch (error) {
    console.error(error)
  }
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getProductList(searchForm)
    tableData.value = res.data
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const resetSearch = () => {
  searchForm.name = ''
  searchForm.categoryId = null
  searchForm.status = null
  fetchData()
}

const openDialog = (row = null) => {
  dialogTitle.value = row ? '编辑商品' : '新增商品'
  if (row) {
    Object.assign(form, row)
  } else {
    Object.assign(form, {
      id: null,
      name: '',
      categoryId: null,
      price: 0,
      originalPrice: 0,
      stock: 0,
      image: '',
      description: '',
      status: 1
    })
  }
  dialogVisible.value = true
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

const handleUpload = async (options) => {
  try {
    const res = await uploadImage(options.file)
    form.image = res.data
    ElMessage.success('上传成功')
  } catch (error) {
    console.error(error)
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (form.id) {
          await updateProduct(form)
          ElMessage.success('更新成功')
        } else {
          await addProduct(form)
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

const handleStatusChange = async (row) => {
  try {
    await updateProductStatus(row.id, row.status)
    ElMessage.success('状态更新成功')
  } catch (error) {
    row.status = row.status === 1 ? 0 : 1
  }
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除该商品吗？', '提示', {
      type: 'warning'
    })
    await deleteProduct(id)
    ElMessage.success('删除成功')
    fetchData()
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  fetchCategoryList()
  fetchData()
})
</script>

<style scoped>
.avatar-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 100px;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-uploader:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  color: #8c939d;
}
</style>
