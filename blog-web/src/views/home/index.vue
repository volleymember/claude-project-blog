<template>
  <div>
    <!-- Hero Section -->
    <section class="relative overflow-hidden py-20 md:py-28">
      <!-- Background grid decoration -->
      <div class="absolute inset-0 grid-lines opacity-40"></div>
      <div class="absolute inset-0 bg-gradient-to-b from-transparent via-transparent to-[var(--color-bg)]"></div>

      <div class="container-blog relative">
        <div class="max-w-3xl">
          <!-- Greeting badge -->
          <div class="inline-flex items-center gap-2 px-3.5 py-1.5 rounded-full bg-[var(--color-accent-soft)] border border-[var(--color-border)] mb-6 animate-fade-in">
            <span class="w-2 h-2 rounded-full bg-[var(--color-text-secondary)] animate-pulse-soft"></span>
            <span class="text-xs font-medium text-[var(--color-accent)] font-mono">Available for hire</span>
          </div>

          <!-- Main heading -->
          <h1 class="font-display text-4xl md:text-display-lg text-[var(--color-text-primary)] mb-6 animate-fade-in-up leading-tight">
            你好，我是
            <span class="text-gradient">{{ config.title || '开发者' }}</span>
          </h1>

          <!-- Typing effect subtitle -->
          <div class="flex items-center gap-0 mb-8 h-8 animate-fade-in-up" style="animation-delay: 0.15s; opacity: 0;">
            <span class="text-lg md:text-xl text-[var(--color-text-secondary)]">{{ typedText }}</span>
            <span class="w-0.5 h-6 bg-[var(--color-accent)] rounded-full ml-0.5" :class="cursorVisible ? 'opacity-100' : 'opacity-0'" style="transition: opacity 0.1s;"></span>
          </div>

          <!-- CTA buttons -->
          <div class="flex flex-wrap gap-3 animate-fade-in-up" style="animation-delay: 0.3s; opacity: 0;">
            <router-link to="/archive" class="btn btn-primary">
              <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
              </svg>
              浏览文章
            </router-link>
            <router-link to="/about" class="btn btn-ghost">
              <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
              </svg>
              了解更多
            </router-link>
          </div>

          <!-- Quick stats -->
          <div class="flex items-center gap-6 md:gap-8 mt-10 animate-fade-in-up" style="animation-delay: 0.45s; opacity: 0;">
            <div class="flex items-center gap-2">
              <span class="text-2xl font-display font-bold text-[var(--color-text-primary)]">{{ total }}</span>
              <span class="text-xs text-[var(--color-text-tertiary)] uppercase tracking-wider">篇文章</span>
            </div>
            <div class="w-px h-8 bg-[var(--color-border)]"></div>
            <div class="flex items-center gap-2">
              <span class="text-2xl font-display font-bold text-[var(--color-text-primary)]">{{ categories.length }}</span>
              <span class="text-xs text-[var(--color-text-tertiary)] uppercase tracking-wider">个分类</span>
            </div>
            <div class="w-px h-8 bg-[var(--color-border)]"></div>
            <div class="flex items-center gap-2">
              <span class="text-2xl font-display font-bold text-[var(--color-text-primary)]">{{ tags.length }}</span>
              <span class="text-xs text-[var(--color-text-tertiary)] uppercase tracking-wider">个标签</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Main Content -->
    <section class="pb-20">
      <div class="container-blog">
        <div class="grid grid-cols-1 lg:grid-cols-12 gap-6 lg:gap-8">
          <!-- Article List -->
          <div class="lg:col-span-8">
            <!-- Category Filter -->
            <div class="flex items-center gap-3 mb-8 overflow-x-auto scrollbar-hide pb-2">
              <button
                @click="selectCategory(null)"
                class="tag flex-shrink-0"
                :class="selectedCategory === null ? '!bg-[var(--color-accent)] !text-white shadow-glow' : ''"
              >
                全部
              </button>
              <button
                v-for="cat in categories"
                :key="cat.id"
                @click="selectCategory(cat.id)"
                class="tag flex-shrink-0"
                :class="selectedCategory === cat.id ? '!bg-[var(--color-accent)] !text-white shadow-glow' : ''"
              >
                {{ cat.name }}
              </button>
            </div>

            <!-- Loading Skeleton -->
            <div v-if="loading" class="space-y-5">
              <div v-for="i in 3" :key="i" class="card">
                <div class="flex gap-5 items-start">
                  <div class="flex-1 space-y-3">
                    <div class="skeleton h-4 w-24 rounded-full"></div>
                    <div class="skeleton h-5 w-3/4"></div>
                    <div class="skeleton h-4 w-full"></div>
                    <div class="skeleton h-4 w-2/3"></div>
                  </div>
                  <div class="skeleton w-32 h-24 rounded-xl flex-shrink-0 hidden sm:block"></div>
                </div>
              </div>
            </div>

            <!-- Articles -->
            <div v-else class="space-y-5">
              <article
                v-for="(article, index) in articles"
                :key="article.id"
                class="card group cursor-pointer animate-fade-in-up"
                :style="{ animationDelay: `${index * 0.05}s`, opacity: 0 }"
                @click="goToArticle(article.id)"
              >
                <div class="flex gap-5 items-start">
                  <div class="flex-1 min-w-0">
                    <!-- Meta -->
                    <div class="flex items-center gap-2 mb-3">
                      <span v-if="article.categoryName" class="tag text-[10px]">{{ article.categoryName }}</span>
                      <span class="text-xs text-[var(--color-text-tertiary)] font-mono">{{ formatDate(article.createTime) }}</span>
                      <span class="text-[var(--color-text-tertiary)] text-xs">&middot;</span>
                      <span class="text-xs text-[var(--color-text-tertiary)] flex items-center gap-1">
                        <svg xmlns="http://www.w3.org/2000/svg" class="w-3 h-3" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" /><path stroke-linecap="round" stroke-linejoin="round" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" /></svg>
                        {{ article.viewCount }}
                      </span>
                    </div>

                    <!-- Title -->
                    <h2 class="text-lg font-display font-semibold text-[var(--color-text-primary)] mb-2 group-hover:text-[var(--color-accent)] transition-colors duration-300 line-clamp-2">
                      {{ article.title }}
                    </h2>

                    <!-- Summary -->
                    <p class="text-sm text-[var(--color-text-secondary)] line-clamp-2 mb-3 leading-relaxed">
                      {{ article.summary || '暂无摘要' }}
                    </p>

                    <!-- Tags -->
                    <div v-if="article.tags && article.tags.length" class="flex flex-wrap gap-1.5">
                      <span v-for="tag in article.tags" :key="tag.id" class="text-[10px] font-mono px-2 py-0.5 rounded-md bg-[var(--color-border-subtle)] text-[var(--color-text-tertiary)]">
                        #{{ tag.name }}
                      </span>
                    </div>
                  </div>

                  <!-- Thumbnail -->
                  <div v-if="article.coverImage" class="w-28 h-28 sm:w-36 sm:h-28 rounded-xl overflow-hidden flex-shrink-0 hidden sm:flex items-center">
                    <img :src="article.coverImage" :alt="article.title" class="w-full h-full object-cover group-hover:scale-110 transition-transform duration-700" />
                  </div>
                </div>
              </article>

              <!-- Empty State -->
              <div v-if="articles.length === 0 && !loading" class="card text-center py-16">
                <svg xmlns="http://www.w3.org/2000/svg" class="w-16 h-16 mx-auto mb-4 text-[var(--color-text-tertiary)] opacity-30" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M19 20H5a2 2 0 01-2-2V6a2 2 0 012-2h10a2 2 0 012 2v1m2 13a2 2 0 01-2-2V7m2 13a2 2 0 002-2V9a2 2 0 00-2-2h-2m-4-3H9M7 16h6M7 8h6v4H7V8z" />
                </svg>
                <p class="text-[var(--color-text-tertiary)] text-sm">暂无文章</p>
              </div>
            </div>

            <!-- Pagination -->
            <div v-if="total > pageSize" class="flex justify-center mt-10">
              <div class="flex items-center gap-2">
                <button
                  @click="changePage(pageNum - 1)"
                  :disabled="pageNum === 1"
                  class="btn btn-ghost text-sm disabled:opacity-30 disabled:cursor-not-allowed"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7" /></svg>
                  上一页
                </button>

                <div class="flex items-center gap-1.5 px-2">
                  <template v-for="p in visiblePages" :key="p">
                    <button
                      v-if="p !== '...'"
                      @click="changePage(p as number)"
                      class="w-8 h-8 rounded-lg text-sm font-medium transition-all duration-300"
                      :class="p === pageNum
                        ? 'bg-[var(--color-accent)] text-white shadow-glow'
                        : 'text-[var(--color-text-secondary)] hover:bg-[var(--color-accent-soft)]'"
                    >
                      {{ p }}
                    </button>
                    <span v-else class="text-[var(--color-text-tertiary)] text-sm px-1">...</span>
                  </template>
                </div>

                <button
                  @click="changePage(pageNum + 1)"
                  :disabled="pageNum >= totalPages"
                  class="btn btn-ghost text-sm disabled:opacity-30 disabled:cursor-not-allowed"
                >
                  下一页
                  <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7" /></svg>
                </button>
              </div>
            </div>
          </div>

          <!-- Sidebar -->
          <aside class="lg:col-span-4 space-y-6">
            <!-- Author Card -->
            <div class="gradient-border">
              <div class="text-center py-3">
                <div class="avatar-ring w-20 h-20 mx-auto mb-4">
                  <div class="w-full h-full rounded-full bg-[var(--color-logo)] flex items-center justify-center text-white text-2xl font-display font-bold">
                    {{ (config.title || 'B')[0] }}
                  </div>
                </div>
                <h3 class="font-display font-bold text-lg text-[var(--color-text-primary)]">{{ config.title || '博客作者' }}</h3>
                <p class="text-sm text-[var(--color-text-tertiary)] mt-1">{{ config.subtitle || '热爱技术，热爱生活' }}</p>
                <div class="flex justify-center gap-2 mt-4">
                  <a v-if="config.github" :href="config.github" target="_blank" rel="noopener noreferrer"
                    class="w-8 h-8 rounded-lg flex items-center justify-center text-[var(--color-text-tertiary)] hover:text-[var(--color-accent)] hover:bg-[var(--color-accent-soft)] transition-all">
                    <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 24 24"><path fill-rule="evenodd" d="M12 2C6.477 2 2 6.484 2 12.017c0 4.425 2.865 8.18 6.839 9.504.5.092.682-.217.682-.483 0-.237-.008-.868-.013-1.703-2.782.605-3.369-1.343-3.369-1.343-.454-1.158-1.11-1.466-1.11-1.466-.908-.62.069-.608.069-.608 1.003.07 1.531 1.032 1.531 1.032.892 1.53 2.341 1.088 2.91.832.092-.647.35-1.088.636-1.338-2.22-.253-4.555-1.113-4.555-4.951 0-1.093.39-1.988 1.029-2.688-.103-.253-.446-1.272.098-2.65 0 0 .84-.27 2.75 1.026A9.564 9.564 0 0112 6.844c.85.004 1.705.115 2.504.337 1.909-1.296 2.747-1.027 2.747-1.027.546 1.379.202 2.398.1 2.651.64.7 1.028 1.595 1.028 2.688 0 3.848-2.339 4.695-4.566 4.943.359.309.678.92.678 1.855 0 1.338-.012 2.419-.012 2.747 0 .268.18.58.688.482A10.019 10.019 0 0022 12.017C22 6.484 17.522 2 12 2z" clip-rule="evenodd" /></svg>
                  </a>
                  <a v-if="config.email" :href="`mailto:${config.email}`"
                    class="w-8 h-8 rounded-lg flex items-center justify-center text-[var(--color-text-tertiary)] hover:text-[var(--color-accent)] hover:bg-[var(--color-accent-soft)] transition-all">
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" /></svg>
                  </a>
                </div>
              </div>
            </div>

            <!-- Hot Articles -->
            <div class="card">
              <h3 class="section-title mb-5">热门文章</h3>
              <div v-if="hotLoading" class="space-y-4">
                <div v-for="i in 5" :key="i" class="flex items-start gap-3">
                  <div class="skeleton w-6 h-6 rounded-md flex-shrink-0"></div>
                  <div class="flex-1">
                    <div class="skeleton h-4 w-full"></div>
                  </div>
                </div>
              </div>
              <div v-else class="space-y-3">
                <router-link
                  v-for="(article, index) in hotArticles"
                  :key="article.id"
                  :to="`/article/${article.id}`"
                  class="flex items-start gap-3 group/hot"
                >
                  <span
                    class="flex-shrink-0 w-6 h-6 rounded-md flex items-center justify-center text-xs font-mono font-bold transition-all duration-300"
                    :class="[
                      index < 3
                        ? 'bg-[var(--color-accent)] text-white'
                        : 'bg-[var(--color-border-subtle)] text-[var(--color-text-tertiary)] group-hover/hot:bg-[var(--color-accent-soft)] group-hover/hot:text-[var(--color-accent)]'
                    ]"
                  >
                    {{ index + 1 }}
                  </span>
                  <span class="text-sm text-[var(--color-text-secondary)] group-hover/hot:text-[var(--color-accent)] transition-colors line-clamp-2 leading-snug">
                    {{ article.title }}
                  </span>
                </router-link>
              </div>
            </div>

            <!-- Tags Cloud -->
            <div class="card">
              <h3 class="section-title mb-5">标签云</h3>
              <div v-if="tagsLoading" class="flex flex-wrap gap-2">
                <div v-for="i in 8" :key="i" class="skeleton h-7 w-16 rounded-full"></div>
              </div>
              <div v-else class="flex flex-wrap gap-2">
                <span
                  v-for="tag in tags"
                  :key="tag.id"
                  class="tag"
                >
                  {{ tag.name }}
                </span>
              </div>
            </div>
          </aside>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAppStore } from '@/stores/app'
