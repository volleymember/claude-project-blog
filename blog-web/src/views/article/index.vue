<template>
  <div>
    <!-- Article -->
    <article v-if="article" class="pb-20">
      <!-- Article Header -->
      <section class="relative overflow-hidden pt-16 pb-10 md:pt-20 md:pb-14">
        <div class="absolute inset-0 grid-lines opacity-20"></div>
        <div class="absolute inset-0 bg-gradient-to-b from-transparent via-transparent to-[var(--color-bg)]"></div>

        <div class="container-blog relative">
          <div class="max-w-3xl mx-auto animate-fade-in-up">
            <!-- Breadcrumb -->
            <div class="flex items-center gap-2 mb-6">
              <router-link to="/" class="text-sm text-[var(--color-text-tertiary)] hover:text-[var(--color-accent)] transition-colors">首页</router-link>
              <svg xmlns="http://www.w3.org/2000/svg" class="w-3 h-3 text-[var(--color-text-tertiary)]" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7" /></svg>
              <router-link v-if="article.categorySlug" :to="`/category/${article.categorySlug}`" class="text-sm text-[var(--color-text-tertiary)] hover:text-[var(--color-accent)] transition-colors">{{ article.categoryName }}</router-link>
            </div>

            <!-- Category + Meta -->
            <div class="flex items-center gap-2 mb-4">
              <router-link v-if="article.categoryName" :to="`/category/${article.categorySlug}`" class="tag">
                {{ article.categoryName }}
              </router-link>
              <span class="text-xs font-mono text-[var(--color-text-tertiary)]">{{ formatDate(article.createTime) }}</span>
              <span class="text-[var(--color-text-tertiary)] text-xs">&middot;</span>
              <span class="text-xs text-[var(--color-text-tertiary)] flex items-center gap-1">
                <svg xmlns="http://www.w3.org/2000/svg" class="w-3 h-3" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" /><path stroke-linecap="round" stroke-linejoin="round" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" /></svg>
                {{ article.viewCount }}
              </span>
              <span class="text-[var(--color-text-tertiary)] text-xs">&middot;</span>
              <span class="text-xs text-[var(--color-text-tertiary)] flex items-center gap-1">
                <svg xmlns="http://www.w3.org/2000/svg" class="w-3 h-3" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" /></svg>
                {{ article.likeCount || 0 }}
              </span>
            </div>

            <!-- Title -->
            <h1 class="font-display text-display-sm md:text-display-lg text-[var(--color-text-primary)] leading-tight mb-5">
              {{ article.title }}
            </h1>

            <!-- Tags -->
            <div v-if="article.tags && article.tags.length" class="flex flex-wrap gap-2">
              <span v-for="tag in article.tags" :key="tag.id" class="tag">#{{ tag.name }}</span>
            </div>
          </div>
        </div>
      </section>

      <!-- Cover Image -->
      <div v-if="article.coverImage" class="container-blog mb-10 animate-fade-in-up" style="animation-delay: 0.2s; opacity: 0;">
        <div class="max-w-4xl mx-auto">
          <img :src="article.coverImage" :alt="article.title" class="w-full rounded-2xl shadow-elevated object-cover max-h-[400px]" />
        </div>
      </div>

      <!-- Content Area -->
      <div class="container-blog">
        <div class="grid grid-cols-1 lg:grid-cols-12 gap-6 lg:gap-8">
          <!-- Article Body -->
          <div class="lg:col-span-9">
            <div class="card animate-fade-in-up" style="animation-delay: 0.15s; opacity: 0;">
              <div class="article-content markdown-body" v-html="renderedContent"></div>
            </div>

            <!-- Like Button -->
            <div class="mt-8 flex justify-center animate-fade-in-up" style="animation-delay: 0.3s; opacity: 0;">
              <button
                class="group flex items-center gap-2 px-6 py-3 rounded-2xl font-medium text-sm transition-all duration-300"
                :class="hasLiked
                  ? 'bg-[var(--color-accent-soft)] text-[var(--color-accent)] border border-[var(--color-border)] hover:bg-[var(--color-accent-soft)]'
                  : 'bg-[var(--color-accent-soft)] text-[var(--color-accent)] border border-[var(--color-border)] hover:bg-[var(--color-accent)] hover:text-white hover:shadow-glow'"
                @click="toggleLike"
              >
                <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 transition-transform duration-300 group-hover:scale-110" :fill="hasLiked ? 'currentColor' : 'none'" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                </svg>
                {{ hasLiked ? '已点赞' : '点赞' }}
                <span class="font-mono text-xs opacity-70">({{ article.likeCount || 0 }})</span>
              </button>
            </div>

            <!-- Comments Section -->
            <section class="mt-10">
              <div class="card animate-fade-in-up" style="animation-delay: 0.35s; opacity: 0;">
                <h3 class="section-title mb-6">
                  评论
                  <span class="ml-2 text-sm font-mono font-normal text-[var(--color-text-tertiary)]">({{ comments.length }})</span>
                </h3>

                <!-- Comment Form -->
                <div class="mb-8 p-5 rounded-xl bg-[var(--color-accent-soft)] border border-[var(--color-border)]">
                  <div class="grid grid-cols-1 md:grid-cols-2 gap-3 mb-3">
                    <input
                      v-model="commentForm.nickname"
                      type="text"
                      placeholder="昵称 *"
                      class="px-3.5 py-2.5 rounded-xl bg-[var(--color-surface)] border border-[var(--color-border)] text-[var(--color-text-primary)] placeholder:text-[var(--color-text-tertiary)] focus:outline-none focus:border-[var(--color-accent)] focus:ring-1 focus:ring-[var(--color-accent-glow)] transition-all text-sm"
                    />
                    <input
                      v-model="commentForm.email"
                      type="email"
                      placeholder="邮箱（可选）"
                      class="px-3.5 py-2.5 rounded-xl bg-[var(--color-surface)] border border-[var(--color-border)] text-[var(--color-text-primary)] placeholder:text-[var(--color-text-tertiary)] focus:outline-none focus:border-[var(--color-accent)] focus:ring-1 focus:ring-[var(--color-accent-glow)] transition-all text-sm"
                    />
                  </div>
                  <textarea
                    v-model="commentForm.content"
                    placeholder="写下你的想法..."
                    rows="3"
                    class="w-full px-3.5 py-2.5 rounded-xl bg-[var(--color-surface)] border border-[var(--color-border)] text-[var(--color-text-primary)] placeholder:text-[var(--color-text-tertiary)] focus:outline-none focus:border-[var(--color-accent)] focus:ring-1 focus:ring-[var(--color-accent-glow)] transition-all resize-none text-sm"
                  ></textarea>
                  <div class="flex justify-end mt-3">
                    <button @click="submitComment" :disabled="submitting" class="btn btn-primary text-sm disabled:opacity-50">
                      {{ submitting ? '提交中...' : '发表评论' }}
                    </button>
                  </div>
                </div>

                <!-- Comment List -->
                <div v-if="comments.length" class="space-y-5">
                  <div v-for="comment in comments" :key="comment.id" class="group/comment">
                    <div class="flex gap-3">
                      <div class="w-8 h-8 rounded-full bg-[var(--color-text-primary)] flex items-center justify-center text-white text-xs font-bold flex-shrink-0">
                        {{ comment.nickname?.[0] || '?' }}
                      </div>
                      <div class="flex-1 min-w-0">
                        <div class="flex items-center gap-2 mb-1">
                          <span class="text-sm font-medium text-[var(--color-text-primary)]">{{ comment.nickname }}</span>
                          <span class="text-[10px] font-mono text-[var(--color-text-tertiary)]">{{ formatDate(comment.createTime) }}</span>
                        </div>
                        <p class="text-sm text-[var(--color-text-secondary)] leading-relaxed">{{ comment.content }}</p>
                        <button
                          @click="replyTo(comment)"
                          class="mt-2 text-xs text-[var(--color-text-tertiary)] hover:text-[var(--color-accent)] transition-colors opacity-0 group-hover/comment:opacity-100"
                        >
                          回复
                        </button>
                      </div>
                    </div>

                    <!-- Replies -->
                    <div v-if="comment.children && comment.children.length" class="ml-11 mt-3 pl-4 border-l-2 border-[var(--color-border)] space-y-4">
                      <div v-for="child in comment.children" :key="child.id">
                        <div class="flex gap-3">
                          <div class="w-6 h-6 rounded-full bg-[var(--color-text-tertiary)] flex items-center justify-center text-white text-[10px] font-bold flex-shrink-0">
                            {{ child.nickname?.[0] || '?' }}
                          </div>
                          <div>
                            <div class="flex items-center gap-2 mb-1">
                              <span class="text-xs font-medium text-[var(--color-text-primary)]">{{ child.nickname }}</span>
                              <span v-if="child.replyToNickname" class="text-[10px] text-[var(--color-text-tertiary)]">
                                回复 <span class="text-[var(--color-accent)]">{{ child.replyToNickname }}</span>
                              </span>
                            </div>
                            <p class="text-xs text-[var(--color-text-secondary)] leading-relaxed">{{ child.content }}</p>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                <div v-else class="text-center py-10">
                  <p class="text-[var(--color-text-tertiary)] text-sm">暂无评论，来说点什么吧</p>
                </div>
              </div>
            </section>
          </div>

          <!-- Sidebar -->
          <aside class="lg:col-span-3 space-y-6">
            <!-- Author -->
            <div class="gradient-border">
              <div class="text-center py-2">
                <div class="w-14 h-14 mx-auto mb-3 rounded-full bg-[var(--color-logo)] flex items-center justify-center text-white text-lg font-display font-bold">
                  {{ article?.authorName?.[0] || 'A' }}
                </div>
                <h4 class="text-sm font-semibold text-[var(--color-text-primary)]">{{ article?.authorName || '作者' }}</h4>
              </div>
            </div>

            <!-- Related -->
            <div class="card">
              <h3 class="section-title mb-5">相关推荐</h3>
              <div class="space-y-3">
                <router-link
                  v-for="item in relatedArticles"
                  :key="item.id"
                  :to="`/article/${item.id}`"
                  class="flex items-start gap-3 group/rel"
                >
                  <span class="w-1.5 h-1.5 rounded-full bg-[var(--color-accent)] flex-shrink-0 mt-2"></span>
                  <span class="text-sm text-[var(--color-text-secondary)] group-hover/rel:text-[var(--color-accent)] transition-colors line-clamp-2 leading-snug">
                    {{ item.title }}
                  </span>
                </router-link>
              </div>
            </div>
          </aside>
        </div>
      </div>
    </article>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import dayjs from 'dayjs'
