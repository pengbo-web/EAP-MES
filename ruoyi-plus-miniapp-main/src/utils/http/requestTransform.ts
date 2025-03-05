import type { CustomRequestOptions } from '../../types/request';

export type UniRequestConfig = UniApp.RequestOptions;
export type UniResponse = UniApp.RequestSuccessCallbackResult;
/**
 * 合并requestOptions
 */
export type UniUploadConfig = UniApp.UploadFileOption & UniRequestConfig;

export interface CreateRequestConfig extends UniRequestConfig {
  /**
   * 转换器
   */
  transform?: RequestTransform;
  /**
   * 自定义options
   */
  requestOptions?: CustomRequestOptions;
}

export abstract class RequestTransform {
  /**
   * 在发送请求之前调用的函数
   */
  beforeRequestHook?: (config: UniRequestConfig, options: CustomRequestOptions) => UniRequestConfig;

  /**
   * 处理响应数据
   */
  transformResponseHook?: (response: UniResponse, options: CustomRequestOptions) => any;

  /**
   * 请求失败处理  一般是网络错误  即httpCode !== 200
   */
  requestCatchHook?: (error: Error, options: CustomRequestOptions) => void;
}
