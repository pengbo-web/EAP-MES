import type { InrecVO, InrecForm, InrecQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询入库记录列表
* @param params
* @returns 入库记录列表
*/
export function inrecList(params?: InrecQuery) {
  return requestClient.get<PageResult<InrecVO>>('/mes/inrec/list', { params });
}

/**
 * 导出入库记录列表
 * @param params
 * @returns 入库记录列表
 */
export function inrecExport(params?: InrecQuery) {
  return commonExport('/mes/inrec/export', params ?? {});
}

/**
 * 查询入库记录详情
 * @param id id
 * @returns 入库记录详情
 */
export function inrecInfo(id: ID) {
  return requestClient.get<InrecVO>(`/mes/inrec/${id}`);
}

/**
 * 新增入库记录
 * @param data
 * @returns void
 */
export function inrecAdd(data: InrecForm) {
  return requestClient.postWithMsg<void>('/mes/inrec', data);
}

/**
 * 更新入库记录
 * @param data
 * @returns void
 */
export function inrecUpdate(data: InrecForm) {
  return requestClient.putWithMsg<void>('/mes/inrec', data);
}

/**
 * 删除入库记录
 * @param id id
 * @returns void
 */
export function inrecRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/inrec/${id}`);
}
