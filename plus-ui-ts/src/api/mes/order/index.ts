import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { OrderVO, OrderForm, OrderQuery } from '@/api/mes/order/types';

/**
 * 查询销售订单列表
 * @param query
 * @returns {*}
 */

export const listOrder = (query?: OrderQuery): AxiosPromise<OrderVO[]> => {
  return request({
    url: '/mes/order/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询销售订单详细
 * @param id
 */
export const getOrder = (id: string | number): AxiosPromise<OrderVO> => {
  return request({
    url: '/mes/order/' + id,
    method: 'get'
  });
};

/**
 * 新增销售订单
 * @param data
 */
export const addOrder = (data: OrderForm) => {
  return request({
    url: '/mes/order',
    method: 'post',
    data: data
  });
};

/**
 * 修改销售订单
 * @param data
 */
export const updateOrder = (data: OrderForm) => {
  return request({
    url: '/mes/order',
    method: 'put',
    data: data
  });
};

/**
 * 删除销售订单
 * @param id
 */
export const delOrder = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/order/' + id,
    method: 'delete'
  });
};
