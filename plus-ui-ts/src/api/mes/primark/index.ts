import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { PrimarkVO, PrimarkForm, PrimarkQuery } from '@/api/mes/primark/types';

/**
 * 查询打单列表列表
 * @param query
 * @returns {*}
 */

export const listPrimark = (query?: PrimarkQuery): AxiosPromise<PrimarkVO[]> => {
  return request({
    url: '/mes/primark/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询打单列表详细
 * @param id
 */
export const getPrimark = (id: string | number): AxiosPromise<PrimarkVO> => {
  return request({
    url: '/mes/primark/' + id,
    method: 'get'
  });
};

/**
 * 新增打单列表
 * @param data
 */
export const addPrimark = (data: PrimarkForm) => {
  return request({
    url: '/mes/primark',
    method: 'post',
    data: data
  });
};

/**
 * 修改打单列表
 * @param data
 */
export const updatePrimark = (data: PrimarkForm) => {
  return request({
    url: '/mes/primark',
    method: 'put',
    data: data
  });
};

/**
 * 删除打单列表
 * @param id
 */
export const delPrimark = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/primark/' + id,
    method: 'delete'
  });
};
