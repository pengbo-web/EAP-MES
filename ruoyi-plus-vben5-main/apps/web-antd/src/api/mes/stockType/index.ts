import type { StockTypeVO, StockTypeForm, StockTypeQuery } from './model';

import type { ID, IDS } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询库存分类列表
* @param params
* @returns 库存分类列表
*/
export function stockTypeList(params?: StockTypeQuery) {
  return requestClient.get<StockTypeVO[]>(`/mes/stockType/list`, { params });
}


/**
 * 查询库存分类详情
 * @param id id
 * @returns 库存分类详情
 */
export function stockTypeInfo(id: ID) {
  return requestClient.get<StockTypeVO>(`/mes/stockType/${id}`);
}

/**
 * 新增库存分类
 * @param data
 * @returns void
 */
export function stockTypeAdd(data: StockTypeForm) {
  return requestClient.postWithMsg<void>('/mes/stockType', data);
}

/**
 * 更新库存分类
 * @param data
 * @returns void
 */
export function stockTypeUpdate(data: StockTypeForm) {
  return requestClient.putWithMsg<void>('/mes/stockType', data);
}

/**
 * 删除库存分类
 * @param id id
 * @returns void
 */
export function stockTypeRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/stockType/${id}`);
}
