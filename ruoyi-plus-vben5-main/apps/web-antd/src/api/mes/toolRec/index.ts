import type { ToolRecVO, ToolRecForm, ToolRecQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询夹具记录列表
* @param params
* @returns 夹具记录列表
*/
export function toolRecList(params?: ToolRecQuery) {
  return requestClient.get<PageResult<ToolRecVO>>('/mes/toolRec/list', { params });
}

/**
 * 导出夹具记录列表
 * @param params
 * @returns 夹具记录列表
 */
export function toolRecExport(params?: ToolRecQuery) {
  return commonExport('/mes/toolRec/export', params ?? {});
}

/**
 * 查询夹具记录详情
 * @param id id
 * @returns 夹具记录详情
 */
export function toolRecInfo(id: ID) {
  return requestClient.get<ToolRecVO>(`/mes/toolRec/${id}`);
}

/**
 * 新增夹具记录
 * @param data
 * @returns void
 */
export function toolRecAdd(data: ToolRecForm) {
  return requestClient.postWithMsg<void>('/mes/toolRec', data);
}

/**
 * 更新夹具记录
 * @param data
 * @returns void
 */
export function toolRecUpdate(data: ToolRecForm) {
  return requestClient.putWithMsg<void>('/mes/toolRec', data);
}

/**
 * 删除夹具记录
 * @param id id
 * @returns void
 */
export function toolRecRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/toolRec/${id}`);
}
