import type { ModelrecVO, ModelrecForm, ModelrecQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询模具使用记录列表
* @param params
* @returns 模具使用记录列表
*/
export function modelrecList(params?: ModelrecQuery) {
  return requestClient.get<PageResult<ModelrecVO>>('/mes/modelrec/list', { params });
}

/**
 * 导出模具使用记录列表
 * @param params
 * @returns 模具使用记录列表
 */
export function modelrecExport(params?: ModelrecQuery) {
  return commonExport('/mes/modelrec/export', params ?? {});
}

/**
 * 查询模具使用记录详情
 * @param id id
 * @returns 模具使用记录详情
 */
export function modelrecInfo(id: ID) {
  return requestClient.get<ModelrecVO>(`/mes/modelrec/${id}`);
}

/**
 * 新增模具使用记录
 * @param data
 * @returns void
 */
export function modelrecAdd(data: ModelrecForm) {
  return requestClient.postWithMsg<void>('/mes/modelrec', data);
}

/**
 * 更新模具使用记录
 * @param data
 * @returns void
 */
export function modelrecUpdate(data: ModelrecForm) {
  return requestClient.putWithMsg<void>('/mes/modelrec', data);
}

/**
 * 删除模具使用记录
 * @param id id
 * @returns void
 */
export function modelrecRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/modelrec/${id}`);
}
