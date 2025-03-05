import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { StinfoVO, StinfoForm, StinfoQuery } from '@/api/mes/stinfo/types';

/**
 * 查询库位调拨列表
 * @param query
 * @returns {*}
 */

export const listStinfo = (query?: StinfoQuery): AxiosPromise<StinfoVO[]> => {
  return request({
    url: '/mes/stinfo/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询库位调拨详细
 * @param id
 */
export const getStinfo = (id: string | number): AxiosPromise<StinfoVO> => {
  return request({
    url: '/mes/stinfo/' + id,
    method: 'get'
  });
};

/**
 * 新增库位调拨
 * @param data
 */
export const addStinfo = (data: StinfoForm) => {
  return request({
    url: '/mes/stinfo',
    method: 'post',
    data: data
  });
};

/**
 * 修改库位调拨
 * @param data
 */
export const updateStinfo = (data: StinfoForm) => {
  return request({
    url: '/mes/stinfo',
    method: 'put',
    data: data
  });
};

/**
 * 删除库位调拨
 * @param id
 */
export const delStinfo = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/stinfo/' + id,
    method: 'delete'
  });
};
