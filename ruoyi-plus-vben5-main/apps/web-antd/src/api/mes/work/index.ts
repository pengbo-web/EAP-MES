import type { WorkVO, WorkForm, WorkQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询生产派单列表
* @param params
* @returns 生产派单列表
*/
export function workList(params?: WorkQuery) {
  return requestClient.get<PageResult<WorkVO>>('/mes/work/list', { params });
}

/**
 * 导出生产派单列表
 * @param params
 * @returns 生产派单列表
 */
export function workExport(params?: WorkQuery) {
  return commonExport('/mes/work/export', params ?? {});
}

/**
 * 查询生产派单详情
 * @param id id
 * @returns 生产派单详情
 */
export function workInfo(id: ID) {
  return requestClient.get<WorkVO>(`/mes/work/${id}`);
}

/**
 * 新增生产派单
 * @param data
 * @returns void
 */
export function workAdd(data: WorkForm) {
  return requestClient.postWithMsg<void>('/mes/work', data);
}

/**
 * 更新生产派单
 * @param data
 * @returns void
 */
export function workUpdate(data: WorkForm) {
  return requestClient.putWithMsg<void>('/mes/work', data);
}

/**
 * 删除生产派单
 * @param id id
 * @returns void
 */
export function workRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/work/${id}`);
}
