<template>
  <tm-app id="app">
    <!-- 筛选 写在class里的话没有效果 -->
    <tm-sticky offset="stop">
      <template #sticky>
        <tm-filterMenu ref="filter" :fixed="true">
          <tm-filterMenu-item
            icon="tmicon-info-circle-fill"
            un-icon="tmicon-info-circle"
            :height="170"
            title="公告类型"
          >
            <view class="fulled flex flex-col gap-5">
              <DictRadioGroup
                class="flex fulled px-15"
                v-model="commonSearch.noticeType"
                model="radio"
                :dict-value="getDict(DictEnum.NOTICE_TYPE)"
              ></DictRadioGroup>
              <view class="flex flex-row-center-center gap-15 px-15">
                <tm-button
                  block
                  :height="68"
                  size="small"
                  class="flex-1"
                  @click="handleSearchSubmit"
                  >搜索</tm-button
                >
                <tm-button
                  block
                  :height="68"
                  size="small"
                  class="flex-1"
                  color="orange"
                  @click="handleSearchReset"
                  >重置</tm-button
                >
              </view>
            </view>
          </tm-filterMenu-item>
          <tm-filterMenu-item
            icon="tmicon-filter-fill"
            un-icon="tmicon-filter"
            :height="260"
            title="筛选"
          >
            <view class="fulled flex flex-col gap-12">
              <view class="flex flex-col flex-col-left-center gap-10 px-15">
                <tm-text label="公告标题" :font-size="28"></tm-text>
                <tm-input
                  class="flex-1"
                  :border="1"
                  placeholder="输入公告标题"
                  v-model="search.noticeTitle"
                ></tm-input>
              </view>
              <view class="flex flex-row-center-center gap-15 px-15">
                <tm-button
                  block
                  :height="68"
                  size="small"
                  class="flex-1"
                  @click="handleSearchSubmit"
                  >搜索</tm-button
                >
                <tm-button
                  block
                  :height="68"
                  size="small"
                  class="flex-1"
                  color="orange"
                  @click="handleSearchReset"
                  >重置</tm-button
                >
              </view>
            </view>
          </tm-filterMenu-item>
        </tm-filterMenu>
      </template>
    </tm-sticky>
    <!-- 没有搜索到结果 -->
    <tm-result v-if="page.total === 0 && !loading" status="empty" :show-btn="false"></tm-result>
    <!-- 数据展示 -->
    <tm-scrolly ref="scrollRef" :height="listHeight" :refresher="false" @bottom="handleLoad">
      <tm-card
        v-for="(item, index) in page.list"
        :key="index"
        :margin="[0, 15]"
        :round="0"
        title="通知/公告"
        :status="item.createTime"
      >
        <template #content>
          <tm-descriptions :label-width="150" :width="686" :column="1" :round="1" text>
            <tm-descriptions-item
              :font-size="descriptionFontHeight"
              label="标题"
              :value="item.noticeTitle"
            ></tm-descriptions-item>
            <tm-descriptions-item
              :font-size="descriptionFontHeight"
              label="公告ID"
              :value="String(item.noticeId)"
            ></tm-descriptions-item>
            <tm-descriptions-item
              :font-size="descriptionFontHeight"
              label="公告类型"
              :value="item.noticeType"
            >
              <view>
                <DictTag
                  :dict-value="getDict(DictEnum.NOTICE_TYPE)"
                  :value="item.noticeType"
                ></DictTag>
              </view>
            </tm-descriptions-item>
            <tm-descriptions-item
              :font-size="descriptionFontHeight"
              label="公告状态"
              :value="item.noticeType"
            >
              <DictTag :dict-value="getDict(DictEnum.NOTICE_STATUS)" :value="item.status"></DictTag>
            </tm-descriptions-item>
          </tm-descriptions>
        </template>
        <template #action>
          <view class="fulled flex flex-start gap-15">
            <tm-button
              class="flex-1"
              :margin="[0, 5]"
              block
              color="blue"
              :height="68"
              :font-size="25"
              size="small"
              icon="tmicon-edit"
              label="查看"
              @click="handleInfo(item.noticeId)"
            ></tm-button>
            <tm-button
              class="flex-1"
              :margin="[0, 5]"
              block
              color="deep-orange"
              icon="tmicon-delete-fill"
              :height="68"
              :font-size="25"
              size="small"
              label="删除"
              @click="handleRemove(item)"
            ></tm-button>
          </view>
        </template>
      </tm-card>
      <!-- 加载骨架屏 -->
      <tm-skeleton v-if="loading && !finish"></tm-skeleton>
    </tm-scrolly>
    <!-- 返回顶部 -->
    <tm-float-button
      :disabledScrollTo="true"
      position="br"
      :width="100"
      :height="100"
      :offset="[25, 256]"
      :btn="{ icon: 'tmicon-angle-up', linear: '', color: 'white', label: '' }"
      @click="toTop"
    ></tm-float-button>
    <!-- 显示count -->
    <view v-if="page.total > 0" :margin="[0]" class="flex flex-col" style="height: 30px">
      <view class="fulled-height flex flex-row flex-row-center-center">
        <tm-divider :label="totalText"></tm-divider>
      </view>
    </view>
  </tm-app>
