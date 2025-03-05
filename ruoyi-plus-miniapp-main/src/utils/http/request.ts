import type {
  CreateRequestConfig,
  UniRequestConfig,
  UniResponse,
  UniUploadConfig,
} from '@/utils/http/requestTransform';
import type { CustomRequestOptions } from '../../types/request';
import { cloneDeep, isFunction } from 'lodash-es';
import { ContentTypeEnum } from '@/enums/httpEnum';

export class HttpRequest {
  private readonly config: CreateRequestConfig;

  constructor(config: CreateRequestConfig) {
    this.config = config;
  }

  private getTransform() {
    const { transform } = this.config;
    return transform;
  }

  request<T = any>(config: UniRequestConfig, options?: CustomRequestOptions) {
    let conf: CreateRequestConfig = cloneDeep(config);
    const transform = this.getTransform();

    // 合并CustomRequestOptions
    const { requestOptions } = this.config;
    const opt: CustomRequestOptions = Object.assign({}, requestOptions, options);

    const { beforeRequestHook, transformResponseHook, requestCatchHook } = transform || {};
    // 请求前处理数据
    if (beforeRequestHook && isFunction(beforeRequestHook)) {
      conf = beforeRequestHook(conf, opt);
    }
    conf.requestOptions = opt;

    // loading
    if (opt.showLoading) {
      uni.showLoading({ title: '加载中...', mask: true });
    }
    return new Promise<T>((resolve, reject) => {
      uni
        .request(conf)
        .then((response) => {
          // 需要先关闭loading 否则会提示`请注意 showLoading 与 hideLoading 必须配对使用`
          if (opt.showLoading) {
            uni.hideLoading();
          }
          // 请求后处理
          if (transformResponseHook && isFunction(transformResponseHook)) {
            try {
              const ret = transformResponseHook(response, opt);
              resolve(ret);
            } catch (err) {
              reject(err || new Error('request error'));
            }
          }
        })
        .catch((err: Error) => {
          // 网络错误会走到这里
          if (opt.showLoading) {
            uni.hideLoading();
          }
          // 请求后处理
          if (transformResponseHook && isFunction(requestCatchHook)) {
            requestCatchHook(err, opt);
          }
        });
    });
  }

  uploadFile<T = any>(config: UniUploadConfig, options?: CustomRequestOptions) {
    let conf: CreateRequestConfig = cloneDeep(config);
    const transform = this.getTransform();

    // 合并CustomRequestOptions
    const { requestOptions } = this.config;
    const opt: CustomRequestOptions = Object.assign({}, requestOptions, options);

    const { beforeRequestHook, transformResponseHook, requestCatchHook } = transform || {};
    // 请求前处理数据
    if (beforeRequestHook && isFunction(beforeRequestHook)) {
      conf = beforeRequestHook(conf, opt);
    }
    conf.requestOptions = opt;

    // loading
    if (opt.showLoading) {
      uni.showLoading({ title: '加载中...', mask: true });
    }

    // 这里是做一个转换器 把所有参数都转为Upload
    const uniUploadOption: UniApp.UploadFileOption = {
      url: conf.url,
      files: config.files,
      file: config.file,
      filePath: config.filePath,
      name: config.name,
      // 不需要拍Content-Type  他会自己设置
      header: {
        ...conf.header,
      },
      timeout: conf.timeout,
      formData: config.formData,
    };

    return new Promise<T>((resolve, reject) => {
      uni
        .uploadFile(uniUploadOption)
        .then((response) => {
          // 需要先关闭loading 否则会提示`请注意 showLoading 与 hideLoading 必须配对使用`
          if (opt.showLoading) {
            uni.hideLoading();
          }
          // 请求后处理
          if (transformResponseHook && isFunction(transformResponseHook)) {
            // 这里直接做一个适配器
            const fileRet: UniResponse = {
              ...response,
              header: {},
              cookies: [],
            };
            try {
              const ret = transformResponseHook(fileRet, opt);
              resolve(ret);
            } catch (err) {
              reject(err || new Error('request error'));
            }
          }
        })
        .catch((err: Error) => {
          // 网络错误会走到这里
          if (opt.showLoading) {
            uni.hideLoading();
          }
          // 请求后处理
          if (transformResponseHook && isFunction(requestCatchHook)) {
            requestCatchHook(err, opt);
          }
        });
    });
  }

  get<T = any>(config: UniRequestConfig, options?: CustomRequestOptions): Promise<T> {
    return this.request({ ...config, method: 'GET' }, options);
  }

  post<T = any>(config: UniRequestConfig, options?: CustomRequestOptions): Promise<T> {
    return this.request({ ...config, method: 'POST' }, options);
  }

  put<T = any>(config: UniRequestConfig, options?: CustomRequestOptions): Promise<T> {
    return this.request({ ...config, method: 'PUT' }, options);
  }

  delete<T = any>(config: UniRequestConfig, options?: CustomRequestOptions): Promise<T> {
    return this.request({ ...config, method: 'DELETE' }, options);
  }

  postWithMsg<T = any>(config: UniRequestConfig, options?: CustomRequestOptions): Promise<T> {
    return this.request({ ...config, method: 'POST' }, { ...options, successMessageMode: 'toast' });
  }

  putWithMsg<T = any>(config: UniRequestConfig, options?: CustomRequestOptions): Promise<T> {
    return this.request({ ...config, method: 'PUT' }, { ...options, successMessageMode: 'toast' });
  }

  deleteWithMsg<T = any>(config: UniRequestConfig, options?: CustomRequestOptions): Promise<T> {
    return this.request(
      { ...config, method: 'DELETE' },
      { ...options, successMessageMode: 'toast' },
    );
  }
}
