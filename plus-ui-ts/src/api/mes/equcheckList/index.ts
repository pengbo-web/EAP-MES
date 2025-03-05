import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { EqucheckListVO, EqucheckListForm, EqucheckListQuery } from '@/api/mes/equcheckList/types';

/**
 * 查询设备点检列列表
 * @param query
 * @returns {*}
 */

export const listEqucheckList = (query?: EqucheckListQuery): AxiosPromise<EqucheckListVO[]> => {
  return request({
    url: '/mes/equcheckList/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询设备点检列详细
 * @param id
 */
export const getEqucheckList = (id: string | number): AxiosPromise<EqucheckListVO> => {
  return request({
    url: '/mes/equcheckList/' + id,
    method: 'get'
  });
};

/**
 * 新增设备点检列
 * @param data
 */
export const addEqucheckList = (data: EqucheckListForm) => {
  return request({
    url: '/mes/equcheckList',
    method: 'post',
    data: data
  });
};

/**
 * 修改设备点检列
 * @param data
 */
export const updateEqucheckList = (data: EqucheckListForm) => {
  return request({
    url: '/mes/equcheckList',
    method: 'put',
    data: data
  });
};

/**
 * 删除设备点检列
 * @param id
 */
export const delEqucheckList = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/equcheckList/' + id,
    method: 'delete'
  });
};
