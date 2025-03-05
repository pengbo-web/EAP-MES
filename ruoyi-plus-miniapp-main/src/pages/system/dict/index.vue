<template>
  <tm-app>
    <!-- 筛选 写在class里的话没有效果 -->
    <tm-sticky style="position: fixed; z-index: 1" offset="stop">
      <template #sticky>
        <tm-filterMenu :fixed="true" @on-button="handleClick">
          <tm-filterMenu-item
            is-button
            icon="tmicon-plus-circle-fill"
            un-icon="tmicon-plus-circle"
            un-font-color="black"
            font-color="black"
            title="新增字典"
          >
          </tm-filterMenu-item>
          <tm-filterMenu-item
            icon="tmicon-redo"
            un-icon="tmicon-redo"
            is-button
            title="刷新缓存"
            un-font-color="black"
            font-color="black"
          >
          </tm-filterMenu-item>
          <tm-filterMenu-item
            icon="tmicon-filter-fill"
            un-icon="tmicon-filter"
            :height="450"
            title="筛选"
          >
            <view class="fulled flex flex-col gap-20">
              <view class="fulled flex flex-col flex-col-left-center gap-10 px-15">
                <tm-text label="字典名称" :font-size="28"></tm-text>
                <tm-input
                  class="flex-1"
                  v-model="formData.dictName"
                  placeholder="输入字典名称"
                ></tm-input>
              </view>
              <view class="fulled flex flex-col flex-col-left-center gap-10 px-15">
                <tm-text label="字典类型" :font-size="28"></tm-text>
                <tm-input
                  class="flex-1"
                  v-model="formData.dictType"
                  placeholder="输入字典类型"
                ></tm-input>
              </view>
              <view class="fulled flex flex-row-center-center gap-15">
                <tm-button size="small" class="flex-1" @click="loadInfo">搜索</tm-button>
                <tm-button color="orange" class="flex-1" size="small" @click="handleReset"
                  >重置</tm-button
                >
              </view>
            </view>
          </tm-filterMenu-item>
        </tm-filterMenu>
      </template>
    </tm-sticky>
    <tm-divider></tm-divider>
    <view style="margin-top: 45rpx"></view>

    <view v-if="dictTypes">
      <!-- total为0 -->
      <tm-result v-if="dictTypes.total === 0" status="empty" :show-btn="false"></tm-result>
      <!-- 展示结果 -->
      <tm-card
        v-for="(item, index) in dictTypes.rows"
        :key="index"
        :margin="[0, 10]"
        :round="0"
        :title="item.dictName"
      >
        <template #content>
          <tm-descriptions :label-width="150" :width="680" :column="1" text>
            <tm-descriptions-item
              class="mt-10 mb-10"
              :font-size="26"
              label="ID"
              :value="String(item.dictId)"
            ></tm-descriptions-item>
            <tm-descriptions-item
              class="mt-10 mb-10"
              :font-size="26"
              label="字典类型"
              :value="item.dictType"
            ></tm-descriptions-item>
            <tm-descriptions-item
              class="mt-10 mb-10"
              :font-size="26"
              label="备注"
              :value="item.remark"
            ></tm-descriptions-item>
            <tm-descriptions-item
              class="mt-10 mb-10"
              :font-size="26"
              label="创建时间"
              :value="item.createTime"
            ></tm-descriptions-item>
          </tm-descriptions>
        </template>
        <template #action>
          <view class="fulled flex flex-end gap-10">
            <tm-button
              color="green"
              size="small"
              label="详情设置"
              @click="toDictDataPage(item.dictType)"
            ></tm-button>
            <tm-button size="small" label="编辑" @click="handleEdit(item.dictId)"></tm-button>
            <tm-button
              color="red"
              size="small"
              label="删除"
              @click="handleRemove(item.dictId)"
            ></tm-button>
          </view>
        </template>
      </tm-card>
      <tm-divider real-color :label="countText"></tm-divider>
    </view>
    <!-- 为空加载骨架屏-->
    <tm-skeleton v-if="!dictTypes"></tm-skeleton>
  </tm-app>
</template>

<script setup lang="ts">
  import { PageResult } from '@/api/base';
  import { dictList, dictTypeRemove, refreshDictTypeCache } from '@/api/system/dict/dictType';
  import { DictType } from '@/api/system/dict/dictType.model';
  import { ref } from 'vue';
  import { onLoad, onPullDownRefresh } from '@dcloudio/uni-app';
  import { language$t } from '@/tmui/tool/lib/language';
  import { reactive } from 'vue';
  import { computed } from 'vue';

  async function handleClick(_: boolean, index: number) {
    switch (index) {
      case 0:
        uni.navigateTo({ url: '/pages/system/dict/DictTypeSubPage' });
        break;
      case 1:
        await refreshDictTypeCache();
        break;
    }
  }
  /**
   * 数据较少 没有用虚拟滚动
   */
  const dictTypes = ref<PageResult<DictType> | null>();
  const formData = reactive({
    dictName: '',
    dictType: '',
  });
  const countText = computed(() => {
    return `共${dictTypes.value?.total}条数据`;
  });

  async function loadInfo() {
    const ret = await dictList(formData);
    console.log(ret);
    dictTypes.value = ret;
  }

  onLoad(async () => {
    await loadInfo();
  });

  onPullDownRefresh(async () => {
    await handleReset().finally(() => {
      uni.stopPullDownRefresh();
    });
  });

  async function handleReset() {
    Object.assign(formData, {
      dictName: '',
      dictType: '',
    });
    await loadInfo();
  }

  function handleEdit(dictId: number) {
    uni.navigateTo({ url: '/pages/system/dict/DictTypeSubPage?dictId=' + dictId });
  }

  function toDictDataPage(dictType: string) {
    uni.navigateTo({ url: '/pages/system/dict/DictData?dictType=' + dictType });
  }

  function handleRemove(dictId: number) {
    uni.showModal({
      title: language$t('message.info.text'),
      content: language$t('message.confirm.delete'),
      success: async function (res) {
        if (res.confirm) {
          await dictTypeRemove([dictId]);
          await loadInfo();
        }
      },
    });
  }
</script>

<style scoped></style>
