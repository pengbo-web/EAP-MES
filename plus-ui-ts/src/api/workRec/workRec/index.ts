import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { WorkRecVO, WorkRecForm, WorkRecQuery } from '@/api/workRec/workRec/types';

/**
 * 查询工作记录列表
 * @param query
 * @returns {*}
 */

export const listWorkRec = (query?: WorkRecQuery): AxiosPromise<WorkRecVO[]> => {
  return request({
    url: '/workRec/workRec/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询工作记录详细
 * @param id
 */
export const getWorkRec = (id: string | number): AxiosPromise<WorkRecVO> => {
  return request({
    url: '/workRec/workRec/' + id,
    method: 'get'
  });
};

/**
 * 新增工作记录
 * @param data
 */
export const addWorkRec = (data: WorkRecForm) => {
  return request({
    url: '/workRec/workRec',
    method: 'post',
    data: data
  });
};

/**
 * 修改工作记录
 * @param data
 */
export const updateWorkRec = (data: WorkRecForm) => {
  return request({
    url: '/workRec/workRec',
    method: 'put',
    data: data
  });
};

/**
 * 删除工作记录
 * @param id
 */
export const delWorkRec = (id: string | number | Array<string | number>) => {
  return request({
    url: '/workRec/workRec/' + id,
    method: 'delete'
  });
};
