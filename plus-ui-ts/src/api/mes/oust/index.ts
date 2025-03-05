import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { OustVO, OustForm, OustQuery } from '@/api/mes/oust/types';

/**
 * 查询其他出库列表
 * @param query
 * @returns {*}
 */

export const listOust = (query?: OustQuery): AxiosPromise<OustVO[]> => {
  return request({
    url: '/mes/oust/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询其他出库详细
 * @param id
 */
export const getOust = (id: string | number): AxiosPromise<OustVO> => {
  return request({
    url: '/mes/oust/' + id,
    method: 'get'
  });
};

/**
 * 新增其他出库
 * @param data
 */
export const addOust = (data: OustForm) => {
  return request({
    url: '/mes/oust',
    method: 'post',
    data: data
  });
};

/**
 * 修改其他出库
 * @param data
 */
export const updateOust = (data: OustForm) => {
  return request({
    url: '/mes/oust',
    method: 'put',
    data: data
  });
};

/**
 * 删除其他出库
 * @param id
 */
export const delOust = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/oust/' + id,
    method: 'delete'
  });
};
