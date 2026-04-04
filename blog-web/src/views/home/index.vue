<template>
  <div class="container-custom">
    <div class="grid grid-cols-1 lg:grid-cols-4 gap-8">
      <!-- 左侧文章列表 -->
      <div class="lg:col-span-3">
        <!-- 分类筛选 -->
        <div class="card mb-6">
          <div class="flex flex-wrap gap-2">
            <button
              @click="selectCategory(null)"
              :class="selectedCategory === null ? 'bg-primary-light dark:bg-primary-dark text-white' : 'bg-gray-100 dark:bg-gray-700'"
              class="px-4 py-1.5 rounded-full text-sm transition-colors"
            >
              全部
            </button>
            <button
              v-for="cat in categories"
              :key="cat.id"
              @click="selectCategory(cat.id)"
              :class="selectedCategory === cat.id ? 'bg-primary-light dark:bg-primary-dark text-white' : 'bg-gray-100 dark:bg-gray-700'"
              class="px-4 py-1.5 rounded-full text-sm transition-colors"
            >
              {{ cat.name }}
            </button>
          </div>
        </div>

        <!-- 文章列表 -->
        <div v-if="loading" class="space-y-6">
          <div v-for="i in 3" :key="i" class="card animate-pulse">
            <div class="h-6 bg-gray-200 dark:bg-gray-700 rounded w-3/4 mb-4"></div>
            <div class="h-4 bg-gray-200 dark:bg-gray-700 rounded w-full mb-2"></div>
            <div class="h-4 bg-gray-200 dark:bg-gray-700 rounded w-2/3"></div>
          </div>
        </div>

        <div v-else class="space-y-6">
          <article v-for="article in articles" :key="article.id" class="card group cursor-pointer" @click="goToArticle(article.id)">
            <!-- 封面图 -->
            <div v-if="article.coverImage" class="mb-4 -mx-6 -mt-6 overflow-hidden rounded-t-lg">
              <img :src="article.coverImage" :alt="article.title" class="w-full h-48 object-cover group-hover:scale-105 transition-transform duration-300" />
            </div>

            <!-- 文章信息 -->
            <div class="flex items-center gap-2 mb-3 text-sm text-gray-500 dark:text-gray-400">
              <span v-if="article.categoryName" class="tag">{{ article.categoryName }}</span>
              <span>{{ formatDate(article.createTime) }}</span>
              <span>·</span>
              <span>{{ article.viewCount }} 阅读</span>
            </div>

            <!-- 标题 -->
            <h2 class="text-xl font-semibold mb-2 group-hover:text-primary-light dark:group-hover:text-primary-dark transition-colors">
              {{ article.title }}
            </h2>

            <!-- 摘要 -->
            <p class="text-gray-600 dark:text-gray-400 line-clamp-2">{{ article.summary || '暂无摘要' }}</p>

            <!-- 标签 -->
            <div v-if="article.tags && article.tags.length" class="flex flex-wrap gap-2 mt-4">
              <span v-for="tag in article.tags" :key="tag.id" class="tag">{{ tag.name }}</span>
            </div>
          </article>

          <!-- 空状态 -->
          <div v-if="articles.length === 0" class="card text-center py-12 text-gray-500">
            暂无文章
          </div>
        </div>

        <!-- 分页 -->
        <div v-if="total > pageSize" class="flex justify-center mt-8">
          <nav class="flex items-center gap-2">
            <button
              @click="changePage(pageNum - 1)"
              :disabled="pageNum === 1"
              class="px-3 py-1 rounded border border-border-light dark:border-border-dark disabled:opacity-50 disabled:cursor-not-allowed hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors"
            >
              上一页
            </button>
            <span class="px-4 py-1 text-sm text-gray-600 dark:text-gray-400">
              {{ pageNum }} / {{ totalPages }}
            </span>
            <button
              @click="changePage(pageNum + 1)"
              :disabled="pageNum >= totalPages"
              class="px-3 py-1 rounded border border-border-light dark:border-border-dark disabled:opacity-50 disabled:cursor-not-allowed hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors"
            >
              下一页
            </button>
          </nav>
        </div>
      </div>

      <!-- 右侧边栏 -->
      <aside class="space-y-6">
        <!-- 热门文章 -->
        <div class="card">
          <h3 class="text-lg font-semibold mb-4 pb-2 border-b border-border-light dark:border-border-dark">热门文章</h3>
          <div v-if="hotLoading" class="space-y-3">
            <div v-for="i in 5" :key="i" class="h-4 bg-gray-200 dark:bg-gray-700 rounded animate-pulse"></div>
          </div>
          <ul v-else class="space-y-3">
            <li v-for="(article, index) in hotArticles" :key="article.id" class="flex items-start gap-3">
              <span class="flex-shrink-0 w-6 h-6 rounded bg-primary-light dark:bg-primary-dark text-white text-sm flex items-center justify-center">
                {{ index + 1 }}
              </span>
              <router-link :to="`/article/${article.id}`" class="text-sm hover:text-primary-light dark:hover:text-primary-dark transition-colors line-clamp-2">
                {{ article.title }}
              </router-link>
            </li>
          </ul>
        </div>

        <!-- 标签云 -->
        <div class="card">
          <h3 class="text-lg font-semibold mb-4 pb-2 border-b border-border-light dark:border-border-dark">标签</h3>
          <div v-if="tagsLoading" class="flex flex-wrap gap-2">
            <div v-for="i in 8" :key="i" class="h-6 w-16 bg-gray-200 dark:bg-gray-700 rounded-full animate-pulse"></div>
          </div>
          <div v-else class="flex flex-wrap gap-2">
            <span v-for="tag in tags" :key="tag.id" class="tag cursor-pointer hover:bg-primary-light dark:hover:bg-primary-dark hover:text-white transition-colors">
              {{ tag.name }}
            </span>
          </div>
        </div>
      </aside>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import dayjs from 'dayjs'
