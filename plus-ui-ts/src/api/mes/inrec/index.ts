import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { InrecVO, InrecForm, InrecQuery } from '@/api/mes/inrec/types';

/**
 * 查询入库记录列表
 * @param query
 * @returns {*}
 */

export const listInrec = (query?: InrecQuery): AxiosPromise<InrecVO[]> => {
  return request({
    url: '/mes/inrec/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询入库记录详细
 * @param id
 */
export const getInrec = (id: string | number): AxiosPromise<InrecVO> => {
  return request({
    url: '/mes/inrec/' + id,
    method: 'get'
  });
};

/**
 * 新增入库记录
 * @param data
 */
export const addInrec = (data: InrecForm) => {
  return request({
    url: '/mes/inrec',
    method: 'post',
    data: data
  });
};

/**
 * 修改入库记录
 * @param data
 */
export const updateInrec = (data: InrecForm) => {
  return request({
    url: '/mes/inrec',
    method: 'put',
    data: data
  });
};

/**
 * 删除入库记录
 * @param id
 */
export const delInrec = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/inrec/' + id,
    method: 'delete'
  });
};
