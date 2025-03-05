<template>
	<tm-app>
		<tm-sheet _class="pt-30 mt-30">
			<tm-text :font-size="24" _class="text-weight-b" label="流转码"></tm-text>
			<tm-divider></tm-divider>
			<tm-input :searchWidth="120" v-model="scansn" prefix="tmicon-scan" searchLabel="扫一扫"
				@search="searchsn"></tm-input>
			<tm-divider></tm-divider>
			<tm-text :font-size="24" _class="text-weight-b mb-15" label="质检码"></tm-text>
			<tm-input :searchWidth="120" v-model="quaid" prefix="tmicon-scan" searchLabel="扫一扫"
				@search="searchsns"></tm-input>
		</tm-sheet>
		<tm-sheet _class="pt-30 mt-30">
			<tm-button block label="质检" @click="tobegin"></tm-button>
		</tm-sheet>
		<tm-sheet>
			<tm-text :font-size="32" label="进行中的报工"></tm-text>
			<tm-divider></tm-divider>
			<tm-scrolly :refresher="false" v-model="pull" v-model:bottom-value="bottomFresh" @refresh="refresh"
				@bottom="refreshBottom">
				<tm-card v-for="(item, index) in page.list" :key="index" :margin="[0, 15]" :round="0" class="bg-red">
					<template #title>
						<view class="flex flex-row flex-row-center-center gap-10">
							<tm-text :label="'任务编码:' + item.sn" class="text-weight-b" v-if="item.sn != 0"></tm-text>
							<tm-text label="单独报工" class="text-weight-b" v-else></tm-text>
						</view>
					</template>
					<template #status>
						<DictTag :dict-value="getDict(DictEnum.MAKE_STATUS)" :value="item.status"></DictTag>
					</template>

					<template #content>
						<tm-descriptions :label-width="180" :width="686" :column="1" :round="1" text>
							<tm-descriptions-item color="red" class="text-weight-b" :font-size="descriptionFontSize"
								:label-width="180" label="质检结果" :value="'质检未通过'"
								v-if="item.ischecknew > 0"></tm-descriptions-item>
							<tm-descriptions-item color="red" class="text-weight-b" :font-size="descriptionFontSize"
								:label-width="180" label="质检内容" :value="item.remark"
								v-if="item.ischecknew > 0"></tm-descriptions-item>
							<tm-descriptions-item :font-size="descriptionFontSize" :label-width="180" label="产品名称"
								:value="item.productVo.title" v-if="item.productVo"></tm-descriptions-item>
							<tm-descriptions-item :font-size="descriptionFontSize" label="当前生产数量"
								:value="item.markcount"></tm-descriptions-item>
							<tm-descriptions-item :font-size="descriptionFontSize" label="质检次数"
								:value="item.isCheck"></tm-descriptions-item>

						</tm-descriptions>

					</template>
					<template #action>
						<view class="fulled flex flex-start gap-15">
							<tm-button color="red" :margin="[0]" :height="50" icon="tmicon-tongzhifill" size="normal"
								label="异常停工" v-if="item.status == 2" @click="yichang(item.id)"></tm-button>
							<tm-button block class="flex-1" icon="tmicon-pausecircle" :height="50" size="normal"
								label="完工上报" @click="baogong(item.id)" v-if="item.status == 2"></tm-button>
							<tm-button block class="flex-1" disabled color="deep-orange" icon="tmicon-pausecircle"
								:height="68" size="normal" label="仅限查看" v-else></tm-button>
						</view>
					</template>
				</tm-card>
			</tm-scrolly>
			<tm-modal ref="modal" color="grey-5" :height="600" :border="0" text okColor="black" cancelColor="white"
				@ok="bsetcount" linear="bottom" title="填报" v-model:show="showwin">
				<view>
					<tm-input prefixLabel="报工数量" :inputPadding="[15]" :border="1" color="grey-5" height="50"
						:font-size="30" type="textarea" v-model="bgcount"></tm-input>
				</view>
				<view class="mt-10">
					<tm-input prefixLabel="不良品数量" :inputPadding="[15]" placeholder="没有则不用填写" :border="1" color="grey-5"
						height="50" :font-size="30" type="textarea" v-model="blcount"></tm-input>
				</view>
			</tm-modal>
			<tm-modal ref="modal" color="grey-5" :height="800" :border="0" text okColor="black" cancelColor="white"
				@ok="bsetcount" linear="bottom" title="填报" v-model:show="showexp">
				<view>
					<tm-text :font-size="24" _class="text-weight-b" label="停工原因"></tm-text>
					<tm-divider></tm-divider>

					<DictRadioGroup v-model="remark" model="radio" :dict-value="getDict(DictEnum.USER_TASK_TYPE)">
					</DictRadioGroup>
				</view>
				<view class="mt-10">
					<tm-text :font-size="24" _class="text-weight-b mt-10" label="填报数量"></tm-text>
					<tm-divider></tm-divider>
					<tm-input prefixLabel="完成数量" :inputPadding="[15]" placeholder="没有则不用填写" :border="1" color="grey-5"
						height="50" :font-size="30" type="textarea" v-model="bgcount"></tm-input>
					<view class="mt-10">
						<tm-input prefixLabel="不良品数量" :inputPadding="[15]" placeholder="没有则不用填写" :border="1"
							color="grey-5" height="50" :font-size="30" type="textarea" v-model="blcount"></tm-input>
					</view>
				</view>
			</tm-modal>
		</tm-sheet>
	</tm-app>