import { marked } from 'marked'
import DOMPurify from 'dompurify'
import hljs from 'highlight.js'
import { getArticle, getHotArticles } from '@/api/article'
import { getComments, submitComment as submitCommentApi } from '@/api/comment'

const route = useRoute()

const article = ref<any>(null)
const comments = ref<any[]>([])
const relatedArticles = ref<any[]>([])
const submitting = ref(false)
const hasLiked = ref(false)

const commentForm = ref({
  nickname: '',
  email: '',
  content: ''
})

// Custom marked renderer
const renderer = {
  code(code: string, language: string) {
    let highlightedCode = code
    try {
      if (language && hljs.getLanguage(language)) {
        highlightedCode = hljs.highlight(code, { language }).value
      } else {
        highlightedCode = hljs.highlightAuto(code).value
      }
    } catch (e) {
      highlightedCode = code
    }

    const langLabel = language || 'plaintext'

    return `
      <div class="code-block">
        <div class="code-header">
          <span class="code-lang">${langLabel}</span>
          <button class="copy-btn" onclick="copyCode(this)">复制</button>
        </div>
        <pre class="code-content"><code class="hljs language-${language || ''}">${highlightedCode}</code></pre>
      </div>
    `
  }
}

marked.use({ renderer })

const renderedContent = computed(() => {
  if (!article.value?.content) return ''
  const html = marked(article.value.content) as string
  return DOMPurify.sanitize(html, {
    ADD_TAGS: ['iframe', 'button', 'span', 'div', 'pre', 'code'],
    ADD_ATTR: ['class', 'onclick', 'target', 'rel', 'allow', 'allowfullscreen', 'frameborder']
  })
})

