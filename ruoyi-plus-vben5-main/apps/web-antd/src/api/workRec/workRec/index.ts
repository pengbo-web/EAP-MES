import type { WorkRecVO, WorkRecForm, WorkRecQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询工作记录列表
* @param params
* @returns 工作记录列表
*/
export function workRecList(params?: WorkRecQuery) {
  return requestClient.get<PageResult<WorkRecVO>>('/workRec/workRec/list', { params });
}

/**
 * 导出工作记录列表
 * @param params
 * @returns 工作记录列表
 */
export function workRecExport(params?: WorkRecQuery) {
  return commonExport('/workRec/workRec/export', params ?? {});
}

/**
 * 查询工作记录详情
 * @param id id
 * @returns 工作记录详情
 */
export function workRecInfo(id: ID) {
  return requestClient.get<WorkRecVO>(`/workRec/workRec/${id}`);
}

/**
 * 新增工作记录
 * @param data
 * @returns void
 */
export function workRecAdd(data: WorkRecForm) {
  return requestClient.postWithMsg<void>('/workRec/workRec', data);
}

/**
 * 更新工作记录
 * @param data
 * @returns void
 */
export function workRecUpdate(data: WorkRecForm) {
  return requestClient.putWithMsg<void>('/workRec/workRec', data);
}

/**
 * 删除工作记录
 * @param id id
 * @returns void
 */
export function workRecRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/workRec/workRec/${id}`);
}
