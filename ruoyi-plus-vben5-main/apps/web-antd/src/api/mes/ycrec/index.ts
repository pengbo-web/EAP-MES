import type { YcrecVO, YcrecForm, YcrecQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询异常报工审核列表
* @param params
* @returns 异常报工审核列表
*/
export function ycrecList(params?: YcrecQuery) {
  return requestClient.get<PageResult<YcrecVO>>('/mes/ycrec/list', { params });
}

/**
 * 导出异常报工审核列表
 * @param params
 * @returns 异常报工审核列表
 */
export function ycrecExport(params?: YcrecQuery) {
  return commonExport('/mes/ycrec/export', params ?? {});
}

/**
 * 查询异常报工审核详情
 * @param id id
 * @returns 异常报工审核详情
 */
export function ycrecInfo(id: ID) {
  return requestClient.get<YcrecVO>(`/mes/ycrec/${id}`);
}

/**
 * 新增异常报工审核
 * @param data
 * @returns void
 */
export function ycrecAdd(data: YcrecForm) {
  return requestClient.postWithMsg<void>('/mes/ycrec', data);
}

/**
 * 更新异常报工审核
 * @param data
 * @returns void
 */
export function ycrecUpdate(data: YcrecForm) {
  return requestClient.putWithMsg<void>('/mes/ycrec', data);
}

/**
 * 删除异常报工审核
 * @param id id
 * @returns void
 */
export function ycrecRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/ycrec/${id}`);
}
