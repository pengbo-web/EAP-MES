import type { OutorderDeVO, OutorderDeForm, OutorderDeQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询委外详情列表
* @param params
* @returns 委外详情列表
*/
export function outorderDeList(params?: OutorderDeQuery) {
  return requestClient.get<PageResult<OutorderDeVO>>('/mes/outorderDe/list', { params });
}

/**
 * 导出委外详情列表
 * @param params
 * @returns 委外详情列表
 */
export function outorderDeExport(params?: OutorderDeQuery) {
  return commonExport('/mes/outorderDe/export', params ?? {});
}

/**
 * 查询委外详情详情
 * @param id id
 * @returns 委外详情详情
 */
export function outorderDeInfo(id: ID) {
  return requestClient.get<OutorderDeVO>(`/mes/outorderDe/${id}`);
}

/**
 * 新增委外详情
 * @param data
 * @returns void
 */
export function outorderDeAdd(data: OutorderDeForm) {
  return requestClient.postWithMsg<void>('/mes/outorderDe', data);
}

/**
 * 更新委外详情
 * @param data
 * @returns void
 */
export function outorderDeUpdate(data: OutorderDeForm) {
  return requestClient.putWithMsg<void>('/mes/outorderDe', data);
}

/**
 * 删除委外详情
 * @param id id
 * @returns void
 */
export function outorderDeRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/outorderDe/${id}`);
}
