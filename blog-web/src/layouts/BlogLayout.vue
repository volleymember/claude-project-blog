<template>
  <div class="min-h-screen flex flex-col relative noise-overlay">
    <!-- Decorative background orbs -->
    <div class="fixed inset-0 pointer-events-none overflow-hidden z-0">
      <div class="glow-orb glow-orb-primary w-[500px] h-[500px] -top-40 -right-40 animate-aurora"></div>
      <div class="glow-orb glow-orb-violet w-[400px] h-[400px] top-1/3 -left-48 animate-aurora" style="animation-delay: -5s;"></div>
      <div class="glow-orb glow-orb-cyan w-[350px] h-[350px] bottom-20 right-1/4 animate-aurora" style="animation-delay: -10s;"></div>
    </div>

    <!-- Navigation -->
    <header
      class="sticky top-0 z-50 transition-all duration-500"
      :class="[
        scrolled
          ? 'bg-[#fafcfb]/70 dark:bg-[#0c1512]/70 backdrop-blur-2xl shadow-[0_1px_0_0_var(--color-border)]'
          : 'bg-transparent'
      ]"
    >
      <div class="container-blog">
        <div class="flex items-center justify-between h-16 md:h-[72px]">
          <!-- Logo -->
          <router-link to="/" class="flex items-center gap-2.5 group relative z-10">
            <div class="w-8 h-8 rounded-lg bg-[var(--color-logo)] flex items-center justify-center text-white font-display font-bold text-sm transition-transform duration-300 group-hover:scale-110">
              {{ (config.title || 'B')[0] }}
            </div>
            <span class="text-lg font-display font-bold text-gradient hidden sm:block">
              {{ config.title || 'Blog' }}
            </span>
          </router-link>

          <!-- Desktop Navigation -->
          <nav class="hidden md:flex items-center gap-1">
            <router-link
              v-for="item in navItems"
              :key="item.path"
              :to="item.path"
              class="nav-link"
              :class="{ active: isNavActive(item.path) }"
            >
              <span class="flex items-center gap-1.5">
                <component :is="item.icon" class="w-4 h-4 opacity-70" />
                {{ item.label }}
              </span>
            </router-link>
          </nav>

          <!-- Right Actions -->
          <div class="flex items-center gap-1">
            <!-- Search -->
            <router-link
              to="/search"
              class="w-9 h-9 rounded-xl flex items-center justify-center transition-all duration-300 text-[var(--color-text-tertiary)] hover:text-[var(--color-accent)] hover:bg-[var(--color-accent-soft)]"
              title="搜索"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="w-[18px] h-[18px]" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
              </svg>
            </router-link>

            <!-- Theme Toggle -->
            <button
              @click="toggleTheme"
              class="w-9 h-9 rounded-xl flex items-center justify-center transition-all duration-300 text-[var(--color-text-tertiary)] hover:text-[var(--color-accent)] hover:bg-[var(--color-accent-soft)]"
              :title="isDark ? '切换亮色模式' : '切换暗色模式'"
            >
              <transition name="spin-swap" mode="out-in">
                <!-- Sun icon (shown in dark mode) -->
                <svg v-if="isDark" key="sun" xmlns="http://www.w3.org/2000/svg" class="w-[18px] h-[18px]" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z" />
                </svg>
                <!-- Moon icon (shown in light mode) -->
                <svg v-else key="moon" xmlns="http://www.w3.org/2000/svg" class="w-[18px] h-[18px]" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z" />
                </svg>
              </transition>
            </button>

            <!-- GitHub Link -->
            <a
              v-if="config.github"
              :href="config.github"
              target="_blank"
              rel="noopener noreferrer"
              class="w-9 h-9 rounded-xl flex items-center justify-center transition-all duration-300 text-[var(--color-text-tertiary)] hover:text-[var(--color-accent)] hover:bg-[var(--color-accent-soft)]"
              title="GitHub"
            >
              <svg class="w-[18px] h-[18px]" fill="currentColor" viewBox="0 0 24 24">
                <path fill-rule="evenodd" d="M12 2C6.477 2 2 6.484 2 12.017c0 4.425 2.865 8.18 6.839 9.504.5.092.682-.217.682-.483 0-.237-.008-.868-.013-1.703-2.782.605-3.369-1.343-3.369-1.343-.454-1.158-1.11-1.466-1.11-1.466-.908-.62.069-.608.069-.608 1.003.07 1.531 1.032 1.531 1.032.892 1.53 2.341 1.088 2.91.832.092-.647.35-1.088.636-1.338-2.22-.253-4.555-1.113-4.555-4.951 0-1.093.39-1.988 1.029-2.688-.103-.253-.446-1.272.098-2.65 0 0 .84-.27 2.75 1.026A9.564 9.564 0 0112 6.844c.85.004 1.705.115 2.504.337 1.909-1.296 2.747-1.027 2.747-1.027.546 1.379.202 2.398.1 2.651.64.7 1.028 1.595 1.028 2.688 0 3.848-2.339 4.695-4.566 4.943.359.309.678.92.678 1.855 0 1.338-.012 2.419-.012 2.747 0 .268.18.58.688.482A10.019 10.019 0 0022 12.017C22 6.484 17.522 2 12 2z" clip-rule="evenodd" />
              </svg>
            </a>

            <!-- Mobile Menu Toggle -->
            <button
              @click="showMobileMenu = !showMobileMenu"
              class="md:hidden w-9 h-9 rounded-xl flex items-center justify-center transition-all duration-300 text-[var(--color-text-tertiary)] hover:text-[var(--color-accent)] hover:bg-[var(--color-accent-soft)]"
            >
              <svg v-if="!showMobileMenu" xmlns="http://www.w3.org/2000/svg" class="w-[18px] h-[18px]" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M4 6h16M4 12h16M4 18h16" />
              </svg>
              <svg v-else xmlns="http://www.w3.org/2000/svg" class="w-[18px] h-[18px]" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
        </div>

        <!-- Mobile Menu -->
        <transition name="mobile-menu">
          <div v-if="showMobileMenu" class="md:hidden pb-4">
            <nav class="flex flex-col gap-1 glass-panel p-3">
              <router-link
                v-for="item in navItems"
                :key="item.path"
                :to="item.path"
                class="flex items-center gap-3 px-4 py-3 rounded-xl text-sm font-medium transition-all duration-300"
                :class="[
                  isNavActive(item.path)
                    ? 'bg-[var(--color-accent-soft)] text-[var(--color-accent)]'
                    : 'text-[var(--color-text-secondary)] hover:text-[var(--color-text-primary)] hover:bg-[var(--color-accent-soft)]'
                ]"
                @click="showMobileMenu = false"
              >
                <component :is="item.icon" class="w-4 h-4" />
                {{ item.label }}
              </router-link>
            </nav>
          </div>
        </transition>
      </div>
    </header>

    <!-- Main Content -->
    <main class="flex-1 relative z-10">
      <router-view v-slot="{ Component }">
        <transition name="page-fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>

    <!-- Footer -->
    <footer class="relative z-10 border-t border-[var(--color-border)]">
      <div class="container-blog">
        <!-- Top Section -->
        <div class="py-12 md:py-16 grid grid-cols-1 md:grid-cols-3 gap-8 md:gap-12">
          <!-- Brand -->
          <div class="md:col-span-1">
            <div class="flex items-center gap-2.5 mb-4">
              <div class="w-8 h-8 rounded-lg bg-[var(--color-logo)] flex items-center justify-center text-white font-display font-bold text-sm">
                {{ (config.title || 'B')[0] }}
              </div>
              <span class="text-lg font-display font-bold text-gradient">{{ config.title || 'Blog' }}</span>
            </div>
            <p class="text-sm text-[var(--color-text-tertiary)] leading-relaxed max-w-xs">
              {{ config.subtitle || '用代码构建世界，用文字记录思考' }}
            </p>
          </div>

          <!-- Quick Links -->
          <div>
            <h4 class="text-sm font-semibold text-[var(--color-text-primary)] uppercase tracking-wider mb-4 font-display">导航</h4>
            <nav class="flex flex-col gap-2.5">
              <router-link
                v-for="item in navItems"
                :key="item.path"
                :to="item.path"
                class="text-sm text-[var(--color-text-tertiary)] hover:text-[var(--color-accent)] transition-colors link-animated w-fit"
              >
                {{ item.label }}
              </router-link>
            </nav>
          </div>

          <!-- Contact -->
          <div>
            <h4 class="text-sm font-semibold text-[var(--color-text-primary)] uppercase tracking-wider mb-4 font-display">联系</h4>
            <div class="flex flex-col gap-2.5">
              <a
                v-if="config.github"
                :href="config.github"
                target="_blank"
                rel="noopener noreferrer"
                class="flex items-center gap-2 text-sm text-[var(--color-text-tertiary)] hover:text-[var(--color-accent)] transition-colors link-animated w-fit"
              >
                <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 24 24"><path fill-rule="evenodd" d="M12 2C6.477 2 2 6.484 2 12.017c0 4.425 2.865 8.18 6.839 9.504.5.092.682-.217.682-.483 0-.237-.008-.868-.013-1.703-2.782.605-3.369-1.343-3.369-1.343-.454-1.158-1.11-1.466-1.11-1.466-.908-.62.069-.608.069-.608 1.003.07 1.531 1.032 1.531 1.032.892 1.53 2.341 1.088 2.91.832.092-.647.35-1.088.636-1.338-2.22-.253-4.555-1.113-4.555-4.951 0-1.093.39-1.988 1.029-2.688-.103-.253-.446-1.272.098-2.65 0 0 .84-.27 2.75 1.026A9.564 9.564 0 0112 6.844c.85.004 1.705.115 2.504.337 1.909-1.296 2.747-1.027 2.747-1.027.546 1.379.202 2.398.1 2.651.64.7 1.028 1.595 1.028 2.688 0 3.848-2.339 4.695-4.566 4.943.359.309.678.92.678 1.855 0 1.338-.012 2.419-.012 2.747 0 .268.18.58.688.482A10.019 10.019 0 0022 12.017C22 6.484 17.522 2 12 2z" clip-rule="evenodd" /></svg>
                GitHub
              </a>
              <a
                v-if="config.email"
                :href="`mailto:${config.email}`"
                class="flex items-center gap-2 text-sm text-[var(--color-text-tertiary)] hover:text-[var(--color-accent)] transition-colors link-animated w-fit"
              >
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" /></svg>
                {{ config.email }}
              </a>
            </div>
          </div>
        </div>

        <!-- Bottom Bar -->
        <div class="py-6 border-t border-[var(--color-border)] flex flex-col sm:flex-row items-center justify-between gap-3">
          <p class="text-xs text-[var(--color-text-tertiary)]">
            &copy; {{ new Date().getFullYear() }} {{ config.title || 'Blog' }}<span v-if="config.icp" class="ml-1.5">&middot; {{ config.icp }}</span>
          </p>
          <p class="text-xs text-[var(--color-text-tertiary)]">
            Built with Vue & Spring Boot
          </p>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, h } from 'vue'
