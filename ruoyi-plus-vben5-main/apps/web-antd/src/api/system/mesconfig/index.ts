import type { MesconfigVO, MesconfigForm, MesconfigQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询MES配置列表
* @param params
* @returns MES配置列表
*/
export function mesconfigList(params?: MesconfigQuery) {
  return requestClient.get<PageResult<MesconfigVO>>('/system/mesconfig/list', { params });
}

/**
 * 导出MES配置列表
 * @param params
 * @returns MES配置列表
 */
export function mesconfigExport(params?: MesconfigQuery) {
  return commonExport('/system/mesconfig/export', params ?? {});
}

/**
 * 查询MES配置详情
 * @param id id
 * @returns MES配置详情
 */
export function mesconfigInfo(id: ID) {
  return requestClient.get<MesconfigVO>(`/system/mesconfig/${id}`);
}

/**
 * 新增MES配置
 * @param data
 * @returns void
 */
export function mesconfigAdd(data: MesconfigForm) {
  return requestClient.postWithMsg<void>('/system/mesconfig', data);
}

/**
 * 更新MES配置
 * @param data
 * @returns void
 */
export function mesconfigUpdate(data: MesconfigForm) {
  return requestClient.putWithMsg<void>('/system/mesconfig', data);
}

/**
 * 删除MES配置
 * @param id id
 * @returns void
 */
export function mesconfigRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/system/mesconfig/${id}`);
}
