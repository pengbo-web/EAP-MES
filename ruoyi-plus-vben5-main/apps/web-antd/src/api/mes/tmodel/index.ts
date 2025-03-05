import type { TmodelVO, TmodelForm, TmodelQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport,ContentTypeEnum } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询模具管理列表
* @param params
* @returns 模具管理列表
*/
export function tmodelList(params?: TmodelQuery) {
  return requestClient.get<PageResult<TmodelVO>>('/mes/tmodel/list', { params });
}

/**
 * 导出模具管理列表
 * @param params
 * @returns 模具管理列表
 */
export function tmodelExport(params?: TmodelQuery) {
  return commonExport('/mes/tmodel/export', params ?? {});
}

/**
 * 查询模具管理详情
 * @param id id
 * @returns 模具管理详情
 */
export function tmodelInfo(id: ID) {
  return requestClient.get<TmodelVO>(`/mes/tmodel/${id}`);
}

/**
 * 新增模具管理
 * @param data
 * @returns void
 */
export function tmodelAdd(data: TmodelForm) {
  return requestClient.postWithMsg<void>('/mes/tmodel', data);
}
/**
 * 从excel导入用户
 * @param data
 * @returns void
 */
export function tmodelImportData(data: any) {
  return requestClient.post<{ code: number; msg: string }>(
    '/mes/tmodel/importData',
    data,
    {
      headers: {
        'Content-Type': ContentTypeEnum.FORM_DATA,
      },
      isTransformResponse: false,
    },
  );
}

/**
 * 下载用户导入模板
 * @returns blob
 */
export function downloadImportTemplate() {
  return requestClient.post<Blob>(
    '/mes/tmodel/importTemplate',

    {},
    {
      isTransformResponse: false,
      responseType: 'blob',
    },
  );
}

/**
 * 更新模具管理
 * @param data
 * @returns void
 */
export function tmodelUpdate(data: TmodelForm) {
  return requestClient.putWithMsg<void>('/mes/tmodel', data);
}

/**
 * 删除模具管理
 * @param id id
 * @returns void
 */
export function tmodelRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/tmodel/${id}`);
}
