<template>
	<tm-app>
		<tm-side-menu v-model:active="active" menuActiveFontColor="yellow" menuFontColor="black" menuBarBgColor="yellow"
			:itemHeight="100" height="970" :list="list" @click="changeDept">
			<view class="mb-32 mx-32 round-3 overflow" v-for="(item,index) in Userlist">
				<tm-cell :margin="[0, 0]" :titleFontSize="30" :rightText="item.chetime" :title="item.nickName" @click="todetail(item)"> </tm-cell>
			</view>
		</tm-side-menu>
	</tm-app>
</template>

<script lang="ts" setup>
import tmText from '@/tmui/components/tm-text/tm-text.vue'
import tmDivider from '@/tmui/components/tm-divider/tm-divider.vue'
import tmSheet from '@/tmui/components/tm-sheet/tm-sheet.vue'
import tmApp from '@/tmui/components//tm-app//tm-app.vue'
import tmSideMenu from '@/tmui/components/tm-side-menu/tm-side-menu.vue'
import { ref } from 'vue'
import { getDept, getUserList,getKaoqin } from '@/api/mes';
import { onHide, onLoad, onPullDownRefresh, onShow } from '@dcloudio/uni-app';
import { text } from 'stream/consumers'
const active = ref(0)
const list = ref([
])
onLoad(async () => {
	getDeptList()
	getUser()
	
})
async function todetail(item:any){
	console.log(item)
	uni.navigateTo({
		url:'/pages/user/kqinfo?id='+item.userId
	})
}
async function getDeptList() {
	let rec = await getDept();
	let ct1 = {
		text: "全部",
		id: 1
	}
	list.value.push(ct1)
	rec[0].children.forEach((item: any) => {
		let ct = {
			text: item.label,
			id: item.id
		}
		list.value.push(ct)
	})
}
const Userlist=ref<any[]>([])
const deptid=ref(0);
async function getUser() {
	let query = {
	}
	let rec = await getUserList(query)
	Userlist.value=rec.rows
	console.log(rec)
}
async function changeDept(item:any){
	let query = {
		deptId:item.id
	}
	let rec = await getKaoqin(query)
	Userlist.value=rec
	console.log(rec)
}
async function getkq(item:any){
	let q={
		deptId:item
	}
	
	console.log(rec)
}
</script>

<style></style>