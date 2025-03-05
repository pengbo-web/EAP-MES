import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { ProcurementplanVO, ProcurementplanForm, ProcurementplanQuery } from '@/api/mes/procurementplan/types';

/**
 * 查询采购计划列表
 * @param query
 * @returns {*}
 */

export const listProcurementplan = (query?: ProcurementplanQuery): AxiosPromise<ProcurementplanVO[]> => {
  return request({
    url: '/mes/procurementplan/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询采购计划详细
 * @param id
 */
export const getProcurementplan = (id: string | number): AxiosPromise<ProcurementplanVO> => {
  return request({
    url: '/mes/procurementplan/' + id,
    method: 'get'
  });
};

/**
 * 新增采购计划
 * @param data
 */
export const addProcurementplan = (data: ProcurementplanForm) => {
  return request({
    url: '/mes/procurementplan',
    method: 'post',
    data: data
  });
};

/**
 * 修改采购计划
 * @param data
 */
export const updateProcurementplan = (data: ProcurementplanForm) => {
  return request({
    url: '/mes/procurementplan',
    method: 'put',
    data: data
  });
};

/**
 * 删除采购计划
 * @param id
 */
export const delProcurementplan = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/procurementplan/' + id,
    method: 'delete'
  });
};
