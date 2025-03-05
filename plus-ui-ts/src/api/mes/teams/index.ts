import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { TeamsVO, TeamsForm, TeamsQuery } from '@/api/mes/teams/types';

/**
 * 查询班组管理列表
 * @param query
 * @returns {*}
 */

export const listTeams = (query?: TeamsQuery): AxiosPromise<TeamsVO[]> => {
  return request({
    url: '/mes/teams/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询班组管理详细
 * @param id
 */
export const getTeams = (id: string | number): AxiosPromise<TeamsVO> => {
  return request({
    url: '/mes/teams/' + id,
    method: 'get'
  });
};

/**
 * 新增班组管理
 * @param data
 */
export const addTeams = (data: TeamsForm) => {
  return request({
    url: '/mes/teams',
    method: 'post',
    data: data
  });
};

/**
 * 修改班组管理
 * @param data
 */
export const updateTeams = (data: TeamsForm) => {
  return request({
    url: '/mes/teams',
    method: 'put',
    data: data
  });
};

/**
 * 删除班组管理
 * @param id
 */
export const delTeams = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/teams/' + id,
    method: 'delete'
  });
};
