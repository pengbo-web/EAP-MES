import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { TmodelVO, TmodelForm, TmodelQuery } from '@/api/mes/tmodel/types';

/**
 * 查询模具管理列表
 * @param query
 * @returns {*}
 */

export const listTmodel = (query?: TmodelQuery): AxiosPromise<TmodelVO[]> => {
  return request({
    url: '/mes/tmodel/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询模具管理详细
 * @param id
 */
export const getTmodel = (id: string | number): AxiosPromise<TmodelVO> => {
  return request({
    url: '/mes/tmodel/' + id,
    method: 'get'
  });
};

/**
 * 新增模具管理
 * @param data
 */
export const addTmodel = (data: TmodelForm) => {
  return request({
    url: '/mes/tmodel',
    method: 'post',
    data: data
  });
};

/**
 * 修改模具管理
 * @param data
 */
export const updateTmodel = (data: TmodelForm) => {
  return request({
    url: '/mes/tmodel',
    method: 'put',
    data: data
  });
};

/**
 * 删除模具管理
 * @param id
 */
export const delTmodel = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/tmodel/' + id,
    method: 'delete'
  });
};
