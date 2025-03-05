import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { ToolRecVO, ToolRecForm, ToolRecQuery } from '@/api/mes/toolRec/types';

/**
 * 查询夹具记录列表
 * @param query
 * @returns {*}
 */

export const listToolRec = (query?: ToolRecQuery): AxiosPromise<ToolRecVO[]> => {
  return request({
    url: '/mes/toolRec/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询夹具记录详细
 * @param id
 */
export const getToolRec = (id: string | number): AxiosPromise<ToolRecVO> => {
  return request({
    url: '/mes/toolRec/' + id,
    method: 'get'
  });
};

/**
 * 新增夹具记录
 * @param data
 */
export const addToolRec = (data: ToolRecForm) => {
  return request({
    url: '/mes/toolRec',
    method: 'post',
    data: data
  });
};

/**
 * 修改夹具记录
 * @param data
 */
export const updateToolRec = (data: ToolRecForm) => {
  return request({
    url: '/mes/toolRec',
    method: 'put',
    data: data
  });
};

/**
 * 删除夹具记录
 * @param id
 */
export const delToolRec = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/toolRec/' + id,
    method: 'delete'
  });
};
