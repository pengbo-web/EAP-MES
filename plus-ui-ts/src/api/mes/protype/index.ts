import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { ProtypeVO, ProtypeForm, ProtypeQuery } from '@/api/mes/protype/types';

/**
 * 查询②产品类别列表
 * @param query
 * @returns {*}
 */

export const listProtype = (query?: ProtypeQuery): AxiosPromise<ProtypeVO[]> => {
  return request({
    url: '/mes/protype/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询②产品类别详细
 * @param id
 */
export const getProtype = (id: string | number): AxiosPromise<ProtypeVO> => {
  return request({
    url: '/mes/protype/' + id,
    method: 'get'
  });
};

/**
 * 新增②产品类别
 * @param data
 */
export const addProtype = (data: ProtypeForm) => {
  return request({
    url: '/mes/protype',
    method: 'post',
    data: data
  });
};

/**
 * 修改②产品类别
 * @param data
 */
export const updateProtype = (data: ProtypeForm) => {
  return request({
    url: '/mes/protype',
    method: 'put',
    data: data
  });
};

/**
 * 删除②产品类别
 * @param id
 */
export const delProtype = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/protype/' + id,
    method: 'delete'
  });
};
