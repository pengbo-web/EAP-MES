import { httpRequest } from '@/utils/http';
import { ID, IDS, PageQuery, PageResult } from '@/api/base';
import { Client } from './model';

enum Api {
  root = '/system/client',
  clientList = '/system/client/list',
  clientExport = '/system/client/export',
  clientChangeStatus = '/system/client/changeStatus',
}

export function clientList(data?: PageQuery) {
  return httpRequest.get<PageResult<Client>>({ url: Api.clientList, data });
}

export function clientExport() {
  return httpRequest.post<Blob>(
    { url: Api.clientExport, responseType: 'blob' },
    { isTransformResponse: false },
  );
}

export function clientInfo(id: ID) {
  return httpRequest.get<Client>({ url: Api.root + '/' + id });
}

export function clientAdd(data: any) {
  return httpRequest.postWithMsg<void>({ url: Api.root, data });
}

export function clientUpdate(data: any) {
  return httpRequest.putWithMsg<void>({ url: Api.root, data });
}

export function clientChangeStatus(data: any) {
  return httpRequest.putWithMsg<void>({ url: Api.root, data });
}

export function clientRemove(ids: IDS) {
  return httpRequest.deleteWithMsg({ url: Api.root + '/' + ids });
}
