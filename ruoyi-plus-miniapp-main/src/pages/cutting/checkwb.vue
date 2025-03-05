<template>
	<tm-app>
		<tm-sheet :margin="[0, 32]">
			<tm-text :font-size="38" _class="text-weight-b" :label="title"></tm-text>
		</tm-sheet>
		<tm-sheet>
			<tm-text :fontSize="24" _class="text-weight-b" label="请选择类型"></tm-text>
			<tm-divider></tm-divider>
			<tm-segtab :width="636" :list="list" defaultValue="0" class="mt-30 mb-30" @change="changetype"></tm-segtab>
			<!-- //form -->
			<tm-card :margin="[0, 50]" v-for="(item, index) in listvo" :key="index" :border="1">
				<template #title>
					<view>
						<view _class="flex flex-center"><tm-image preview :width="300" :height="300" :src="item.iiim"></tm-image>
						</view>
						<view _class="flex flex-center pt-20">
							{{ index + 1 }}:{{ item.title }}</view>
					</view>
				</template>
				<template #content>
					<tm-sheet :margin="[0, 0, 0, 0]">
						<view class="flex flex-row-center-start mt-10">
							<tm-text>结果：</tm-text>
							<tm-segtab v-model="item.remark" :width="420" :list="[{ text: '正常' }, { text: '异常' }]"
								@change="reschange(item)"></tm-segtab>
						</view>
						<tm-translate :duration="100">
							<view class="flex m-10 pt-30" v-if="item.remark == '1'">
								<tm-text>图片：</tm-text>

								<tm-upload showSort v-model="item.upimg" ref="up" :width="400" :rows="1" :imageHeight="300" :maxFile="1"
									url="https://api-l.llcoms.com/mobile/upload">
								</tm-upload>
							</view>
						</tm-translate>
						<tm-translate>
							<view class="flex mt-10 pt-30" v-if="item.remark == '1'">
								<tm-text>描述：</tm-text>
								<tm-input v-model="item.rebak" :height="264" :width="400" type="textarea" :margin="[0, 0]"></tm-input>
							</view>
						</tm-translate>
					</tm-sheet>
				</template>
			</tm-card>
			<tm-button block label="提交" @click="tjsub"></tm-button>
		</tm-sheet>

	</tm-app>
</template>
<script lang="ts" setup>
import { ref } from "vue";
import { onShow, onLoad } from "@dcloudio/uni-app";
import * as dayjs from "@/tmui/tool/dayjs/esm/index";
import tmApp from "@/tmui/components/tm-app/tm-app.vue";
import tmInput from "@/tmui/components/tm-input/tm-input.vue";
import tmSheet from "@/tmui/components/tm-sheet/tm-sheet.vue";
import tmStepper from "@/tmui/components/tm-stepper/tm-stepper.vue";
import tmRadioGroup from "@/tmui/components/tm-radio-group/tm-radio-group.vue";
import tmRadio from "@/tmui/components/tm-radio/tm-radio.vue";
import tmCheckboxGroup from "@/tmui/components/tm-checkbox-group/tm-checkbox-group.vue";
import tmCheckbox from "@/tmui/components/tm-checkbox/tm-checkbox.vue";
import tmRate from "@/tmui/components/tm-rate/tm-rate.vue";
import tmSlider from "@/tmui/components/tm-slider/tm-slider.vue";
import tmSegtab from "@/tmui/components/tm-segtab/tm-segtab.vue";
import tmSwitch from "@/tmui/components/tm-switch/tm-switch.vue";
import tmUpload from "@/tmui/components/tm-upload/tm-upload.vue";
import tmImage from "@/tmui/components/tm-image/tm-image.vue";
import tmButton from "@/tmui/components/tm-button/tm-button.vue";
import tmText from "@/tmui/components/tm-text/tm-text.vue";
import tmIcon from "@/tmui/components/tm-icon/tm-icon.vue";
import { EquCheckVO } from "@/api/mes/EquCheck";
import { EqucheckListVO } from "@/api/mes/EqucheckList";
import { getEquCheck, getEquCheckList, equinset } from "@/api/mes";
import { EquRecVO } from "@/api/mes/EquRec";
const equid = ref();
const title = ref("");
onLoad((options) => {
	equid.value = options.id;
	getlist();
});
const recinfo = ref<EquRecVO>({});
const info = ref<EquCheckVO>({});
const listvo = ref<EqucheckListVO[]>([]);
async function getlist() {
	info.value = await getEquCheck(equid.value, 1);
	title.value = info.value[0].title;
	getchecklist();
}
async function getchecklist() {
	listvo.value = await getEquCheckList(info.value[0].id);
}
function reschange(e: any) {
	console.log(e);
}
const tabsTitle = ref([
	{ key: "1", title: "点检", icon: "tmicon-ios-leaf" },
	{ key: "2", title: "保养", icon: "tmicon-ios-umbrella" },
]);
const list = ref([
	{ text: "日", id: "0" },
	{ text: "周", id: "1" },
	{ text: "月", id: "2" },
]);
async function changetype(e: any) {
	listvo.value = [];
	info.value = await getEquCheck(equid.value, e);
	title.value = info.value[0].title;
	getchecklist();
}
async function tjsub() {

	for (let i = 0; i < listvo.value.length; i++) {
		if (listvo.value[i].remark == "1") {
			if (listvo.value[i].upimg.length == 0) {
				uni.showToast({
					title: "请上传图片",
					icon: "none",
				});
				return;
			} else {
				listvo.value[i].upimg = listvo.value[i].upimg[0].response;
			}
		}
	}
	recinfo.value.list = listvo.value;
	recinfo.value.equid = equid.value;
	await equinset(recinfo.value);
	uni.navigateBack({ delta: 1 })
}
const listvedio = [
	"https://picsum.photos/200/300?id=43335",
	{
		url: "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4",
		img: "https://picsum.photos/200/300?id=43335",
		type: "video",
	},
	"https://picsum.photos/200/300?id=433",
];
</script>