import { useRoute } from 'vue-router'
import { useAppStore } from '@/stores/app'
import { getSiteConfig } from '@/api/config'

const route = useRoute()
const appStore = useAppStore()
const isDark = computed(() => appStore.isDark)
const config = computed(() => appStore.siteConfig || {})

const showMobileMenu = ref(false)
const scrolled = ref(false)

// SVG icon components for navigation
const HomeIcon = {
  render() {
    return h('svg', { xmlns: 'http://www.w3.org/2000/svg', fill: 'none', viewBox: '0 0 24 24', 'stroke-width': '2', stroke: 'currentColor' }, [
      h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', d: 'M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6' })
    ])
  }
}

const ArchiveIcon = {
  render() {
    return h('svg', { xmlns: 'http://www.w3.org/2000/svg', fill: 'none', viewBox: '0 0 24 24', 'stroke-width': '2', stroke: 'currentColor' }, [
      h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', d: 'M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10' })
    ])
  }
}

const AboutIcon = {
  render() {
    return h('svg', { xmlns: 'http://www.w3.org/2000/svg', fill: 'none', viewBox: '0 0 24 24', 'stroke-width': '2', stroke: 'currentColor' }, [
      h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', d: 'M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z' })
    ])
  }
}

const navItems = [
  { path: '/', label: '首页', icon: HomeIcon },
  { path: '/archive', label: '归档', icon: ArchiveIcon },
  { path: '/about', label: '关于', icon: AboutIcon },
]

