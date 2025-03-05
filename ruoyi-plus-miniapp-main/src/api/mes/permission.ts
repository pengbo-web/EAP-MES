const checkIsIos = async () => {
	return new Promise((resolve) => {
		uni.getSystemInfo({
			success: function (res) {
				resolve(res.platform === 'ios');
			},
		});
	});
};
export default class authorizeUtils {
	/**
	 * 授权前告知用户使用意图
	 * @param content
	 * @returns
	 */
	static showAuthTipModal = async (authorize: string) => {
		// #ifdef H5
		if (1 === 1) {
			return true;
		}
		// #endif
		// ios端在manifest.json配置权限使用说明，以下权限判断仅在安卓端可用
		let isIos = await checkIsIos();
		if (isIos) return true;
		let compat: any = plus.android.importClass('androidx.core.content.ContextCompat');
		let context = plus.android.runtimeMainActivity();
		let result = compat.checkSelfPermission(context, authorize);
		if (result === 0) return true;
		// // 如果已经授权直接返回
		const contentData = {
			['android.permission.ACCESS_FINE_LOCATION']: {
				title: '定位权限说明',
				describe: '便于您使用该功能在位置展示时显示距搜索位置的距离，请您确认授权，否则无法使用该功能',
			},
			['android.permission.READ_EXTERNAL_STORAGE']: {
				title: '相册权限说明',
				describe: '便于您使用该功能上传您的照片/图片/视频及用户修改头像、意见反馈、保存相册时上传图片，请您确认授权，否则无法使用该功能',
			},
			['android.permission.CAMERA']: {
				title: '拍摄权限说明',
				describe: '便于您使用该功能拍摄照片修改头像、意见反馈上传图片等信息，请您确认授权，否则无法使用该功能',
			},
			['android.permission.CALL_PHONE']: {
				title: '拨打电话权限说明',
				describe: '便于您使用该功能拨发货人、司机、客服电话，请您确认授权，否则无法使用该功能',
			},
		};
		return new Promise((resolve) => {
			uni.showModal({
				title: contentData[authorize].title,
				content: contentData[authorize].describe,
				success: (res) => {
					console.log(res)
					resolve(!!res.confirm);
				},
				fail: () => {},
			});
		});
	};
	/**
	 * 用户拒绝授权提示手动授权
	 */
	static showManualAuth = async (authorize: string) => {
		let isIos = await checkIsIos();
		if (isIos) return true;
		const contentData = {
			['android.permission.ACCESS_FINE_LOCATION']: '获取定位权限失败，请手动打开授权或检查系统定位开关',
			['android.permission.READ_EXTERNAL_STORAGE']: '获取相册权限失败，请手动打开授权',
			['android.permission.CAMERA']: '获取拍摄权限失败，请手动打开授权',
			['android.permission.CALL_PHONE']: '获取拨打电话权限失败，请手动打开授权',
		};
		uni.showModal({
			title: '提示',
			content: contentData[authorize],
			confirmText: '去设置',
			success: (res) => {
				if (res.confirm) {
					uni.openAppAuthorizeSetting({
						success(res) {
							console.log(res);
						},
					});
				}
				if (res.cancel) {
					console.log('用户点击取消');
				}
			},
		});
	};
}