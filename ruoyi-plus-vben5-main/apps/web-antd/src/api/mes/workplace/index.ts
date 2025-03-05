import type { WorkplaceVO, WorkplaceForm, WorkplaceQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询工作站列表
* @param params
* @returns 工作站列表
*/
export function workplaceList(params?: WorkplaceQuery) {
  return requestClient.get<PageResult<WorkplaceVO>>('/mes/workplace/list', { params });
}

/**
 * 导出工作站列表
 * @param params
 * @returns 工作站列表
 */
export function workplaceExport(params?: WorkplaceQuery) {
  return commonExport('/mes/workplace/export', params ?? {});
}

/**
 * 查询工作站详情
 * @param id id
 * @returns 工作站详情
 */
export function workplaceInfo(id: ID) {
  return requestClient.get<WorkplaceVO>(`/mes/workplace/${id}`);
}

/**
 * 新增工作站
 * @param data
 * @returns void
 */
export function workplaceAdd(data: WorkplaceForm) {
  return requestClient.postWithMsg<void>('/mes/workplace', data);
}

/**
 * 更新工作站
 * @param data
 * @returns void
 */
export function workplaceUpdate(data: WorkplaceForm) {
  return requestClient.putWithMsg<void>('/mes/workplace', data);
}

/**
 * 删除工作站
 * @param id id
 * @returns void
 */
export function workplaceRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/workplace/${id}`);
}
