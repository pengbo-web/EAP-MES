<template>
  <tm-app>
    <tm-form :margin="[0]" :padding="[10]" v-model="dictType" @submit="handleSubmit">
      <tm-form-item label="字典名称" field="dictName" required :rules="rules.dictName">
        <tm-input
          v-model="dictType.dictName"
          :transprent="true"
          :showBottomBotder="false"
        ></tm-input>
      </tm-form-item>
      <tm-form-item label="字典类型" field="dictType" required :rules="rules.dictType">
        <tm-input
          v-model="dictType.dictType"
          :transprent="true"
          :showBottomBotder="false"
        ></tm-input>
      </tm-form-item>
      <tm-form-item label="备注" field="remark">
        <tm-input
          type="textarea"
          v-model="dictType.remark"
          :transprent="true"
          :showBottomBotder="false"
        ></tm-input>
      </tm-form-item>
      <tm-form-item :border="false">
        <tm-button form-type="submit" block label="确认修改"></tm-button>
      </tm-form-item>
    </tm-form>
    <tm-skeleton v-if="isUpdate && !dictType"></tm-skeleton>
  </tm-app>
</template>

<script setup lang="ts">
  import { onLoad, onPullDownRefresh } from '@dcloudio/uni-app';
  import { dictTypeInfo, dictTypeUpdate, dictTypeAdd } from '@/api/system/dict/dictType';
  import { DictType } from '@/api/system/dict/dictType.model';
  import { ref, reactive, unref } from 'vue';
  import { language$t } from '@/tmui/tool/lib/language';
  import { FormRules } from '@/types/form';

  const dictType = ref({
    dictName: '',
    dictType: '',
    remark: '',
  });
  const dictId = ref<number>(0);
  const isUpdate = ref<boolean>(false);
  const title = ref<string>('字典新增');

  const rules = reactive<FormRules<DictType>>({
    dictName: [{ required: true, message: '请输入字典名称' }],
    dictType: [{ required: true, message: '请输入字典类型' }],
  });

  async function loadInfo() {
    const ret = await dictTypeInfo(dictId.value);
    console.log(ret);
    dictType.value = ret;
  }

  onLoad(async (options) => {
    if (options && options.dictId) {
      isUpdate.value = true;
      title.value = '字典修改';

      dictId.value = options.dictId;
      await loadInfo();
    }
    uni.setNavigationBarTitle({ title: title.value });
  });

  onPullDownRefresh(async () => {
    if (!unref(isUpdate)) {
      uni.stopPullDownRefresh();
      return;
    }
    await loadInfo().finally(() => {
      uni.stopPullDownRefresh();
    });
  });

  function handleSubmit(e: Tmui.tmFormSubmitResult) {
    if (!e.isPass) {
      return;
    }
    const content = unref(isUpdate)
      ? language$t('message.confirm.update')
      : language$t('message.confirm.add');
    uni.showModal({
      title: language$t('message.info.text'),
      content,
      success: async function (res) {
        if (!res.confirm) {
          return;
        }
        if (unref(isUpdate)) {
          await dictTypeUpdate({ ...dictType.value, dictId: dictId.value });
        } else {
          await dictTypeAdd(dictType.value);
        }
      },
    });
  }
</script>

<style scoped></style>
