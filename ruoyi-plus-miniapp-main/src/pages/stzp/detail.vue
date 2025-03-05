<template>
	<tm-app>
		<tm-sheet _class="flex-col">
			<tm-text :font-size="30" _class="text-weight-b" :label="taskinfo.proname"></tm-text>
			<tm-text :font-size="30" _class="text-weight-b" :label="'所需数量:'+taskinfo.needcount"></tm-text>
			<tm-text :font-size="30" _class="text-weight-b" :label="'已生产数量:'+taskinfo.markcount"></tm-text>
			<tm-divider></tm-divider>
			<view>
				<tm-timeline v-for="item,index in taskinfo.productList">
					<tm-timeline-item color="green" :size="32" type="fill" :time="item.title">
						<tm-sheet no-level :shadow="2" :round="3" :margin="[0, 10]">
							<tm-statistic prefix="当前库存数量:" :startVal="0" :decimals="2"
								:endVal="item.stock?item.stock:'0'" :fontSize="32" color="indigo"></tm-statistic>
							<tm-statistic prefix="单个所需数量:" :startVal="0" :decimals="2" :endVal="item.needCount"
								:fontSize="32" color="grey"></tm-statistic>
							<tm-statistic prefix="统计需求数量:" :startVal="0" :decimals="2" :endVal="item.mc" :fontSize="32"
								color="red"></tm-statistic>
							<tm-statistic prefix="可填生产数量:" :startVal="0" :decimals="2" :endVal="item.shengc"
								:fontSize="32" color="primary"></tm-statistic>
							<tm-sheet :margin="[32, 0]" :padding="[0]" v-if="item.tasks.length>0">
									<tm-text color="red" :label="'当前进度:'+item.tasks[0].okprocess/item.tasks[0].allprocess*100+'%'"></tm-text>
									<tm-text color="blue" :label="'生产数量:'+item.tasks[0].needcount"></tm-text>
							</tm-sheet>
							<tm-sheet :margin="[32, 0]" :padding="[0]" v-else>
								<tm-descriptions :label-width="30" :column="1" transprent :round="3" text title="无生产任务">
								</tm-descriptions>
							</tm-sheet>
						</tm-sheet>
					</tm-timeline-item>
				</tm-timeline>
				<tm-divider></tm-divider>
				<tm-tag outlined :shadow="0" color="grey" font-color="black" border-color="black" size="m"
					:label="'可填报数量:'+taskinfo.kc"></tm-tag>
				<tm-input prefixLabel="生产数量" v-model="tcoun" :margin="[10]"></tm-input>
				<tm-button linear="right" linearDeep="dark" block label="提交" @click="tosub(taskinfo.id)"></tm-button>
			</view>
		</tm-sheet>
	</tm-app>
</template>
<script lang="ts" setup>
	import tmApp from '@/tmui/components/tm-app/tm-app.vue'
	import tmSheet from '@/tmui/components/tm-sheet/tm-sheet.vue'
	import tmText from '@/tmui/components/tm-text/tm-text.vue'
	import tmTimeline from '@/tmui/components/tm-timeline/tm-timeline.vue'
	import tmTimelineItem from '@/tmui/components/tm-timeline-item/tm-timeline-item.vue'
	import tmDivider from '@/tmui/components/tm-divider/tm-divider.vue'
	import { ref } from 'vue'
	import { onShow, onLoad, onMounted } from '@dcloudio/uni-app'
	import { instockinsertsn, outrecinsert, getTaskcDetail, TaskcUPdate } from '@/api/mes';
	import { TaskcVO } from "./Taskc"
	const taskinfo = ref<TaskcVO>({})
	const tcoun = ref(0);
	const ids = ref(0);
	onLoad(async (options) => {
		/**
		 * 第一次加载先用onLoad加载
		 */
		await loadInfo(options.id);
		ids.value = options.id
	});
	async function loadInfo(id : number) {
		// 添加参数
		const ret = await getTaskcDetail(id);
		taskinfo.value = ret
	}
	async function tosub(id : any) {
		taskinfo.value.markcount = tcoun
		taskinfo.value.shenc = tcoun
		const res = TaskcUPdate(taskinfo.value)
		await loadInfo(ids.value);
		uni.showToast({ title: '提交成功', icon: 'none' })

	}
</script>