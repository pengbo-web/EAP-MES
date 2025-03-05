import type { OrderDeVO, OrderDeForm, OrderDeQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询订单详情列表
* @param params
* @returns 订单详情列表
*/
export function orderDeList(params?: OrderDeQuery) {
  return requestClient.get<PageResult<OrderDeVO>>('/mes/orderDe/list', { params });
}

/**
 * 导出订单详情列表
 * @param params
 * @returns 订单详情列表
 */
export function orderDeExport(params?: OrderDeQuery) {
  return commonExport('/mes/orderDe/export', params ?? {});
}

/**
 * 查询订单详情详情
 * @param id id
 * @returns 订单详情详情
 */
export function orderDeInfo(id: ID) {
  return requestClient.get<OrderDeVO>(`/mes/orderDe/${id}`);
}

/**
 * 新增订单详情
 * @param data
 * @returns void
 */
export function orderDeAdd(data: OrderDeForm) {
  return requestClient.postWithMsg<void>('/mes/orderDe', data);
}

/**
 * 更新订单详情
 * @param data
 * @returns void
 */
export function orderDeUpdate(data: OrderDeForm) {
  return requestClient.putWithMsg<void>('/mes/orderDe', data);
}

/**
 * 删除订单详情
 * @param id id
 * @returns void
 */
export function orderDeRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/orderDe/${id}`);
}
