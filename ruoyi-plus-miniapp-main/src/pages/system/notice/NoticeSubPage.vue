<template>
  <tm-app v-if="notice">
    <view class="flex flex-row flex-row-center-center mt-30 mb-30">
      <tm-text class="text-weight-b" :label="notice.noticeTitle"></tm-text>
    </view>
    <tm-html :content="notice.noticeContent"></tm-html>
  </tm-app>
</template>

<script setup lang="ts">
  import { noticeInfo } from '@/api/system/notice';
  import type { Notice } from '@/api/system/notice/model';
  import { onLoad, onPullDownRefresh } from '@dcloudio/uni-app';
  import { ref } from 'vue';

  const notice = ref<Notice>();
  const noticeId = ref<number>(0);

  async function load() {
    notice.value = await noticeInfo(noticeId.value);
  }

  onLoad(async (option) => {
    if (option && option.noticeId) {
      noticeId.value = option.noticeId;
      await load();
    }
  });

  onPullDownRefresh(async () => {
    await load();
    uni.stopPullDownRefresh();
  });
</script>

<style scoped></style>
