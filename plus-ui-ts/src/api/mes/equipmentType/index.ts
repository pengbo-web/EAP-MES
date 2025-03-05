import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { EquipmentTypeVO, EquipmentTypeForm, EquipmentTypeQuery } from '@/api/mes/equipmentType/types';

/**
 * 查询⑤设备类别列表
 * @param query
 * @returns {*}
 */

export const listEquipmentType = (query?: EquipmentTypeQuery): AxiosPromise<EquipmentTypeVO[]> => {
  return request({
    url: '/mes/equipmentType/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询⑤设备类别详细
 * @param id
 */
export const getEquipmentType = (id: string | number): AxiosPromise<EquipmentTypeVO> => {
  return request({
    url: '/mes/equipmentType/' + id,
    method: 'get'
  });
};

/**
 * 新增⑤设备类别
 * @param data
 */
export const addEquipmentType = (data: EquipmentTypeForm) => {
  return request({
    url: '/mes/equipmentType',
    method: 'post',
    data: data
  });
};

/**
 * 修改⑤设备类别
 * @param data
 */
export const updateEquipmentType = (data: EquipmentTypeForm) => {
  return request({
    url: '/mes/equipmentType',
    method: 'put',
    data: data
  });
};

/**
 * 删除⑤设备类别
 * @param id
 */
export const delEquipmentType = (id: string | number | Array<string | number>) => {
  return request({
    url: '/mes/equipmentType/' + id,
    method: 'delete'
  });
};
