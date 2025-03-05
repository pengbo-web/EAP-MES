<template>
	<tm-app>
		<view class="mb-32 mx-32 round-3 overflow" v-for="(item,index) in list">
			<tm-cell :rightText="item.chetime"
				:margin="[0, 0]"
				:titleFontSize="30"
				:title="item.nickName">
				<template v-slot:right>
					
				</template>
			</tm-cell>
			
		</view>
	</tm-app>
</template>
<script lang="ts" setup>
import { ref } from 'vue'
import { onShow, onLoad } from '@dcloudio/uni-app'
import tmApp from '@/tmui/components/tm-app/tm-app.vue'
import tmSheet from '@/tmui/components/tm-sheet/tm-sheet.vue'
import tmText from '@/tmui/components/tm-text/tm-text.vue'
import tmCheckbox from '@/tmui/components/tm-checkbox/tm-checkbox.vue'
import tmCell from '@/tmui/components/tm-cell/tm-cell.vue'
import { getDept, getUserList,getKaoqininfo } from '@/api/mes';

onLoad(async(options)=>{
	getInfo(options.id);
})
const list=ref<any[]>([])
async function getInfo(item:any){
	let pa='';
	let rc=await getKaoqininfo(item,pa)
	list.value=rc
	console.log(rc)
}
</script>