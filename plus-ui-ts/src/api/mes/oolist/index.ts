import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { OolistVO, OolistForm, OolistQuery } from '@/api/mes/oolist/types';

/**
 * 查询委外入库详情列表
 * @param query
 * @returns {*}
 */

export const listOolist = (query?: OolistQuery): AxiosPromise<OolistVO[]> => {
  return request({
    url: '/mes/oolist/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询委外入库详情详细
 * @param id
 */
export const getOolist = (id: string | number): AxiosPromise<OolistVO> => {
  return request({
    url: '/mes/oolist/' + id,
    method: 'get'
  });
};

/**
 * 新增委外入库详情
 * @param data
 */
export const addOolist = (data: OolistForm) => {
  return request({
    url: '/mes/oolist',
    method: 'post',
    data: data
  });
};

/**
 * 修改委外入库详情
 * @param data
 */
export const updateOolist = (data: OolistForm) => {
  return request({
    url: '/mes/oolist',
    method: 'put',
    data: data
  });
};

/**
 * 删除委外入库详情
 * @param id
 */
export const delOolist = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/oolist/' + id,
    method: 'delete'
  });
};
