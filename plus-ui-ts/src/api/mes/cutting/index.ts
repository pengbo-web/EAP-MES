import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { CuttingVO, CuttingForm, CuttingQuery } from '@/api/mes/cutting/types';

/**
 * 查询刀具管理列表
 * @param query
 * @returns {*}
 */

export const listCutting = (query?: CuttingQuery): AxiosPromise<CuttingVO[]> => {
  return request({
    url: '/mes/cutting/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询刀具管理详细
 * @param id
 */
export const getCutting = (id: string | number): AxiosPromise<CuttingVO> => {
  return request({
    url: '/mes/cutting/' + id,
    method: 'get'
  });
};

/**
 * 新增刀具管理
 * @param data
 */
export const addCutting = (data: CuttingForm) => {
  return request({
    url: '/mes/cutting',
    method: 'post',
    data: data
  });
};

/**
 * 修改刀具管理
 * @param data
 */
export const updateCutting = (data: CuttingForm) => {
  return request({
    url: '/mes/cutting',
    method: 'put',
    data: data
  });
};

/**
 * 删除刀具管理
 * @param id
 */
export const delCutting = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/cutting/' + id,
    method: 'delete'
  });
};
