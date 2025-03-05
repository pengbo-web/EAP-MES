import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { WuliuVO, WuliuForm, WuliuQuery } from '@/api/mes/wuliu/types';

/**
 * 查询物流信息列表
 * @param query
 * @returns {*}
 */

export const listWuliu = (query?: WuliuQuery): AxiosPromise<WuliuVO[]> => {
  return request({
    url: '/mes/wuliu/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询物流信息详细
 * @param id
 */
export const getWuliu = (id: string | number): AxiosPromise<WuliuVO> => {
  return request({
    url: '/mes/wuliu/' + id,
    method: 'get'
  });
};

/**
 * 新增物流信息
 * @param data
 */
export const addWuliu = (data: WuliuForm) => {
  return request({
    url: '/mes/wuliu',
    method: 'post',
    data: data
  });
};

/**
 * 修改物流信息
 * @param data
 */
export const updateWuliu = (data: WuliuForm) => {
  return request({
    url: '/mes/wuliu',
    method: 'put',
    data: data
  });
};

/**
 * 删除物流信息
 * @param id
 */
export const delWuliu = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/wuliu/' + id,
    method: 'delete'
  });
};
