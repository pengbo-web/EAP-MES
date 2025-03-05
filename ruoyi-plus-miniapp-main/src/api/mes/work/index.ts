import { httpRequest } from "@/utils/http";
import { ID, PageQuery, PageResult } from "@/api/base";
import { WorkVO, WorkForm, WorkQuery } from '@/api/mes/work/types';

/**
 * 查询生产派单列表
 * @param query
 * @returns {*}
 */

export function listWork (data: WorkQuery) {
  return httpRequest.get<PageResult<WorkVO>>({
    url: '/mes/work/list',data,
  });
};
/**
 * 查询生产派单详细
 * @param id
 */
export function getWork (item:any) {
  return httpRequest.get<any>({
    url: '/mes/work/' + item
  });
};

/**
 * 新增生产派单
 * @param data
 */
export const addWork = (data: WorkForm) => {
  return request({
    url: '/mes/work',
    method: 'post',
    data: data
  });
};

/**
 * 修改生产派单
 * @param data
 */
export const updateWork = (data: WorkForm) => {
  return request({
    url: '/mes/work',
    method: 'put',
    data: data
  });
};

/**
 * 删除生产派单
 * @param id
 */
export const delWork = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/work/' + id,
    method: 'delete'
  });
};
