import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { OurecVO, OurecForm, OurecQuery } from '@/api/mes/ourec/types';

/**
 * 查询出库记录列表
 * @param query
 * @returns {*}
 */

export const listOurec = (query?: OurecQuery): AxiosPromise<OurecVO[]> => {
  return request({
    url: '/mes/ourec/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询出库记录详细
 * @param id
 */
export const getOurec = (id: string | number): AxiosPromise<OurecVO> => {
  return request({
    url: '/mes/ourec/' + id,
    method: 'get'
  });
};

/**
 * 新增出库记录
 * @param data
 */
export const addOurec = (data: OurecForm) => {
  return request({
    url: '/mes/ourec',
    method: 'post',
    data: data
  });
};

/**
 * 修改出库记录
 * @param data
 */
export const updateOurec = (data: OurecForm) => {
  return request({
    url: '/mes/ourec',
    method: 'put',
    data: data
  });
};

/**
 * 删除出库记录
 * @param id
 */
export const delOurec = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/ourec/' + id,
    method: 'delete'
  });
};
