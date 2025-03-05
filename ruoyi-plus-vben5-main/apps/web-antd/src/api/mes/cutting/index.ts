import type { CuttingVO, CuttingForm, CuttingQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { requestClient } from '#/api/request';
import { commonExport, ContentTypeEnum } from '#/api/helper';
/**
* 查询刀具管理列表
* @param params
* @returns 刀具管理列表
*/
export function cuttingList(params?: CuttingQuery) {
  return requestClient.get<PageResult<CuttingVO>>('/mes/cutting/list', { params });
}

/**
 * 导出刀具管理列表
 * @param params
 * @returns 刀具管理列表
 */
export function cuttingExport(params?: CuttingQuery) {
  return commonExport('/mes/cutting/export', params ?? {});
}

/**
 * 查询刀具管理详情
 * @param id id
 * @returns 刀具管理详情
 */
export function cuttingInfo(id: ID) {
  return requestClient.get<CuttingVO>(`/mes/cutting/${id}`);
}
/**
 * 从excel导入用户
 * @param data
 * @returns void
 */
export function cuttingImportData(data: any) {
  return requestClient.post<{ code: number; msg: string }>(
    '/mes/cutting/importData',
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
    '/mes/cutting/importTemplate',

    {},
    {
      isTransformResponse: false,
      responseType: 'blob',
    },
  );
}
/**
 * 新增刀具管理
 * @param data
 * @returns void
 */
export function cuttingAdd(data: CuttingForm) {
  return requestClient.postWithMsg<void>('/mes/cutting', data);
}

/**
 * 更新刀具管理
 * @param data
 * @returns void
 */
export function cuttingUpdate(data: CuttingForm) {
  return requestClient.putWithMsg<void>('/mes/cutting', data);
}

/**
 * 删除刀具管理
 * @param id id
 * @returns void
 */
export function cuttingRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/cutting/${id}`);
}
