<template>
	<tm-app>
		<tm-sheet>
			<tm-text :font-size="24" _class="text-weight-b" :label="title"></tm-text>
			<tm-divider></tm-divider>
			<div v-if="show">
				《了云制造》app服务协议<br/>
				
				第一条 协议概述<br/>
				1.1 本协议是您与了了文化科技（杭州）有限责任公司（以下简称"我司"）之间就《了云制造》app的使用所订立的法律协议。<br/>
				1.2 您在注册使用《了云制造》app前,请务必仔细阅读本协议。一旦您开始使用本app,即视为您已充分理解并接受本协议的全部内容。<br/>
				
				第二条 服务内容<br/>
				2.1 《了云制造》app为制造企业提供生产管理、数据分析、工艺优化等数字化管理服务。<br/>
				2.2 我司将根据企业需求,不断优化app功能,提供更加丰富、便利的数字化管理解决方案。<br/>
				
				第三条 用户权利与义务<br/>
				3.1 您有权根据本协议约定使用《了云制造》app及相关服务。<br/>
				3.2 您应当遵守国家法律法规,尊重他人知识产权,不得利用app从事违法或不正当行为。<br/>
				3.3 您有义务妥善保管账号信息,因您泄露账号导致的损失由您自行承担。<br/>
				
				第四条 知识产权声明<br/>
				4.1《了云制造》app及其所包含的一切创作物的知识产权归我司所有。<br/>
				4.2 未经我司许可,您不得对app进行任何形式的复制、修改、传播等侵权行为。<br/>
				
				第五条 免责声明<br/>
				5.1 您使用本app过程中,因不可抗力、病毒、黑客攻击等因素造成的损失,我司不承担任何责任。<br/>
				5.2 我司对app的及时性、安全性、准确性等不做任何保证,对由此造成的损失亦不承担责任。<br/>
				
				第六条 协议终止<br/>
				6.1 若您违反本协议任何条款,我司有权单方面终止向您提供服务。<br/>
				6.2 本协议终止后,您应停止使用《了云制造》app,我司有权要求您删除相关数据。<br/>
				
				第七条 其他条款<br/>
				7.1 本协议的解释及争议解决适用中华人民共和国法律。<br/>
				7.2 本协议未尽事宜,双方可另行签订补充协议。<br/>
				
				了了文化科技(杭州)有限责任公司<br/>
				2024年10月01日
</div>
<div v-if="shows">
				《了云制造》app隐私政策<br/>
				
				尊敬的用户:<br/>
				
				了了文化科技（杭州）有限责任公司（以下简称"我司"）非常重视您的个人信息和隐私权保护。为了让您更好地了解我们对您个人信息的收集和使用情况,特制定本隐私政策,供您参考。<br/>
				
				一、 信息收集与使用<br/>
				
				我们可能会收集您的设备信息(如设备型号、操作系统等)、位置信息(仅在获得您授权时收集)等,以提供更好的服务体验。<br/>
				我们还可能在获得您授权的情况下,收集您的相机和相册信息,以便您上传照片/视频至我司系统。但我们不会在未经授权的情况下访问或使用您的相机和相册。<br/><br/>
				我们收集的信息将仅用于提供《了云制造》app服务,不会用于其他目的或分享给任何第三方。<br/>
				二、 信息保护措施<br/>
				
				我们采取多重加密等技术手段,确保您的个人信息安全。<br/>
				只有获得授权的员工可以接触您的个人信息,他们都将遵守保密协议。<br/>
				我们定期对个人信息进行脱敏处理和备份,防范数据丢失或被篡改的风险。<br/>
				三、 您的权利<br/>
				
				您有权查询、更正或删除我们所持有的您的个人信息。<br/>
				如果您发现我们的行为损害了您的合法权益,可以向相关部门投诉。<br/>
				四、 隐私政策更新<br/>
				
				本隐私政策将根据法律法规的变化及我们业务发展的需要适时更新。我们会通过app内公告、电子邮件等方式及时告知您相关变更。<br/>
				
				感谢您的理解和支持。如有任何疑问,欢迎随时与我们联系。<br/>
				
				了了文化科技（杭州）有限责任公司<br/>
				2024年10月01日<br/>
</div>
		</tm-sheet>
	</tm-app>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { onShow, onLoad } from '@dcloudio/uni-app'
import tmApp from '@/tmui/components/tm-app/tm-app.vue'
import tmSheet from '@/tmui/components/tm-sheet/tm-sheet.vue'
import tmText from '@/tmui/components/tm-text/tm-text.vue'
import tmDivider from '@/tmui/components/tm-divider/tm-divider.vue'
const id=ref(0);
const title=ref('');
const conts=ref('');
const show=ref(false);
const shows=ref(false);
onLoad(async(options)=>{
	if(options.id==1){
		title.value='服务协议'
		show.value=true;
	}else{
		title.value='隐私协议'
		shows.value=true;
	}
})
</script>
<style>
</style>