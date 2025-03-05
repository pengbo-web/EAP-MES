import { httpRequest } from '@/utils/http';
import { IDS, PageQuery, PageResult } from '@/api/base';
import { LoginLog } from './model';

enum Api {
  root = '/monitor/logininfor',
  loginInfoList = '/monitor/logininfor/list',
  loginInfoExport = '/monitor/logininfor/export',
  userUnlock = '/monitor/logininfor/unlock',
  loginInfoClean = '/monitor/logininfor/clean',
}

export function loginInfoList(data?: PageQuery) {
  return httpRequest.get<PageResult<LoginLog>>(
    { url: Api.loginInfoList, data },
    { showLoading: false },
  );
}

export function loginInfoExport() {
  return httpRequest.post<Blob>(
    { url: Api.loginInfoExport, responseType: 'blob' },
    { isTransformResponse: false },
  );
}

export function loginInfoRemove(infoIds: IDS) {
  return httpRequest.deleteWithMsg<void>({ url: Api.root + '/' + infoIds });
}

export function userUnlock(username: string) {
  return httpRequest.get<void>(
    { url: Api.userUnlock + '/' + username },
    { successMessageMode: 'toast' },
  );
}

export function loginInfoClean() {
  return httpRequest.deleteWithMsg<void>({ url: Api.loginInfoClean });
}
