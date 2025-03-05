import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { TaskcVO, TaskcForm, TaskcQuery } from '@/api/mes/taskc/types';

/**
 * 查询组装流转列表
 * @param query
 * @returns {*}
 */

export const listTaskc = (query?: TaskcQuery): AxiosPromise<TaskcVO[]> => {
  return request({
    url: '/mes/taskc/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询组装流转详细
 * @param id
 */
export const getTaskc = (id: string | number): AxiosPromise<TaskcVO> => {
  return request({
    url: '/mes/taskc/' + id,
    method: 'get'
  });
};

/**
 * 新增组装流转
 * @param data
 */
export const addTaskc = (data: TaskcForm) => {
  return request({
    url: '/mes/taskc',
    method: 'post',
    data: data
  });
};

/**
 * 修改组装流转
 * @param data
 */
export const updateTaskc = (data: TaskcForm) => {
  return request({
    url: '/mes/taskc',
    method: 'put',
    data: data
  });
};

/**
 * 删除组装流转
 * @param id
 */
export const delTaskc = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/taskc/' + id,
    method: 'delete'
  });
};
