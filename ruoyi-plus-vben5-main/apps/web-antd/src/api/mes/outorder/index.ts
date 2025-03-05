import type { OutorderVO, OutorderForm, OutorderQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询委外订单列表
* @param params
* @returns 委外订单列表
*/
export function outorderList(params?: OutorderQuery) {
  return requestClient.get<PageResult<OutorderVO>>('/mes/outorder/list', { params });
}

/**
 * 导出委外订单列表
 * @param params
 * @returns 委外订单列表
 */
export function outorderExport(params?: OutorderQuery) {
  return commonExport('/mes/outorder/export', params ?? {});
}

/**
 * 查询委外订单详情
 * @param id id
 * @returns 委外订单详情
 */
export function outorderInfo(id: ID) {
  return requestClient.get<OutorderVO>(`/mes/outorder/${id}`);
}

/**
 * 新增委外订单
 * @param data
 * @returns void
 */
export function outorderAdd(data: OutorderForm) {
  return requestClient.postWithMsg<void>('/mes/outorder', data);
}

/**
 * 更新委外订单
 * @param data
 * @returns void
 */
export function outorderUpdate(data: OutorderForm) {
  return requestClient.putWithMsg<void>('/mes/outorder', data);
}

/**
 * 删除委外订单
 * @param id id
 * @returns void
 */
export function outorderRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/outorder/${id}`);
}
