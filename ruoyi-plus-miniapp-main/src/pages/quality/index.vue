<template>
	<tm-app ref="app" color="grey-5">
		<tm-sheet :margin="[32, 0, 32, 32]">
			<tm-text :font-size="34" _class="text-weight-b" label="请选择质类型"></tm-text>
			<tm-divider></tm-divider>
			<view v-for="(item, index) in qmain">
				<tm-button :linear-color="['#ea3c2d', '#ff893c']" color="red" font-color="white" linear="left" block :shadow="5"
					:label="item.title"></tm-button>
				<tm-divider></tm-divider>
			</view>

		</tm-sheet>
	</tm-app>
</template>
<script lang="ts" setup>
import { ref, nextTick, getCurrentInstance, computed, reactive } from 'vue'
import tmApp from '@/tmui/components/tm-app/tm-app.vue'
import tmInput from '@/tmui/components/tm-input/tm-input.vue'
import tmSheet from '@/tmui/components/tm-sheet/tm-sheet.vue'
import tmButton from '@/tmui/components/tm-button/tm-button.vue'
import tmText from '@/tmui/components/tm-text/tm-text.vue'
import tmFormItem from '@/tmui/components/tm-form-item/tm-form-item.vue'
import tmForm from '@/tmui/components/tm-form/tm-form.vue'
import { onLoad } from '@dcloudio/uni-app'
import { Taskinfo, TaskUPdateos, stockstl } from '@/api/mes/task/task';
import { GetQuaMainList } from '@/api/mes';
import { QualityListVO, QualityListQuery } from '@/api/mes/Quality';
import { QualityMainVO } from '@/api/mes/Qualitymaintype';
import { ProsettingVO } from '@/api/mes/prosetting';

import type { TaskVO } from '@/api/mes/task/task/types';
const app = ref<InstanceType<typeof tmApp> | null>(null)
const form = ref<InstanceType<typeof tmForm> | null>(null)
const taskinfo = reactive<TaskVO>({})
const taskid = ref(0);
const qualist = ref<QualityListVO[]>([])
const fgid = ref(0)
const show = reactive({
})
const showPicker = ref(false)
const prosetlist = ref<ProsettingVO[]>([])
const resultbtn = ref('0');
const qmain = ref<QualityMainVO[]>([])
const prckstr = ref('');
interface list {
	word: string,
	id: number
}
const plist = ref<list[]>([])
onLoad(async (option) => {
	taskid.value = option.id
	getList();
})
async function getList() {
	let res = await GetQuaMainList();
	qmain.value = res
}
const confirm = (e) => {
	console.log(e)
	console.log(prckstr)
	console.log(qualist.value)
}
function prosetConfirm(value: number[]) {
	console.log(value)
}
</script>