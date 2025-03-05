import type { AllrecVO, AllrecForm, AllrecQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询出入库记录列表
* @param params
* @returns 出入库记录列表
*/
export function allrecList(params?: AllrecQuery) {
  return requestClient.get<PageResult<AllrecVO>>('/mes/allrec/list', { params });
}

/**
 * 导出出入库记录列表
 * @param params
 * @returns 出入库记录列表
 */
export function allrecExport(params?: AllrecQuery) {
  return commonExport('/mes/allrec/export', params ?? {});
}

/**
 * 查询出入库记录详情
 * @param id id
 * @returns 出入库记录详情
 */
export function allrecInfo(id: ID) {
  return requestClient.get<AllrecVO>(`/mes/allrec/${id}`);
}

/**
 * 新增出入库记录
 * @param data
 * @returns void
 */
export function allrecAdd(data: AllrecForm) {
  return requestClient.postWithMsg<void>('/mes/allrec', data);
}

/**
 * 更新出入库记录
 * @param data
 * @returns void
 */
export function allrecUpdate(data: AllrecForm) {
  return requestClient.putWithMsg<void>('/mes/allrec', data);
}

/**
 * 删除出入库记录
 * @param id id
 * @returns void
 */
export function allrecRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/allrec/${id}`);
}
