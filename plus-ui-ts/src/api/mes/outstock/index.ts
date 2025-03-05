import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { OutstockVO, OutstockForm, OutstockQuery } from '@/api/mes/outstock/types';
import { UnitmeasureVO, UnitmeasureForm, UnitmeasureQuery } from '@/api/mes/unitmeasure/types';

/**
 * 查询出库管理列表
 * @param query
 * @returns {*}
 */

export const listOutstock = (query?: OutstockQuery): AxiosPromise<OutstockVO[]> => {
  return request({
    url: '/mes/outstock/list',
    method: 'get',
    params: query
  });
};
export const getunitlist = (id: string | number): AxiosPromise<UnitmeasureVO[]> => {
  return request({
    url: '/mes/outstock/unitlist/' + id,
    method: 'get'
  });
};

/**
 * 查询出库管理详细
 * @param id
 */
export const getOutstock = (id: string | number): AxiosPromise<OutstockVO> => {
  return request({
    url: '/mes/outstock/' + id,
    method: 'get'
  });
};

/**
 * 新增出库管理
 * @param data
 */
export const addOutstock = (data: OutstockForm) => {
  return request({
    url: '/mes/outstock',
    method: 'post',
    data: data
  });
};

/**
 * 修改出库管理
 * @param data
 */
export const updateOutstock = (data: OutstockForm) => {
  return request({
    url: '/mes/outstock',
    method: 'put',
    data: data
  });
};

/**
 * 删除出库管理
 * @param id
 */
export const delOutstock = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/outstock/' + id,
    method: 'delete'
  });
};
