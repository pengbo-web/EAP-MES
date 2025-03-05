/**
 * 失败弹窗类型
 */
export type ErrorMessageMode = 'none' | 'modal' | 'toast' | undefined;
/**
 * 成功弹窗类型
 */
export type SuccessMessageMode = ErrorMessageMode;

/**
 * 自定义请求options
 */
export interface CustomRequestOptions {
  /**
   * 是否需要转换响应 即只获取{code, msg, data}中的data
   */
  isTransformResponse?: boolean;
  /**
   * 是否返回原生响应 即uni.request().then(res => )
   */
  isReturnNativeResponse?: boolean;
  /**
   * api后端路径
   */
  apiUrl?: string;
  /**
   * 请求拼接路径
   */
  urlPrefix?: string;
  /**
   * 请求失败弹窗类型
   */
  errorMessageMode?: ErrorMessageMode;
  /**
   * 请求成功弹窗类型
   */
  successMessageMode?: ErrorMessageMode;
  /**
   * 是否携带token
   */
  withToken?: boolean;
  /**
   * clientId
   */
  clientId?: string;
  /**
   * 是否需要加密
   */
  encrypt?: boolean;
  /**
   * 是否显示loading
   */
  showLoading?: boolean;
  /***
   * token前缀 比如'Bearer',
   */
  authenticationScheme?: string;
}

export interface CommonResult<T = any> {
  code: number;
  msg: string;
  data: T;
}
