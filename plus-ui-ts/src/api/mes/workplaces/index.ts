import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { WorkplacesVO, WorkplacesForm, WorkplacesQuery } from '@/api/mes/workplaces/types';

/**
 * 查询工作站列表
 * @param query
 * @returns {*}
 */

export const listWorkplaces = (query?: WorkplacesQuery): AxiosPromise<WorkplacesVO[]> => {
  return request({
    url: '/mes/workplaces/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询工作站详细
 * @param id
 */
export const getWorkplaces = (id: string | number): AxiosPromise<WorkplacesVO> => {
  return request({
    url: '/mes/workplaces/' + id,
    method: 'get'
  });
};

/**
 * 新增工作站
 * @param data
 */
export const addWorkplaces = (data: WorkplacesForm) => {
  return request({
    url: '/mes/workplaces',
    method: 'post',
    data: data
  });
};

/**
 * 修改工作站
 * @param data
 */
export const updateWorkplaces = (data: WorkplacesForm) => {
  return request({
    url: '/mes/workplaces',
    method: 'put',
    data: data
  });
};

/**
 * 删除工作站
 * @param id
 */
export const delWorkplaces = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/workplaces/' + id,
    method: 'delete'
  });
};
