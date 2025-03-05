import type { ToolUseVO, ToolUseForm, ToolUseQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询夹具使用列表
* @param params
* @returns 夹具使用列表
*/
export function toolUseList(params?: ToolUseQuery) {
  return requestClient.get<PageResult<ToolUseVO>>('/mes/toolUse/list', { params });
}

/**
 * 导出夹具使用列表
 * @param params
 * @returns 夹具使用列表
 */
export function toolUseExport(params?: ToolUseQuery) {
  return commonExport('/mes/toolUse/export', params ?? {});
}

/**
 * 查询夹具使用详情
 * @param id id
 * @returns 夹具使用详情
 */
export function toolUseInfo(id: ID) {
  return requestClient.get<ToolUseVO>(`/mes/toolUse/${id}`);
}

/**
 * 新增夹具使用
 * @param data
 * @returns void
 */
export function toolUseAdd(data: ToolUseForm) {
  return requestClient.postWithMsg<void>('/mes/toolUse', data);
}

/**
 * 更新夹具使用
 * @param data
 * @returns void
 */
export function toolUseUpdate(data: ToolUseForm) {
  return requestClient.putWithMsg<void>('/mes/toolUse', data);
}

/**
 * 删除夹具使用
 * @param id id
 * @returns void
 */
export function toolUseRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/toolUse/${id}`);
}
