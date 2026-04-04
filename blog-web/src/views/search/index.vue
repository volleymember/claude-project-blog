<template>
  <div class="container-custom">
    <div class="max-w-2xl mx-auto">
      <!-- 搜索框 -->
      <div class="card mb-6">
        <div class="relative">
          <input
            v-model="keyword"
            type="text"
            placeholder="搜索文章..."
            class="w-full px-4 py-3 pr-12 rounded-lg border border-border-light dark:border-border-dark bg-transparent focus:outline-none focus:ring-2 focus:ring-primary-light dark:focus:ring-primary-dark"
            @keyup.enter="search"
          />
          <button
            @click="search"
            class="absolute right-2 top-1/2 -translate-y-1/2 p-2 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
          </button>
        </div>
      </div>

      <!-- 搜索结果 -->
      <div v-if="searched">
        <p class="text-gray-600 dark:text-gray-400 mb-4">
          找到 {{ total }} 篇与 "{{ lastKeyword }}" 相关的文章
        </p>

        <div v-if="loading" class="space-y-6">
          <div v-for="i in 3" :key="i" class="card animate-pulse">
            <div class="h-6 bg-gray-200 dark:bg-gray-700 rounded w-3/4 mb-4"></div>
            <div class="h-4 bg-gray-200 dark:bg-gray-700 rounded w-full mb-2"></div>
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
          </article>

          <div v-if="articles.length === 0" class="card text-center py-12 text-gray-500">
            未找到相关文章
          </div>
        </div>
      </div>

      <!-- 搜索提示 -->
      <div v-else class="card text-center py-12 text-gray-500">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 mx-auto mb-4 text-gray-300 dark:text-gray-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
        </svg>
        <p>输入关键词搜索文章</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import dayjs from 'dayjs'
import { getArticleList } from '@/api/article'

const router = useRouter()
const route = useRoute()

const keyword = ref('')
const lastKeyword = ref('')
const searched = ref(false)
const loading = ref(false)
const articles = ref<any[]>([])
const total = ref(0)

onMounted(() => {
  if (route.query.q) {
    keyword.value = route.query.q as string
    search()
  }
})

function formatDate(date: string) {
  return dayjs(date).format('YYYY-MM-DD')
}

function goToArticle(id: number) {
  router.push(`/article/${id}`)
}

async function search() {
  if (!keyword.value.trim()) return

  searched.value = true
  loading.value = true
  lastKeyword.value = keyword.value

  // 更新URL
  router.replace({ query: { q: keyword.value } })

  try {
    const res = await getArticleList({
      pageNum: 1,
      pageSize: 20
    })
    // 简单的前端搜索过滤
    const k = keyword.value.toLowerCase()
    articles.value = res.data.list.filter((a: any) =>
      a.title.toLowerCase().includes(k) ||
      (a.summary && a.summary.toLowerCase().includes(k))
    )
    total.value = articles.value.length
  } catch (error) {
    console.error('搜索失败', error)
  } finally {
    loading.value = false
  }
}
</script>