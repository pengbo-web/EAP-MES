import type { SaleoutVO, SaleoutForm, SaleoutQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询销售出库列表
* @param params
* @returns 销售出库列表
*/
export function saleoutList(params?: SaleoutQuery) {
  return requestClient.get<PageResult<SaleoutVO>>('/mes/saleout/list', { params });
}
/**
* 查询销售出库列表
* @param params
* @returns 销售出库列表
*/
export function saleoutrecList(params?: SaleoutQuery) {
  return requestClient.get<PageResult<SaleoutVO>>('/mes/saleout/reclist', { params });
}

/**
 * 导出销售出库列表
 * @param params
 * @returns 销售出库列表
 */
export function saleoutExport(params?: SaleoutQuery) {
  return commonExport('/mes/saleout/export', params ?? {});
}

/**
 * 查询销售出库详情
 * @param id id
 * @returns 销售出库详情
 */
export function saleoutInfo(id: ID) {
  return requestClient.get<SaleoutVO>(`/mes/saleout/${id}`);
}

/**
 * 新增销售出库
 * @param data
 * @returns void
 */
export function saleoutAdd(data: SaleoutForm) {
  return requestClient.postWithMsg<void>('/mes/saleout', data);
}

/**
 * 更新销售出库
 * @param data
 * @returns void
 */
export function saleoutUpdate(data: SaleoutForm) {
  return requestClient.putWithMsg<void>('/mes/saleout', data);
}

/**
 * 删除销售出库
 * @param id id
 * @returns void
 */
export function saleoutRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/saleout/${id}`);
}