import { getArticleList, getHotArticles } from '@/api/article'
import { getCategoryList } from '@/api/category'
import { getTagList } from '@/api/tag'

const router = useRouter()

const loading = ref(false)
const hotLoading = ref(false)
const tagsLoading = ref(false)

const articles = ref<any[]>([])
const categories = ref<any[]>([])
const hotArticles = ref<any[]>([])
const tags = ref<any[]>([])

const selectedCategory = ref<number | null>(null)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const totalPages = computed(() => Math.ceil(total.value / pageSize.value))

onMounted(() => {
  fetchCategories()
  fetchArticles()
  fetchHotArticles()
  fetchTags()
})

watch(selectedCategory, () => {
  pageNum.value = 1
  fetchArticles()
})

function formatDate(date: string) {
  return dayjs(date).format('YYYY-MM-DD')
}

function selectCategory(id: number | null) {
  selectedCategory.value = id
}

function changePage(page: number) {
  pageNum.value = page
  fetchArticles()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

function goToArticle(id: number) {
  router.push(`/article/${id}`)
}

async function fetchCategories() {
  try {
    const res = await getCategoryList()
    categories.value = res.data
  } catch (error) {
    console.error('获取分类失败', error)
  }
}

async function fetchArticles() {
  loading.value = true
  try {
    const res = await getArticleList({
      categoryId: selectedCategory.value || undefined,
      pageNum: pageNum.value,
      pageSize: pageSize.value
    })
    articles.value = res.data.list
    total.value = res.data.total
  } catch (error) {
    console.error('获取文章失败', error)
  } finally {
    loading.value = false
  }
}

async function fetchHotArticles() {
  hotLoading.value = true
  try {
    const res = await getHotArticles(5)
    hotArticles.value = res.data
  } catch (error) {
    console.error('获取热门文章失败', error)
  } finally {
    hotLoading.value = false
  }
}

async function fetchTags() {
  tagsLoading.value = true
  try {
    const res = await getTagList()
    tags.value = res.data
  } catch (error) {
    console.error('获取标签失败', error)
  } finally {
    tagsLoading.value = false
  }
}
</script>