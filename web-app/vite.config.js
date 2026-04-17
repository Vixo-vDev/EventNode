import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

/**
 * FLUJO DE DATOS (EventNode)
 * Rol del archivo: configuracion de build/dev server para la app web.
 * Por que existe: define proxy /api para enrutar peticiones al backend durante desarrollo.
 */
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
