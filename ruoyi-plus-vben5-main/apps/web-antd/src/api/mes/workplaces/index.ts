import type { WorkplacesVO, WorkplacesForm, WorkplacesQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询工作站列表
* @param params
* @returns 工作站列表
*/
export function workplacesList(params?: WorkplacesQuery) {
  return requestClient.get<PageResult<WorkplacesVO>>('/mes/workplaces/list', { params });
}

/**
 * 导出工作站列表
 * @param params
 * @returns 工作站列表
 */
export function workplacesExport(params?: WorkplacesQuery) {
  return commonExport('/mes/workplaces/export', params ?? {});
}

/**
 * 查询工作站详情
 * @param id id
 * @returns 工作站详情
 */
export function workplacesInfo(id: ID) {
  return requestClient.get<WorkplacesVO>(`/mes/workplaces/${id}`);
}

/**
 * 新增工作站
 * @param data
 * @returns void
 */
export function workplacesAdd(data: WorkplacesForm) {
  return requestClient.postWithMsg<void>('/mes/workplaces', data);
}

/**
 * 更新工作站
 * @param data
 * @returns void
 */
export function workplacesUpdate(data: WorkplacesForm) {
  return requestClient.putWithMsg<void>('/mes/workplaces', data);
}

/**
 * 删除工作站
 * @param id id
 * @returns void
 */
export function workplacesRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/workplaces/${id}`);
}