</template>

<script setup lang="ts">
  import { noticeList, noticeRemove } from '@/api/system/notice';
  import type { Notice } from '@/api/system/notice/model';
  import { onLoad, onPullDownRefresh } from '@dcloudio/uni-app';
  import { ref } from 'vue';
  import { getDict } from '@/utils/dict';
  import { DictEnum } from '@/enums/dictEnum';
  import { reactive } from 'vue';
  import tmFilterMenu from '@/tmui/components/tm-filterMenu/tm-filterMenu.vue';
  import { computed } from 'vue';
  import DictRadioGroup from '@/components/DictRadioGroup/index.vue';
  import { onMounted } from 'vue';
  import TmScrolly from '@/tmui/components/tm-scrolly/tm-scrolly.vue';
  import { useWindowInfo } from '@/tmui/tool/useFun/useWindowInfo';
  import { language$t } from '@/tmui/tool/lib/language';

  const scrollRef = ref<InstanceType<typeof TmScrolly> | null>(null);
  function toTop() {
    scrollRef.value?.scrollToTop();
  }

  /**
   * 动态计算高度
   */
  const listHeight = ref<number>(1000);
  onMounted(() => {
    /**
     * 获取页面高度 减去上面筛选的高度 再减去下面底部的高度 单位px
     * 返回需要转换为rpx
     */
    const windowInfo = useWindowInfo();
    /**
     * h5获取的height是所有元素的height
     * 微信获取的height是不包标题栏的
     * 但是h5的标题高度会在top属性显示  微信为0
     * 使用height - top就是实际页面高度 不用判断环境了
     */
    const windowHeight = windowInfo.height;
    const titleHeight = windowInfo.top;
    // 头部区域高度
    const headHeight = 40;
    // 底部区域高度
    const bottomHeight = 30;
    const bottomSafe = windowInfo.bottomSafe;
    const height = uni.$tm.u.torpx(
      windowHeight - titleHeight - headHeight - bottomHeight - bottomSafe,
    );
    listHeight.value = height;
    console.log(`windowHeight: ${windowHeight}px, listHeight: ${height}rpx`);
  });

  const loading = ref(false);
  const finish = ref(false);
  const descriptionFontHeight = ref(26);

  const totalText = computed(() => {
    return `共 ${page.total} 条记录`;
  });

  async function loadInfo(pageNum: number) {
    loading.value = true;
    const requestParam: any = {
      ...search,
    };
    // 添加日期
    if (commonSearch.value[0] && commonSearch.value[1]) {
      requestParam['params[beginTime]'] = commonSearch.formatedValue[0] + ' 00:00:00';
      requestParam['params[endTime]'] = commonSearch.formatedValue[1] + ' 23:59:59';
    }
    // 状态
    if (commonSearch.noticeType) {
      requestParam['noticeType'] = commonSearch.noticeType;
    }
    // 添加参数
    const ret = await noticeList({ pageNum, pageSize: 10, ...requestParam });
    page.current = pageNum;
    page.total = ret.total;
    page.list.push(...ret.rows);

    loading.value = false;
  }

  const page = reactive({
    current: 0,
    total: 0,
    list: [] as Notice[],
  });

  async function handleLoad() {
    loading.value = true;
    setTimeout(async () => {
      if (page.current * 10 >= page.total) {
        console.log('加载完毕');
        // 只弹窗一次
        if (!finish.value) {
          uni.showToast({ title: '没有更多了', icon: 'none', mask: true });
          finish.value = true;
        }
        // 关闭加载
        loading.value = false;
        return;
      }
      await loadInfo(page.current + 1);
      console.log('pagelength: ' + page.list.length);
    }, 500);
  }

  onLoad(async () => {
    /**
     * 第一次加载先用onLoad加载
     */
    await loadInfo(1);
  });

  onPullDownRefresh(async () => {
    // 要先清空以前的表单
    await handleSearchReset();
    uni.stopPullDownRefresh();
  });

  const commonSearch = reactive({
    value: [],
    formatedValue: [],
    noticeType: '',
  });

  /**
   * 用来搜索/重置后关闭表单
   */
  const filter = ref<InstanceType<typeof tmFilterMenu> | null>(null);
  async function handleSearchSubmit() {
    page.list = [];
    await loadInfo(1);
    filter.value?.close();
  }

  async function handleSearchReset() {
    finish.value = false;
    page.list = [];
    // 清空时间选择
    commonSearch.value = [];
    commonSearch.formatedValue = [];
    commonSearch.noticeType = '';
    // 清空表单
    search.noticeTitle = '';
    await loadInfo(1);
    filter.value?.close();
  }

  const search = reactive({
    noticeTitle: '',
  });

  function handleRemove(notice: Notice) {
    uni.showModal({
      title: language$t('message.info.text'),
      content: language$t('message.confirm.delete'),
      success: async function (res) {
        if (res.confirm) {
          await noticeRemove([notice.noticeId]);
          await handleSearchReset();
        }
      },
    });
  }

  function handleInfo(noticeId: number) {
    uni.navigateTo({ url: '/pages/system/notice/NoticeSubPage?noticeId=' + noticeId });
  }
</script>

<style scoped></style>
