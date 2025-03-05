import type { ProcurementplanVO, ProcurementplanForm, ProcurementplanQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询采购计划列表
* @param params
* @returns 采购计划列表
*/
export function procurementplanList(params?: ProcurementplanQuery) {
  return requestClient.get<PageResult<ProcurementplanVO>>('/mes/procurementplan/list', { params });
}

/**
 * 导出采购计划列表
 * @param params
 * @returns 采购计划列表
 */
export function procurementplanExport(params?: ProcurementplanQuery) {
  return commonExport('/mes/procurementplan/export', params ?? {});
}

/**
 * 查询采购计划详情
 * @param id id
 * @returns 采购计划详情
 */
export function procurementplanInfo(id: ID) {
  return requestClient.get<ProcurementplanVO>(`/mes/procurementplan/${id}`);
}

/**
 * 新增采购计划
 * @param data
 * @returns void
 */
export function procurementplanAdd(data: ProcurementplanForm) {
  return requestClient.postWithMsg<void>('/mes/procurementplan', data);
}

/**
 * 更新采购计划
 * @param data
 * @returns void
 */
export function procurementplanUpdate(data: ProcurementplanForm) {
  return requestClient.putWithMsg<void>('/mes/procurementplan', data);
}

/**
 * 删除采购计划
 * @param id id
 * @returns void
 */
export function procurementplanRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/procurementplan/${id}`);
}
