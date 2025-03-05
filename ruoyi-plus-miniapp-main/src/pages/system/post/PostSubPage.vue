<template>
  <tm-app>
    <tm-form
      v-if="loadSuccess"
      :margin="[0]"
      :padding="[10]"
      v-model="formData"
      @submit="handleSubmit"
    >
      <tm-form-item label="岗位名称" field="postName" :rules="formRules.postName">
        <tm-input
          v-model="formData.postName"
          :transprent="true"
          :showBottomBotder="false"
        ></tm-input>
      </tm-form-item>
      <tm-form-item label="岗位编码" field="postCode" :rules="formRules.postCode">
        <tm-input
          v-model="formData.postCode"
          :transprent="true"
          :showBottomBotder="false"
        ></tm-input>
      </tm-form-item>
      <tm-form-item label="排序" field="postSort" :rules="formRules.postSort">
        <tm-input
          v-model="formData.postSort"
          type="number"
          :transprent="true"
          :showBottomBotder="false"
        ></tm-input>
      </tm-form-item>
      <tm-form-item label="岗位状态" field="status" :rules="formRules.status">
        <DictRadioGroup
          v-model="formData.status"
          :dict-value="getDict(DictEnum.NORMAL_DISABLE)"
        ></DictRadioGroup>
      </tm-form-item>
      <tm-form-item :border="false">
        <view class="flex flex-col gap-5">
          <tm-button form-type="submit" block label="提交"></tm-button>
          <tm-button
            color="orange"
            form-type="reset"
            v-if="!isUpdate"
            block
            label="重置"
          ></tm-button>
        </view>
      </tm-form-item>
    </tm-form>
    <tm-skeleton v-if="!loadSuccess"></tm-skeleton>
  </tm-app>
</template>

<script setup lang="ts">
  import { postAdd, postUpdate, postInfo } from '@/api/system/post';
  import type { Post } from '@/api/system/post/model';
  import { onLoad, onPullDownRefresh } from '@dcloudio/uni-app';
  import { ref } from 'vue';
  import type { FormRules } from '@/types/form';
  import { reactive } from 'vue';
  import DictRadioGroup from '@/components/DictRadioGroup/index.vue';
  import { getDict } from '@/utils/dict';
  import { DictEnum } from '@/enums/dictEnum';

  const postId = ref(0);
  const isUpdate = ref(false);
  const loadSuccess = ref(false);
  const formData = ref({
    postName: '',
    postCode: '',
    postSort: 0,
    status: '0',
  });

  const formRules = reactive<FormRules<Post>>({
    postName: [{ required: true, message: '请输入岗位名称' }],
    postCode: [{ required: true, message: '请输入岗位编码' }],
    postSort: [{ required: true, message: '请输入显示顺序' }],
    status: [{ required: true, message: '请选择状态' }],
  });

  const titles = {
    add: '岗位新增',
    update: '岗位修改',
  };

  async function loadInfo() {
    if (postId.value) {
      const ret = await postInfo(postId.value);
      formData.value = {
        ...ret,
      };
    }
  }

  onLoad(async (option) => {
    if (option && option.postId) {
      postId.value = option.postId;
      await loadInfo();
      isUpdate.value = true;
    }
    if (isUpdate.value) {
      uni.setNavigationBarTitle({ title: titles.update });
    } else {
      uni.setNavigationBarTitle({ title: titles.add });
    }
    loadSuccess.value = true;
  });

  function handleSubmit(e: Tmui.tmFormSubmitResult) {
    if (!e.isPass) {
      return;
    }
    uni.showModal({
      title: '提示',
      content: '是否确认提交',
      success: async (res) => {
        if (res.confirm) {
          if (isUpdate.value) {
            await postUpdate({ ...formData.value, postId: postId.value });
          } else {
            await postAdd(formData.value);
          }
          uni.navigateBack({});
        }
      },
    });
  }

  onPullDownRefresh(async () => {
    if (isUpdate.value) {
      await loadInfo();
    }
    uni.stopPullDownRefresh();
  });
</script>

<style scoped></style>
