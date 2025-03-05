<template>
  <tm-app id="app">
    <!-- 筛选 写在class里的话没有效果 -->
    <tm-sticky v-if="enableSearch" style="position: fixed; z-index: 1" offset="stop">
      <template #sticky>
        <tm-filterMenu ref="filter" :fixed="true">
          <tm-filterMenu-item
            v-if="showDateSearch"
            icon="tmicon-clock-fill"
            un-icon="tmicon-clock"
            :height="880"
            title="日期"
          >
            <view class="fulled flex flex-col gap-20">
              <tm-time-between
                :asyncModel="true"
                :show-btn="false"
                v-model="defaultSearch.date"
                v-model:model-str="defaultSearch.formatedDate"
              ></tm-time-between>
              <view class="fulled flex flex-row-center-center gap-15">
                <tm-button block class="flex-1" @click="handleSearchSubmit">搜索</tm-button>
                <tm-button block class="flex-1" color="orange" @click="handleSearchReset"
                  >重置</tm-button
                >
              </view>
            </view>
          </tm-filterMenu-item>
          <tm-filterMenu-item
            v-if="showStatusSearch"
            icon="tmicon-info-circle-fill"
            un-icon="tmicon-info-circle"
            :height="200"
            title="状态"
          >
            <view class="fulled flex flex-col gap-5">
              <DictRadioGroup
                v-model="defaultSearch.status"
                model="radio"
                :dict-value="getDict(DictEnum.NORMAL_DISABLE)"
              ></DictRadioGroup>
              <view class="fulled flex flex-row gap-10">
                <tm-button block class="flex-1" @click="handleSearchSubmit">搜索</tm-button>
                <tm-button block class="flex-1" color="orange" @click="handleSearchReset"
                  >重置</tm-button
                >
              </view>
            </view>
          </tm-filterMenu-item>
          <slot name="search">
            <!-- 筛选插槽 -->
          </slot>
        </tm-filterMenu>
      </template>
    </tm-sticky>
    <!-- 要设置一个相同高度的元素  不然会被遮住 -->
    <view v-if="enableSearch" style="margin-top: 45px"></view>
    <!-- 没有搜索到结果 -->
    <tm-result v-if="page.total === 0" status="empty" :show-btn="false"></tm-result>
    <!-- 数据展示 -->
    <tm-virtual-list
      v-if="page.total > 0"
      scrollViewInTo=""
      :firstLoad="false"
      :load="handleLoad"
      :width="750"
      :height="listHeight"
      :data="page.list"
      :itemHeight="cardHeight"
    >
      <template #default="{ data }">
        <!-- 放在虚拟滚动里 这里的index有问题 -->
        <tm-card
          v-for="item in data"
          :key="item[idProp]"
          :height="cardHeight"
          :margin="[0, 10]"
          :round="0"
        >
          <template #title>
            <slot name="title" :item="item"></slot>
          </template>
          <template #status>
            <slot name="status" :item="item"></slot>
          </template>
          <template #content>
            <slot name="content" :item="item"></slot>
          </template>
          <template #action>
            <slot name="action" :item="item"></slot>
          </template>
        </tm-card>
      </template>
    </tm-virtual-list>
    <!--  -->
    <!-- 显示count -->
    <view v-if="page.total > 0" :margin="[0]" class="fulled count">
      <tm-divider :label="totalText"></tm-divider>
    </view>
  </tm-app>
</template>

<script lang="ts">
  export default {
    name: 'PageWrapper',
  };
</script>

