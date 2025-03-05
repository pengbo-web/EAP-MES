import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { AllrecVO, AllrecForm, AllrecQuery } from '@/api/mes/allrec/types';

/**
 * 查询出入库记录列表
 * @param query
 * @returns {*}
 */

export const listAllrec = (query?: AllrecQuery): AxiosPromise<AllrecVO[]> => {
  return request({
    url: '/mes/allrec/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询出入库记录详细
 * @param id
 */
export const getAllrec = (id: string | number): AxiosPromise<AllrecVO> => {
  return request({
    url: '/mes/allrec/' + id,
    method: 'get'
  });
};

/**
 * 新增出入库记录
 * @param data
 */
export const addAllrec = (data: AllrecForm) => {
  return request({
    url: '/mes/allrec',
    method: 'post',
    data: data
  });
};

/**
 * 修改出入库记录
 * @param data
 */
export const updateAllrec = (data: AllrecForm) => {
  return request({
    url: '/mes/allrec',
    method: 'put',
    data: data
  });
};

/**
 * 删除出入库记录
 * @param id
 */
export const delAllrec = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/allrec/' + id,
    method: 'delete'
  });
};
