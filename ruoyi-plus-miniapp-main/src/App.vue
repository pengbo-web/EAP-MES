<template> </template>
<script lang="ts" setup>
  import { onLaunch } from '@dcloudio/uni-app';
  import { useUserStore } from '@/store/modules/user';
  import { getToken } from './utils/auth';

  const userStore = useUserStore();
  /**
   * 启动时判断是否登录
   * 只会在应用打开时调用一次
   * 在微信没有问题  但是H5中可以通过地址栏跳转 当然会显示登录过期弹窗  但是逻辑不太正确
   */
  onLaunch(async () => {
    if (getToken()) {
      await userStore.afterLoginAction();
    } else {
      userStore.resetUserState(true);
    }
  });
</script>


<style lang="less">
  /* #ifdef APP-PLUS-NVUE */
  @import './tmui/scss/nvue.css';
  /* #endif */
  /* #ifndef APP-PLUS-NVUE */
  @import './tmui/scss/noNvue.css';
  /* #endif */

  /** 自带的图标太少 使用图鸟的图标库  使用时加上前缀tn-icon就行  */
  /** https://vue3.tuniaokj.com/zh-CN/component/icon.html#%E5%9B%BE%E6%A0%87%E5%B1%95%E7%A4%BA */
  @import '@tuniao/tn-icon/dist/index.css';

  /* #ifdef H5 */
  /* 隐藏H5的滚动条  */
  html {
    overflow-x: hidden;
  }
  body::-webkit-scrollbar {
    display: none;
  }
  /* #endif */
</style>
<style lang="scss">
	@import "@/uni_modules/vk-uview-ui/index.scss";
</style>
