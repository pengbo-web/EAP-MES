import type { TaskcVO, TaskcForm, TaskcQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询组装流转列表
* @param params
* @returns 组装流转列表
*/
export function taskcList(params?: TaskcQuery) {
  return requestClient.get<PageResult<TaskcVO>>('/mes/taskc/list', { params });
}

/**
 * 导出组装流转列表
 * @param params
 * @returns 组装流转列表
 */
export function taskcExport(params?: TaskcQuery) {
  return commonExport('/mes/taskc/export', params ?? {});
}

/**
 * 查询组装流转详情
 * @param id id
 * @returns 组装流转详情
 */
export function taskcInfo(id: ID) {
  return requestClient.get<TaskcVO>(`/mes/taskc/${id}`);
}

/**
 * 新增组装流转
 * @param data
 * @returns void
 */
export function taskcAdd(data: TaskcForm) {
  return requestClient.postWithMsg<void>('/mes/taskc', data);
}

/**
 * 更新组装流转
 * @param data
 * @returns void
 */
export function taskcUpdate(data: TaskcForm) {
  return requestClient.putWithMsg<void>('/mes/taskc', data);
}

/**
 * 删除组装流转
 * @param id id
 * @returns void
 */
export function taskcRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/taskc/${id}`);
}
