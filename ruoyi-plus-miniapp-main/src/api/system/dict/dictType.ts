import { httpRequest } from '@/utils/http';
import { ID, IDS, PageQuery, PageResult } from '@/api/base';
import { DictType } from './dictType.model';

enum Api {
  root = '/system/dict/type',
  dictTypeList = '/system/dict/type/list',
  dictTypeExport = '/system/dict/type/export',
  dictTypeRefreshCache = '/system/dict/type/refreshCache',
  dictOptionSelctList = '/system/dict/type/optionselect',
}

export function dictList(data?: PageQuery & any) {
  return httpRequest.get<PageResult<DictType>>({ url: Api.dictTypeList, data });
}

export function dictExport() {
  return httpRequest.post<Blob>(
    {
      url: Api.dictTypeExport,
      responseType: 'blob',
    },
    {
      // 返回为二进制数据 不需要转换
      isTransformResponse: false,
    },
  );
}

export function dictTypeRemove(dictIds: IDS) {
  return httpRequest.deleteWithMsg<void>({ url: Api.root + '/' + dictIds });
}

/**
 * 刷新缓存
 * @returns
 */
export function refreshDictTypeCache() {
  return httpRequest.deleteWithMsg<void>({ url: Api.dictTypeRefreshCache });
}

export function dictTypeAdd(data: any) {
  return httpRequest.postWithMsg<void>({ url: Api.root, data });
}

export function dictTypeUpdate(data: any) {
  return httpRequest.putWithMsg<void>({ url: Api.root, data });
}

export function dictTypeInfo(dictId: ID) {
  return httpRequest.get<DictType>({ url: Api.root + '/' + dictId });
}

/**
 * 下拉框  返回值和list一样
 * @returns
 */
export function dictOptionSelctList() {
  return httpRequest.get<DictType[]>({ url: Api.dictOptionSelctList });
}
