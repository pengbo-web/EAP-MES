<template>
  <tm-app>
    <tm-sheet :border="1" :margin="[0]" :padding="[0]">
      <tm-cell
        :margin="[15, 0]"
        :padding="[20]"
        :titleFontSize="30"
        :avatar="userData?.avatar || ''"
        title="头像"
        right-icon=""
        bottomBorder
      >
        <template #right>
          <tm-avatar
            v-if="userData?.avatar"
            :img="userData?.avatar || ''"
            :round="20"
            :size="120"
          ></tm-avatar>
          <tm-avatar v-else :img="globConfig.defaultAvatar" :round="20" :size="120"></tm-avatar>
        </template>
      </tm-cell>
      <tm-cell
        :margin="[10, 0]"
        title="昵称"
        :rightText="userData?.nickName"
        rightIcon=""
        bottomBorder
      >
      </tm-cell>
      <tm-cell :margin="[10, 0]" title="账号" :rightText="userData?.userName" rightIcon="">
      </tm-cell>
    </tm-sheet>
    <tm-sheet :margin="[0, 25]" :padding="[0]" :border="1">
      <tm-cell
        bottomBorder
        :margin="[10, 0]"
        title="邮箱"
        :rightText="userData?.email"
        rightIcon=""
      >
      </tm-cell>
      <tm-cell
        bottomBorder
        :margin="[10, 0]"
        title="手机号"
        :rightText="userData?.phonenumber"
        rightIcon=""
      >
      </tm-cell>
      <tm-cell
        bottomBorder
        :margin="[10, 0]"
        title="部门"
        :rightText="userData?.dept.deptName"
        rightIcon=""
      >
      </tm-cell>
      <tm-cell
        bottomBorder
        :margin="[10, 0]"
        title="岗位"
        :rightText="userData?.postName || '无'"
        rightIcon=""
      >
      </tm-cell>
      <tm-cell
        bottomBorder
        :margin="[10, 0]"
        title="登录IP"
        :rightText="userData?.loginIp"
        rightIcon=""
      >
      </tm-cell>
      <tm-cell :margin="[10, 0]" title="上次登陆" :rightText="userData?.loginDate" rightIcon="">
      </tm-cell>
    </tm-sheet>
    <view class="mx-15 overflow">
      <tm-button
        icon="tmicon-undo"
        block
        :linear-color="['#ea3c2d', '#ff9d14']"
        color="orange"
        font-color="white"
        linear="left"
        @click="changestat"
        >注销账号</tm-button
      >
    </view>
  </tm-app>
</template>

<script setup lang="ts">
  import { userProfile } from '@/api/system/profile';
  import type { User } from '@/api/system/profile/model';
  import { onLoad, onPullDownRefresh } from '@dcloudio/uni-app';
  import { ref } from 'vue';
  import { useUserStore } from '@/store/modules/user';
import { userStatusChange } from '@/api/system/user';
  import { globConfig } from '@/config';

  const userData = ref<User | null>(null);
  const userStore = useUserStore();
  async function changestat(){
	  userData.value.status="1"
	  userStatusChange(userData.value)
	  userStore.confirmLogOut()
  }
  async function loadUserInfo() {
    const ret = await userProfile();
    if (!ret.user.avatar) {
      ret.user.avatar = globConfig.defaultAvatar;
    }
    userData.value = ret.user;
    userData.value.postName = ret.postGroup;
    console.log(ret);
  }

  // 岗位是单独的
  onLoad(async () => {
    await loadUserInfo();
  });

  onPullDownRefresh(async () => {
    await loadUserInfo().finally(() => {
      uni.stopPullDownRefresh();
    });
  });
</script>

<style scoped></style>
