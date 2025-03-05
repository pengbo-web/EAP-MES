import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { OutsourcingVO, OutsourcingForm, OutsourcingQuery } from '@/api/mes/outsourcing/types';

/**
 * 查询外协列列表
 * @param query
 * @returns {*}
 */

export const listOutsourcing = (query?: OutsourcingQuery): AxiosPromise<OutsourcingVO[]> => {
  return request({
    url: '/mes/outsourcing/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询外协列详细
 * @param id
 */
export const getOutsourcing = (id: string | number): AxiosPromise<OutsourcingVO> => {
  return request({
    url: '/mes/outsourcing/' + id,
    method: 'get'
  });
};

/**
 * 新增外协列
 * @param data
 */
export const addOutsourcing = (data: OutsourcingForm) => {
  return request({
    url: '/mes/outsourcing',
    method: 'post',
    data: data
  });
};

/**
 * 修改外协列
 * @param data
 */
export const updateOutsourcing = (data: OutsourcingForm) => {
  return request({
    url: '/mes/outsourcing',
    method: 'put',
    data: data
  });
};

/**
 * 删除外协列
 * @param id
 */
export const delOutsourcing = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/outsourcing/' + id,
    method: 'delete'
  });
};
