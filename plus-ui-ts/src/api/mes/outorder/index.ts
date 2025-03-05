import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { OutorderVO, OutorderForm, OutorderQuery } from '@/api/mes/outorder/types';

/**
 * 查询委外订单列表
 * @param query
 * @returns {*}
 */

export const listOutorder = (query?: OutorderQuery): AxiosPromise<OutorderVO[]> => {
  return request({
    url: '/mes/outorder/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询委外订单详细
 * @param id
 */
export const getOutorder = (id: string | number): AxiosPromise<OutorderVO> => {
  return request({
    url: '/mes/outorder/' + id,
    method: 'get'
  });
};

/**
 * 新增委外订单
 * @param data
 */
export const addOutorder = (data: OutorderForm) => {
  return request({
    url: '/mes/outorder',
    method: 'post',
    data: data
  });
};

/**
 * 修改委外订单
 * @param data
 */
export const updateOutorder = (data: OutorderForm) => {
  return request({
    url: '/mes/outorder',
    method: 'put',
    data: data
  });
};

/**
 * 删除委外订单
 * @param id
 */
export const delOutorder = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/outorder/' + id,
    method: 'delete'
  });
};