<script setup lang="ts">
  import { onLoad, onPullDownRefresh } from '@dcloudio/uni-app';
  import { getDict } from '@/utils/dict';
  import { DictEnum } from '@/enums/dictEnum';
  import tmFilterMenu from '@/tmui/components/tm-filterMenu/tm-filterMenu.vue';
  import DictRadioGroup from '@/components/DictRadioGroup/index.vue';
  import { onMounted, ref, reactive, computed } from 'vue';
  import { PageQuery, PageResult } from '@/api/base';

  export interface Props {
    /**
     * 卡片高度 必填
     */
    cardHeight: number;
    /**
     * 是否开启搜索 即页面最上方的筛选
     */
    enableSearch?: boolean;
    /**
     * 是否显示日期搜索
     */
    showDateSearch?: boolean;
    /**
     * 是否显示状态搜索
     */
    showStatusSearch?: boolean;
    /**
     * 请求list的api
     * @param data page请求参数
     * @returns
     */
    api: (data?: PageQuery) => Promise<PageResult<any>>;
    /**
     * 虚拟滚动组件的v-for index不唯一  需要传一个item的唯一Id用来遍历
     */
    idProp: string;
    /**
     * 是否开启下拉功能 需要在pages.json设置
     */
    enablePulldown?: boolean;
    /**
     * 每页的条数  默认10
     */
    pageSize?: number;
    /**
     * list到底加载的等待时间 ms 不加等待的话样式会乱
     */
    wait?: number;
    /**
     * 搜索的字段
     */
    search: Record<string, string | number | boolean>;
  }

  const props = withDefaults(defineProps<Props>(), {
    /**
     * 卡片高度 必填
     */
    cardHeight: 500,
    /**
     * 是否开启搜索 即页面最上方的筛选
     */
    enableSearch: true,
    /**
     * 是否显示日期搜索
     */
    showDateSearch: true,
    /**
     * 是否显示状态搜索
     */
    showStatusSearch: true,
    /**
     * 是否开启下拉功能 需要在pages.json设置
     */
    enablePulldown: true,
    /**
     * 每页的条数  默认10
     */
    pageSize: 10,
    /**
     * list到底加载的等待时间 ms 不加等待的话样式会乱
     */
    wait: 500,
  });

  /**
   * 默认给一大较大的值 微信加载有问题 不知道原因
   */
  const listHeight = ref<number>(1400);
  onMounted(() => {
    /**
     * 获取页面高度 减去上面筛选的高度 再减去下面底部的高度 单位px
     * 返回需要转换为rpx
     */
    const filterHeight = 50;
    const bottomHeight = 30;
    const { windowHeight } = uni.getSystemInfoSync();
    const height = uni.$tm.u.torpx(windowHeight - filterHeight - bottomHeight);
    listHeight.value = height;
    console.log(`windowHeight: ${windowHeight}px, listHeight: ${height}rpx`);
  });

  const totalText = computed(() => {
    return `共 ${page.total} 条记录`;
  });

  const defaultSearch = reactive({
    /**
     * 日期控件的值
     */
    date: [],
    /**
     * 日期控件format的值
     */
    formatedDate: [],
    /**
     * 状态
     */
    status: '',
  });

  async function loadInfo(pageNum: number) {
    const requestParam: any = {
      ...props.search,
    };
    // 是否开启筛选功能
    const enableSearch = props.enableSearch;
    // 添加日期
    if (enableSearch && props.showDateSearch && defaultSearch.date[0] && defaultSearch.date[1]) {
      requestParam['params[beginTime]'] = defaultSearch.formatedDate[0] + ' 00:00:00';
      requestParam['params[endTime]'] = defaultSearch.formatedDate[1] + ' 23:59:59';
    }
    if (enableSearch && props.showStatusSearch) {
      requestParam['status'] = defaultSearch.status;
    }
    // 添加参数
    const ret = await props.api({ pageNum, pageSize: 10, ...requestParam });
    // 当前页码数
    page.current = pageNum;
    // total条数
    page.total = ret.total;
    // 添加而不是替换
    page.list.push(...ret.rows);
  }

  const page = reactive({
    current: 0,
    total: 0,
    list: [] as any[],
  });

  /**
   * 返回true是成功加载  false是加载失败
   * @param position
   */
  function handleLoad(position: string) {
    return new Promise((resolve) => {
      setTimeout(async () => {
        console.log(position);
        /**
         * 顶部只会在页面加载后触发一次
         */
        if (position === 'top') {
          page.list = [];
        }
        if (position === 'bottom') {
          console.log('到底部了');
        }
        if (page.current * 10 >= page.total) {
          console.log('加载完毕');
          resolve(true);
          return;
        }
        await loadInfo(page.current + 1);
        console.log(page.list.length);
        resolve(true);
      }, props.wait);
    });
  }

  /**
   * 第一次加载先用onLoad加载
   */
  onLoad(async () => {
    await loadInfo(1);
  });

  onPullDownRefresh(async () => {
    // 要先清空以前的表单
    handleSearchReset().finally(() => {
      uni.stopPullDownRefresh();
    });
  });

  const filter = ref<InstanceType<typeof tmFilterMenu> | null>(null);
  async function handleSearchSubmit() {
    page.list = [];
    await loadInfo(1);
    filter.value?.close();
  }

  async function handleSearchReset() {
    page.list = [];
    // 清空时间选择
    Object.assign(defaultSearch, {
      date: [],
      formatedDate: [],
      status: '',
    });
    // 清空传递过来的表单
    Object.keys(props.search).forEach((key) => {
      const value = props.search[key];
      if (typeof value === 'string') {
        props.search[key] = '';
      }
      if (typeof value === 'number') {
        props.search[key] = 0;
      }
      if (typeof value === 'boolean') {
        props.search[key] = false;
      }
    });
    // 刷新
    await loadInfo(1);
    filter.value?.close();
  }

  /**
   * 导出 子组件需要使用
   */
  defineExpose({
    handleSearchSubmit,
    handleSearchReset,
  });
</script>

<style scoped>
  .count {
    height: 30px;
  }
</style>
