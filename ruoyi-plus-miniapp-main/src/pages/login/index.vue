<template>
	<tm-app ref="app">
		<tm-sheet transprent class="flex flex-center" :margin="[25, 25, 25, 0]">
			<view class="flex flex-row flex-row-center-center gap-6">
				<tm-text label="了云制造" :fontSize="60" class="text-weight-b"></tm-text>
				<tm-icon color="green" :font-size="40" name="tmicon-plus-circle-fill"></tm-icon>
			</view>
		</tm-sheet>
		<!-- 小程序的placeholder字体颜色有问题 添加placeholder-style可以解决 -->
		<tm-form @submit="handleSubmit" ref="formRef" v-model="formData" :border="false" :padding="[25, 25, 25, 0]">
			<tm-form-item v-if="tenantData.tenantEnabled" required :padding="[0, 0]" :errHeight="20" field="tenantId">
				<tm-input :inputPadding="[25, 0]" :height="80" :round="15" :border="2" prefix="tn-icon-company"
					suffix="tmicon-angle-right" v-model.lazy="formData.tenantId"
					:placeholder="language$t('login.input.company')" focusColor="pink"
					placeholder-style="color:#c0c4cc"></tm-input>
			</tm-form-item>

			<tm-form-item required :errHeight="20" field="username" :padding="[0]" :rules="formRules.username">
				<tm-input :placeholder="language$t('login.input.username')" focusColor="pink" prefix="tn-icon-my-circle"
					:border="2" :height="80" :round="15" v-model.lazy="formData.username"
					:inputPadding="[25, 0]"></tm-input>
			</tm-form-item>
			<tm-form-item required :border="false" :errHeight="20" field="password" :rules="formRules.password">
				<tm-input password :round="15" :height="80" :border="2" prefix="tn-icon-safe"
					:placeholder="language$t('login.input.password')" focusColor="pink" v-model.lazy="formData.password"
					:inputPadding="[25, 0]"></tm-input>
			</tm-form-item>
			<tm-form-item v-if="captchaData.captchaEnable" required :errHeight="20" :border="false" field="code"
				:rules="formRules.code">
				<tm-input prefix="tn-icon-message" type="number" :placeholder="language$t('login.input.captcha')"
					:round="15" :height="80" :border="2" focusColor="pink" v-model.lazy="formData.code"
					:inputPadding="[25, 0]">
					<template #right>
						<tm-image style="margin-right: -28rpx" :src="captchaData.captchaImage" :width="180" :height="85"
							:round="5" @click="loadCaptchaImage"></tm-image>
					</template>
				</tm-input>
			</tm-form-item>
			<tm-form-item :border="false">
				<tm-button form-type="submit" :round="15" icon="tn-icon-home" block
					:label="language$t('login.submit')"></tm-button>
				<tm-button icon="tn-icon-circle-arrow" :margin="[0, 30]" :round="15" color="orange" block
					@click="handleReset" :label="language$t('login.reset')"></tm-button>
			</tm-form-item>
			<tm-form-item :border="false">
				<view class="ml-30 mr-20 flex flex-between">
					<!-- 切换语言 -->
					<view id="language" class="flex flex-row flex-row-center-center gap-6" @click="switchLanguage">
						<tm-icon name="tmicon-md-planet"></tm-icon>
						<tm-text :label="language$t('language')" :fontSize="26"></tm-text>
					</view>
					<view id="theme" class="flex flex-row flex-row-center-center gap-6" @click="themeToggle">
						<tm-icon name="tmicon-ios-color-palette"></tm-icon>
						<tm-text :label="language$t('user.index.theme.dark')" :fontSize="26"></tm-text>
					</view>
				</view>
			</tm-form-item>
		</tm-form>
		<tm-sheet transprent class="flex flex-center" :margin="[25, 25, 25, 0]">
			<view class="flex flex-row flex-row-center-center gap-9">
				<tm-checkbox :value="allcheck" value="30" color="black" style="border-color: black;"
					border="3"></tm-checkbox>

				<tm-text :fontSize="22" color="grey" class="text-weight-b">

					已阅读并同意</tm-text>
				<navigator url="/pages/login/read?id=1" style="font-size: 12px;">【服务协议】</navigator>和<navigator
					style="font-size: 12px;" url="/pages/login/read?id=2">【隐私政策】</navigator>
			</view>
		</tm-sheet>
		<!-- <te-sheet>
			<view>
				<tm-button @click="openscan">扫描</tm-button>
				<tm-button @click="toprint">打印</tm-button>
			</view>
		</te-sheet> -->
		<tm-modal title="温馨提示" height="300" content="请阅读并同意[服务协议]和[隐私政策]" okText="同意并继续" @ok="took"
			v-model:show="showWin"></tm-modal>

		<!-- 租户选择框 -->
		<tm-picker v-model:show="tenantData.showPicker" :columns="tenantData.tenantList" mapKey="companyName"
			@confirm="tenantConfirm"></tm-picker>
	</tm-app>
</template>

