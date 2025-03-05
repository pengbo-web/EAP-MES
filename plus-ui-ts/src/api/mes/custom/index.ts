import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { CustomVO, CustomForm, CustomQuery } from '@/api/mes/custom/types';

/**
 * 查询客户供应商列表
 * @param query
 * @returns {*}
 */

export const listCustom = (query?: CustomQuery): AxiosPromise<CustomVO[]> => {
  return request({
    url: '/mes/custom/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询客户供应商详细
 * @param id
 */
export const getCustom = (id: string | number): AxiosPromise<CustomVO> => {
  return request({
    url: '/mes/custom/' + id,
    method: 'get'
  });
};

/**
 * 新增客户供应商
 * @param data
 */
export const addCustom = (data: CustomForm) => {
  return request({
    url: '/mes/custom',
    method: 'post',
    data: data
  });
};
export const addCustom2 = (data: CustomForm) => {
  return request({
    url: '/mes/custom/vo',
    method: 'post',
    data: data
  });
};

/**
 * 修改客户供应商
 * @param data
 */
export const updateCustom = (data: CustomForm) => {
  return request({
    url: '/mes/custom',
    method: 'put',
    data: data
  });
};

/**
 * 删除客户供应商
 * @param id
 */
export const delCustom = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/custom/' + id,
    method: 'delete'
  });
};
