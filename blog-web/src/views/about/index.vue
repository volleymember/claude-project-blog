<template>
  <div class="container-custom">
    <div class="max-w-2xl mx-auto">
      <div class="card text-center">
        <!-- 头像 -->
        <div class="w-32 h-32 mx-auto mb-6 rounded-full bg-gradient-to-br from-primary-light to-blue-400 flex items-center justify-center text-white text-4xl font-bold">
          B
        </div>

        <h1 class="text-2xl font-bold mb-2">{{ config.title || '我的博客' }}</h1>
        <p class="text-gray-600 dark:text-gray-400 mb-6">{{ config.subtitle || '记录生活，分享技术' }}</p>

        <!-- 简介 -->
        <div class="text-left mb-8">
          <h3 class="text-lg font-semibold mb-4">关于我</h3>
          <p class="text-gray-600 dark:text-gray-400 leading-relaxed">
            你好！欢迎来到我的博客。
          </p>
          <p class="text-gray-600 dark:text-gray-400 leading-relaxed mt-4">
            这里是我记录技术学习、生活感悟的地方。我会在这里分享一些技术文章、学习笔记和个人想法。
          </p>
        </div>

        <!-- 技术栈 -->
        <div class="text-left mb-8">
          <h3 class="text-lg font-semibold mb-4">技术栈</h3>
          <div class="flex flex-wrap gap-2">
            <span class="tag">Java</span>
            <span class="tag">Spring Boot</span>
            <span class="tag">Vue.js</span>
            <span class="tag">TypeScript</span>
            <span class="tag">MySQL</span>
            <span class="tag">Redis</span>
            <span class="tag">Docker</span>
          </div>
        </div>

        <!-- 联系方式 -->
        <div class="text-left">
          <h3 class="text-lg font-semibold mb-4">联系方式</h3>
          <div class="space-y-3">
            <a v-if="config.github" :href="config.github" target="_blank" class="flex items-center gap-3 text-gray-600 dark:text-gray-400 hover:text-primary-light dark:hover:text-primary-dark transition-colors">
              <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 24 24">
                <path fill-rule="evenodd" d="M12 2C6.477 2 2 6.484 2 12.017c0 4.425 2.865 8.18 6.839 9.504.5.092.682-.217.682-.483 0-.237-.008-.868-.013-1.703-2.782.605-3.369-1.343-3.369-1.343-.454-1.158-1.11-1.466-1.11-1.466-.908-.62.069-.608.069-.608 1.003.07 1.531 1.032 1.531 1.032.892 1.53 2.341 1.088 2.91.832.092-.647.35-1.088.636-1.338-2.22-.253-4.555-1.113-4.555-4.951 0-1.093.39-1.988 1.029-2.688-.103-.253-.446-1.272.098-2.65 0 0 .84-.27 2.75 1.026A9.564 9.564 0 0112 6.844c.85.004 1.705.115 2.504.337 1.909-1.296 2.747-1.027 2.747-1.027.546 1.379.202 2.398.1 2.651.64.7 1.028 1.595 1.028 2.688 0 3.848-2.339 4.695-4.566 4.943.359.309.678.92.678 1.855 0 1.338-.012 2.419-.012 2.747 0 .268.18.58.688.482A10.019 10.019 0 0022 12.017C22 6.484 17.522 2 12 2z" clip-rule="evenodd" />
              </svg>
              GitHub
            </a>
            <a v-if="config.email" :href="`mailto:${config.email}`" class="flex items-center gap-3 text-gray-600 dark:text-gray-400 hover:text-primary-light dark:hover:text-primary-dark transition-colors">
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
              </svg>
              {{ config.email }}
            </a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useAppStore } from '@/stores/app'
import { getSiteConfig } from '@/api/config'

const appStore = useAppStore()
const config = computed(() => appStore.siteConfig || {})

onMounted(async () => {
  if (!appStore.siteConfig) {
    try {
      const res = await getSiteConfig()
      appStore.setSiteConfig(res.data)
    } catch (error) {
      console.error('获取配置失败', error)
    }
  }
})
</script>