import dayjs from 'dayjs'
import { getArticleList, getHotArticles } from '@/api/article'
import { getCategoryList } from '@/api/category'
import { getTagList } from '@/api/tag'

const router = useRouter()
const appStore = useAppStore()
const config = computed(() => appStore.siteConfig || {})

// Typing effect
const phrases = computed(() => [
  config.value.subtitle || '用代码构建世界，用文字记录思考',
  '热爱技术，追求卓越',
  '持续学习，不断成长',
])
const typedText = ref('')
const cursorVisible = ref(true)
const currentPhraseIndex = ref(0)
const isDeleting = ref(false)
let typingTimer: ReturnType<typeof setTimeout> | null = null

function typeEffect() {
  const phrases_list = phrases.value
  const current = phrases_list[currentPhraseIndex.value % phrases_list.length]

  if (!isDeleting.value) {
    typedText.value = current.substring(0, typedText.value.length + 1)
    if (typedText.value === current) {
      typingTimer = setTimeout(() => { isDeleting.value = true }, 2200)
      return
    }
    typingTimer = setTimeout(typeEffect, 80 + Math.random() * 40)
  } else {
    typedText.value = current.substring(0, typedText.value.length - 1)
    if (typedText.value === '') {
      isDeleting.value = false
      currentPhraseIndex.value++
      typingTimer = setTimeout(typeEffect, 400)
      return
    }
    typingTimer = setTimeout(typeEffect, 35)
  }
}

// Cursor blink
let cursorTimer: ReturnType<typeof setInterval> | null = null

onMounted(() => {
  fetchCategories()
  fetchArticles()
  fetchHotArticles()
  fetchTags()

  // Start typing after initial animations settle
  typingTimer = setTimeout(typeEffect, 800)
  cursorTimer = setInterval(() => {
    cursorVisible.value = !cursorVisible.value
  }, 530)
})

onUnmounted(() => {
  if (typingTimer) clearTimeout(typingTimer)
  if (cursorTimer) clearInterval(cursorTimer)
})

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

const visiblePages = computed(() => {
  const pages: (number | string)[] = []
  const tp = totalPages.value
  const cp = pageNum.value

  if (tp <= 7) {
    for (let i = 1; i <= tp; i++) pages.push(i)
  } else {
    pages.push(1)
    if (cp > 3) pages.push('...')
    for (let i = Math.max(2, cp - 1); i <= Math.min(tp - 1, cp + 1); i++) {
      pages.push(i)
    }
    if (cp < tp - 2) pages.push('...')
    pages.push(tp)
  }
  return pages
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
