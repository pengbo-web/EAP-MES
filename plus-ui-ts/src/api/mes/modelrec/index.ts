import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { ModelrecVO, ModelrecForm, ModelrecQuery } from '@/api/mes/modelrec/types';

/**
 * 查询模具使用记录列表
 * @param query
 * @returns {*}
 */

export const listModelrec = (query?: ModelrecQuery): AxiosPromise<ModelrecVO[]> => {
  return request({
    url: '/mes/modelrec/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询模具使用记录详细
 * @param id
 */
export const getModelrec = (id: string | number): AxiosPromise<ModelrecVO> => {
  return request({
    url: '/mes/modelrec/' + id,
    method: 'get'
  });
};

/**
 * 新增模具使用记录
 * @param data
 */
export const addModelrec = (data: ModelrecForm) => {
  return request({
    url: '/mes/modelrec',
    method: 'post',
    data: data
  });
};

/**
 * 修改模具使用记录
 * @param data
 */
export const updateModelrec = (data: ModelrecForm) => {
  return request({
    url: '/mes/modelrec',
    method: 'put',
    data: data
  });
};

/**
 * 删除模具使用记录
 * @param id
 */
export const delModelrec = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/modelrec/' + id,
    method: 'delete'
  });
};