<script setup lang="ts">
	import { ref, reactive } from 'vue';
	import { } from '@/api/auth/captcha';
	import { tenantList } from '@/api/auth';
	import { captchaImage } from '@/api/auth/captcha';
	import { TenantOption } from '@/api/auth/model';
	import { onLoad } from '@dcloudio/uni-app';
	import { isInteger } from '@/tmui/tool/function/util';
	import tmForm from '@/tmui/components/tm-form/tm-form.vue';
	import { unref } from 'vue';
	import { useUserStore } from '@/store/modules/user';
	import { language$t } from '@/tmui/tool/lib/language';
	import type { FormRules } from '@/types/form';
	import { useTheme } from '@/common/useTheme';
	import checkUpdate from '@/uni_modules/uni-upgrade-center-app/utils/check-update'
	import AppVue from '@/App.vue';
	import { initDingH5RemoteDebug } from "dingtalk-h5-remote-debug";
	
	const { themeToggle } = useTheme();
	const allcheck = ref(true);
	const showWin = ref(false);
	/**
	 * todo 要封装复用
	 */
	function switchLanguage() {
		const currentLanguage = uni.getLocale();
		console.log(`当前语言: ${currentLanguage}`);
		if (currentLanguage === 'zh-Hans') {
			uni.setLocale('en');
			return;
		}
		if (currentLanguage === 'en') {
			uni.setLocale('zh-Hans');
			return;
		}
	}

	interface TenantData {
		/**
		 * 显示在label上的值  只做显示使用
		 */
		label : string;
		showPicker : boolean;
		tenantEnabled : boolean;
		tenantList : TenantOption[];
	}
	const tenantData = reactive<TenantData>({
		label: language$t('login.input.company'),
		showPicker: false,
		tenantEnabled: true,
		tenantList: [],
	});
	const defaultTenantId = '';
	// 常规的 打印 扫描
	// #ifdef APP-NVUE

	// #endif
	const openscan = () => {
	}
	async function toprint(){
	

	}
	async function initTenant() {
		const ret = await tenantList();
		console.log(ret);
		tenantData.tenantEnabled = ret.tenantEnabled;
		tenantData.tenantList = ret.voList;

		// 默认选中第一项
		if (ret.tenantEnabled && ret.voList.length > 0) {
			tenantConfirm([2]);
		}
	}
	function took(e) {
		allcheck.value = false
		let pra = {
			isPass: true
		}
		handleSubmit(pra);
	}

	/**
	 *
	 * @param value 返回索引 一般为[0]
	 */
	function tenantConfirm(value : number[]) {
		const currentIndex = value[0];
		tenantData.label = tenantData.tenantList[currentIndex].companyName;
		// formData.value.tenantId = tenantData.tenantList[currentIndex].tenantId;
		formData.value.tenantId = '';
		console.log(value);
	}

	/**
	 * 验证码相关
	 */
	const captchaData = reactive({
		captchaEnable: true,
		captchaImage: '',
	});

	async function loadCaptchaImage() {
		const ret = await captchaImage();
		captchaData.captchaEnable = ret.captchaEnabled;
		captchaData.captchaImage = 'data:image/png;base64,' + ret.img;
		if (captchaData.captchaEnable) {
			formData.value.uuid = ret.uuid;
			// 清除之前输入的验证码
			formData.value.code = '';
		}
	}
	onLoad(async () => {
		if (uni.getStorageSync('saveusername')) {
			formData.value.username = uni.getStorageSync('saveusername')
			formData.value.password = uni.getStorageSync('savepassword')
		}
		//初始化
		    
		// 隐藏返回首页按钮 登录页不应该跳转
		// #ifdef MP-WEIXIN
		uni.hideHomeButton();
		// #endif
		await initTenant();
		await loadCaptchaImage();
		checkUpdate();
	});

	/**
	 * !!! 注意表单参数只能用ref(需要调用reset方法时)  否则微信调用重置方法会报错(H5正常)  TypeError: "formData" is read-only
	 * 编译时也会警告 (vite:esbuild plugin) This assignment will throw because "formData" is a constant
	 * 如果确定不需要调用reset 等功能可以正常使用reactive
	 *
	 * 解决办法有两个1.使用ref  2.自己做一个reset方法 Object.assign(reactiveData, {xxx})
	 */
	const formData = ref({
		username: '',
		password: '',
		code: '',
		uuid: '',
		tenantId: defaultTenantId,
	});

	const formRef = ref<InstanceType<typeof tmForm> | null>(null);
	const formRules = reactive<FormRules<typeof formData.value>>({
		username: [
			{
				required: true,
				message: language$t('login.input.username'),
				validator: (value : string) => value && value.length > 0,
			},
		],
		password: [
			{
				required: true,
				message: language$t('login.input.password'),
				validator: (value : string) => value && value.length > 0,
			},
		],
		code: [
			{
				required: true,
				message: language$t('login.input.code'),
				validator: (value : string) => value && value.length > 0,
			},
			{
				required: true,
				message: language$t('login.input.captcha'),
				validator: (value : string) => isInteger(value),
			},
		],
		uuid: [],
		tenantId: [],
	});

	function handleReset() {
		if (unref(formRef)) {
			unref(formRef)?.clearValidate();
			unref(formRef)?.reset();
		}
		// 默认选中第一个租户
		if (tenantData.tenantEnabled && tenantData.tenantList.length > 0) {
			tenantConfirm([0]);
		}
	}


	const userStore = useUserStore();
	async function handleSubmit(e : Tmui.tmFormSubmitResult) {
		if (!e.isPass) {
			return;
		}

		if (allcheck.value) {
			showWin.value = true;
			return;
		}
		try {
			const requestParam : any = {
				username: formData.value.username,
				password: formData.value.password,
				grantType: 'password',
			};
			// 验证码启用需要添加参数
			if (captchaData.captchaEnable) {
				requestParam.uuid = formData.value.uuid;
				requestParam.code = formData.value.code;
			}
			// 租户启用需要添加参数
			if (tenantData.tenantEnabled) {
				requestParam.tenantId = formData.value.tenantId;
			} else {
				// 没有启用租户的话添加默认租户
				requestParam.tenantId = defaultTenantId;
			}
			// 登录
			await userStore.login(requestParam);
		} catch (e : any) {
			if (e instanceof Error) {
				const { message } = e;
				formData.value.code = '';
				await loadCaptchaImage();
			}
		}
	}
</script>

<style scoped></style>