// Global copy function
if (typeof window !== 'undefined') {
  (window as any).copyCode = function(btn: HTMLElement) {
    const codeBlock = btn.closest('.code-block')
    const code = codeBlock?.querySelector('code')?.textContent || ''
    navigator.clipboard.writeText(code).then(() => {
      btn.textContent = '已复制'
      btn.classList.add('copied')
      setTimeout(() => {
        btn.textContent = '复制'
        btn.classList.remove('copied')
      }, 2000)
    })
  }
}

function checkLikeStatus() {
  if (!article.value?.id) return
  const likedArticles = JSON.parse(localStorage.getItem('likedArticles') || '[]')
  hasLiked.value = likedArticles.includes(article.value.id)
}

function toggleLike() {
  if (!article.value) return
  const likedArticles: number[] = JSON.parse(localStorage.getItem('likedArticles') || '[]')
  const articleId = article.value.id

  if (hasLiked.value) {
    article.value.likeCount = Math.max(0, (article.value.likeCount || 0) - 1)
    const index = likedArticles.indexOf(articleId)
    if (index > -1) likedArticles.splice(index, 1)
    hasLiked.value = false
  } else {
    article.value.likeCount = (article.value.likeCount || 0) + 1
    likedArticles.push(articleId)
    hasLiked.value = true
  }

  localStorage.setItem('likedArticles', JSON.stringify(likedArticles))
}

