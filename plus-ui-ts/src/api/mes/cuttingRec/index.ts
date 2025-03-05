import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { CuttingRecVO, CuttingRecForm, CuttingRecQuery } from '@/api/mes/cuttingRec/types';

/**
 * 查询刀具记录审核列表
 * @param query
 * @returns {*}
 */

export const listCuttingRec = (query?: CuttingRecQuery): AxiosPromise<CuttingRecVO[]> => {
  return request({
    url: '/mes/cuttingRec/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询刀具记录审核详细
 * @param id
 */
export const getCuttingRec = (id: string | number): AxiosPromise<CuttingRecVO> => {
  return request({
    url: '/mes/cuttingRec/' + id,
    method: 'get'
  });
};

/**
 * 新增刀具记录审核
 * @param data
 */
export const addCuttingRec = (data: CuttingRecForm) => {
  return request({
    url: '/mes/cuttingRec',
    method: 'post',
    data: data
  });
};

/**
 * 修改刀具记录审核
 * @param data
 */
export const updateCuttingRec = (data: CuttingRecForm) => {
  return request({
    url: '/mes/cuttingRec',
    method: 'put',
    data: data
  });
};

/**
 * 删除刀具记录审核
 * @param id
 */
export const delCuttingRec = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/cuttingRec/' + id,
    method: 'delete'
  });
};
