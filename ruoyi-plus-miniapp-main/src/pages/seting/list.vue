<template>
	<tm-app>
		<!-- 筛选 写在class里的话没有效果 -->
		<tm-sticky offset="stop">
			<template #sticky>
				<tm-filterMenu ref="filter" :fixed="true">
					<tm-filterMenu-item icon="tmicon-info-circle-fill" un-icon="tmicon-info-circle" :height="170" title="状态">
						<view class="fulled flex flex-col gap-5">
							<DictRadioGroup class="flex fulled px-15" v-model="commonSearch.status" model="radio"
								:dict-value="getDict(DictEnum.MAKE_STATUS)"></DictRadioGroup>
							<view class="flex flex-row-center-center gap-15 px-15">
								<tm-button block :height="68" size="small" class="flex-1" @click="handleSearchSubmit">搜索</tm-button>
								<tm-button block :height="68" size="small" class="flex-1" color="orange"
									@click="handleSearchReset">重置</tm-button>
							</view>
						</view>
					</tm-filterMenu-item>
					<tm-filterMenu-item icon="tmicon-filter-fill" un-icon="tmicon-filter" :height="410" title="筛选">
						<view class="fulled flex flex-col gap-12">
							<view class="flex flex-col flex-col-left-center gap-10 px-15">
								<tm-text label="工序排号" :font-size="28"></tm-text>
								<tm-input class="flex-1" :border="1" placeholder="工序排号" v-model="search.currprocess"></tm-input>
							</view>
							<view class="flex flex-col flex-col-left-center gap-10 px-15">
								<tm-text label="流转号" :font-size="28"></tm-text>
								<tm-input :border="1" class="flex-1" placeholder="流转号" v-model="search.sn"></tm-input>
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
			<tm-card v-for="(item, index) in page.list" :key="index" :margin="[0, 15]" :round="0" class="bg-red">
				<template #title>
					<view class="flex flex-row flex-row-center-center gap-10">
						<tm-text :label="'任务编码:' + item.sn" class="text-weight-b"></tm-text>
					</view>
				</template>

				<template #content>
					<tm-descriptions :label-width="180" :width="686" :column="1" :round="1" text>
						<tm-descriptions-item color="red" class="text-weight-b" :font-size="descriptionFontSize" :label-width="180"
							label="提交人" :value="item.username" ></tm-descriptions-item>
						<tm-descriptions-item color="red" class="text-weight-b" :font-size="descriptionFontSize" :label-width="180"
							label="质检结果" :value="'注意！未质检'" v-if="item.isCheck == 0"></tm-descriptions-item>
						<tm-descriptions-item color="red" class="text-weight-b" :font-size="descriptionFontSize" :label-width="180"
							label="质检结果" :value="'质检未通过'" v-if="item.ischecknew > 0"></tm-descriptions-item>
						<tm-descriptions-item color="red" class="text-weight-b" :font-size="descriptionFontSize" :label-width="180"
							label="质检内容" :value="item.remark" v-if="item.ischecknew > 0"></tm-descriptions-item>
						<tm-descriptions-item :font-size="descriptionFontSize" :label-width="180" label="产品名称"
							:value="item.productVo.title"></tm-descriptions-item>
							<tm-descriptions-item :font-size="descriptionFontSize" :label-width="180" label="图号"
								:value="item.productVo.sn"></tm-descriptions-item>
						<tm-descriptions-item :font-size="descriptionFontSize" label="所需生产数量" :value="item.needcount"
							v-if="item.processVo.typeId != 4"></tm-descriptions-item>
						<tm-descriptions-item :font-size="descriptionFontSize" label="所需入库数量" :value="item.needcount"
							v-if="item.processVo.typeId == 4"></tm-descriptions-item>
						<tm-descriptions-item :font-size="descriptionFontSize" label="当前生产数量" :value="item.markcount"
							v-if="item.processVo.typeId != 4"></tm-descriptions-item>
						<tm-descriptions-item :font-size="descriptionFontSize" label="当前入库数量" :value="item.markcount"
							v-if="item.processVo.typeId == 4"></tm-descriptions-item>
						<tm-descriptions-item :font-size="descriptionFontSize" label="质检次数"
							:value="item.isCheck"></tm-descriptions-item>
						<tm-descriptions-item :font-size="descriptionFontSize" label="工序"
							:value="item.processVo.title"></tm-descriptions-item>
						<tm-descriptions-item :font-size="descriptionFontSize" label="顺序"
							:value="item.currprocess"></tm-descriptions-item>
						<tm-descriptions-item :font-size="descriptionFontSize" label="完成工序"
							:value="item.okprocess"></tm-descriptions-item>
						<tm-descriptions-item :font-size="descriptionFontSize" label="全部工序" color="red" :value="item.allprocess">
						</tm-descriptions-item>
					</tm-descriptions>

				</template>
				<template #action v-if="item.processVo.typeId == 0">
					<view class="fulled flex flex-start gap-15">
						<tm-button block class="flex-1" color="blue" :height="90" size="normal" icon="tmicon-caret-right"
							label="开始报工" v-if="item.status == 1 || item.status == 0" @click="tobegin(item.id)"></tm-button>
						<tm-button color="red" :margin="[0]" :height="90" icon="tmicon-tongzhifill" size="normal" label="异常停工"
							v-if="item.status == 2" @click="yichang(item.id)"></tm-button>
						<tm-button block class="flex-1" icon="tmicon-pausecircle" :height="90" size="normal" label="完工上报"
							@click="baogong(item.id)" v-if="item.status == 2"></tm-button>
						<tm-button block class="flex-1" disabled color="deep-orange" icon="tmicon-pausecircle" :height="68"
							size="normal" label="仅限查看" v-else></tm-button>
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
		<tm-modal ref="modal" color="grey-5" :height="600" :border="0" text okColor="black" cancelColor="white"
			@ok="bsetcount" linear="bottom" title="填报" v-model:show="showwin">
			<view>
				<tm-input prefixLabel="报工数量" :inputPadding="[15]" :border="1" color="grey-5" height="50" :font-size="30"
					type="textarea" v-model="bgcount"></tm-input>
			</view>
			<view class="mt-10">
				<tm-input prefixLabel="不良品数量" :inputPadding="[15]" placeholder="没有则不用填写" :border="1" color="grey-5" height="50"
					:font-size="30" type="textarea" v-model="blcount"></tm-input>
			</view>
		</tm-modal>
		<tm-modal ref="modal" color="grey-5" :height="800" :border="0" text okColor="black" cancelColor="white"
			@ok="bsetcount" linear="bottom" title="填报" v-model:show="showexp">
			<view>
				<tm-text :font-size="24" _class="text-weight-b" label="停工原因"></tm-text>
				<tm-divider></tm-divider>

				<DictRadioGroup v-model="remark" model="radio" :dict-value="getDict(DictEnum.USER_TASK_TYPE)"></DictRadioGroup>
			</view>
			<view class="mt-10">
				<tm-text :font-size="24" _class="text-weight-b mt-10" label="填报数量"></tm-text>
				<tm-divider></tm-divider>
				<tm-input prefixLabel="完成数量" :inputPadding="[15]" placeholder="没有则不用填写" :border="1" color="grey-5" height="50"
					:font-size="30" type="textarea" v-model="bgcount"></tm-input>
				<view class="mt-10">
					<tm-input prefixLabel="不良品数量" :inputPadding="[15]" placeholder="没有则不用填写" :border="1" color="grey-5"
						height="50" :font-size="30" type="textarea" v-model="blcount"></tm-input>
				</view>
			</view>
		</tm-modal>
		<tm-modal ref="modal" color="grey-5" :height="400" :border="0" text okColor="black" cancelColor="white" @ok="ruku"
			linear="bottom" title="填报" v-model:show="kurushow">
			<view>
				<tm-input prefixLabel="入库数量" :inputPadding="[15]" :border="1" color="grey-5" height="50" :font-size="30"
					type="textarea" v-model="rkcount"></tm-input>
			</view>
		</tm-modal>
	</tm-app>
