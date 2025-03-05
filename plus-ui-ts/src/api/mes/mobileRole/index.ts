import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { MobileRoleVO, MobileRoleForm, MobileRoleQuery } from '@/api/mes/mobileRole/types';

/**
 * 查询移动端权限列表
 * @param query
 * @returns {*}
 */

export const listMobileRole = (query?: MobileRoleQuery): AxiosPromise<MobileRoleVO[]> => {
  return request({
    url: '/mes/mobileRole/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询移动端权限详细
 * @param id
 */
export const getMobileRole = (id: string | number): AxiosPromise<MobileRoleVO> => {
  return request({
    url: '/mes/mobileRole/' + id,
    method: 'get'
  });
};

/**
 * 新增移动端权限
 * @param data
 */
export const addMobileRole = (data: MobileRoleForm) => {
  return request({
    url: '/mes/mobileRole',
    method: 'post',
    data: data
  });
};

/**
 * 修改移动端权限
 * @param data
 */
export const updateMobileRole = (data: MobileRoleForm) => {
  return request({
    url: '/mes/mobileRole',
    method: 'put',
    data: data
  });
};

/**
 * 删除移动端权限
 * @param id
 */
export const delMobileRole = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/mobileRole/' + id,
    method: 'delete'
  });
};
