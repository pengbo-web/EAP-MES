import type {
  RequestTransform,
  UniRequestConfig,
  UniResponse,
  CreateRequestConfig,
} from '@/utils/http/requestTransform';
import type { CustomRequestOptions } from '../../types/request';
import { ResultEnum, RequestMsgEnum } from '@/enums/httpEnum';
import { isEmpty, isNull, isUndefined } from 'lodash-es';
import { isString } from '@/tmui/tool/function/util';
import { generateAesKey, encryptBase64, encryptWithAes } from '@/utils/encryption/crypto';
import { encrypt } from '@/utils/encryption/jsencrypt';
import { HttpRequest } from '@/utils/http/request';
import { cloneDeep, clone, isObject } from 'lodash-es';
import { useGlobSetting } from '@/common/useSetting';
import { ContentTypeEnum } from '@/enums/httpEnum';
import { getToken } from '../auth';
import { useUserStoreWithOut } from '@/store/modules/user';
import { language$t } from '@/tmui/tool/lib/language';

const transform: RequestTransform = {
  /**
   * 在发送请求之前调用的函数
   */
  beforeRequestHook: (config: UniRequestConfig, options: CustomRequestOptions) => {
    const { apiUrl, urlPrefix, clientId } = options;
    // 由于uni.request没有baseUrl  需要拼接
    let realRequestUrl = '';
    if (apiUrl && isString(apiUrl)) {
      if (urlPrefix && isString(urlPrefix)) {
        // apiurl + 前缀 + 实际请求路径
        realRequestUrl = `${apiUrl}${urlPrefix}${config.url}`;
      } else {
        realRequestUrl = `${apiUrl}${config.url}`;
      }
    }
    // 赋值
    config.url = realRequestUrl;

    if (!config.header) {
      config.header = {};
    }
    // 添加clientId
    config.header['clientid'] = clientId;

    // 添加token
    if (options.withToken) {
      const token = getToken() || '';
      if (options.authenticationScheme) {
        config.header['Authorization'] = `${options.authenticationScheme} ${token}`;
      } else {
        config.header['Authorization '] = token;
      }
    }

    // 需要加密 && post/put请求
    if (options.encrypt && ['POST', 'PUT'].includes(config.method?.toUpperCase() || '')) {
      const aesKey = generateAesKey();
      config.header['isEncrypt'] = true;
      config.header['encrypt-key'] = encrypt(encryptBase64(aesKey));

      if (typeof config.data === 'object') {
        config.data = encryptWithAes(JSON.stringify(config.data), aesKey);
      } else {
        config.data = encryptWithAes(config.data || '', aesKey);
      }
    }

    // 添加语言
    let language = uni.getLocale();
    if (language.startsWith('zh')) {
      language = 'zh_CN';
    } else if (language.startsWith('en')) {
      language = 'en_US';
    }
    config.header['content-language'] = language;
    return config;
  },

  /**
   * 处理响应数据
   */
  transformResponseHook: (response: UniResponse, options: CustomRequestOptions) => {
    const { isTransformResponse, isReturnNativeResponse } = options;
    // 是否需要返回原生响应
    if (isReturnNativeResponse) {
      return response;
    }
    // 不需要转换响应  直接获取UniResponse.data
    // 用于页面代码可能需要直接获取code，data，message这些信息时开启
    if (!isTransformResponse) {
      return response.data;
    }
    let uniResponseData = response.data;
    // 使用uni.uploadFile 不会自动进行string -> json的转换 需要手动操作
    if (typeof uniResponseData === 'string') {
      try {
        uniResponseData = JSON.parse(uniResponseData);
      } catch (e) {
        return uniResponseData;
      }
    }
    // 如果返回中没有data
    if (!uniResponseData) {
      throw new Error(RequestMsgEnum.FAIL);
    }
    // 解构数据
    const { code, msg, data } = <any>uniResponseData;
    // code为200  请求成功
    const hasSuccess = Reflect.has(<any>uniResponseData, 'code') && code === ResultEnum.SUCCESS;
    if (hasSuccess) {
      // 请求成功消息
      let successMsg = msg;

      if (isNull(successMsg) || isUndefined(successMsg) || isEmpty(successMsg)) {
        successMsg = language$t('message.request.success');
      }

      // 弹窗提示
      if (options.successMessageMode === 'modal') {
        uni.showModal({
          title: language$t('message.info.text'),
          content: successMsg,
          showCancel: false,
        });
      } else if (options.successMessageMode === 'toast') {
        uni.showToast({
          title: successMsg,
          icon: 'success',
        });
      }

      // 在这里要做转换 ruoyi-plus没有采用严格的{code, msg, data}模式
      // 如果有data 直接返回data
      if (data) {
        return data;
      }
      // 没有data  将剩余的参数封装成data
      const transformData: any = {};
      Object.keys(<any>uniResponseData).forEach((key) => {
        transformData[key] = (uniResponseData as any)[key];
      });
      return transformData;
    }

    // 在此处根据自己项目的实际情况对不同的code执行不同的操作
    // 如果不希望中断当前请求，请return数据，否则直接抛出异常即可
    let timeoutMsg = language$t('message.request.fail');
    switch (code) {
      case ResultEnum.TIMEOUT:
        const userStore = useUserStoreWithOut();
        uni.showModal({
          title: language$t('message.info.text'),
          content: language$t('message.login.timeout'),
          showCancel: false,
          success() {
            userStore.resetUserState(true);
          },
        });
        console.log('这里是登录过期');
        return;
      default:
        if (msg) {
          timeoutMsg = msg;
        }
    }

    // 错误弹窗
    if (options.errorMessageMode === 'modal') {
      uni.showModal({
        title: language$t('message.info.text'),
        content: timeoutMsg,
        showCancel: false,
      });
    } else if (options.errorMessageMode === 'toast') {
      uni.showToast({
        title: timeoutMsg,
        icon: 'error',
      });
    }

    // 最后抛出异常
    throw new Error(timeoutMsg || RequestMsgEnum.FAIL);
  },
  /**
   * 请求失败处理  一般是网络错误  即httpCode !== 200
   */
  requestCatchHook: (error: any, options: CustomRequestOptions) => {
    console.log('请求失败:', error);
    const errorMessageMode = options.errorMessageMode || 'none';
    const err = error.errMsg;
    let errMsg = err || language$t('message.unknown.error');

    if (err.includes('request:fail')) {
      errMsg = language$t('message.request.fail');
    }

    // 错误弹窗
    if (errorMessageMode === 'modal') {
      uni.showModal({
        title: language$t('message.info.text'),
        content: errMsg,
        showCancel: false,
      });
    } else if (errorMessageMode === 'toast') {
      uni.showToast({
        title: errMsg,
        icon: 'error',
      });
    }
  },
};

