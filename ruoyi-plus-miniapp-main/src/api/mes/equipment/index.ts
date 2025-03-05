import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { EquipmentVO, EquipmentForm, EquipmentQuery } from '@/api/mes/equipment/types';

/**
 * 查询设备列表列表
 * @param query
 * @returns {*}
 */

export const listEquipment = (query?: EquipmentQuery): AxiosPromise<EquipmentVO[]> => {
  return request({
    url: '/mes/equipment/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询设备列表详细
 * @param id
 */
export const getEquipment = (id: string | number): AxiosPromise<EquipmentVO> => {
  return request({
    url: '/mes/equipment/' + id,
    method: 'get'
  });
};

/**
 * 新增设备列表
 * @param data
 */
export const addEquipment = (data: EquipmentForm) => {
  return request({
    url: '/mes/equipment',
    method: 'post',
    data: data
  });
};

/**
 * 修改设备列表
 * @param data
 */
export const updateEquipment = (data: EquipmentForm) => {
  return request({
    url: '/mes/equipment',
    method: 'put',
    data: data
  });
};

/**
 * 删除设备列表
 * @param id
 */
export const delEquipment = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/equipment/' + id,
    method: 'delete'
  });
};
