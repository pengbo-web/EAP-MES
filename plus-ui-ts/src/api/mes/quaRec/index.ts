import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { QuaRecVO, QuaRecForm, QuaRecQuery } from '@/api/mes/quaRec/types';

/**
 * 查询质检列表列表
 * @param query
 * @returns {*}
 */

export const listQuaRec = (query?: QuaRecQuery): AxiosPromise<QuaRecVO[]> => {
  return request({
    url: '/mes/quaRec/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询质检列表详细
 * @param id
 */
export const getQuaRec = (id: string | number): AxiosPromise<QuaRecVO> => {
  return request({
    url: '/mes/quaRec/' + id,
    method: 'get'
  });
};

/**
 * 新增质检列表
 * @param data
 */
export const addQuaRec = (data: QuaRecForm) => {
  return request({
    url: '/mes/quaRec',
    method: 'post',
    data: data
  });
};

/**
 * 修改质检列表
 * @param data
 */
export const updateQuaRec = (data: QuaRecForm) => {
  return request({
    url: '/mes/quaRec',
    method: 'put',
    data: data
  });
};
export const getindexlist = () => {
  return request({
    url: '/mes/index/list',
    method: 'get'
  });
};

/**
 * 删除质检列表
 * @param id
 */
export const delQuaRec = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/quaRec/' + id,
    method: 'delete'
  });
};
