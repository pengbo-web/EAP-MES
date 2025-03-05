import type { CuttingRecVO, CuttingRecForm, CuttingRecQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询刀具记录审核列表
* @param params
* @returns 刀具记录审核列表
*/
export function cuttingRecList(params?: CuttingRecQuery) {
  return requestClient.get<PageResult<CuttingRecVO>>('/mes/cuttingRec/list', { params });
}

/**
 * 导出刀具记录审核列表
 * @param params
 * @returns 刀具记录审核列表
 */
export function cuttingRecExport(params?: CuttingRecQuery) {
  return commonExport('/mes/cuttingRec/export', params ?? {});
}

/**
 * 查询刀具记录审核详情
 * @param id id
 * @returns 刀具记录审核详情
 */
export function cuttingRecInfo(id: ID) {
  return requestClient.get<CuttingRecVO>(`/mes/cuttingRec/${id}`);
}

/**
 * 新增刀具记录审核
 * @param data
 * @returns void
 */
export function cuttingRecAdd(data: CuttingRecForm) {
  return requestClient.postWithMsg<void>('/mes/cuttingRec', data);
}

/**
 * 更新刀具记录审核
 * @param data
 * @returns void
 */
export function cuttingRecUpdate(data: CuttingRecForm) {
  return requestClient.putWithMsg<void>('/mes/cuttingRec', data);
}

/**
 * 删除刀具记录审核
 * @param id id
 * @returns void
 */
export function cuttingRecRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/cuttingRec/${id}`);
}
