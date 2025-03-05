import type { InstVO, InstForm, InstQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询其他入库列表
* @param params
* @returns 其他入库列表
*/
export function instList(params?: InstQuery) {
  return requestClient.get<PageResult<InstVO>>('/mes/inst/list', { params });
}

/**
 * 导出其他入库列表
 * @param params
 * @returns 其他入库列表
 */
export function instExport(params?: InstQuery) {
  return commonExport('/mes/inst/export', params ?? {});
}

/**
 * 查询其他入库详情
 * @param id id
 * @returns 其他入库详情
 */
export function instInfo(id: ID) {
  return requestClient.get<InstVO>(`/mes/inst/${id}`);
}

/**
 * 新增其他入库
 * @param data
 * @returns void
 */
export function instAdd(data: InstForm) {
  return requestClient.postWithMsg<void>('/mes/inst', data);
}

/**
 * 更新其他入库
 * @param data
 * @returns void
 */
export function instUpdate(data: InstForm) {
  return requestClient.putWithMsg<void>('/mes/inst', data);
}

/**
 * 删除其他入库
 * @param id id
 * @returns void
 */
export function instRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/inst/${id}`);
}
