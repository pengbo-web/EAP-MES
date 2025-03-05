import { httpRequest } from '@/utils/http';
import { IDS, PageQuery, PageResult } from '@/api/base';
import { OperateLog } from './model';

enum Api {
  root = '/monitor/operlog',
  operLogList = '/monitor/operlog/list',
  operLogExport = '/monitor/operlog/export',
  operLogClean = '/monitor/operlog/clean',
}

export function operLogList(data?: PageQuery & any) {
  return httpRequest.get<PageResult<OperateLog>>({ url: Api.operLogList, data });
}

export function operLogDelete(operIds: IDS) {
  return httpRequest.deleteWithMsg<void>({ url: Api.root + '/' + operIds });
}

export function operLogClean() {
  return httpRequest.deleteWithMsg<void>({ url: Api.operLogClean });
}

export function operLogExport() {
  // 一定要加 responseType: 'blob'
  // 一定要加 responseType: 'blob'
  // 一定要加 responseType: 'blob'
  // 否则拿到的是string
  return httpRequest.post<Blob>(
    { url: Api.operLogExport, responseType: 'blob' },
    { isTransformResponse: false },
  );
}
