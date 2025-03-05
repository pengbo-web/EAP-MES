import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { PriceRecVO, PriceRecForm, PriceRecQuery } from '@/api/mes/priceRec/types';

/**
 * 查询价格波动列表
 * @param query
 * @returns {*}
 */

export const listPriceRec = (query?: PriceRecQuery): AxiosPromise<PriceRecVO[]> => {
  return request({
    url: '/mes/priceRec/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询价格波动详细
 * @param id
 */
export const getPriceRec = (id: string | number): AxiosPromise<PriceRecVO> => {
  return request({
    url: '/mes/priceRec/' + id,
    method: 'get'
  });
};

/**
 * 新增价格波动
 * @param data
 */
export const addPriceRec = (data: PriceRecForm) => {
  return request({
    url: '/mes/priceRec',
    method: 'post',
    data: data
  });
};

/**
 * 修改价格波动
 * @param data
 */
export const updatePriceRec = (data: PriceRecForm) => {
  return request({
    url: '/mes/priceRec',
    method: 'put',
    data: data
  });
};

/**
 * 删除价格波动
 * @param id
 */
export const delPriceRec = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/priceRec/' + id,
    method: 'delete'
  });
};
