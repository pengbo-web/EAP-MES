import type { StockVO, StockForm, StockQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询仓库信息列表
* @param params
* @returns 仓库信息列表
*/
export function stockList(params?: StockQuery) {
  return requestClient.get<PageResult<StockVO>>('/mes/stock/list', { params });
}

/**
 * 导出仓库信息列表
 * @param params
 * @returns 仓库信息列表
 */
export function stockExport(params?: StockQuery) {
  return commonExport('/mes/stock/export', params ?? {});
}

/**
 * 查询仓库信息详情
 * @param id id
 * @returns 仓库信息详情
 */
export function stockInfo(id: ID) {
  return requestClient.get<StockVO>(`/mes/stock/${id}`);
}
/**
 * 获取供应商
 * @param id 
 * @returns 
 */
export function getcustom(id: ID) {
  return requestClient.get<any>(`/mes/stock/getcust/${id}`);
}


/**
 * 新增仓库信息
 * @param data
 * @returns void
 */
export function stockAdd(data: StockForm) {
  return requestClient.postWithMsg<void>('/mes/stock', data);
}

/**
 * 更新仓库信息
 * @param data
 * @returns void
 */
export function stockUpdate(data: StockForm) {
  return requestClient.putWithMsg<void>('/mes/stock', data);
}

/**
 * 删除仓库信息
 * @param id id
 * @returns void
 */
export function stockRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/stock/${id}`);
}
