import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5174,
    proxy: {
      '/api/checkin': {
        target: 'http://localhost:8086',
        changeOrigin: true,
      },
      '/api/analytics': {
        target: 'http://localhost:8087',
        changeOrigin: true,
      },
    },
  },
});
