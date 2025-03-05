import type { OrderVO, OrderForm, OrderQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询销售订单列表
* @param params
* @returns 销售订单列表
*/
export function orderList(params?: OrderQuery) {
  return requestClient.get<PageResult<OrderVO>>('/mes/order/list', { params });
}

/**
 * 导出销售订单列表
 * @param params
 * @returns 销售订单列表
 */
export function orderExport(params?: OrderQuery) {
  return commonExport('/mes/order/export', params ?? {});
}

/**
 * 查询销售订单详情
 * @param id id
 * @returns 销售订单详情
 */
export function orderInfo(id: ID) {
  return requestClient.get<OrderVO>(`/mes/order/${id}`);
}

export function bequickapi(id: ID) {
  return requestClient.postWithMsg<OrderVO>(`/mes/order/bequick/${id}`);
}

/**
 * 新增销售订单
 * @param data
 * @returns void
 */
export function orderAdd(data: OrderForm) {
  return requestClient.postWithMsg<void>('/mes/order', data);
}

/**
 * 更新销售订单
 * @param data
 * @returns void
 */
export function orderUpdate(data: OrderForm) {
  return requestClient.putWithMsg<void>('/mes/order', data);
}

/**
 * 删除销售订单
 * @param id id
 * @returns void
 */
export function orderRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/order/${id}`);
}
