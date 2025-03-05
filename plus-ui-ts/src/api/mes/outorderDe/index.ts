import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { OutorderDeVO, OutorderDeForm, OutorderDeQuery } from '@/api/mes/outorderDe/types';

/**
 * 查询委外详情列表
 * @param query
 * @returns {*}
 */

export const listOutorderDe = (query?: OutorderDeQuery): AxiosPromise<OutorderDeVO[]> => {
  return request({
    url: '/mes/outorderDe/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询委外详情详细
 * @param id
 */
export const getOutorderDe = (id: string | number): AxiosPromise<OutorderDeVO> => {
  return request({
    url: '/mes/outorderDe/' + id,
    method: 'get'
  });
};

/**
 * 新增委外详情
 * @param data
 */
export const addOutorderDe = (data: OutorderDeForm) => {
  return request({
    url: '/mes/outorderDe',
    method: 'post',
    data: data
  });
};

/**
 * 修改委外详情
 * @param data
 */
export const updateOutorderDe = (data: OutorderDeForm) => {
  return request({
    url: '/mes/outorderDe',
    method: 'put',
    data: data
  });
};

/**
 * 删除委外详情
 * @param id
 */
export const delOutorderDe = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/outorderDe/' + id,
    method: 'delete'
  });
};
