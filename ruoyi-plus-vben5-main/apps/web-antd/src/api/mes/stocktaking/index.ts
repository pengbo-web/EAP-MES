import type { StocktakingVO, StocktakingForm, StocktakingQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询库位盘点列表
* @param params
* @returns 库位盘点列表
*/
export function stocktakingList(params?: StocktakingQuery) {
  return requestClient.get<PageResult<StocktakingVO>>('/mes/stocktaking/list', { params });
}

/**
 * 导出库位盘点列表
 * @param params
 * @returns 库位盘点列表
 */
export function stocktakingExport(params?: StocktakingQuery) {
  return commonExport('/mes/stocktaking/export', params ?? {});
}

/**
 * 查询库位盘点详情
 * @param id id
 * @returns 库位盘点详情
 */
export function stocktakingInfo(id: ID) {
  return requestClient.get<StocktakingVO>(`/mes/stocktaking/${id}`);
}

/**
 * 新增库位盘点
 * @param data
 * @returns void
 */
export function stocktakingAdd(data: StocktakingForm) {
  return requestClient.postWithMsg<void>('/mes/stocktaking', data);
}

/**
 * 更新库位盘点
 * @param data
 * @returns void
 */
export function stocktakingUpdate(data: StocktakingForm) {
  return requestClient.putWithMsg<void>('/mes/stocktaking', data);
}

/**
 * 删除库位盘点
 * @param id id
 * @returns void
 */
export function stocktakingRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/stocktaking/${id}`);
}
