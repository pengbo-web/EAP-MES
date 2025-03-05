import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { OutRecVO, OutRecForm, OutRecQuery } from '@/api/mes/outRec/types';

/**
 * 查询外协记录列表
 * @param query
 * @returns {*}
 */

export const listOutRec = (query?: OutRecQuery): AxiosPromise<OutRecVO[]> => {
  return request({
    url: '/mes/outRec/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询外协记录详细
 * @param id
 */
export const getOutRec = (id: string | number): AxiosPromise<OutRecVO> => {
  return request({
    url: '/mes/outRec/' + id,
    method: 'get'
  });
};

/**
 * 新增外协记录
 * @param data
 */
export const addOutRec = (data: OutRecForm) => {
  return request({
    url: '/mes/outRec',
    method: 'post',
    data: data
  });
};

/**
 * 修改外协记录
 * @param data
 */
export const updateOutRec = (data: OutRecForm) => {
  return request({
    url: '/mes/outRec',
    method: 'put',
    data: data
  });
};

/**
 * 删除外协记录
 * @param id
 */
export const delOutRec = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/outRec/' + id,
    method: 'delete'
  });
};
