<template>
  <tm-app>
    <tm-cropimg @cance="handleCance" @confirm="handleConfirm"></tm-cropimg>
  </tm-app>
</template>

<script setup lang="ts">
  import { userUpdateAvatar } from '@/api/system/profile';
  import { useUserStore } from '@/store/modules/user';
  /**
   * 取消 返回上一层页面
   */
  function handleCance() {
    uni.navigateBack();
  }

  const userStore = useUserStore();
  async function handleConfirm(filePath: string) {
    console.log(filePath);
    await userUpdateAvatar(filePath);
    // 重新获取个人信息
    await userStore.getUserInfoAction();
    // 提示
    uni.showToast({
      title: '上传头像成功',
      icon: 'success',
      mask: true,
    });
    // 返回前一页
    uni.navigateBack();
  }
</script>

<style scoped></style>
