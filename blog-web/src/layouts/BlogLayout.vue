<template>
  <div class="min-h-screen flex flex-col bg-background-light dark:bg-background-dark transition-colors duration-300">
    <!-- 顶部导航 -->
    <header class="sticky top-0 z-50 backdrop-blur-md border-b border-gray-200 dark:border-gray-800 bg-white/90 dark:bg-gray-900/90 transition-colors">
      <div class="max-w-6xl mx-auto px-4">
        <div class="flex items-center justify-between h-16">
          <!-- Logo + 导航 -->
          <div class="flex items-center space-x-8">
            <!-- Logo -->
            <router-link to="/" class="flex items-center">
              <span class="text-2xl font-bold bg-gradient-to-r from-blue-500 to-cyan-500 bg-clip-text text-transparent">
                {{ config.title || 'Blog' }}
              </span>
            </router-link>

            <!-- 桌面端导航 -->
            <nav class="hidden md:flex items-center space-x-1">
              <router-link
                to="/"
                class="nav-item"
                :class="{ 'nav-active': $route.path === '/' }"
              >
                首页
              </router-link>
              <router-link
                to="/archive"
                class="nav-item"
                :class="{ 'nav-active': $route.path === '/archive' }"
              >
                归档
              </router-link>
              <router-link
                to="/about"
                class="nav-item"
                :class="{ 'nav-active': $route.path === '/about' }"
              >
                关于
              </router-link>
            </nav>
          </div>

          <!-- 右侧操作 -->
          <div class="flex items-center space-x-2">
            <!-- 搜索按钮 -->
            <router-link
              to="/search"
              class="p-2.5 rounded-full hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors text-gray-600 dark:text-gray-400"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
              </svg>
            </router-link>

            <!-- 主题切换 -->
            <button
              @click="toggleTheme"
              class="p-2.5 rounded-full hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors text-gray-600 dark:text-gray-400"
            >
              <svg v-if="isDark" xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z" />
              </svg>
              <svg v-else xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z" />
              </svg>
            </button>

            <!-- 移动端菜单按钮 -->
            <button
              @click="showMobileMenu = !showMobileMenu"
              class="md:hidden p-2.5 rounded-full hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors text-gray-600 dark:text-gray-400"
            >
              <svg v-if="!showMobileMenu" xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
              </svg>
              <svg v-else xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
        </div>

        <!-- 移动端菜单 -->
        <transition name="slide">
          <div v-if="showMobileMenu" class="md:hidden py-3 border-t border-gray-200 dark:border-gray-800">
            <nav class="flex flex-col space-y-1">
              <router-link to="/" class="nav-item-mobile" :class="{ 'nav-active-mobile': $route.path === '/' }" @click="showMobileMenu = false">
                首页
              </router-link>
              <router-link to="/archive" class="nav-item-mobile" :class="{ 'nav-active-mobile': $route.path === '/archive' }" @click="showMobileMenu = false">
                归档
              </router-link>
              <router-link to="/about" class="nav-item-mobile" :class="{ 'nav-active-mobile': $route.path === '/about' }" @click="showMobileMenu = false">
                关于
              </router-link>
            </nav>
          </div>
        </transition>
      </div>
    </header>

    <!-- 主内容区 -->
    <main class="flex-1 py-8">
      <router-view />
    </main>

    <!-- 底部 -->
    <footer class="border-t border-gray-200 dark:border-gray-800 bg-white dark:bg-gray-900 py-8 transition-colors">
      <div class="max-w-6xl mx-auto px-4 text-center">
        <p class="text-gray-500 dark:text-gray-400 text-sm">{{ config.subtitle || '记录生活，分享技术' }}</p>
        <p class="text-gray-400 dark:text-gray-500 text-sm mt-2">
          © {{ new Date().getFullYear() }} {{ config.title || 'Blog' }}
          <span v-if="config.icp" class="ml-2">· {{ config.icp }}</span>
        </p>
      </div>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useAppStore } from '@/stores/app'
import { getSiteConfig } from '@/api/config'

const appStore = useAppStore()
const isDark = computed(() => appStore.isDark)
const config = computed(() => appStore.siteConfig || {})

const showMobileMenu = ref(false)

function toggleTheme() {
  appStore.toggleTheme()
}

onMounted(async () => {
  appStore.initTheme()
  try {
    const res = await getSiteConfig()
    appStore.setSiteConfig(res.data)
  } catch (error) {
    console.error('获取配置失败', error)
  }
})
</script>

<style scoped>
/* 导航项样式 */
.nav-item {
  @apply px-4 py-2 text-sm font-medium rounded-full transition-all duration-200;
  color: #4b5563;
}

.nav-item:hover {
  color: #3b82f6;
  background-color: #f3f4f6;
}

.dark .nav-item {
  color: #e5e7eb;
}

.dark .nav-item:hover {
  color: #60a5fa;
  background-color: #374151;
}

.nav-active {
  color: #3b82f6;
  background-color: rgba(59, 130, 246, 0.1);
}

.dark .nav-active {
  color: #60a5fa;
  background-color: rgba(59, 130, 246, 0.2);
}

/* 移动端导航 */
.nav-item-mobile {
  @apply px-4 py-3 text-sm font-medium rounded-lg transition-colors;
  color: #4b5563;
}

.nav-item-mobile:hover {
  background-color: #f3f4f6;
}

.dark .nav-item-mobile {
  color: #e5e7eb;
}

.dark .nav-item-mobile:hover {
  background-color: #374151;
}

.nav-active-mobile {
  color: #3b82f6;
  background-color: rgba(59, 130, 246, 0.1);
}

.dark .nav-active-mobile {
  color: #60a5fa;
  background-color: rgba(59, 130, 246, 0.2);
}

/* 过渡动画 */
.slide-enter-active,
.slide-leave-active {
  transition: all 0.2s ease;
}

.slide-enter-from,
.slide-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}
</style>