export function deepMerge<T = any>(src: any = {}, target: any = {}): T {
  let key: string;
  const res: any = cloneDeep(src);
  for (key in target) {
    res[key] = isObject(res[key]) ? deepMerge(res[key], target[key]) : target[key];
  }
  return res;
}

function createRequest(config?: CreateRequestConfig) {
  const globSetting = useGlobSetting();
  const defaultConfig: CreateRequestConfig = {
    // 这里必填 但是这个值是没有意义的
    url: '',
    timeout: 10 * 1000,
    header: { 'Content-Type': ContentTypeEnum.JSON },
    transform: clone(transform),
    requestOptions: {
      authenticationScheme: 'Bearer',
      // 是否需要转换响应 即只获取{code, msg, data}中的data
      isTransformResponse: true,
      // 是否返回原生响应 即uni.request().then(res => )
      isReturnNativeResponse: false,
      // api后端路径
      apiUrl: globSetting.apiUrl,
      // 请求拼接路径
      urlPrefix: globSetting.urlPrefix,
      // 请求失败弹窗类型
      errorMessageMode: 'toast',
      // 请求成功弹窗类型
      successMessageMode: 'none',
      // 是否携带token
      withToken: true,
      // clientId
      clientId: globSetting.clientId,
      // 不需要加密
      encrypt: false,
      // 是否显示loading
      showLoading: true,
    },
  };
  return new HttpRequest(deepMerge(defaultConfig, config || {}));
}
// 导出
export const httpRequest = createRequest();
