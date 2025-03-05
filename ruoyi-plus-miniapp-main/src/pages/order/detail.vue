<template>
	<tm-app>
		<tm-sheet >
			<tm-text :font-size="32" label="采购入库"></tm-text>
			<tm-scrolly :refresher="false" height="700">
				<tm-card v-for="(item, index) in proinfo.inrecList" :key="index" :margin="[0, 15]" :round="0" >
					<template #content>
						<tm-descriptions :label-width="180" :width="686" :column="1" :round="1" text>
							<tm-descriptions-item :font-size="descriptionFontSize" label="入库信息"
								:value="item.fromtype"></tm-descriptions-item>
							<tm-descriptions-item :font-size="descriptionFontSize" label="入库时间"
								:value="item.createTime"></tm-descriptions-item>
							<tm-descriptions-item :font-size="descriptionFontSize" label="入库数量"
								:value="item.count"></tm-descriptions-item>
								<tm-descriptions-item :font-size="descriptionFontSize" label="物料"
									:value="item.stockVo.tilte"></tm-descriptions-item>
									<tm-descriptions-item :font-size="descriptionFontSize" label="规格"
										:value="item.stockVo.gk"></tm-descriptions-item>
										<tm-descriptions-item :font-size="descriptionFontSize" label="备注"
											:value="item.remark"></tm-descriptions-item>
						</tm-descriptions>
					</template>
				</tm-card>
				
			</tm-scrolly>

		</tm-sheet>
		<tm-sheet >
			<tm-text :font-size="32" label="委外入库"></tm-text>
			<tm-scrolly :refresher="false" height="700">
				<tm-card v-for="(item, index) in proinfo.oolistVos" :key="index" :margin="[0, 15]" :round="0" >
					<template #content>
						<tm-descriptions :label-width="180" :width="686" :column="1" :round="1" text>
							<tm-descriptions-item :font-size="descriptionFontSize" label="物料信息"
								:value="item.stockVo.tilte"></tm-descriptions-item>
							<tm-descriptions-item :font-size="descriptionFontSize" label="出库时间"
								:value="item.createTime"></tm-descriptions-item>
							<tm-descriptions-item :font-size="descriptionFontSize" label="出库数量"
								:value="item.counts"></tm-descriptions-item>
								<tm-descriptions-item :font-size="descriptionFontSize" label="备注"
									:value="item.remark"></tm-descriptions-item>
						</tm-descriptions>
					</template>
				</tm-card>
				
			</tm-scrolly>
		
		</tm-sheet>
		<tm-sheet >
			<tm-text :font-size="32" label="委外出库"></tm-text>
			<tm-scrolly :refresher="false" height="700">
				<tm-card v-for="(item, index) in proinfo.saleoutList" :key="index" :margin="[0, 15]" :round="0" >
					<template #content>
						<tm-descriptions :label-width="180" :width="686" :column="1" :round="1" text>
							<tm-descriptions-item :font-size="descriptionFontSize" label="物料信息"
								:value="item.stockVo.tilte"></tm-descriptions-item>
							<tm-descriptions-item :font-size="descriptionFontSize" label="出库时间"
								:value="item.createTime"></tm-descriptions-item>
							<tm-descriptions-item :font-size="descriptionFontSize" label="出库数量"
								:value="item.counts"></tm-descriptions-item>
								<tm-descriptions-item :font-size="descriptionFontSize" label="备注"
									:value="item.remark"></tm-descriptions-item>
						</tm-descriptions>
					</template>
				</tm-card>
				
			</tm-scrolly>
		
		</tm-sheet>
		<tm-sheet >
			<tm-text :font-size="32" label="销售出库"></tm-text>
			<tm-scrolly :refresher="false" height="700">
				<tm-card v-for="(item, index) in proinfo.outorderList" :key="index" :margin="[0, 15]" :round="0" >
					<template #content>
						<tm-descriptions :label-width="180" :width="686" :column="1" :round="1" text>
							<tm-descriptions-item :font-size="descriptionFontSize" label="物料信息"
								:value="item.stockVo.tilte"></tm-descriptions-item>
							<tm-descriptions-item :font-size="descriptionFontSize" label="出库时间"
								:value="item.createTime"></tm-descriptions-item>
							<tm-descriptions-item :font-size="descriptionFontSize" label="出库数量"
								:value="item.counts"></tm-descriptions-item>
								<tm-descriptions-item :font-size="descriptionFontSize" label="备注"
									:value="item.remark"></tm-descriptions-item>
						</tm-descriptions>
					</template>
				</tm-card>
				
			</tm-scrolly>
		
		</tm-sheet>
	</tm-app>
</template>
<script lang="ts" setup>
	import { ref, onMounted } from 'vue'
	import {  onLoad } from '@dcloudio/uni-app'
	import tmApp from '@/tmui/components/tm-app/tm-app.vue'
	import TmScrolly from '@/tmui/components/tm-scrolly/tm-scrolly.vue';
	import { useWindowInfo } from '@/tmui/tool/useFun/useWindowInfo';
	import {orderinfo } from '@/api/mes';
	import { StockVO } from '@/api/mes/Stock';
	const proinfo = ref<any[]>([])
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
	async function ProtypeList(id:any) {
		let ret = await orderinfo(id)
		
		proinfo.value = ret
	}
	
	

	onLoad(async (options) => {
		ProtypeList(options.id)
	});
</script>