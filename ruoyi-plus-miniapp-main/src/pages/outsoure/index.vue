<template>
	<tm-app>
		<tm-sheet>
			<tm-text :font-size="34" _class="text-weight-b" label="外协列表"></tm-text>
		</tm-sheet>
		<tm-card :status="item.createTime" :title="item.productVo.title" v-for="(item, index) in list" :key="index">


			<template #content>
				<tm-descriptions :label-width="180" :width="686" :column="1" :round="1" text>
					<tm-descriptions-item color="red" class="text-weight-b" :label-width="180" label="流转任务"
						:value="item.sn"></tm-descriptions-item>
					<tm-descriptions-item color="red" class="text-weight-b" :label-width="180" label="物料名称"
						:value="item.productVo.title"></tm-descriptions-item>
					<tm-descriptions-item color="red" class="text-weight-b" :label-width="180" label="外协名称"
						:value="item.outsourcingVo.title"></tm-descriptions-item>
					<tm-descriptions-item color="red" class="text-weight-b" :label-width="180" label="外协联系电话"
						:value="item.outsourcingVo.mobile"></tm-descriptions-item>
					<tm-descriptions-item color="red" class="text-weight-b" :label-width="180" label="外协地址"
						:value="item.outsourcingVo.address"></tm-descriptions-item>
				</tm-descriptions>
			</template>
			<template v-slot:action v-if="item.status != 2">
				<view class="flex flex-row flex-row-center-end flex-1">
					<tm-button :round="24" color="red" :margin="[24, 0]" label="外协完成" :font-size="24" :width="120" :height="64"
						@click="failecurr(item)"></tm-button>
				</view>
			</template>
		</tm-card>
	</tm-app>
</template>
<script lang="ts" setup>
import { ref } from 'vue'
import { onShow, onLoad } from '@dcloudio/uni-app'
import tmApp from '@/tmui/components/tm-app/tm-app.vue'
import tmSheet from '@/tmui/components/tm-sheet/tm-sheet.vue'
import tmText from '@/tmui/components/tm-text/tm-text.vue'
import tmCard from '@/tmui/components/tm-card/tm-card.vue'
import tmButton from '@/tmui/components/tm-button/tm-button.vue'
import { ProcurementList, Procurementput, Outreclist, outrecdo } from '@/api/mes';
import { ProcurementplanVO, ProcurementplanForm, ProcurementplanQuery } from '@/api/mes/procurementtypes';
import { OutRecVO } from "./Outrec";
import type { TaskVO } from '@/api/mes/task/task/types';
import { listTask, Taskdo, TaskUPdate, TaskRec } from '@/api/mes/task/task';
const list = ref<OutRecVO[]>([])

const info = ref<OutRecVO>({})
onLoad(async () => {
	/**
	 * 第一次加载先用onLoad加载
	 */
	getprocurelist()

});
async function getprocurelist() {
	list.value = await Outreclist()
}
async function failecurr(item: any) {
	let para = {
		id: item.id,
		outId: item.outId,
		proId: item.proId,
		taskId: item.taskId,
		status: '2'
	}
	await outrecdo(para)

	let nowitem = item.taskVo
	//处理流程
	nowitem.markcount = nowitem.needcount
	nowitem.status = '3'

	await TaskUPdate(nowitem);
}
</script>