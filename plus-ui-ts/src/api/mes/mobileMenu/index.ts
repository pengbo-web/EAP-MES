import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { MobileMenuVO, MobileMenuForm, MobileMenuQuery } from '@/api/mes/mobileMenu/types';

/**
 * 查询移动端目录列表
 * @param query
 * @returns {*}
 */

export const listMobileMenu = (query?: MobileMenuQuery): AxiosPromise<MobileMenuVO[]> => {
  return request({
    url: '/mes/mobileMenu/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询移动端目录详细
 * @param id
 */
export const getMobileMenu = (id: string | number): AxiosPromise<MobileMenuVO> => {
  return request({
    url: '/mes/mobileMenu/' + id,
    method: 'get'
  });
};

/**
 * 新增移动端目录
 * @param data
 */
export const addMobileMenu = (data: MobileMenuForm) => {
  return request({
    url: '/mes/mobileMenu',
    method: 'post',
    data: data
  });
};

/**
 * 修改移动端目录
 * @param data
 */
export const updateMobileMenu = (data: MobileMenuForm) => {
  return request({
    url: '/mes/mobileMenu',
    method: 'put',
    data: data
  });
};

/**
 * 删除移动端目录
 * @param id
 */
export const delMobileMenu = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/mobileMenu/' + id,
    method: 'delete'
  });
};
