<template>
  <tm-app>
    <tm-form
      v-if="loadSuccess"
      v-model="formData"
      @submit="handleSubmit"
      :border="true"
      :label-width="120"
      :margin="[15]"
      :padding="[0, 0]"
    >
      <tm-form-item label="昵称" field="nickName" :rules="formRules.nickName">
        <tm-input
          placeholder="输入昵称"
          v-model="formData.nickName"
          :showBottomBotder="true"
          :transprent="true"
        ></tm-input>
      </tm-form-item>
      <tm-form-item label="邮箱" field="email" :rules="formRules.email">
        <tm-input
          placeholder="输入邮箱地址"
          v-model="formData.email"
          :showBottomBotder="false"
          :transprent="true"
        ></tm-input>
      </tm-form-item>
      <tm-form-item label="手机号" field="phonenumber" :rules="formRules.phonenumber">
        <tm-input
          placeholder="请输入联系电话"
          v-model="formData.phonenumber"
          :showBottomBotder="false"
          :transprent="true"
        ></tm-input>
      </tm-form-item>
      <tm-form-item label="性别" field="sex" :rules="formRules.sex">
        <tm-radio-group v-model="formData.sex">
          <tm-radio color="blue" label="男" value="0"></tm-radio>
          <tm-radio color="pink" label="女" value="1"></tm-radio>
        </tm-radio-group>
      </tm-form-item>
      <tm-form-item :border="false">
        <tm-button form-type="submit" block label="修改信息"></tm-button>
      </tm-form-item>
    </tm-form>
    <tm-skeleton v-if="!loadSuccess"></tm-skeleton>
  </tm-app>
</template>

<script setup lang="ts">
  import { isEmail, isPhone } from '@/tmui/tool/function/util';
  import { ref, reactive } from 'vue';
  import { userProfile, userProfileUpdate } from '@/api/system/profile';
  import { onLoad, onPullDownRefresh } from '@dcloudio/uni-app';
  import { useUserStore } from '@/store/modules/user';
  import { PageEnum } from '@/enums/pageEnum';
  import { FormRules } from '@/types/form';

  const loadSuccess = ref(false);
  async function loadInfo() {
    const { user } = await userProfile();
    formData.value.email = user.email;
    formData.value.nickName = user.nickName;
    formData.value.phonenumber = user.phonenumber;
    formData.value.sex = user.sex;
    formData.value.userId = user.userId;
    loadSuccess.value = true;
  }

  onLoad(async () => {
    await loadInfo();
  });

  onPullDownRefresh(async () => {
    loadSuccess.value = false;
    await loadInfo().finally(() => {
      uni.stopPullDownRefresh();
    });
  });

  const formData = ref({
    nickName: '',
    email: '',
    phonenumber: '',
    sex: '',
    userId: 0,
  });

  const formRules = reactive<FormRules<typeof formData.value>>({
    nickName: [
      {
        required: true,
        message: '请输入昵称',
        validator: (value: string) => value && value.length > 0,
      },
    ],
    email: [
      {
        required: true,
        message: '请输入邮箱地址',
        validator: (value: string) => value && value.length > 0,
      },
      {
        required: true,
        message: '输入正确的邮箱地址',
        validator: (value: string) => isEmail(value),
      },
    ],
    phonenumber: [
      {
        required: true,
        message: '请输入联系电话',
        validator: (value: string) => value && value.length > 0,
      },
      {
        required: true,
        message: '输入正确的联系电话',
        validator: (value: string) => isPhone(value),
      },
    ],
    sex: [
      {
        required: true,
        message: '请选择性别',
        validator: (value: string) => value && value.length > 0,
      },
    ],
  });

  function handleSubmit(e: Tmui.tmFormSubmitResult) {
    if (!e.isPass) {
      return;
    }
    uni.showModal({
      title: '提示',
      content: '是否更新个人信息',
      success: async function (res) {
        if (res.confirm) {
          await userProfileUpdate(formData.value);
          // 重新获取&&设置到store
          await useUserStore().getUserInfoAction();
          // 跳转到页面
          uni.switchTab({ url: PageEnum.USER });
        }
      },
    });
  }
</script>

<style scoped></style>
