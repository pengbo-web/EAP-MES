<template>
  <tm-app>
    <tm-form
      v-if="loadSuccess"
      :margin="[0]"
      :padding="[10]"
      v-model="formData"
      @submit="handleSubmit"
    >
      <tm-form-item label="参数名称" field="configKey" :rules="formRules.configKey">
        <tm-input
          v-model="formData.configKey"
          :transprent="true"
          :showBottomBotder="false"
        ></tm-input>
      </tm-form-item>
      <tm-form-item label="参数键名" field="configName" :rules="formRules.configName">
        <tm-input
          v-model="formData.configName"
          :transprent="true"
          :showBottomBotder="false"
        ></tm-input>
      </tm-form-item>
      <tm-form-item label="参数键值" field="configValue" :rules="formRules.configValue">
        <tm-input
          v-model="formData.configValue"
          :transprent="true"
          :showBottomBotder="false"
        ></tm-input>
      </tm-form-item>
      <tm-form-item label="系统内置" field="configType" :rules="formRules.configType">
        <DictRadioGroup
          v-model="formData.configType"
          :dict-value="getDict(DictEnum.YES_NO)"
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
  import { configInfo, configAdd, configUpdate } from '@/api/system/config';
  import type { Config } from '@/api/system/config/model';
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
    configKey: '',
    configName: '',
    configType: '',
    configValue: '',
  });

  const formRules = reactive<FormRules<Config>>({
    configKey: [{ required: true, message: '请输入' }],
    configName: [{ required: true, message: '请输入' }],
    configType: [{ required: true, message: '请输入' }],
    configValue: [{ required: true, message: '请选择' }],
  });

  const titles = {
    add: '参数新增',
    update: '参数修改',
  };

  async function loadInfo() {
    if (postId.value) {
      const ret = await configInfo(postId.value);
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
            await configUpdate({ ...formData.value, postId: postId.value });
          } else {
            await configAdd(formData.value);
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
