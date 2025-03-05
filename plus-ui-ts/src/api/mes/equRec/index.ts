import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { EquRecVO, EquRecForm, EquRecQuery } from '@/api/mes/equRec/types';

/**
 * 查询设备点检记录列表
 * @param query
 * @returns {*}
 */

export const listEquRec = (query?: EquRecQuery): AxiosPromise<EquRecVO[]> => {
  return request({
    url: '/mes/equRec/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询设备点检记录详细
 * @param id
 */
export const getEquRec = (id: string | number): AxiosPromise<EquRecVO> => {
  return request({
    url: '/mes/equRec/' + id,
    method: 'get'
  });
};

/**
 * 新增设备点检记录
 * @param data
 */
export const addEquRec = (data: EquRecForm) => {
  return request({
    url: '/mes/equRec',
    method: 'post',
    data: data
  });
};

/**
 * 修改设备点检记录
 * @param data
 */
export const updateEquRec = (data: EquRecForm) => {
  return request({
    url: '/mes/equRec',
    method: 'put',
    data: data
  });
};

/**
 * 删除设备点检记录
 * @param id
 */
export const delEquRec = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/equRec/' + id,
    method: 'delete'
  });
};
