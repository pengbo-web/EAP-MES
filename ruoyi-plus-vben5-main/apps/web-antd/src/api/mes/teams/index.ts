import type { TeamsVO, TeamsForm, TeamsQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询班组管理列表
* @param params
* @returns 班组管理列表
*/
export function teamsList(params?: TeamsQuery) {
  return requestClient.get<PageResult<TeamsVO>>('/mes/teams/list', { params });
}

/**
 * 导出班组管理列表
 * @param params
 * @returns 班组管理列表
 */
export function teamsExport(params?: TeamsQuery) {
  return commonExport('/mes/teams/export', params ?? {});
}

/**
 * 查询班组管理详情
 * @param id id
 * @returns 班组管理详情
 */
export function teamsInfo(id: ID) {
  return requestClient.get<TeamsVO>(`/mes/teams/${id}`);
}

/**
 * 新增班组管理
 * @param data
 * @returns void
 */
export function teamsAdd(data: TeamsForm) {
  return requestClient.postWithMsg<void>('/mes/teams', data);
}

/**
 * 更新班组管理
 * @param data
 * @returns void
 */
export function teamsUpdate(data: TeamsForm) {
  return requestClient.putWithMsg<void>('/mes/teams', data);
}

/**
 * 删除班组管理
 * @param id id
 * @returns void
 */
export function teamsRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/teams/${id}`);
}
