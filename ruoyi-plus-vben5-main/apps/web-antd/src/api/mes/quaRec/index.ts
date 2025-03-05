import type { QuaRecVO, QuaRecForm, QuaRecQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询质检列表列表
* @param params
* @returns 质检列表列表
*/
export function quaRecList(params?: QuaRecQuery) {
  return requestClient.get<PageResult<QuaRecVO>>('/mes/quaRec/list', { params });
}

/**
 * 导出质检列表列表
 * @param params
 * @returns 质检列表列表
 */
export function quaRecExport(params?: QuaRecQuery) {
  return commonExport('/mes/quaRec/export', params ?? {});
}

/**
 * 查询质检列表详情
 * @param id id
 * @returns 质检列表详情
 */
export function quaRecInfo(id: ID) {
  return requestClient.get<QuaRecVO>(`/mes/quaRec/${id}`);
}

/**
 * 新增质检列表
 * @param data
 * @returns void
 */
export function quaRecAdd(data: QuaRecForm) {
  return requestClient.postWithMsg<void>('/mes/quaRec', data);
}

/**
 * 更新质检列表
 * @param data
 * @returns void
 */
export function quaRecUpdate(data: QuaRecForm) {
  return requestClient.putWithMsg<void>('/mes/quaRec', data);
}

/**
 * 删除质检列表
 * @param id id
 * @returns void
 */
export function quaRecRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/quaRec/${id}`);
}
