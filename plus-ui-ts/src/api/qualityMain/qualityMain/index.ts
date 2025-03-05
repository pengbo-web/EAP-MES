import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { QualityMainVO, QualityMainForm, QualityMainQuery } from '@/api/qualityMain/qualityMain/types';

/**
 * 查询质检主表列表
 * @param query
 * @returns {*}
 */

export const listQualityMain = (query?: QualityMainQuery): AxiosPromise<QualityMainVO[]> => {
  return request({
    url: '/qualityMain/qualityMain/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询质检主表详细
 * @param id
 */
export const getQualityMain = (id: string | number): AxiosPromise<QualityMainVO> => {
  return request({
    url: '/qualityMain/qualityMain/' + id,
    method: 'get'
  });
};

/**
 * 新增质检主表
 * @param data
 */
export const addQualityMain = (data: QualityMainForm) => {
  return request({
    url: '/qualityMain/qualityMain',
    method: 'post',
    data: data
  });
};

/**
 * 修改质检主表
 * @param data
 */
export const updateQualityMain = (data: QualityMainForm) => {
  return request({
    url: '/qualityMain/qualityMain',
    method: 'put',
    data: data
  });
};

/**
 * 删除质检主表
 * @param id
 */
export const delQualityMain = (id: string | number | Array<string | number>) => {
  return request({
    url: '/qualityMain/qualityMain/' + id,
    method: 'delete'
  });
};
