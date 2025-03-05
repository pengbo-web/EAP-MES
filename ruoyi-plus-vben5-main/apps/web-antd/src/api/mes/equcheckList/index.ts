import type { EqucheckListVO, EqucheckListForm, EqucheckListQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询设备点检列列表
* @param params
* @returns 设备点检列列表
*/
export function equcheckListList(params?: EqucheckListQuery) {
  return requestClient.get<PageResult<EqucheckListVO>>('/mes/equcheckList/list', { params });
}

/**
 * 导出设备点检列列表
 * @param params
 * @returns 设备点检列列表
 */
export function equcheckListExport(params?: EqucheckListQuery) {
  return commonExport('/mes/equcheckList/export', params ?? {});
}

/**
 * 查询设备点检列详情
 * @param id id
 * @returns 设备点检列详情
 */
export function equcheckListInfo(id: ID) {
  return requestClient.get<EqucheckListVO>(`/mes/equcheckList/${id}`);
}

/**
 * 新增设备点检列
 * @param data
 * @returns void
 */
export function equcheckListAdd(data: EqucheckListForm) {
  return requestClient.postWithMsg<void>('/mes/equcheckList', data);
}

/**
 * 更新设备点检列
 * @param data
 * @returns void
 */
export function equcheckListUpdate(data: EqucheckListForm) {
  return requestClient.putWithMsg<void>('/mes/equcheckList', data);
}

/**
 * 删除设备点检列
 * @param id id
 * @returns void
 */
export function equcheckListRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/equcheckList/${id}`);
}
