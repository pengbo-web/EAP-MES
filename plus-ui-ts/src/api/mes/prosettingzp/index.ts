import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { ProsettingzpVO, ProsettingzpForm, ProsettingzpQuery } from '@/api/mes/prosettingzp/types';

/**
 * 查询组装排程列表
 * @param query
 * @returns {*}
 */

export const listProsettingzp = (query?: ProsettingzpQuery): AxiosPromise<ProsettingzpVO[]> => {
  return request({
    url: '/mes/prosettingzp/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询组装排程详细
 * @param id
 */
export const getProsettingzp = (id: string | number): AxiosPromise<ProsettingzpVO> => {
  return request({
    url: '/mes/prosettingzp/' + id,
    method: 'get'
  });
};

/**
 * 新增组装排程
 * @param data
 */
export const addProsettingzp = (data: ProsettingzpForm) => {
  return request({
    url: '/mes/prosettingzp',
    method: 'post',
    data: data
  });
};

/**
 * 修改组装排程
 * @param data
 */
export const updateProsettingzp = (data: ProsettingzpForm) => {
  return request({
    url: '/mes/prosettingzp',
    method: 'put',
    data: data
  });
};

/**
 * 删除组装排程
 * @param id
 */
export const delProsettingzp = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/prosettingzp/' + id,
    method: 'delete'
  });
};
