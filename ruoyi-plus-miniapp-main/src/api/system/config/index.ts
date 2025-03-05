import { httpRequest } from '@/utils/http';
import { ID, IDS, PageQuery, PageResult } from '@/api/base';
import { Config } from './model';
// import { Result } from '/#/axios';

enum Api {
  root = '/system/config',
  configList = '/system/config/list',
  configExport = '/system/config/export',
  configRefreshCache = '/system/config/refreshCache',
  configInfoByKey = '/system/config/configKey',
}

export function configList(data?: PageQuery) {
  return httpRequest.get<PageResult<Config>>({ url: Api.configList, data });
}

export function configInfo(configId: ID) {
  return httpRequest.get<Config>({ url: Api.root + '/' + configId });
}

export function configExport() {
  return httpRequest.post<Blob>(
    { url: Api.configExport, responseType: 'blob' },
    { isTransformResponse: false },
  );
}

export function configRefreshCache() {
  return httpRequest.deleteWithMsg<void>({ url: Api.configRefreshCache });
}

export function configUpdate(data: any) {
  return httpRequest.putWithMsg<void>({ url: Api.root, data });
}

export function configAdd(data: any) {
  return httpRequest.postWithMsg<void>({ url: Api.root, data });
}

export function configRemove(configIds: IDS) {
  return httpRequest.deleteWithMsg<void>({ url: Api.root + '/' + configIds });
}

/**
 * 返回结果是在msg里???
 * @param configKey configKey
 * @returns
 */
export function configInfoByKey(configKey: string) {
  return httpRequest.get<Config>({ url: Api.configInfoByKey + '/' + configKey });
}
