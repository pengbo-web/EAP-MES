import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { ProcessVO, ProcessForm, ProcessQuery } from '@/api/mes/process/types';

/**
 * 查询⑧工艺管理列表
 * @param query
 * @returns {*}
 */

export const listProcess = (query?: ProcessQuery): AxiosPromise<ProcessVO[]> => {
  return request({
    url: '/mes/process/list',
    method: 'get',
    params: query
  });
};
/**
 * 查询⑧工艺管理列表
 * @param query
 * @returns {*}
 */

export const listByProcess = (query?: ProcessQuery): AxiosPromise<ProcessVO[]> => {
  return request({
    url: '/mes/process/listby',
    method: 'get',
    params: query
  });
};
/**
 * 根据工作站获取所有工序
 * @param query
 * @returns {*}
 */

export const listBystring = (datas: number[]) => {
  return request({
    url: '/mes/process/getstrings',
    method: 'post',
    data: datas
  });
};
/**
 * 查询⑧工艺管理详细
 * @param id
 */
export const getProcess = (id: string | number): AxiosPromise<ProcessVO> => {
  return request({
    url: '/mes/process/' + id,
    method: 'get'
  });
};

/**
 * 新增⑧工艺管理
 * @param data
 */
export const addProcess = (data: ProcessForm) => {
  return request({
    url: '/mes/process',
    method: 'post',
    data: data
  });
};

/**
 * 修改⑧工艺管理
 * @param data
 */
export const updateProcess = (data: ProcessForm) => {
  return request({
    url: '/mes/process',
    method: 'put',
    data: data
  });
};

/**
 * 删除⑧工艺管理
 * @param id
 */
export const delProcess = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/process/' + id,
    method: 'delete'
  });
};
