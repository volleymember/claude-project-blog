<template>
  <div class="container-custom">
    <div class="card mb-6">
      <h1 class="text-2xl font-bold">归档</h1>
      <p class="text-gray-600 dark:text-gray-400 mt-2">共 {{ totalCount }} 篇文章</p>
    </div>

    <!-- 归档列表 -->
    <div v-if="loading" class="space-y-6">
      <div v-for="i in 3" :key="i" class="card animate-pulse">
        <div class="h-6 bg-gray-200 dark:bg-gray-700 rounded w-24 mb-4"></div>
        <div class="space-y-3">
          <div v-for="j in 3" :key="j" class="h-4 bg-gray-200 dark:bg-gray-700 rounded w-3/4"></div>
        </div>
      </div>
    </div>

    <div v-else class="space-y-6">
      <div v-for="(group, yearMonth) in groupedArticles" :key="yearMonth" class="card">
        <h2 class="text-lg font-semibold mb-4 pb-2 border-b border-border-light dark:border-border-dark">
          {{ yearMonth }}
          <span class="text-sm font-normal text-gray-500 ml-2">({{ group.length }}篇)</span>
        </h2>
        <ul class="space-y-3">
          <li v-for="article in group" :key="article.id" class="flex items-center gap-4">
            <span class="text-sm text-gray-500 dark:text-gray-400 flex-shrink-0 w-16">
              {{ formatDate(article.createTime) }}
            </span>
            <router-link
              :to="`/article/${article.id}`"
              class="hover:text-primary-light dark:hover:text-primary-dark transition-colors line-clamp-1"
            >
              {{ article.title }}
            </router-link>
          </li>
        </ul>
      </div>

      <div v-if="Object.keys(groupedArticles).length === 0" class="card text-center py-12 text-gray-500">
        暂无文章
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import dayjs from 'dayjs'
import { getArchiveList } from '@/api/article'

const loading = ref(false)
const articles = ref<any[]>([])

const totalCount = computed(() => articles.value.length)

const groupedArticles = computed(() => {
  const groups: Record<string, any[]> = {}
  articles.value.forEach(article => {
    const yearMonth = dayjs(article.createTime).format('YYYY年MM月')
    if (!groups[yearMonth]) {
      groups[yearMonth] = []
    }
    groups[yearMonth].push(article)
  })
  return groups
})

onMounted(() => {
  fetchArticles()
})

function formatDate(date: string) {
  return dayjs(date).format('MM-DD')
}

async function fetchArticles() {
  loading.value = true
  try {
    const res = await getArchiveList()
    articles.value = res.data
  } catch (error) {
    console.error('获取归档失败', error)
  } finally {
    loading.value = false
  }
}
</script>