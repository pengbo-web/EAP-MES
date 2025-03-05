import { defineConfig } from 'vite';
import uni from '@dcloudio/vite-plugin-uni';
import vueJsx from '@vitejs/plugin-vue-jsx';
import { resolve } from 'path';
import pkg from './package.json';
import moment from 'moment';

const { dependencies, devDependencies, name, version } = pkg;
const __APP_INFO__ = {
  pkg: { dependencies, devDependencies, name, version },
  lastBuildTime: moment().format('YYYY-MM-DD HH:mm:ss'),
};

// import Components from 'unplugin-vue-components/vite'
// https://vitejs.dev/config/
export default defineConfig({
  build: {
    target: 'es6',
  },
  resolve: {
    alias: [
      {
        find: '@',
        replacement: resolve(__dirname, 'src'),
      },
    ],
  },
  server: {
    port: 1314,
    // 选项写法
    proxy: {
      '/pag': {
        target: 'https://cdn.tmui.design',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '/api'),
      },
    },
  },
  plugins: [uni(), vueJsx()],
  define: {
    __APP_INFO__: JSON.stringify(__APP_INFO__),
  },
});
