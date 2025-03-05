import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { StockTypeVO, StockTypeForm, StockTypeQuery } from '@/api/mes/stockType/types';

/**
 * 查询库存分类列表
 * @param query
 * @returns {*}
 */

export const listStockType = (query?: StockTypeQuery): AxiosPromise<StockTypeVO[]> => {
  return request({
    url: '/mes/stockType/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询库存分类详细
 * @param id
 */
export const getStockType = (id: string | number): AxiosPromise<StockTypeVO> => {
  return request({
    url: '/mes/stockType/' + id,
    method: 'get'
  });
};

/**
 * 新增库存分类
 * @param data
 */
export const addStockType = (data: StockTypeForm) => {
  return request({
    url: '/mes/stockType',
    method: 'post',
    data: data
  });
};

/**
 * 修改库存分类
 * @param data
 */
export const updateStockType = (data: StockTypeForm) => {
  return request({
    url: '/mes/stockType',
    method: 'put',
    data: data
  });
};

/**
 * 删除库存分类
 * @param id
 */
export const delStockType = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/stockType/' + id,
    method: 'delete'
  });
};
