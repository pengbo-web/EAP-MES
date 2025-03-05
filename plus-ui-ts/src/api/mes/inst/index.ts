import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { InstVO, InstForm, InstQuery } from '@/api/mes/inst/types';

/**
 * 查询其他入库列表
 * @param query
 * @returns {*}
 */

export const listInst = (query?: InstQuery): AxiosPromise<InstVO[]> => {
  return request({
    url: '/mes/inst/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询其他入库详细
 * @param id
 */
export const getInst = (id: string | number): AxiosPromise<InstVO> => {
  return request({
    url: '/mes/inst/' + id,
    method: 'get'
  });
};

/**
 * 新增其他入库
 * @param data
 */
export const addInst = (data: InstForm) => {
  return request({
    url: '/mes/inst',
    method: 'post',
    data: data
  });
};

/**
 * 修改其他入库
 * @param data
 */
export const updateInst = (data: InstForm) => {
  return request({
    url: '/mes/inst',
    method: 'put',
    data: data
  });
};

/**
 * 删除其他入库
 * @param id
 */
export const delInst = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/inst/' + id,
    method: 'delete'
  });
};
