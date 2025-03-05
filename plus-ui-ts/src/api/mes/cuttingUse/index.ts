import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { CuttingUseVO, CuttingUseForm, CuttingUseQuery } from '@/api/mes/cuttingUse/types';

/**
 * 查询刀具使用统计列表
 * @param query
 * @returns {*}
 */

export const listCuttingUse = (query?: CuttingUseQuery): AxiosPromise<CuttingUseVO[]> => {
  return request({
    url: '/mes/cuttingUse/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询刀具使用统计详细
 * @param id
 */
export const getCuttingUse = (id: string | number): AxiosPromise<CuttingUseVO> => {
  return request({
    url: '/mes/cuttingUse/' + id,
    method: 'get'
  });
};

/**
 * 新增刀具使用统计
 * @param data
 */
export const addCuttingUse = (data: CuttingUseForm) => {
  return request({
    url: '/mes/cuttingUse',
    method: 'post',
    data: data
  });
};

/**
 * 修改刀具使用统计
 * @param data
 */
export const updateCuttingUse = (data: CuttingUseForm) => {
  return request({
    url: '/mes/cuttingUse',
    method: 'put',
    data: data
  });
};

/**
 * 删除刀具使用统计
 * @param id
 */
export const delCuttingUse = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/cuttingUse/' + id,
    method: 'delete'
  });
};
