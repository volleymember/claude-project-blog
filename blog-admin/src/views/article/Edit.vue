<template>
  <div class="article-edit">
    <el-card shadow="never">
      <template #header>
        <span>{{ isEdit ? '编辑文章' : '新建文章' }}</span>
      </template>

      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入文章标题" />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="分类" prop="categoryId">
              <el-select v-model="form.categoryId" placeholder="请选择分类" style="width: 100%;">
                <el-option
                  v-for="item in categories"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="标签">
              <el-select v-model="form.tagIds" multiple placeholder="请选择标签" style="width: 100%;">
                <el-option
                  v-for="item in tags"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="摘要">
          <el-input v-model="form.summary" type="textarea" :rows="3" placeholder="文章摘要" />
        </el-form-item>

        <el-form-item label="内容" prop="content">
          <div class="editor-container">
            <Toolbar :editor="editorRef" :defaultConfig="toolbarConfig" mode="default" />
            <Editor
              v-model="form.content"
              :defaultConfig="editorConfig"
              mode="default"
              class="editor"
              @onCreated="handleCreated"
            />
          </div>
        </el-form-item>

        <el-form-item label="封面图">
          <el-input v-model="form.coverImage" placeholder="封面图URL" />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio :label="0">草稿</el-radio>
                <el-radio :label="1">发布</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="置顶">
              <el-switch v-model="form.isTop" :active-value="1" :inactive-value="0" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="SEO关键词">
          <el-input v-model="form.seoKeywords" placeholder="多个关键词用逗号分隔" />
        </el-form-item>

        <el-form-item label="SEO描述">
          <el-input v-model="form.seoDescription" type="textarea" :rows="2" placeholder="SEO描述" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleSubmit">保存</el-button>
          <el-button @click="router.back()">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, onBeforeUnmount, shallowRef } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import '@wangeditor/editor/dist/css/style.css'
import { getArticle, createArticle, updateArticle } from '@/api/article'
import { getCategoryList } from '@/api/category'
import { getTagList } from '@/api/tag'

const router = useRouter()
const route = useRoute()

const formRef = ref()
const loading = ref(false)
const categories = ref<any[]>([])
const tags = ref<any[]>([])
const editorRef = shallowRef()

const isEdit = computed(() => !!route.params.id)

const form = reactive({
  title: '',
  summary: '',
  content: '',
  coverImage: '',
  categoryId: undefined as number | undefined,
  tagIds: [] as number[],
  isTop: 0,
  status: 0,
  seoKeywords: '',
  seoDescription: ''
})

const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
}

const toolbarConfig = {}
const editorConfig = {
  placeholder: '请输入文章内容...',
  MENU_CONF: {
    uploadImage: {
      server: '/api/upload/image',
      fieldName: 'file'
    }
  }
}

function handleCreated(editor: any) {
  editorRef.value = editor
}

onMounted(async () => {
  await Promise.all([fetchCategories(), fetchTags()])
  if (isEdit.value) {
    fetchArticle()
  }
})

onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor) {
    editor.destroy()
  }
})

async function fetchCategories() {
  try {
    const res = await getCategoryList()
    categories.value = res.data
  } catch (error) {
    console.error('获取分类失败', error)
  }
}

async function fetchTags() {
  try {
    const res = await getTagList()
    tags.value = res.data
  } catch (error) {
    console.error('获取标签失败', error)
  }
}

async function fetchArticle() {
  try {
    const res = await getArticle(Number(route.params.id))
    Object.assign(form, res.data)
    form.tagIds = res.data.tags?.map((t: any) => t.id) || []
  } catch (error) {
    console.error('获取文章失败', error)
  }
}

async function handleSubmit() {
  const valid = await formRef.value.validate()
  if (!valid) return

  loading.value = true
  try {
    if (isEdit.value) {
      await updateArticle(Number(route.params.id), form)
    } else {
      await createArticle(form)
    }
    ElMessage.success('保存成功')
    router.push('/article')
  } catch (error) {
    console.error('保存失败', error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.article-edit {
  margin: 20px;

  .editor-container {
    border: 1px solid #ccc;
    width: 100%;

    .editor {
      height: 400px;
      overflow-y: hidden;
    }
  }
}
</style>