</template>
<script lang="ts" setup>
	import { ref, computed, nextTick, onMounted, reactive } from 'vue'
	import { listsbywt, Taskdo, TaskUPdate, TaskRec, TaskUPdateByScan } from '@/api/mes/task/task';
	import { onShow, onLoad, onPullDownRefresh } from '@dcloudio/uni-app'
	import { getDict } from '@/utils/dict';
	import { DictEnum } from '@/enums/dictEnum';
	import tmFilterMenu from '@/tmui/components/tm-filterMenu/tm-filterMenu.vue';
	import DictRadioGroup from '@/components/DictRadioGroup/index.vue';
	import TmScrolly from '@/tmui/components/tm-scrolly/tm-scrolly.vue';
	import { useWindowInfo } from '@/tmui/tool/useFun/useWindowInfo';
	import { Scandosm } from '@/api/mes';
	import type { TaskVO } from '@/api/mes/task/task/types';
	import authorizeUtils from '@/api/mes/permission';
	const test = ref('')
	function search(val : string) {
		console.log(val)
	}
	const quaid = ref();
	const scansn = ref();
	const scrollRef = ref<InstanceType<typeof TmScrolly> | null>(null);
	function toTop() {
		scrollRef.value?.scrollToTop();
	}
	const showwin = ref(false);
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
	const descriptionFontSize = ref(25);
	const bgcount = ref('');
	const blcount = ref('');
	const remark = ref('');
	const bgid = ref(0);
	const recid = ref(0);
	const ycbg = ref(false);
	const taskinfo = ref<TaskVO>({})
	const totalText = computed(() => {
		return `共 ${page.total} 条记录`;
	});
	const showexp = ref(false);
	function baogong(id : number) {
		console.log(id)
		bgid.value = id
		showwin.value = true
	};
	function yichang(id : number) {
		bgid.value = id
		ycbg.value = true;
		showexp.value = true
	};
	// #ifdef APP-PLUS
	const scanner = uni.requireNativePlugin('Ba-Scanner')
	//#endif
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
					scansn.value = Number(ret.result)
				}
			});
	}
	async function searchsns() {
		// #ifdef APP-NVUE
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
				console.log(ret)
				if (ret.result) {
					quaid.value = Number(ret.result)
				}
			});
	}
	async function tobegin() {
		uni.navigateTo({
			url: '/pages/quality/listmain?id=' + scansn.value + '&qmainid=' + quaid.value
		})
	}
	async function loadInfo(pageNum : number) {
		loading.value = true;
		const requestParam : any = {
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
		const ret = await listsbywt({ pageNum, pageSize: 10, ...requestParam });
		console.log(ret)
		page.current = pageNum;
		page.total = ret.total;
		page.list.push(...ret.rows);

		loading.value = false;
	}
	const page = reactive({
		current: 0,
		total: 0,
		list: [] as TaskVO[],
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
	async function handleSearchReset() {
		finish.value = false;
		page.list = [];
		// 清空时间选择
		commonSearch.value = [];
		commonSearch.formatedValue = [];
		commonSearch.status = '';
		// 清空表单
		search.clientKey = '';
		search.clientSecret = '';
		await loadInfo(1);
		filter.value?.close();
	}
	async function bsetcount() {
		let rcount = 0;
		let nowitem;
		page.list.forEach((item : any) => {
			if (item.id == bgid.value) {
				rcount = parseInt(item.markcount)
				nowitem = item
			}
		})
		if (nowitem.scrapcount == null) {
			nowitem.scrapcount = 0
		}
		if (nowitem.redoingcount == null) {
			nowitem.redoingcount = 0
		}
		rcount = rcount + parseInt(bgcount.value)
		nowitem.markcount = rcount
		if (nowitem.sn == 0) {
			nowitem.status = 3
		} else {
			if (nowitem.needcount > rcount) {
				nowitem.status = 1
			} else {
				nowitem.status = 3
			}
		}
		nowitem.redoingcount = blcount.value
		if (ycbg.value) {
			nowitem.status = 0
			nowitem.remark = remark.value
		}
		let rec = await TaskUPdateByScan(nowitem);
		uni.stopPullDownRefresh();
	}
	const commonSearch = reactive({
		value: [],
		formatedValue: [],
		status: '',
	});
</script>