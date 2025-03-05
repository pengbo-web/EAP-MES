import type { OutstockVO, OutstockForm, OutstockQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询出库管理列表
* @param params
* @returns 出库管理列表
*/
export function outstockList(params?: OutstockQuery) {
  return requestClient.get<PageResult<OutstockVO>>('/mes/outstock/list', { params });
}

/**
 * 导出出库管理列表
 * @param params
 * @returns 出库管理列表
 */
export function outstockExport(params?: OutstockQuery) {
  return commonExport('/mes/outstock/export', params ?? {});
}

/**
 * 查询出库管理详情
 * @param id id
 * @returns 出库管理详情
 */
export function outstockInfo(id: ID) {
  return requestClient.get<OutstockVO>(`/mes/outstock/${id}`);
}

/**
 * 新增出库管理
 * @param data
 * @returns void
 */
export function outstockAdd(data: OutstockForm) {
  return requestClient.postWithMsg<void>('/mes/outstock', data);
}

/**
 * 更新出库管理
 * @param data
 * @returns void
 */
export function outstockUpdate(data: OutstockForm) {
  return requestClient.putWithMsg<void>('/mes/outstock', data);
}

/**
 * 删除出库管理
 * @param id id
 * @returns void
 */
export function outstockRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/outstock/${id}`);
}
