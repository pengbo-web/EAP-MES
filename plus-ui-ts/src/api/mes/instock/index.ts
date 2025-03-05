import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { InstockVO, InstockForm, InstockQuery } from '@/api/mes/instock/types';

/**
 * 查询入库信息列表
 * @param query
 * @returns {*}
 */

export const listInstock = (query?: InstockQuery): AxiosPromise<InstockVO[]> => {
  return request({
    url: '/mes/instock/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询入库信息详细
 * @param id
 */
export const getInstock = (id: string | number): AxiosPromise<InstockVO> => {
  return request({
    url: '/mes/instock/' + id,
    method: 'get'
  });
};

/**
 * 新增入库信息
 * @param data
 */
export const addInstock = (data: InstockForm) => {
  return request({
    url: '/mes/instock',
    method: 'post',
    data: data
  });
};

/**
 * 修改入库信息
 * @param data
 */
export const updateInstock = (data: InstockForm) => {
  return request({
    url: '/mes/instock',
    method: 'put',
    data: data
  });
};

/**
 * 删除入库信息
 * @param id
 */
export const delInstock = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/instock/' + id,
    method: 'delete'
  });
};
