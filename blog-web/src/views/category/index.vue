<template>
  <div class="container-custom">
    <div class="grid grid-cols-1 lg:grid-cols-4 gap-8">
      <!-- 文章列表 -->
      <div class="lg:col-span-3">
        <div class="card mb-6">
          <h1 class="text-2xl font-bold">{{ category?.name || '分类' }}</h1>
          <p v-if="category?.description" class="text-gray-600 dark:text-gray-400 mt-2">{{ category.description }}</p>
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
            <div class="flex items-center gap-2 mb-3 text-sm text-gray-500 dark:text-gray-400">
              <span>{{ formatDate(article.createTime) }}</span>
              <span>·</span>
              <span>{{ article.viewCount }} 阅读</span>
            </div>

            <h2 class="text-xl font-semibold mb-2 group-hover:text-primary-light dark:group-hover:text-primary-dark transition-colors">
              {{ article.title }}
            </h2>

            <p class="text-gray-600 dark:text-gray-400 line-clamp-2">{{ article.summary || '暂无摘要' }}</p>

            <div v-if="article.tags && article.tags.length" class="flex flex-wrap gap-2 mt-4">
              <span v-for="tag in article.tags" :key="tag.id" class="tag">{{ tag.name }}</span>
            </div>
          </article>

          <div v-if="articles.length === 0" class="card text-center py-12 text-gray-500">
            该分类下暂无文章
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

      <!-- 侧边栏 -->
      <aside class="space-y-6">
        <div class="card">
          <h3 class="text-lg font-semibold mb-4 pb-2 border-b border-border-light dark:border-border-dark">分类</h3>
          <ul class="space-y-2">
            <li v-for="cat in categories" :key="cat.id">
              <router-link
                :to="`/category/${cat.slug}`"
                class="flex items-center justify-between py-1 hover:text-primary-light dark:hover:text-primary-dark transition-colors"
                :class="{ 'text-primary-light dark:text-primary-dark': cat.slug === route.params.slug }"
              >
                <span>{{ cat.name }}</span>
                <span class="text-sm text-gray-500">{{ cat.articleCount || 0 }}</span>
              </router-link>
            </li>
          </ul>
        </div>
      </aside>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import dayjs from 'dayjs'
import { getCategoryArticles, getCategoryList } from '@/api/category'

const route = useRoute()
const router = useRouter()

const loading = ref(false)
const category = ref<any>(null)
const articles = ref<any[]>([])
const categories = ref<any[]>([])

const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const totalPages = computed(() => Math.ceil(total.value / pageSize.value))

watch(() => route.params.slug, () => {
  pageNum.value = 1
  fetchCategory()
  fetchArticles()
})

onMounted(() => {
  fetchCategories()
  fetchCategory()
  fetchArticles()
})

function formatDate(date: string) {
  return dayjs(date).format('YYYY-MM-DD')
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

async function fetchCategory() {
  const cat = categories.value.find(c => c.slug === route.params.slug)
  if (cat) {
    category.value = cat
  } else {
    try {
      const res = await getCategoryList()
      category.value = res.data.find((c: any) => c.slug === route.params.slug)
    } catch (error) {
      console.error('获取分类失败', error)
    }
  }
}

async function fetchArticles() {
  loading.value = true
  try {
    const res = await getCategoryArticles(route.params.slug as string, {
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
</script>