import type { EtoolVO, EtoolForm, EtoolQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport, ContentTypeEnum } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询工装夹具列表
* @param params
* @returns 工装夹具列表
*/
export function etoolList(params?: EtoolQuery) {
  return requestClient.get<PageResult<EtoolVO>>('/mes/etool/list', { params });
}

/**
 * 导出工装夹具列表
 * @param params
 * @returns 工装夹具列表
 */
export function etoolExport(params?: EtoolQuery) {
  return commonExport('/mes/etool/export', params ?? {});
}

/**
 * 查询工装夹具详情
 * @param id id
 * @returns 工装夹具详情
 */
export function etoolInfo(id: ID) {
  return requestClient.get<EtoolVO>(`/mes/etool/${id}`);
}
/**
 * 从excel导入用户
 * @param data
 * @returns void
 */
export function etoolImportData(data: any) {
  return requestClient.post<{ code: number; msg: string }>(
    '/mes/etool/importData',
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
    '/mes/etool/importTemplate',

    {},
    {
      isTransformResponse: false,
      responseType: 'blob',
    },
  );
}
/**
 * 新增工装夹具
 * @param data
 * @returns void
 */
export function etoolAdd(data: EtoolForm) {
  return requestClient.postWithMsg<void>('/mes/etool', data);
}

/**
 * 更新工装夹具
 * @param data
 * @returns void
 */
export function etoolUpdate(data: EtoolForm) {
  return requestClient.putWithMsg<void>('/mes/etool', data);
}

/**
 * 删除工装夹具
 * @param id id
 * @returns void
 */
export function etoolRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/etool/${id}`);
}
