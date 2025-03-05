<template>
	<tm-app>
		<!-- 筛选 写在class里的话没有效果 -->
	
		<!-- 没有搜索到结果 -->
		<tm-result v-if="page.total === 0 && !loading" status="empty" :show-btn="false"></tm-result>
		<!-- 数据展示 -->
		<tm-scrolly ref="scrollRef" :height="listHeight" :refresher="false" >
			<tm-card v-for="(item, index) in page.list" :key="index" :margin="[0, 15]" :round="0" class="bg-red">
				

				<template #content>
					<tm-descriptions :label-width="180" :width="686" :column="1" :round="1" text>
						
						<tm-descriptions-item :font-size="descriptionFontSize" :label-width="180" label="物料"
							:value="item.tilte"></tm-descriptions-item>
							<tm-descriptions-item :font-size="descriptionFontSize" :label-width="180" label="编码"
								:value="item.sn"></tm-descriptions-item>
						<tm-descriptions-item :font-size="descriptionFontSize" label="规格" :value="item.gk"
							></tm-descriptions-item>
						<tm-descriptions-item :font-size="descriptionFontSize" label="当前库存量" :value="item.nowstock"
							></tm-descriptions-item>
						<tm-descriptions-item :font-size="descriptionFontSize" label="类别"
							:value="item.type"></tm-descriptions-item>
						<tm-descriptions-item :font-size="descriptionFontSize" label="预警库存"
							:value="item.warnnum"></tm-descriptions-item>
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
		
	</tm-app>
</template>
<script setup lang="ts">
import { getwran } from '@/api/mes';
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
	requestParam['type']='原材料'
	// 添加参数
	let ret = await getwran({ pageNum, pageSize: 1000, ...requestParam });
	page.current = pageNum;
	page.total = ret.total;
	resdd.value=ret.msg
	page.list.push(...ret);

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