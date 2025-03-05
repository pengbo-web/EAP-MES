import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { TaskVO, TaskForm, TaskQuery } from '@/api/task/task/types';

/**
 * 查询任务流转列表
 * @param query
 * @returns {*}
 */

export const listTask = (query?: TaskQuery): AxiosPromise<TaskVO[]> => {
  return request({
    url: '/task/task/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询任务流转详细
 * @param id
 */
export const getTask = (id: string | number): AxiosPromise<TaskVO> => {
  return request({
    url: '/task/task/' + id,
    method: 'get'
  });
};

/**
 * 新增任务流转
 * @param data
 */
export const addTask = (data: TaskForm) => {
  return request({
    url: '/task/task',
    method: 'post',
    data: data
  });
};

/**
 * 修改任务流转
 * @param data
 */
export const updateTask = (data: TaskForm) => {
  return request({
    url: '/task/task',
    method: 'put',
    data: data
  });
};

/**
 * 删除任务流转
 * @param id
 */
export const delTask = (id: string | number | Array<string | number>) => {
  return request({
    url: '/task/task/' + id,
    method: 'delete'
  });
};