onMounted(() => {
  fetchArticle()
  fetchComments()
  fetchRelated()
})

watch(() => route.params.id, () => {
  if (route.params.id) {
    fetchArticle()
    fetchComments()
    fetchRelated()
  }
})

function formatDate(date: string) {
  return dayjs(date).format('YYYY-MM-DD HH:mm')
}

async function fetchArticle() {
  try {
    const res = await getArticle(Number(route.params.id))
    article.value = res.data
    checkLikeStatus()
  } catch (error) {
    console.error('获取文章失败', error)
  }
}

async function fetchComments() {
  try {
    const res = await getComments(Number(route.params.id))
    comments.value = res.data
  } catch (error) {
    console.error('获取评论失败', error)
  }
}

async function fetchRelated() {
  try {
    const res = await getHotArticles(5)
    relatedArticles.value = res.data.filter((a: any) => a.id !== Number(route.params.id)).slice(0, 4)
  } catch (error) {
    console.error('获取相关文章失败', error)
  }
}

async function submitComment() {
  if (!commentForm.value.nickname || !commentForm.value.content) {
    alert('请填写昵称和评论内容')
    return
  }

  submitting.value = true
  try {
    await submitCommentApi({
      articleId: Number(route.params.id),
      nickname: commentForm.value.nickname,
      email: commentForm.value.email,
      content: commentForm.value.content
    })
    commentForm.value.content = ''
    alert('评论提交成功，等待审核')
    fetchComments()
  } catch (error) {
    console.error('提交评论失败', error)
  } finally {
    submitting.value = false
  }
}

function replyTo(comment: any) {
  commentForm.value.content = `@${comment.nickname} `
}
</script>

<style>
/* Code block styles (global for v-html) */
.code-block {
  margin: 1.5rem 0;
  border-radius: 12px;
  overflow: hidden;
  background: #0d1117;
  border: 1px solid rgba(255, 255, 255, 0.06);
}

.code-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 16px;
  background: rgba(255, 255, 255, 0.03);
  border-bottom: 1px solid rgba(255, 255, 255, 0.06);
}

.code-lang {
  font-size: 11px;
  color: #6b7280;
  font-family: var(--font-mono);
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.copy-btn {
  padding: 3px 10px;
  font-size: 11px;
  color: #9ca3af;
  background: rgba(255, 255, 255, 0.06);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s;
  font-family: var(--font-mono);
}

.copy-btn:hover {
  background: rgba(255, 255, 255, 0.1);
  color: #e5e7eb;
}

.copy-btn.copied {
  background: rgba(16, 185, 129, 0.2);
  color: #10b981;
  border-color: rgba(16, 185, 129, 0.3);
}

.code-content {
  margin: 0;
  padding: 16px;
  overflow-x: auto;
  background: transparent;
}

.code-content code {
  font-family: var(--font-mono);
  font-size: 13px;
  line-height: 1.7;
  color: #e6edf3;
}

/* Highlight.js syntax colors */
.hljs-comment, .hljs-quote { color: #8b949e; font-style: italic; }
.hljs-keyword, .hljs-selector-tag { color: #ff7b72; }
.hljs-string, .hljs-attr { color: #a5d6ff; }
.hljs-number { color: #79c0ff; }
.hljs-function { color: #d2a8ff; }
.hljs-class, .hljs-title { color: #ffa657; }
.hljs-variable, .hljs-params { color: #ffa657; }
.hljs-built_in { color: #ffa657; }
.hljs-type { color: #ff7b72; }
.hljs-meta { color: #79c0ff; }
.hljs-literal { color: #79c0ff; }
.hljs-symbol { color: #79c0ff; }
.hljs-bullet { color: #79c0ff; }
.hljs-addition { color: #aff5b4; background: rgba(46, 160, 67, 0.15); }
.hljs-deletion { color: #ffdcd7; background: rgba(248, 81, 73, 0.15); }
</style>
