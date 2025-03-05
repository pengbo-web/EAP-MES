import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { WorkVO, WorkForm, WorkQuery } from '@/api/mes/work/types';

/**
 * 查询生产派单列表
 * @param query
 * @returns {*}
 */

export const listWork = (query?: WorkQuery): AxiosPromise<WorkVO[]> => {
  return request({
    url: '/mes/work/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询生产派单详细
 * @param id
 */
export const getWork = (id: string | number): AxiosPromise<WorkVO> => {
  return request({
    url: '/mes/work/' + id,
    method: 'get'
  });
};

/**
 * 新增生产派单
 * @param data
 */
export const addWork = (data: WorkForm) => {
  return request({
    url: '/mes/work',
    method: 'post',
    data: data
  });
};

/**
 * 修改生产派单
 * @param data
 */
export const updateWork = (data: WorkForm) => {
  return request({
    url: '/mes/work',
    method: 'put',
    data: data
  });
};

/**
 * 删除生产派单
 * @param id
 */
export const delWork = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/work/' + id,
    method: 'delete'
  });
};
