import type { MURVO, MURForm, MURQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询移动套餐列表
* @param params
* @returns 移动套餐列表
*/
export function mURList(params?: MURQuery) {
  return requestClient.get<PageResult<MURVO>>('/mes/mUR/list', { params });
}

/**
 * 导出移动套餐列表
 * @param params
 * @returns 移动套餐列表
 */
export function mURExport(params?: MURQuery) {
  return commonExport('/mes/mUR/export', params ?? {});
}

/**
 * 查询移动套餐详情
 * @param id id
 * @returns 移动套餐详情
 */
export function mURInfo(id: ID) {
  return requestClient.get<MURVO>(`/mes/mUR/${id}`);
}

/**
 * 新增移动套餐
 * @param data
 * @returns void
 */
export function mURAdd(data: MURForm) {
  return requestClient.postWithMsg<void>('/mes/mUR', data);
}

/**
 * 更新移动套餐
 * @param data
 * @returns void
 */
export function mURUpdate(data: MURForm) {
  return requestClient.putWithMsg<void>('/mes/mUR', data);
}

/**
 * 删除移动套餐
 * @param id id
 * @returns void
 */
export function mURRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/mUR/${id}`);
}
