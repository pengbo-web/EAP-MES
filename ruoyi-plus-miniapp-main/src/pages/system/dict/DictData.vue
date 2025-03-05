<template>
  <tm-app>
    <view v-if="dataList">
      <!-- total为0 -->
      <tm-result v-if="dataList.total === 0" status="empty" :show-btn="false"></tm-result>
      <!-- 加载数据 -->
      <tm-card v-for="(item, index) in dataList.rows" :key="index" :margin="[0, 10]" :round="0">
        <template #title>
          <view class="flex flex-row flex-row-center-center gap-10">
            <tm-text label="标签: " class="text-weight-b"></tm-text>
            <DictTag :dict-value="dataList.rows" :value="item.dictValue"></DictTag>
          </view>
        </template>
        <template #content>
          <tm-descriptions :label-width="150" :width="686" :column="1" :round="1" text>
            <tm-descriptions-item :font-size="25" label="字典键值" :value="item.dictValue">
            </tm-descriptions-item>
            <tm-descriptions-item :font-size="25" label="字典排序" :value="String(item.dictSort)">
            </tm-descriptions-item>
            <tm-descriptions-item :font-size="25" label="备注" :value="item.remark">
            </tm-descriptions-item>
            <tm-descriptions-item :font-size="25" label="创建时间" :value="item.createTime">
            </tm-descriptions-item>
          </tm-descriptions>
        </template>
        <template #action>
          <view class="flex flex-end fulled gap-10">
            <tm-button :padding="[20, 20]" size="small" label="修改"></tm-button>
            <tm-button color="red" size="small" label="删除"></tm-button>
          </view>
        </template>
      </tm-card>
    </view>
    <!-- 为空的时候加载骨架 -->
    <tm-skeleton v-if="!dataList"></tm-skeleton>
  </tm-app>
</template>

<script setup lang="ts">
  import { onLoad } from '@dcloudio/uni-app';
  import { dictDataList } from '@/api/system/dict/dictData';
  import type { DictData } from '@/api/system/dict/dictData.model';
  import { ref } from 'vue';
  import type { PageResult } from '@/api/base';
  import DictTag from '@/components/DictTag/index.vue';

  const dictType = ref<string>('');
  const dataList = ref<PageResult<DictData>>();

  onLoad(async (option) => {
    if (option && option.dictType) {
      dictType.value = option.dictType;
      const ret = await dictDataList({ dictType: dictType.value });
      console.log(ret);
      dataList.value = ret;
    }
  });
</script>

<style scoped></style>
