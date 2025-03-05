<template>
	<tm-app>
		<tm-sheet>
			<tm-text :font-size="24" _class="text-weight-b" label="我的工作站"></tm-text>
			<tm-divider></tm-divider>
			<tm-grid :width="638" :col="3">
				<tm-grid-item :height="320" v-for="(item, index) in equpimenlist" :key="index" @click="tocheck(item)"
					count="维保" color="light-green">
					<tm-sheet :padding="[10]" :shadow="2" :round="2" color="blue" text>
						<tm-image :width="160" :height="160" src="http://images.llcoms.com/base/CNC.png"></tm-image>
						<tm-text _class="pt-10" :font-size="28" :label="item.title" class="flex-center"></tm-text>
					</tm-sheet>
				</tm-grid-item>
			</tm-grid>
		</tm-sheet>
	</tm-app>
</template>
<script lang="ts" setup>
import { ref } from 'vue'
import { onShow, onLoad } from '@dcloudio/uni-app'
import tmApp from '@/tmui/components/tm-app/tm-app.vue'
import tmSheet from '@/tmui/components/tm-sheet/tm-sheet.vue'
import tmText from '@/tmui/components/tm-text/tm-text.vue'
import tmIcon from '@/tmui/components/tm-icon/tm-icon.vue'
import tmDivider from '@/tmui/components/tm-divider/tm-divider.vue'
import tmGridItem from '@/tmui/components/tm-grid-item/tm-grid-item.vue'
import tmGrid from '@/tmui/components/tm-grid/tm-grid.vue'
import { EquipmentVO } from '@/api/mes/equipment/types'
import { equlist } from '@/api/mes';
import { ProcurementList, Procurementput, Outreclist, outrecdo, toollist } from '@/api/mes';
import { EtoolVO } from '@/api/mes/Etool'
const testNum = ref([])
const equpimenlist = ref<EquipmentVO[]>([])
const toolist = ref<EtoolVO[]>([])
for (let i = 0; i < 10; i++) {
	testNum.value.push(i)
}
onLoad(() => {
	getlist()
})
async function getlist() {
	equpimenlist.value = await equlist();
	console.log(equpimenlist.value)
	toolist.value = await toollist();
}
const del = (index) => {
	testNum.value.splice(index, 1)
}
const add = (index) => {
	testNum.value.push(index)
}
function tocheck(item: any) {
	uni.navigateTo({
		url: '/pages/equiment/checkwb?id=' + item.id
	})
}
</script>