import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { StocktakingVO, StocktakingForm, StocktakingQuery } from '@/api/mes/stocktaking/types';

/**
 * 查询库位盘点列表
 * @param query
 * @returns {*}
 */

export const listStocktaking = (query?: StocktakingQuery): AxiosPromise<StocktakingVO[]> => {
  return request({
    url: '/mes/stocktaking/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询库位盘点详细
 * @param id
 */
export const getStocktaking = (id: string | number): AxiosPromise<StocktakingVO> => {
  return request({
    url: '/mes/stocktaking/' + id,
    method: 'get'
  });
};

/**
 * 新增库位盘点
 * @param data
 */
export const addStocktaking = (data: StocktakingForm) => {
  return request({
    url: '/mes/stocktaking',
    method: 'post',
    data: data
  });
};

/**
 * 修改库位盘点
 * @param data
 */
export const updateStocktaking = (data: StocktakingForm) => {
  return request({
    url: '/mes/stocktaking',
    method: 'put',
    data: data
  });
};

/**
 * 删除库位盘点
 * @param id
 */
export const delStocktaking = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/stocktaking/' + id,
    method: 'delete'
  });
};
