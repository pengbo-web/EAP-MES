import type { ProcessVO, ProcessForm, ProcessQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询⑧工艺管理列表
* @param params
* @returns ⑧工艺管理列表
*/
export function processList(params?: ProcessQuery) {
  return requestClient.get<PageResult<ProcessVO>>('/mes/process/list', { params });
}
export function processListbyids(data:any[]) {
  return  requestClient.post<any>('/mes/process/getstrings', data);
}

/**
 * 导出⑧工艺管理列表
 * @param params
 * @returns ⑧工艺管理列表
 */
export function processExport(params?: ProcessQuery) {
  return commonExport('/mes/process/export', params ?? {});
}

/**
 * 查询⑧工艺管理详情
 * @param id id
 * @returns ⑧工艺管理详情
 */
export function processInfo(id: ID) {
  return requestClient.get<ProcessVO>(`/mes/process/${id}`);
}

/**
 * 新增⑧工艺管理
 * @param data
 * @returns void
 */
export function processAdd(data: ProcessForm) {
  return requestClient.postWithMsg<void>('/mes/process', data);
}


/**
 * 更新⑧工艺管理
 * @param data
 * @returns void
 */
export function processUpdate(data: ProcessForm) {
  return requestClient.putWithMsg<void>('/mes/process', data);
}

/**
 * 删除⑧工艺管理
 * @param id id
 * @returns void
 */
export function processRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/process/${id}`);
}
