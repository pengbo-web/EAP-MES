import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { EquCheckVO, EquCheckForm, EquCheckQuery } from '@/api/mes/equCheck/types';

/**
 * 查询设备点检列表
 * @param query
 * @returns {*}
 */

export const listEquCheck = (query?: EquCheckQuery): AxiosPromise<EquCheckVO[]> => {
  return request({
    url: '/mes/equCheck/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询设备点检详细
 * @param id
 */
export const getEquCheck = (id: string | number): AxiosPromise<EquCheckVO> => {
  return request({
    url: '/mes/equCheck/' + id,
    method: 'get'
  });
};

/**
 * 新增设备点检
 * @param data
 */
export const addEquCheck = (data: EquCheckForm) => {
  return request({
    url: '/mes/equCheck',
    method: 'post',
    data: data
  });
};

/**
 * 修改设备点检
 * @param data
 */
export const updateEquCheck = (data: EquCheckForm) => {
  return request({
    url: '/mes/equCheck',
    method: 'put',
    data: data
  });
};

/**
 * 删除设备点检
 * @param id
 */
export const delEquCheck = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/equCheck/' + id,
    method: 'delete'
  });
};
