import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { ProcessTypeVO, ProcessTypeForm, ProcessTypeQuery } from '@/api/mes/processType/types';

/**
 * 查询⑦工艺类别列表
 * @param query
 * @returns {*}
 */

export const listProcessType = (query?: ProcessTypeQuery): AxiosPromise<ProcessTypeVO[]> => {
  return request({
    url: '/mes/processType/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询⑦工艺类别详细
 * @param id
 */
export const getProcessType = (id: string | number): AxiosPromise<ProcessTypeVO> => {
  return request({
    url: '/mes/processType/' + id,
    method: 'get'
  });
};

/**
 * 新增⑦工艺类别
 * @param data
 */
export const addProcessType = (data: ProcessTypeForm) => {
  return request({
    url: '/mes/processType',
    method: 'post',
    data: data
  });
};

/**
 * 修改⑦工艺类别
 * @param data
 */
export const updateProcessType = (data: ProcessTypeForm) => {
  return request({
    url: '/mes/processType',
    method: 'put',
    data: data
  });
};

/**
 * 删除⑦工艺类别
 * @param id
 */
export const delProcessType = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/processType/' + id,
    method: 'delete'
  });
};
