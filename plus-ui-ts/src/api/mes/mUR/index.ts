import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { MURVO, MURForm, MURQuery } from '@/api/mes/mUR/types';

/**
 * 查询移动套餐列表
 * @param query
 * @returns {*}
 */

export const listMUR = (query?: MURQuery): AxiosPromise<MURVO[]> => {
  return request({
    url: '/mes/mUR/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询移动套餐详细
 * @param id
 */
export const getMUR = (id: string | number): AxiosPromise<MURVO> => {
  return request({
    url: '/mes/mUR/' + id,
    method: 'get'
  });
};

/**
 * 新增移动套餐
 * @param data
 */
export const addMUR = (data: MURForm) => {
  return request({
    url: '/mes/mUR',
    method: 'post',
    data: data
  });
};

/**
 * 修改移动套餐
 * @param data
 */
export const updateMUR = (data: MURForm) => {
  return request({
    url: '/mes/mUR',
    method: 'put',
    data: data
  });
};

/**
 * 删除移动套餐
 * @param id
 */
export const delMUR = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/mUR/' + id,
    method: 'delete'
  });
};
