import { httpRequest } from '@/utils/http';
import { ID, IDS, PageQuery, PageResult } from '@/api/base';
import { DictData } from './dictData.model';

enum Api {
  root = '/system/dict/data',
  dictDataList = '/system/dict/data/list',
  dictDataExport = '/system/dict/data/export',
}

/**
 * 主要是DictTag组件使用
 * @param dictType 字典类型
 * @returns
 */
export function dictDataInfo(dictType: string) {
  return httpRequest.get<DictData[]>({ url: Api.root + '/type/' + dictType });
}

export function dictDataList(data?: PageQuery & any) {
  return httpRequest.get<PageResult<DictData>>({ url: Api.dictDataList, data });
}

export function dictDataExport() {
  return httpRequest.post<Blob>(
    { url: Api.dictDataExport, responseType: 'blob' },
    { isTransformResponse: false },
  );
}

export function dictDataRemove(dictIds: IDS) {
  return httpRequest.deleteWithMsg<void>({ url: Api.root + '/' + dictIds });
}

export function dictDataAdd(data: any) {
  return httpRequest.postWithMsg<void>({ url: Api.root, data });
}

export function dictDataUpdate(data: any) {
  return httpRequest.putWithMsg<void>({ url: Api.root, data });
}

// 查询字典数据详细
export function dictDetailInfo(dictCode: ID) {
  return httpRequest.get<DictData>({ url: Api.root + '/' + dictCode });
}
