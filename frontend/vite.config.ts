import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5174,
    proxy: {
      '/analytics': {
        target: 'http://localhost:8087',
        changeOrigin: true,
      },
      '/checkins': {
        target: 'http://localhost:8086',
        changeOrigin: true,
      },
      '/events': {
        target: 'http://localhost:8086',
        changeOrigin: true,
      },
    },
  },
});
