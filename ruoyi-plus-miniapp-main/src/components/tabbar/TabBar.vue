<template>
  <view>
    <!-- tabbar高度为75px -->
    <!-- 生成一个占位元素 高度为tabbar高度)+safeTop 由于iphone系列下面会有一个返回条 所以不统一 需要计算 -->
    <!-- place属性用来占位 会自动计算有没有包含安全区 -->
    <tm-tabbar place :shadow="20" :autoSelect="false" v-model:active="activeIndex">
      <tm-tabbar-item
        v-for="(item, index) in tabBarList"
        :key="item.url"
        v-bind="item"
        :text="transformText(item.text)"
        @click="setActiveIndex(index)"
      >
      </tm-tabbar-item>
    </tm-tabbar>
  </view>
</template>

<script setup lang="ts">
  import { useTabBarStore } from '@/store/modules/tabbar';
  import { storeToRefs } from 'pinia';
  import { getCurrentPageUrl } from '@/utils/page';
  import { onLoad } from '@dcloudio/uni-app';
  import { language } from '@/tmui/tool/lib/language';

  function transformText(text: string) {
    if (text.startsWith('$')) {
      return language(text.substring(1));
    }
    return text;
  }

  const tabBarStore = useTabBarStore();
  const { activeIndex, tabBarList } = storeToRefs(tabBarStore);
  // 除了拦截器  这里也要设置  因为H5可能是从地址栏进入的
  onLoad(() => {
    const currentUrl = getCurrentPageUrl();
    const currentIndex = tabBarList.value.findIndex((item) => item.url.includes(currentUrl));
    if (currentIndex === -1) {
      console.warn(`未找到对应的tabbar ${currentUrl}`);
      throw new Error('tabbar错误');
    }
    tabBarStore.setActiveIndex(currentIndex);
  });

  /**
   * 这里其实可以不要的  因为已经使用了sitchTab拦截器  在拦截器里会自动设置index
   * 因为要跳转  肯定得用switchTab
   * @param index index
   */
  function setActiveIndex(index: number) {
    tabBarStore.setActiveIndex(index);
  }
</script>

<style scoped></style>
