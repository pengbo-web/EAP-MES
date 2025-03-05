import type { TaskVO, TaskForm, TaskQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询任务流转列表
* @param params
* @returns 任务流转列表
*/
export function taskList(params?: TaskQuery) {
  return requestClient.get<PageResult<TaskVO>>('/task/task/list', { params });
}

/**
 * 导出任务流转列表
 * @param params
 * @returns 任务流转列表
 */
export function taskExport(params?: TaskQuery) {
  return commonExport('/task/task/export', params ?? {});
}

/**
 * 查询任务流转详情
 * @param id id
 * @returns 任务流转详情
 */
export function taskInfo(id: ID) {
  return requestClient.get<TaskVO>(`/task/task/${id}`);
}

/**
 * 新增任务流转
 * @param data
 * @returns void
 */
export function taskAdd(data: TaskForm) {
  return requestClient.postWithMsg<void>('/task/task', data);
}

/**
 * 更新任务流转
 * @param data
 * @returns void
 */
export function taskUpdate(data: TaskForm) {
  return requestClient.putWithMsg<void>('/task/task', data);
}

/**
 * 删除任务流转
 * @param id id
 * @returns void
 */
export function taskRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/task/task/${id}`);
}
