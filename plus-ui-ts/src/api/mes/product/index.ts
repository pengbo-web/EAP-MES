import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { ProductVO, ProductForm, ProductQuery } from '@/api/mes/product/types';

/**
 * 查询③产品管理列表
 * @param query
 * @returns {*}
 */

export const listProduct = (query?: ProductQuery): AxiosPromise<ProductVO[]> => {
  return request({
    url: '/mes/product/list',
    method: 'get',
    params: query
  });
};
export const getbom = (id:any): AxiosPromise<ProductVO[]> => {
  return request({
    url: '/mes/product/getboml/'+id,
    method: 'get',
  });
};

/**
 * 查询③产品管理列表
 * @param query
 * @returns {*}
 */

export const listProducts = (query?: ProductQuery): AxiosPromise<ProductVO[]> => {
  return request({
    url: '/mes/product/lists',
    method: 'get',
    params: query
  });
};

/**
 * 查询③产品管理详细
 * @param id
 */
export const getProduct = (id: string | number): AxiosPromise<ProductVO> => {
  return request({
    url: '/mes/product/' + id,
    method: 'get'
  });
};

/**
 * 新增③产品管理
 * @param data
 */
export const addProduct = (data: ProductForm) => {
  return request({
    url: '/mes/product',
    method: 'post',
    data: data
  });
};

/**
 * 修改③产品管理
 * @param data
 */
export const updateProduct = (data: ProductForm) => {
  return request({
    url: '/mes/product',
    method: 'put',
    data: data
  });
};

/**
 * 删除③产品管理
 * @param id
 */
export const delProduct = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/product/' + id,
    method: 'delete'
  });
};
