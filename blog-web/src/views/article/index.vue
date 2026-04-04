<template>
  <div class="container-custom">
    <div class="grid grid-cols-1 lg:grid-cols-4 gap-8">
      <!-- 文章内容 -->
      <div class="lg:col-span-3">
        <article v-if="article" class="card">
          <!-- 文章头部 -->
          <header class="mb-6">
            <div class="flex items-center gap-2 mb-3 text-sm text-gray-500 dark:text-gray-400">
              <router-link v-if="article.categoryName" :to="`/category/${article.categorySlug}`" class="tag">
                {{ article.categoryName }}
              </router-link>
              <span>{{ formatDate(article.createTime) }}</span>
              <span>·</span>
              <span>{{ article.viewCount }} 阅读</span>
              <span>·</span>
              <span>{{ article.likeCount || 0 }} 点赞</span>
            </div>

            <h1 class="text-2xl md:text-3xl font-bold mb-4">{{ article.title }}</h1>

            <!-- 标签 -->
            <div v-if="article.tags && article.tags.length" class="flex flex-wrap gap-2">
              <span v-for="tag in article.tags" :key="tag.id" class="tag">{{ tag.name }}</span>
            </div>
          </header>

          <!-- 封面图 -->
          <div v-if="article.coverImage" class="mb-6 -mx-6 -mt-6 overflow-hidden">
            <img :src="article.coverImage" :alt="article.title" class="w-full h-64 md:h-80 object-cover" />
          </div>

          <!-- 文章内容 -->
          <div class="markdown-body" v-html="renderedContent"></div>

          <!-- 点赞 -->
          <div class="mt-8 pt-6 border-t border-border-light dark:border-border-dark flex items-center justify-center">
            <button
              class="btn flex items-center gap-2 transition-all duration-200"
              :class="hasLiked ? 'bg-red-500 text-white hover:bg-red-600' : 'btn-primary'"
              @click="toggleLike"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" :fill="hasLiked ? 'currentColor' : 'none'" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
              </svg>
              {{ hasLiked ? '取消点赞' : '点赞' }} ({{ article.likeCount || 0 }})
            </button>
          </div>
        </article>

        <!-- 评论区 -->
        <section class="card mt-6">
          <h3 class="text-lg font-semibold mb-4 pb-2 border-b border-border-light dark:border-border-dark">
            评论 ({{ comments.length }})
          </h3>

          <!-- 评论表单 -->
          <div class="mb-6">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
              <input
                v-model="commentForm.nickname"
                type="text"
                placeholder="昵称 *"
                class="px-4 py-2 rounded-lg border border-border-light dark:border-border-dark bg-transparent focus:outline-none focus:ring-2 focus:ring-primary-light dark:focus:ring-primary-dark"
              />
              <input
                v-model="commentForm.email"
                type="email"
                placeholder="邮箱"
                class="px-4 py-2 rounded-lg border border-border-light dark:border-border-dark bg-transparent focus:outline-none focus:ring-2 focus:ring-primary-light dark:focus:ring-primary-dark"
              />
            </div>
            <textarea
              v-model="commentForm.content"
              placeholder="写下你的评论..."
              rows="4"
              class="w-full px-4 py-2 rounded-lg border border-border-light dark:border-border-dark bg-transparent focus:outline-none focus:ring-2 focus:ring-primary-light dark:focus:ring-primary-dark resize-none"
            ></textarea>
            <div class="flex justify-end mt-2">
              <button @click="submitComment" :disabled="submitting" class="btn btn-primary">
                {{ submitting ? '提交中...' : '发表评论' }}
              </button>
            </div>
          </div>

          <!-- 评论列表 -->
          <div v-if="comments.length" class="space-y-4">
            <div v-for="comment in comments" :key="comment.id" class="p-4 rounded-lg bg-gray-50 dark:bg-gray-800/50">
              <div class="flex items-center gap-2 mb-2">
                <span class="font-medium">{{ comment.nickname }}</span>
                <span class="text-xs text-gray-500 dark:text-gray-400">{{ formatDate(comment.createTime) }}</span>
              </div>
              <p class="text-gray-600 dark:text-gray-400">{{ comment.content }}</p>

              <!-- 子评论 -->
              <div v-if="comment.children && comment.children.length" class="mt-4 ml-4 pl-4 border-l-2 border-border-light dark:border-border-dark space-y-3">
                <div v-for="child in comment.children" :key="child.id" class="text-sm">
                  <div class="flex items-center gap-2 mb-1">
                    <span class="font-medium">{{ child.nickname }}</span>
                    <span v-if="child.replyToNickname" class="text-gray-500 dark:text-gray-400">
                      回复 <span class="text-primary-light dark:text-primary-dark">{{ child.replyToNickname }}</span>
                    </span>
                  </div>
                  <p class="text-gray-600 dark:text-gray-400">{{ child.content }}</p>
                </div>
              </div>

              <!-- 回复按钮 -->
              <button
                @click="replyTo(comment)"
                class="mt-2 text-sm text-primary-light dark:text-primary-dark hover:underline"
              >
                回复
              </button>
            </div>
          </div>

          <div v-else class="text-center text-gray-500 dark:text-gray-400 py-8">
            暂无评论，快来发表第一条评论吧！
          </div>
        </section>
      </div>

      <!-- 侧边栏 -->
      <aside class="space-y-6">
        <!-- 作者信息 -->
        <div class="card text-center">
          <div class="w-20 h-20 mx-auto mb-4 rounded-full bg-gray-200 dark:bg-gray-700 flex items-center justify-center">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-10 w-10 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
            </svg>
          </div>
          <h4 class="font-semibold">{{ article?.authorName || '作者' }}</h4>
        </div>

        <!-- 相关推荐 -->
        <div class="card">
          <h3 class="text-lg font-semibold mb-4 pb-2 border-b border-border-light dark:border-border-dark">相关推荐</h3>
          <ul class="space-y-3">
            <li v-for="item in relatedArticles" :key="item.id">
              <router-link :to="`/article/${item.id}`" class="text-sm hover:text-primary-light dark:hover:text-primary-dark transition-colors line-clamp-2">
                {{ item.title }}
              </router-link>
            </li>
          </ul>
        </div>
      </aside>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, nextTick, watch } from 'vue'
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

