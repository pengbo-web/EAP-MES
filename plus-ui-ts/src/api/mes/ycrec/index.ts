import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { YcrecVO, YcrecForm, YcrecQuery } from '@/api/mes/ycrec/types';

/**
 * 查询异常报工审核列表
 * @param query
 * @returns {*}
 */

export const listYcrec = (query?: YcrecQuery): AxiosPromise<YcrecVO[]> => {
  return request({
    url: '/mes/ycrec/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询异常报工审核详细
 * @param id
 */
export const getYcrec = (id: string | number): AxiosPromise<YcrecVO> => {
  return request({
    url: '/mes/ycrec/' + id,
    method: 'get'
  });
};

/**
 * 新增异常报工审核
 * @param data
 */
export const addYcrec = (data: YcrecForm) => {
  return request({
    url: '/mes/ycrec',
    method: 'post',
    data: data
  });
};

/**
 * 修改异常报工审核
 * @param data
 */
export const updateYcrec = (data: YcrecForm) => {
  return request({
    url: '/mes/ycrec',
    method: 'put',
    data: data
  });
};

/**
 * 删除异常报工审核
 * @param id
 */
export const delYcrec = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/ycrec/' + id,
    method: 'delete'
  });
};
