import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { ProsettingVO, ProsettingForm, ProsettingQuery } from '@/api/mes/prosetting/types';

/**
 * 查询排程设置列表
 * @param query
 * @returns {*}
 */

export const listProsetting = (query?: ProsettingQuery): AxiosPromise<ProsettingVO[]> => {
  return request({
    url: '/mes/prosetting/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询排程设置详细
 * @param id
 */
export const getProsetting = (id: string | number): AxiosPromise<ProsettingVO> => {
  return request({
    url: '/mes/prosetting/' + id,
    method: 'get'
  });
};

/**
 * 新增排程设置
 * @param data
 */
export const addProsetting = (data: ProsettingForm) => {
  return request({
    url: '/mes/prosetting',
    method: 'post',
    data: data
  });
};

/**
 * 修改排程设置
 * @param data
 */
export const updateProsetting = (data: ProsettingForm) => {
  return request({
    url: '/mes/prosetting',
    method: 'put',
    data: data
  });
};

/**
 * 删除排程设置
 * @param id
 */
export const delProsetting = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/prosetting/' + id,
    method: 'delete'
  });
};
