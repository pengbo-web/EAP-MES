import type { PriceRecVO, PriceRecForm, PriceRecQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询价格波动列表
* @param params
* @returns 价格波动列表
*/
export function priceRecList(params?: PriceRecQuery) {
  return requestClient.get<PageResult<PriceRecVO>>('/mes/priceRec/list', { params });
}

/**
 * 导出价格波动列表
 * @param params
 * @returns 价格波动列表
 */
export function priceRecExport(params?: PriceRecQuery) {
  return commonExport('/mes/priceRec/export', params ?? {});
}

/**
 * 查询价格波动详情
 * @param id id
 * @returns 价格波动详情
 */
export function priceRecInfo(id: ID) {
  return requestClient.get<PriceRecVO>(`/mes/priceRec/${id}`);
}

/**
 * 新增价格波动
 * @param data
 * @returns void
 */
export function priceRecAdd(data: PriceRecForm) {
  return requestClient.postWithMsg<void>('/mes/priceRec', data);
}

/**
 * 更新价格波动
 * @param data
 * @returns void
 */
export function priceRecUpdate(data: PriceRecForm) {
  return requestClient.putWithMsg<void>('/mes/priceRec', data);
}

/**
 * 删除价格波动
 * @param id id
 * @returns void
 */
export function priceRecRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/priceRec/${id}`);
}
