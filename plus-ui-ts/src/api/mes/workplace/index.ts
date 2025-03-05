import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { WorkplaceVO, WorkplaceForm, WorkplaceQuery } from '@/api/mes/workplace/types';

/**
 * 查询④车间设置列表
 * @param query
 * @returns {*}
 */

export const listWorkplace = (query?: WorkplaceQuery): AxiosPromise<WorkplaceVO[]> => {
  return request({
    url: '/mes/workplace/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询④车间设置详细
 * @param id
 */
export const getWorkplace = (id: string | number): AxiosPromise<WorkplaceVO> => {
  return request({
    url: '/mes/workplace/' + id,
    method: 'get'
  });
};

/**
 * 新增④车间设置
 * @param data
 */
export const addWorkplace = (data: WorkplaceForm) => {
  return request({
    url: '/mes/workplace',
    method: 'post',
    data: data
  });
};

/**
 * 修改④车间设置
 * @param data
 */
export const updateWorkplace = (data: WorkplaceForm) => {
  return request({
    url: '/mes/workplace',
    method: 'put',
    data: data
  });
};

/**
 * 删除④车间设置
 * @param id
 */
export const delWorkplace = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/workplace/' + id,
    method: 'delete'
  });
};
