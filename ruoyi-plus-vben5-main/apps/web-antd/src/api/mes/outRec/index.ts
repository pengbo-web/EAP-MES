import type { OutRecVO, OutRecForm, OutRecQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询外协记录列表
* @param params
* @returns 外协记录列表
*/
export function outRecList(params?: OutRecQuery) {
  return requestClient.get<PageResult<OutRecVO>>('/mes/outRec/list', { params });
}

/**
 * 导出外协记录列表
 * @param params
 * @returns 外协记录列表
 */
export function outRecExport(params?: OutRecQuery) {
  return commonExport('/mes/outRec/export', params ?? {});
}

/**
 * 查询外协记录详情
 * @param id id
 * @returns 外协记录详情
 */
export function outRecInfo(id: ID) {
  return requestClient.get<OutRecVO>(`/mes/outRec/${id}`);
}

/**
 * 新增外协记录
 * @param data
 * @returns void
 */
export function outRecAdd(data: OutRecForm) {
  return requestClient.postWithMsg<void>('/mes/outRec', data);
}

/**
 * 更新外协记录
 * @param data
 * @returns void
 */
export function outRecUpdate(data: OutRecForm) {
  return requestClient.putWithMsg<void>('/mes/outRec', data);
}

/**
 * 删除外协记录
 * @param id id
 * @returns void
 */
export function outRecRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/outRec/${id}`);
}
