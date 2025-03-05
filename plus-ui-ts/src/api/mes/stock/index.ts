import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { StockVO, StockForm, StockQuery } from '@/api/mes/stock/types';

/**
 * 查询仓库信息列表
 * @param query
 * @returns {*}
 */

export const listStock = (query?: StockQuery): AxiosPromise<StockVO[]> => {
  return request({
    url: '/mes/stock/list',
    method: 'get',
    params: query
  });
};
export const rulist = (id:any,query?: any): AxiosPromise<StockVO[]> => {
  return request({
    url: '/mes/stock/rulist/' + id,
    method: 'get',
    params: query
  });
};
export const chulist = (id:any,query?: any): AxiosPromise<StockVO[]> => {
  return request({
    url: '/mes/stock/chulist/' + id,
    method: 'get',
    params: query
  });
};

/**
 * 查询仓库信息详细
 * @param id
 */
export const getStock = (id: string | number): AxiosPromise<StockVO> => {
  return request({
    url: '/mes/stock/' + id,
    method: 'get'
  });
};

/**
 * 新增仓库信息
 * @param data
 */
export const addStock = (data: StockForm) => {
  return request({
    url: '/mes/stock',
    method: 'post',
    data: data
  });
};

/**
 * 修改仓库信息
 * @param data
 */
export const updateStock = (data: StockForm) => {
  return request({
    url: '/mes/stock',
    method: 'put',
    data: data
  });
};

/**
 * 删除仓库信息
 * @param id
 */
export const delStock = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/stock/' + id,
    method: 'delete'
  });
};
