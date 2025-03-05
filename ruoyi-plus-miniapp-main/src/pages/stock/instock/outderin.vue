<template>
	<tm-app>

		<tm-sheet>
			<view class="flex-row flex flex-wrap">
				<tm-tag color="green" size="s" :label="resdd"></tm-tag>
			</view>
		</tm-sheet>

		<!-- 没有搜索到结果 -->
		<tm-result v-if="page.total === 0 && !loading" status="empty" :show-btn="false"></tm-result>

		<!-- 数据展示 -->
		<tm-scrolly ref="scrollRef" :height="listHeight" :refresher="false" @bottom="handleLoad">
			<tm-card v-for="(item, index) in page.list" :key="index" :margin="[0, 15]" :round="0"
				:color="item.istop === 1?'#FFC107':'#fff'">
				<template #title>
					<view class="flex flex-row flex-row-center-center gap-10">
						<!-- <tm-text :label="'生产物料:' + item.proinfo.title" class="text-weight-b"></tm-text> -->
					</view>
				</template>
				<template #content>
					<tm-descriptions :label-width="180" :width="686" :column="1" :round="1" text>
						<!-- <tm-descriptions-item :font-size="descriptionFontSize" :label-width="180" label="订单信息"
							:value="item.orderVo.custname"></tm-descriptions-item>
						<tm-descriptions-item :font-size="descriptionFontSize" :label-width="180" label="订单号">
							<tm-text style="color:red;font-weight: 700;">{{ item.orderVo.custno }}</tm-text>
						</tm-descriptions-item> -->
						<tm-descriptions-item :font-size="descriptionFontSize" :label-width="180" label="客户名称"
							:value="item.outorderVo.cname"></tm-descriptions-item>
						<tm-descriptions-item :font-size="descriptionFontSize" :label-width="180" label="物料编码"
							:value="item.stockVo.tilte"></tm-descriptions-item>
						<tm-descriptions-item :font-size="descriptionFontSize" :label-width="180" label="物料规格"
							:value="item.stockVo.gk"></tm-descriptions-item>
						<tm-descriptions-item :font-size="descriptionFontSize" :label-width="180" label="出库数量"
							:value="item.counts"></tm-descriptions-item>
						<tm-descriptions-item :font-size="descriptionFontSize" :label-width="180" label="申请时间"
							:value="item.createTime"></tm-descriptions-item>
						<tm-descriptions-item :font-size="descriptionFontSize" :label-width="180" label="出库库位">
							<tm-input border="2" :searchWidth="120" v-model="kwid" prefix="tmicon-scan"
								placeholder="扫描条形码" searchLabel="扫一扫" @search="searchsn"></tm-input>
						</tm-descriptions-item>
					</tm-descriptions>
				</template>
				<template v-slot:action>
					<view class="flex flex-row flex-row-center-end flex-1">
						<tm-button :round="24" :margin="[24, 0]" color="red" label="提交" :font-size="24" :width="120"
							:height="64" @click="subt(item)"></tm-button>
					</view>
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
	import { oolists,outorderupdate } from '@/api/mes';
	import { onLoad, onPullDownRefresh } from '@dcloudio/uni-app';
	import { ref, reactive, computed, onMounted } from 'vue';
	import { useWindowInfo } from '@/tmui/tool/useFun/useWindowInfo';
	import authorizeUtils from '@/api/mes/permission';
	const scrollRef = ref<InstanceType<typeof TmScrolly> | null>(null);
	const loading = ref(false);
	const finish = ref(false);
	const resdd = ref('');
	const descriptionFontSize = ref(25);
	const totalText = computed(() => `共 ${page.total} 条记录`);

	const page = reactive({
		current: 0,
		total: 0,
		list: [] as any[],
	});

	const commonSearch = reactive({
		value: [],
		formatedValue: [],
		stockname: '',
	});
	const kwid=ref(0);
	// #ifdef APP-PLUS
	const scanner = uni.requireNativePlugin('Ba-Scanner')
	// #endif
	async function searchsn() {
		// #ifdef APP-PLUS
		let authFlag = await authorizeUtils.showAuthTipModal("");
		if (!authFlag) return;
		// #endif
		scanner.onScan({
			isContinuous: true,
			scanTimeSpace: 2000,
			isShowVibrate: true,
			isShowBeep: false,
		},
			(ret : { result : string; }) => {
				if (ret.result) {
					kwid.value = Number(ret.result)
				}
			});
	}
	const search = reactive({
		clientKey: '',
		clientSecret: '',
		currprocess: '',
		sn: '',
		prosn: ''
	});

	async function subt(item : any) {
		item.counts=Number(item.counts)
		item.status='2'
		item.kwid=kwid.value
		await outorderupdate(item);
		uni.showToast({ title: '提交成功', icon: 'none', mask: true });
		loadInfo(1)
	}

	async function loadInfo(pageNum : number) {
		loading.value = true;
		const requestParam : any = { ...search };
		if (commonSearch.value[0] && commonSearch.value[1]) {
			requestParam['params[beginCreateTime]'] = commonSearch.formatedValue[0] + ' 00:00:00';
			requestParam['params[endCreateTime]'] = commonSearch.formatedValue[1] + ' 23:59:59';
		}
		if (commonSearch.stockname) {
			requestParam['stockname'] = commonSearch.stockname;
		}
		requestParam['status'] = '1';

		const ret = await oolists({ pageNum, pageSize: 10, ...requestParam });
		page.current = pageNum;
		page.total = ret.total;
		resdd.value = ret.msg;
		page.list.push(...ret.rows);
		loading.value = false;
	}

	async function handleLoad() {
		loading.value = true;
		setTimeout(async () => {
			if (page.current * 10 >= page.total) {
				if (!finish.value) {
					uni.showToast({ title: '没有更多了', icon: 'none', mask: true });
					finish.value = true;
				}
				loading.value = false;
				return;
			}
			await loadInfo(page.current + 1);
		}, 500);
	}

	onLoad(async () => {
		await loadInfo(1);
	});

	onPullDownRefresh(async () => {
		await handleSearchReset();
		uni.stopPullDownRefresh();
	});

	const filter = ref<InstanceType<typeof tmFilterMenu> | null>(null);
	async function handleSearchSubmit() {
		page.list = [];
		await loadInfo(1);
		filter.value?.close();
	}

	async function handleSearchReset() {
		finish.value = false;
		page.list = [];
		commonSearch.value = [];
		commonSearch.formatedValue = [];
		commonSearch.stockname = '';
		search.clientKey = '';
		search.clientSecret = '';
		search.currprocess = '';
		search.sn = '';
		search.prosn = '';
		await loadInfo(1);
		filter.value?.close();
	}

	function toll(item : any) {
		uni.navigateTo({ url: '/pages/work/list?id=' + item });
	}

	function toll1(item : any) {
		uni.navigateTo({ url: '/pages/work/pri?id=' + item });
	}

	function toll2() {
		uni.navigateTo({ url: '/pages/work/workrec' });
	}

	function toTop() {
		scrollRef.value?.scrollToTop();
	}
	const listHeight = ref<number>(1000);
	onMounted(() => {
		const windowInfo = useWindowInfo();
		const windowHeight = windowInfo.height;
		const titleHeight = windowInfo.top;
		const headHeight = 40;
		const bottomHeight = 30;
		const bottomSafe = windowInfo.bottomSafe;
		const height = uni.$tm.u.torpx(windowHeight - titleHeight - headHeight - bottomHeight - bottomSafe);
		listHeight.value = height;
	});
</script>

<style scoped>
	.red-border {
		border: 2px solid red;
	}
</style>