</template>
<script setup lang="ts">
import { listTaskbyuid, Taskdo, TaskUPdate, TaskRec } from '@/api/mes/task/task';
import type { TaskVO } from '@/api/mes/task/task/types';
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
import { instockinsertsn, outrecinsert } from '@/api/mes';
import { useWindowInfo } from '@/tmui/tool/useFun/useWindowInfo';
import { InstockVO } from "@/api/mes/instock";
import { OutRecVO } from "@/api/mes/Outrec";
const instockform = ref<InstockVO>({})
const outrecform = ref<OutRecVO>({})
const scrollRef = ref<InstanceType<typeof TmScrolly> | null>(null);
function toTop() {
	scrollRef.value?.scrollToTop();
}
const showwin = ref(false);
const usdid=ref(0);
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
const descriptionFontSize = ref(25);
const bgcount = ref('');
const blcount = ref('');
const remark = ref('');
const bgid = ref(0);
const recid = ref(0);
const ycbg = ref(false);
const totalText = computed(() => {
	return `共 ${page.total} 条记录`;
});
const showexp = ref(false);
const kurushow = ref(false);
const rkcount = ref();
const tsfrom = ref<TaskVO>({})
function baogong(id: number) {
	bgid.value = id
	showwin.value = true
};
function yichang(id: number) {
	bgid.value = id
	ycbg.value = true;
	showexp.value = true
};
async function tobegin(id: number) {
	let rec = await Taskdo(id);
	if (rec) {
		recid.value = rec;
		page.list.forEach((item: any) => {
			if (item.id == id) {
				item.status = 2
			}
		})
	}
}
async function outsouring(items: any) {
	let rec = await Taskdo(items.id);
	if (rec) {
		recid.value = rec;
		page.list.forEach((item: any) => {
			if (item.id == items.id) {
				item.status = 2
			}
		})
	}
	outrecform.value.proId = items.proId
	outrecform.value.outId = items.outsourcingVo.id
	outrecform.value.count = items.needcount
	outrecform.value.status = '1'
	outrecform.value.sn = items.sn
	outrecform.value.taskId = items.id
	await outrecinsert(outrecform.value)

}
function gotoUrl(item: any, id: any) {
	if (item == 'ht') {
		uni.navigateTo({ url: '/pages/stock/outstock/index?id=' + id });
	}
	if (item == 'zj') {
		uni.navigateTo({ url: '/pages/quality/index?id=' + id });
	}
}
function rukushow(item: any) {
	kurushow.value = true
	tsfrom.value = item
}
async function ruku() {
	instockform.value.proId = tsfrom.value.proId
	instockform.value.count = rkcount.value
	instockform.value.fromtype = '流转入库'

	await instockinsertsn(instockform.value)

	//处理流程
	let rcount = parseInt(tsfrom.value.markcount);
	rcount = rcount + parseInt(rkcount.value)
	tsfrom.value.markcount = rcount
	if (tsfrom.value.needcount > rcount) {
		tsfrom.value.status = '1'
	} else {
		tsfrom.value.status = '3'
	}
	await TaskUPdate(tsfrom.value);
}
async function bsetcount() {
	let rcount = 0;
	let nowitem;
	page.list.forEach((item: any) => {
		if (item.id == bgid.value) {
			rcount = parseInt(item.markcount)
			nowitem = item
		}
	})
	
	rcount = rcount + parseInt(bgcount.value)
	nowitem.markcount = rcount
	if (nowitem.needcount > rcount) {
		nowitem.status = 1
	} else {
		nowitem.status = 3
	}
	nowitem.redoingcount = blcount.value
	if (ycbg.value) {
		nowitem.status = 0
		nowitem.remark = remark.value
	}
	let rec = await TaskUPdate(nowitem);
	let params = {
		id: recid.value,
		count: parseInt(bgcount.value),
		defective: blcount.value,
		lastcount: rcount,
		mustbe: 0
	}
	if (ycbg.value) {
		params.mustbe = 1
	}
	await TaskRec(nowitem.id, params);
	page.current=0;
	page.total=0;
	page.list=[] as TaskVO[];
	await loadInfo(1);
}
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
	const ret = await listTaskbyuid(usdid.value,typ.value,{ pageNum, pageSize: 10, ...requestParam });
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
	}, 500);
}
const typ=ref(0);
onLoad(async (options) => {
	/**
	 * 第一次加载先用onLoad加载
	 */
	usdid.value=options.id
	typ.value=options.type
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
	search.clientKey = '';
	search.clientSecret = '';
	search.currprocess='';
	search.sn='';
	await loadInfo(1);
	filter.value?.close();
}

const search = reactive({
	clientKey: '',
	clientSecret: '',
	currprocess:'',
	sn:''
});
</script>

<style scoped></style>