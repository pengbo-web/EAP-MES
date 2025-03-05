import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { QualityListVO, QualityListForm, QualityListQuery } from '@/api/mes/qualityList/types';

/**
 * 查询质检内容列表
 * @param query
 * @returns {*}
 */

export const listQualityList = (query?: QualityListQuery): AxiosPromise<QualityListVO[]> => {
  return request({
    url: '/mes/qualityList/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询质检内容详细
 * @param id
 */
export const getQualityList = (id: string | number): AxiosPromise<QualityListVO> => {
  return request({
    url: '/mes/qualityList/' + id,
    method: 'get'
  });
};

/**
 * 新增质检内容
 * @param data
 */
export const addQualityList = (data: QualityListForm) => {
  return request({
    url: '/mes/qualityList',
    method: 'post',
    data: data
  });
};

/**
 * 修改质检内容
 * @param data
 */
export const updateQualityList = (data: QualityListForm) => {
  return request({
    url: '/mes/qualityList',
    method: 'put',
    data: data
  });
};

/**
 * 删除质检内容
 * @param id
 */
export const delQualityList = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/qualityList/' + id,
    method: 'delete'
  });
};
