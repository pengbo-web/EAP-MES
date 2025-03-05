import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { QuaformVO, QuaformForm, QuaformQuery } from '@/api/mes/quaform/types';

/**
 * 查询来料检列表
 * @param query
 * @returns {*}
 */

export const listQuaform = (query?: QuaformQuery): AxiosPromise<QuaformVO[]> => {
  return request({
    url: '/mes/quaform/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询来料检详细
 * @param id
 */
export const getQuaform = (id: string | number): AxiosPromise<QuaformVO> => {
  return request({
    url: '/mes/quaform/' + id,
    method: 'get'
  });
};

/**
 * 新增来料检
 * @param data
 */
export const addQuaform = (data: QuaformForm) => {
  return request({
    url: '/mes/quaform',
    method: 'post',
    data: data
  });
};

/**
 * 修改来料检
 * @param data
 */
export const updateQuaform = (data: QuaformForm) => {
  return request({
    url: '/mes/quaform',
    method: 'put',
    data: data
  });
};

/**
 * 删除来料检
 * @param id
 */
export const delQuaform = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/quaform/' + id,
    method: 'delete'
  });
};
