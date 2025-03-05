import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { UnitmeasureVO, UnitmeasureForm, UnitmeasureQuery } from '@/api/mes/unitmeasure/types';

/**
 * 查询①计量单位列表
 * @param query
 * @returns {*}
 */

export const listUnitmeasure = (query?: UnitmeasureQuery): AxiosPromise<UnitmeasureVO[]> => {
  return request({
    url: '/mes/unitmeasure/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询①计量单位详细
 * @param id
 */
export const getUnitmeasure = (id: string | number): AxiosPromise<UnitmeasureVO> => {
  return request({
    url: '/mes/unitmeasure/' + id,
    method: 'get'
  });
};

/**
 * 新增①计量单位
 * @param data
 */
export const addUnitmeasure = (data: UnitmeasureForm) => {
  return request({
    url: '/mes/unitmeasure',
    method: 'post',
    data: data
  });
};

/**
 * 修改①计量单位
 * @param data
 */
export const updateUnitmeasure = (data: UnitmeasureForm) => {
  return request({
    url: '/mes/unitmeasure',
    method: 'put',
    data: data
  });
};

/**
 * 删除①计量单位
 * @param id
 */
export const delUnitmeasure = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/unitmeasure/' + id,
    method: 'delete'
  });
};
