import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { SaleoutVO, SaleoutForm, SaleoutQuery } from '@/api/mes/saleout/types';

/**
 * 查询销售出库列表
 * @param query
 * @returns {*}
 */

export const listSaleout = (query?: SaleoutQuery): AxiosPromise<SaleoutVO[]> => {
  return request({
    url: '/mes/saleout/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询销售出库详细
 * @param id
 */
export const getSaleout = (id: string | number): AxiosPromise<SaleoutVO> => {
  return request({
    url: '/mes/saleout/' + id,
    method: 'get'
  });
};

/**
 * 新增销售出库
 * @param data
 */
export const addSaleout = (data: SaleoutForm) => {
  return request({
    url: '/mes/saleout',
    method: 'post',
    data: data
  });
};

/**
 * 修改销售出库
 * @param data
 */
export const updateSaleout = (data: SaleoutForm) => {
  return request({
    url: '/mes/saleout',
    method: 'put',
    data: data
  });
};

/**
 * 删除销售出库
 * @param id
 */
export const delSaleout = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/saleout/' + id,
    method: 'delete'
  });
};
