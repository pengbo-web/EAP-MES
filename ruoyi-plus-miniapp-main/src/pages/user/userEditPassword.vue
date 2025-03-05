<template>
  <tm-app>
    <tm-form
      v-model="formData"
      :border="false"
      :margin="[20, 20]"
      :padding="[10]"
      ref="formRef"
      @submit="handleSubmit"
    >
      <tm-form-item label="原密码" field="oldPassword" required :rules="formRules.oldPassword">
        <tm-input
          v-model="formData.oldPassword"
          :round="10"
          placeholder="输入原密码"
          password
          show-clear
          :input-padding="[30, 0]"
        ></tm-input>
      </tm-form-item>
      <tm-form-item label="新密码" field="newPassword" required :rules="formRules.newPassword">
        <tm-input
          v-model="formData.newPassword"
          :round="10"
          placeholder="输入新密码"
          show-clear
          password
          :input-padding="[30, 0]"
        ></tm-input>
      </tm-form-item>
      <tm-form-item
        label="确认密码"
        field="confirmPassword"
        required
        :rules="formRules.confirmPassword"
      >
        <tm-input
          v-model="formData.confirmPassword"
          :round="10"
          placeholder="输入确认密码"
          show-clear
          password
          :input-padding="[30, 0]"
        ></tm-input>
      </tm-form-item>
      <tm-form-item>
        <view class="flex flex-col">
          <tm-button form-type="submit" label="修改密码" block></tm-button>
          <tm-button :shadow="0" text form-type="reset" label="重置输入" block></tm-button>
        </view>
      </tm-form-item>
    </tm-form>
  </tm-app>
</template>

<script setup lang="ts">
  import { ref } from 'vue';
  import { userUpdatePassword } from '@/api/system/profile';
  import { useUserStore } from '@/store/modules/user';
  import { reactive } from 'vue';

  const formData = ref({
    oldPassword: '',
    newPassword: '',
    confirmPassword: '',
  });

  /**
   * 为什么要用这种写法
   * 因为懒 又想要ts提示
   * 直接[key : string]是没有提示的
   */
  const formRules = reactive<{ [key in keyof typeof formData.value]: Tmui.tmFormRules[] }>({
    oldPassword: [
      {
        required: true,
        message: '请输入原密码',
        validator: (value: string) => value && value.length > 0,
      },
    ],
    newPassword: [
      { required: true, message: '请输入新密码' },
      {
        required: true,
        message: '密码长度应大于6位',
        validator: (value: string) => value && value.length > 6,
      },
      {
        required: true,
        message: '新密码不能与原密码相同',
        validator: (value: string) => value !== formData.value.oldPassword,
      },
    ],
    confirmPassword: [
      { required: true, message: '请输入确认密码' },
      {
        required: true,
        message: '两次密码不一致',
        validator: (value: string) => value === formData.value.newPassword,
      },
    ],
  });

  const userStore = useUserStore();
  function handleSubmit(e: Tmui.tmFormSubmitResult) {
    if (!e.isPass) return;
    uni.showModal({
      title: '提示',
      content: '是否更新密码',
      success: async function (res) {
        if (res.confirm) {
          try {
            await userUpdatePassword(formData.value);
            await userStore.logout(true);
          } catch (e) {}
        }
      },
    });
  }
</script>

<style scoped></style>
