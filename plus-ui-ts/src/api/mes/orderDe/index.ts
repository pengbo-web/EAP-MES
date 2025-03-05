import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { OrderDeVO, OrderDeForm, OrderDeQuery } from '@/api/mes/orderDe/types';

/**
 * 查询订单详情列表
 * @param query
 * @returns {*}
 */

export const listOrderDe = (query?: OrderDeQuery): AxiosPromise<OrderDeVO[]> => {
  return request({
    url: '/mes/orderDe/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询订单详情详细
 * @param id
 */
export const getOrderDe = (id: string | number): AxiosPromise<OrderDeVO> => {
  return request({
    url: '/mes/orderDe/' + id,
    method: 'get'
  });
};

/**
 * 新增订单详情
 * @param data
 */
export const addOrderDe = (data: OrderDeForm) => {
  return request({
    url: '/mes/orderDe',
    method: 'post',
    data: data
  });
};

/**
 * 修改订单详情
 * @param data
 */
export const updateOrderDe = (data: OrderDeForm) => {
  return request({
    url: '/mes/orderDe',
    method: 'put',
    data: data
  });
};

/**
 * 删除订单详情
 * @param id
 */
export const delOrderDe = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/orderDe/' + id,
    method: 'delete'
  });
};
