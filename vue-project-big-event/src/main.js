// main.js
import './assets/main.scss'
import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import router from '@/router'
import { createPinia } from 'pinia'
import { createPersistedState } from 'pinia-persistedstate-plugin'
import App from './App.vue'

const pinia = createPinia()
const persist = createPersistedState()

const app = createApp(App)

app.use(pinia)
pinia.use(persist)
app.use(ElementPlus)
app.use(router)

app.mount('#app')