// 自定义渲染器
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

// 配置 marked
marked.use({ renderer })

const renderedContent = computed(() => {
  if (!article.value?.content) return ''
  const html = marked(article.value.content) as string
  return DOMPurify.sanitize(html, {
    ADD_TAGS: ['iframe', 'button', 'span', 'div', 'pre', 'code'],
    ADD_ATTR: ['class', 'onclick', 'target', 'rel', 'allow', 'allowfullscreen', 'frameborder']
  })
})

// 添加复制代码的全局函数
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

// 检查点赞状态
function checkLikeStatus() {
  if (!article.value?.id) return
  const likedArticles = JSON.parse(localStorage.getItem('likedArticles') || '[]')
  hasLiked.value = likedArticles.includes(article.value.id)
}

// 切换点赞状态
function toggleLike() {
  if (!article.value) return

  const likedArticles: number[] = JSON.parse(localStorage.getItem('likedArticles') || '[]')
  const articleId = article.value.id

  if (hasLiked.value) {
    // 取消点赞
    article.value.likeCount = Math.max(0, (article.value.likeCount || 0) - 1)
    const index = likedArticles.indexOf(articleId)
    if (index > -1) {
      likedArticles.splice(index, 1)
    }
    hasLiked.value = false
  } else {
    // 点赞
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
/* 代码块样式 */
.code-block {
  margin: 1.5rem 0;
  border-radius: 8px;
  overflow: hidden;
  background: #1e1e1e;
  border: 1px solid #333;
}

.code-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 16px;
  background: #2d2d2d;
  border-bottom: 1px solid #404040;
}

.code-lang {
  font-size: 12px;
  color: #888;
  font-family: 'SF Mono', Monaco, Consolas, monospace;
}

.copy-btn {
  padding: 4px 12px;
  font-size: 12px;
  color: #aaa;
  background: #404040;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s;
}

.copy-btn:hover {
  background: #505050;
  color: #fff;
}

.copy-btn.copied {
  background: #22c55e;
  color: #fff;
}

.code-content {
  margin: 0;
  padding: 16px;
  overflow-x: auto;
  background: #1e1e1e;
}

.code-content code {
  font-family: 'Fira Code', 'SF Mono', Monaco, Consolas, monospace;
  font-size: 14px;
  line-height: 1.6;
  color: #e4e4e7;
}

/* Markdown 通用样式 */
.markdown-body {
  line-height: 1.8;
  color: #374151;
}

.dark .markdown-body {
  color: #e5e7eb;
}

.markdown-body h1,
.markdown-body h2,
.markdown-body h3,
.markdown-body h4,
.markdown-body h5,
.markdown-body h6 {
  margin-top: 1.5em;
  margin-bottom: 0.5em;
  font-weight: 600;
  line-height: 1.3;
  color: #111827;
}

.dark .markdown-body h1,
.dark .markdown-body h2,
.dark .markdown-body h3,
.dark .markdown-body h4,
.dark .markdown-body h5,
.dark .markdown-body h6 {
  color: #f9fafb;
}

.markdown-body h1 { font-size: 1.75em; border-bottom: 1px solid #e5e7eb; padding-bottom: 0.3em; }
.markdown-body h2 { font-size: 1.5em; border-bottom: 1px solid #e5e7eb; padding-bottom: 0.3em; }
.markdown-body h3 { font-size: 1.25em; }
.markdown-body h4 { font-size: 1.1em; }

.dark .markdown-body h1,
.dark .markdown-body h2 {
  border-bottom-color: #374151;
}

.markdown-body p {
  margin-bottom: 1em;
  color: #374151;
}

.dark .markdown-body p {
  color: #d1d5db;
}

.markdown-body ul,
.markdown-body ol {
  padding-left: 2em;
  margin-bottom: 1em;
}

.markdown-body li {
  margin-bottom: 0.25em;
  color: #374151;
}

.dark .markdown-body li {
  color: #d1d5db;
}

.markdown-body blockquote {
  margin: 1em 0;
  padding: 0.5em 1em;
  border-left: 4px solid #3b82f6;
  background: #eff6ff;
  color: #1e40af;
}

.dark .markdown-body blockquote {
  background: #1e3a5f;
  color: #93c5fd;
  border-left-color: #60a5fa;
}

.markdown-body a {
  color: #3b82f6;
  text-decoration: none;
}

.dark .markdown-body a {
  color: #60a5fa;
}

.markdown-body a:hover {
  text-decoration: underline;
  color: #2563eb;
}

.dark .markdown-body a:hover {
  color: #93c5fd;
}

.markdown-body img {
  max-width: 100%;
  border-radius: 8px;
  margin: 1em 0;
}

.markdown-body table {
  width: 100%;
  border-collapse: collapse;
  margin: 1em 0;
}

.markdown-body th,
.markdown-body td {
  border: 1px solid #e5e7eb;
  padding: 8px 12px;
  text-align: left;
  color: #374151;
}

.dark .markdown-body th,
.dark .markdown-body td {
  border-color: #374151;
  color: #e5e7eb;
}

.markdown-body th {
  background: #f3f4f6;
  font-weight: 600;
  color: #111827;
}

.dark .markdown-body th {
  background: #1f2937;
  color: #f9fafb;
}

.markdown-body hr {
  border: none;
  border-top: 1px solid #e5e7eb;
  margin: 2em 0;
}

.dark .markdown-body hr {
  border-top-color: #374151;
}

/* 行内代码 */
.markdown-body code:not(.hljs) {
  padding: 2px 6px;
  background: #f3f4f6;
  border-radius: 4px;
  font-family: 'Fira Code', 'SF Mono', Monaco, Consolas, monospace;
  font-size: 0.9em;
  color: #db2777;
}

.dark .markdown-body code:not(.hljs) {
  background: #374151;
  color: #f472b6;
}

/* Highlight.js 主题补充 */
.hljs-comment,
.hljs-quote { color: #6a9955; }
.hljs-keyword,
.hljs-selector-tag { color: #569cd6; }
.hljs-string,
.hljs-attr { color: #ce9178; }
.hljs-number { color: #b5cea8; }
.hljs-function { color: #dcdcaa; }
.hljs-class,
.hljs-title { color: #4ec9b0; }
.hljs-variable,
.hljs-params { color: #9cdcfe; }
.hljs-built_in { color: #4ec9b0; }
.hljs-type { color: #4ec9b0; }

/* 强调文本 */
.markdown-body strong {
  color: #111827;
  font-weight: 600;
}

.dark .markdown-body strong {
  color: #f9fafb;
}

.markdown-body em {
  color: #374151;
  font-style: italic;
}

.dark .markdown-body em {
  color: #d1d5db;
}

/* 删除线 */
.markdown-body del {
  color: #9ca3af;
}

.dark .markdown-body del {
  color: #6b7280;
}
</style>