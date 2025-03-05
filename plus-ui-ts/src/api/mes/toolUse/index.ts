import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { ToolUseVO, ToolUseForm, ToolUseQuery } from '@/api/mes/toolUse/types';

/**
 * 查询夹具使用列表
 * @param query
 * @returns {*}
 */

export const listToolUse = (query?: ToolUseQuery): AxiosPromise<ToolUseVO[]> => {
  return request({
    url: '/mes/toolUse/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询夹具使用详细
 * @param id
 */
export const getToolUse = (id: string | number): AxiosPromise<ToolUseVO> => {
  return request({
    url: '/mes/toolUse/' + id,
    method: 'get'
  });
};

/**
 * 新增夹具使用
 * @param data
 */
export const addToolUse = (data: ToolUseForm) => {
  return request({
    url: '/mes/toolUse',
    method: 'post',
    data: data
  });
};

/**
 * 修改夹具使用
 * @param data
 */
export const updateToolUse = (data: ToolUseForm) => {
  return request({
    url: '/mes/toolUse',
    method: 'put',
    data: data
  });
};

/**
 * 删除夹具使用
 * @param id
 */
export const delToolUse = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/toolUse/' + id,
    method: 'delete'
  });
};
