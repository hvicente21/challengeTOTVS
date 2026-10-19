import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/meetings': 'http://localhost:8080',
      '/transcricoes': 'http://localhost:8080',
      '/resultados': 'http://localhost:8080',
      '/alertas': 'http://localhost:8080',
      '/palavras-chave': 'http://localhost:8080'
    }
  }
});
