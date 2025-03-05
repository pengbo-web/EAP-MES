import type { ProductVO, ProductForm, ProductQuery } from './model';

import type { ID, IDS } from '#/api/common';

import { requestClient } from '#/api/request';
import { commonExport, ContentTypeEnum } from '#/api/helper';
/**
* 查询产品管理列表
* @param params
* @returns 产品管理列表
*/
export function productList(params?: ProductQuery) {
  return requestClient.get<ProductVO[]>(`/mes/product/list`, { params });
}
export function productLists(params?: ProductQuery) {
  return requestClient.get<ProductVO[]>(`/mes/product/lists`, { params });
}
export function productListsbyid(id:ID,params?: ProductQuery) {
  return requestClient.get<ProductVO[]>(`/mes/product/getboml/${id}`, { params });
}


/**
 * 查询产品管理详情
 * @param id id
 * @returns 产品管理详情
 */
export function productInfo(id: ID) {
  return requestClient.get<ProductVO>(`/mes/product/${id}`);
}

/**
 * 新增产品管理
 * @param data
 * @returns void
 */
export function productAdd(data: ProductForm) {
  return requestClient.postWithMsg<void>('/mes/product', data);
}
export function productAdds(data: any) {
  return requestClient.postWithMsg<void>('/mes/product/ins', data);
}

/**
 * 更新产品管理
 * @param data
 * @returns void
 */
export function productUpdate(data: ProductForm) {
  return requestClient.putWithMsg<void>('/mes/product', data);
}

/**
 * 删除产品管理
 * @param id id
 * @returns void
 */
export function productRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/product/${id}`);
}
/**
 * 从excel导入用户
 * @param data
 * @returns void
 */
export function productImportData(data: any) {
  return requestClient.post<{ code: number; msg: string }>(
    '/mes/product/importData',
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
    '/mes/product/importTemplate',
    {},
    {
      isTransformResponse: false,
      responseType: 'blob',
    },
  );
}

/**
 * 导出excel
 * @param data data
 * @returns blob
 */
export function productExport(data: any) {
  return commonExport('/mes/product/export', data);
}
