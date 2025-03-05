<template>
  <tm-app>
    <!-- 导航栏 -->
    <tm-navbar v-if="useCustomNavBar" :title="title" :hide-home="true"></tm-navbar>
    <slot name="default"></slot>
    <!-- 底部的tab-bar -->
    <tab-bar />
  </tm-app>
</template>

<script lang="ts">
  import { defineComponent } from 'vue';
  import TabBar from '@/components/tabbar/TabBar.vue';
  import { onLoad } from '@dcloudio/uni-app';

  export default defineComponent({
    name: 'TabBarPageWrapper',
    components: {
      TabBar,
    },
    props: {
      title: {
        type: String,
        default: '',
      },
      useCustomNavBar: {
        type: Boolean,
        default: false,
      },
    },
    setup() {
      /**
       * 隐藏原生tabbar
       */
      onLoad(() => {
        uni.hideTabBar();
      });
    },
  });
</script>

<style scoped></style>
