import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { EtoolVO, EtoolForm, EtoolQuery } from '@/api/mes/etool/types';

/**
 * 查询工装夹具列表
 * @param query
 * @returns {*}
 */

export const listEtool = (query?: EtoolQuery): AxiosPromise<EtoolVO[]> => {
  return request({
    url: '/mes/etool/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询工装夹具详细
 * @param id
 */
export const getEtool = (id: string | number): AxiosPromise<EtoolVO> => {
  return request({
    url: '/mes/etool/' + id,
    method: 'get'
  });
};

/**
 * 新增工装夹具
 * @param data
 */
export const addEtool = (data: EtoolForm) => {
  return request({
    url: '/mes/etool',
    method: 'post',
    data: data
  });
};

/**
 * 修改工装夹具
 * @param data
 */
export const updateEtool = (data: EtoolForm) => {
  return request({
    url: '/mes/etool',
    method: 'put',
    data: data
  });
};

/**
 * 删除工装夹具
 * @param id
 */
export const delEtool = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/etool/' + id,
    method: 'delete'
  });
};