function isNavActive(path: string) {
  if (path === '/') return route.path === '/'
  return route.path.startsWith(path)
}

function toggleTheme() {
  appStore.toggleTheme()
}

function handleScroll() {
  scrolled.value = window.scrollY > 20
}

onMounted(async () => {
  appStore.initTheme()
  window.addEventListener('scroll', handleScroll, { passive: true })
  handleScroll()

  try {
    const res = await getSiteConfig()
    appStore.setSiteConfig(res.data)
  } catch (error) {
    console.error('获取配置失败', error)
  }
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
/* Page transition */
.page-fade-enter-active {
  transition: opacity 0.3s ease, transform 0.3s cubic-bezier(0.16, 1, 0.3, 1);
}

.page-fade-leave-active {
  transition: opacity 0.15s ease;
}

.page-fade-enter-from {
  opacity: 0;
  transform: translateY(8px);
}

.page-fade-leave-to {
  opacity: 0;
}

/* Theme toggle spin swap */
.spin-swap-enter-active,
.spin-swap-leave-active {
  transition: all 0.25s cubic-bezier(0.16, 1, 0.3, 1);
}

.spin-swap-enter-from {
  opacity: 0;
  transform: rotate(-90deg) scale(0.8);
}

.spin-swap-leave-to {
  opacity: 0;
  transform: rotate(90deg) scale(0.8);
}

/* Mobile menu transition */
.mobile-menu-enter-active {
  transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
}

.mobile-menu-leave-active {
  transition: all 0.2s ease;
}

.mobile-menu-enter-from {
  opacity: 0;
  transform: translateY(-8px);
}

.mobile-menu-leave-to {
  opacity: 0;
  transform: translateY(-8px);
}
</style>
