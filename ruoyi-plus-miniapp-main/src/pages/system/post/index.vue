<template>
  <tm-app id="app">
    <!-- 筛选 写在class里的话没有效果 -->
    <tm-sticky offset="stop">
      <template #sticky>
        <tm-filterMenu ref="filter" :fixed="true">
          <tm-filterMenu-item icon="tmicon-info-circle-fill" un-icon="tmicon-info-circle" :height="170" title="状态">
            <view class="fulled flex flex-col gap-5">
              <DictRadioGroup class="flex fulled px-15" v-model="commonSearch.status" model="radio"
                :dict-value="getDict(DictEnum.NORMAL_DISABLE)"></DictRadioGroup>
              <view class="flex flex-row-center-center gap-15 px-15">
                <tm-button block :height="68" size="small" class="flex-1" @click="handleSearchSubmit">搜索</tm-button>
                <tm-button block :height="68" size="small" class="flex-1" color="orange"
                  @click="handleSearchReset">重置</tm-button>
              </view>
            </view>
          </tm-filterMenu-item>
          <tm-filterMenu-item icon="tmicon-filter-fill" un-icon="tmicon-filter" :height="400" title="筛选">
            <view class="fulled flex flex-col gap-12">
              <view class="flex flex-col flex-col-left-center gap-10 px-15">
                <tm-text label="岗位名称" :font-size="28"></tm-text>
                <tm-input class="flex-1" :border="1" placeholder="输入岗位名称" v-model="search.postName"></tm-input>
              </view>
              <view class="flex flex-col flex-col-left-center gap-10 px-15">
                <tm-text label="岗位编码" :font-size="28"></tm-text>
                <tm-input class="flex-1" :border="1" placeholder="输入岗位编码" v-model="search.postCode"></tm-input>
              </view>
              <view class="flex flex-row-center-center gap-15 px-15">
                <tm-button block :height="68" size="small" class="flex-1" @click="handleSearchSubmit">搜索</tm-button>
                <tm-button block :height="68" size="small" class="flex-1" color="orange"
                  @click="handleSearchReset">重置</tm-button>
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
      <tm-card v-for="(item, index) in page.list" :key="index" :margin="[0, 15]" :round="0" :status="item.createTime">
        <template #title>
          <view class="flex flex-row flex-row-center-center gap-10">
            <tm-text :label="item.postName" class="text-weight-b"></tm-text>
          </view>
        </template>
        <template #content>
          <tm-descriptions :label-width="130" :width="686" :column="1" :round="1" text>
            <tm-descriptions-item class="mt-10 mb-10" :font-size="descriptionFontSize" label="岗位编号"
              :value="String(item.postId)">
            </tm-descriptions-item>
            <tm-descriptions-item class="mt-10 mb-10" :font-size="descriptionFontSize" label="岗位编码"
              :value="item.postCode"></tm-descriptions-item>
            <tm-descriptions-item class="mt-10 mb-10" :font-size="descriptionFontSize" label="状态">
              <DictTag :dict-value="getDict(DictEnum.NORMAL_DISABLE)" :value="item.status"></DictTag>
            </tm-descriptions-item>
          </tm-descriptions>
        </template>
        <template #action>
          <view class="fulled flex flex-end gap-10">
            <tm-button size="small" :height="60" label="编辑" color="light-blue"
              @click="toSubPage(item.postId)"></tm-button>
            <tm-button color="red" size="small" :height="60" label="删除" @click="handleRemove(item)"></tm-button>
          </view>
        </template>
      </tm-card>
      <!-- 加载骨架屏 -->
      <tm-skeleton v-if="loading && !finish"></tm-skeleton>
    </tm-scrolly>
    <tm-float-button :disabledScrollTo="true" position="br" :width="100" :height="100" :offset="[25, 330]"
      :btn="{ icon: 'tmicon-plus-circle-fill', linear: '', color: 'green', label: '' }"
      @click="toSubPage()"></tm-float-button>
    <!-- 返回顶部 -->
    <tm-float-button :disabledScrollTo="true" position="br" :width="100" :height="100" :offset="[25, 230]"
      :btn="{ icon: 'tmicon-angle-up', linear: '', color: 'white', label: '' }" @click="toTop"></tm-float-button>
    <!-- 显示count -->
    <view v-if="page.total > 0" :margin="[0]" class="flex flex-col" style="height: 30px">
      <view class="fulled-height flex flex-row flex-row-center-center">
        <tm-divider :label="totalText"></tm-divider>
      </view>
    </view>
  </tm-app>
</template>

<script setup lang="ts">
import { postList, postRemove } from '@/api/system/post';
import type { Post } from '@/api/system/post/model';
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
const descriptionFontSize = ref(26);

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
  if (commonSearch.status) {
    requestParam['status'] = commonSearch.status;
  }
  // 添加参数
  const ret = await postList({ pageNum, pageSize: 10, ...requestParam });
  page.current = pageNum;
  page.total = ret.total;
  page.list.push(...ret.rows);

  loading.value = false;
}

const page = reactive({
  current: 0,
  total: 0,
  list: [] as Post[],
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
  uni.startPullDownRefresh;
  // 要先清空以前的表单
  await handleSearchReset();
  uni.stopPullDownRefresh();
});

const commonSearch = reactive({
  value: [],
  formatedValue: [],
  status: '',
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
  commonSearch.status = '';
  // 清空表单
  search.postName = '';
  search.postCode = '';
  await loadInfo(1);
  filter.value?.close();
}

const search = reactive({
  postName: '',
  postCode: '',
});

function toSubPage(postId?: number) {
  if (postId) {
    uni.navigateTo({ url: `/pages/system/post/PostSubPage?postId=${postId}` });
    return;
  }
  uni.navigateTo({ url: '/pages/system/post/PostSubPage' });
}

function handleRemove(post: Post) {
  uni.showModal({
    title: language$t('message.info.text'),
    content: `是否删除岗位[${post.postName}]?`,
    success: async (res) => {
      if (res.confirm) {
        await postRemove([post.postId]);
        await handleSearchReset();
      }
    },
  });
}
</script>

<style scoped></style>
