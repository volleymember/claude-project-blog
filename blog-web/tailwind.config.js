/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        primary: {
          light: '#4C8BF5',
          dark: '#409EFF',
        },
        background: {
          light: '#F5F7FA',
          dark: '#1E1E1E',
        },
        text: {
          light: '#333333',
          dark: '#EEEEEE',
        },
        card: {
          light: '#FFFFFF',
          dark: '#2D2D2D',
        },
        border: {
          light: '#E4E7ED',
          dark: '#4C4C4C',
        }
      },
      fontFamily: {
        sans: ['-apple-system', 'BlinkMacSystemFont', 'Segoe UI', 'Roboto', 'Helvetica Neue', 'Arial', 'sans-serif'],
        mono: ['SF Mono', 'Monaco', 'Inconsolata', 'Fira Mono', 'Droid Sans Mono', 'Source Code Pro', 'monospace'],
      }
    },
  },
  plugins: [
    require('@tailwindcss/typography'),
  ],
}