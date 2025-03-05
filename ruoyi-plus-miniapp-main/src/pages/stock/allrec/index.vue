<template>
	<tm-app>
		<!-- 筛选 写在class里的话没有效果 -->
		<tm-sticky offset="stop">
			<template #sticky>
				<tm-filterMenu ref="filter" :fixed="true">
					<tm-filterMenu-item icon="tmicon-info-circle-fill" un-icon="tmicon-info-circle" :height="320" title="物料">
						<view class="fulled flex flex-col gap-5">
							<tm-text :font-size="28" _class="text-weight-b" label="物料名称"></tm-text>
							<tm-input class="flex fulled px-15" v-model="commonSearch.stockname" model="text"
								/>
								<tm-text :font-size="28" _class="text-weight-b" label="物料编码"></tm-text>
								<tm-input class="flex fulled px-15" v-model="commonSearch.stockname" model="text"
									/>
							<view class="flex flex-row-center-center gap-15 px-15">
								<tm-button block :height="68" size="small" class="flex-1" @click="handleSearchSubmit">搜索</tm-button>
								<tm-button block :height="68" size="small" class="flex-1" color="orange"
									@click="handleSearchReset">重置</tm-button>
							</view>
						</view>
					</tm-filterMenu-item>
					<tm-filterMenu-item
					  icon="tmicon-clock-fill"
					  un-icon="tmicon-clock"
					  :height="850"
					  title="日期"
					>
					  <view class="fulled flex flex-col gap-15">
					    <tm-time-between
					      class="ml-20"
					      :asyncModel="true"
					      :show-btn="false"
					      v-model="commonSearch.value"
					      v-model:model-str="commonSearch.formatedValue"
					    ></tm-time-between>
					    <view class="flex flex-row-center-center gap-15">
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
		<tm-sheet>
			<view class="flex-row flex flex-wrap">
							<tm-tag color="green" size="s" :label="resdd"></tm-tag>
						</view>
		</tm-sheet>
		<!-- 没有搜索到结果 -->
		<tm-result v-if="page.total === 0 && !loading" status="empty" :show-btn="false"></tm-result>
		<!-- 数据展示 -->
		<tm-scrolly ref="scrollRef" :height="listHeight" :refresher="false" @bottom="handleLoad">
			<tm-card v-for="(item, index) in page.list" :key="index" :margin="[0, 15]" :round="0" class="bg-red">
				<template #title>
					<view class="flex flex-row flex-row-center-center gap-10">
						<tm-text :label="'出库数量:' + item.counts" class="text-weight-b"></tm-text>
					</view>
				</template>

				<template #content>
					<tm-descriptions :label-width="180" :width="686" :column="1" :round="1" text>
						
						<tm-descriptions-item :font-size="descriptionFontSize" :label-width="180" label="物料"
							:value="item.stockVo.tilte"></tm-descriptions-item>
							<tm-descriptions-item :font-size="descriptionFontSize" :label-width="180" label="编码"
								:value="item.stockVo.sn"></tm-descriptions-item>
						<tm-descriptions-item :font-size="descriptionFontSize" label="规格" :value="item.stockVo.gk"
							></tm-descriptions-item>
						<tm-descriptions-item :font-size="descriptionFontSize" label="来源" :value="item.fromtype"
							></tm-descriptions-item>
						<tm-descriptions-item :font-size="descriptionFontSize" label="当前库存量" :value="item.stockVo.nowstock"
							></tm-descriptions-item>
						<tm-descriptions-item :font-size="descriptionFontSize" label="当前入库数量" :value="item.markcount"
							></tm-descriptions-item>
						<tm-descriptions-item :font-size="descriptionFontSize" label="类别"
							:value="item.type"></tm-descriptions-item>
						<tm-descriptions-item :font-size="descriptionFontSize" label="数量"
							:value="item.counts"></tm-descriptions-item>
						<tm-descriptions-item :font-size="descriptionFontSize" label="时间"
							:value="item.createTime"></tm-descriptions-item>
					</tm-descriptions>
				</template>
			</tm-card>
			<!-- 加载骨架屏 -->
			<tm-skeleton v-if="loading && !finish"></tm-skeleton>
		</tm-scrolly>
		<!-- 返回顶部 -->
		<tm-float-button :disabledScrollTo="true" position="br" :width="100" :height="100" :offset="[25, 256]"
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
import { AllStock } from '@/api/mes';
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
});

const loading = ref(false);
const finish = ref(false);
const resdd=ref('');
const descriptionFontSize = ref(25);
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
		requestParam['params[beginCreateTime]'] = commonSearch.formatedValue[0] + ' 00:00:00';
		requestParam['params[endCreateTime]'] = commonSearch.formatedValue[1] + ' 23:59:59';
	}
	// 状态
	if (commonSearch.stockname) {
		requestParam['stockname'] = commonSearch.stockname;
	}
	
	// 添加参数
	let ret = await AllStock({ pageNum, pageSize: 10, ...requestParam });
	page.current = pageNum;
	page.total = ret.total;
	resdd.value=ret.msg
	console.log(ret)
	page.list.push(...ret.rows);

	loading.value = false;
}

const page = reactive({
	current: 0,
	total: 0,
	list: [] as any[],
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
	}, 500);
}

onLoad(async (options) => {
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
	stockname: '',
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
	commonSearch.stockname = '';
	// 清空表单
	search.clientKey = '';
	search.clientSecret = '';
	search.currprocess='';
	search.sn='';
	search.prosn='';
	await loadInfo(1);
	filter.value?.close();
}

const search = reactive({
	clientKey: '',
	clientSecret: '',
	currprocess:'',
	sn:'',
	prosn:''
});
</script>

<style scoped></style>