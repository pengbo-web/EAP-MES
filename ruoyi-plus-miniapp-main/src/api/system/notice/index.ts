import { httpRequest } from '@/utils/http';
import { ID, IDS, PageQuery, PageResult } from '@/api/base';
import { Notice } from './model';

enum Api {
  root = '/system/notice',
  noticeList = '/system/notice/list',
}

export function noticeList(data?: PageQuery) {
  return httpRequest.get<PageResult<Notice>>({ url: Api.noticeList, data });
}

export function noticeInfo(noticeId: ID) {
  return httpRequest.get<Notice>({ url: Api.root + '/' + noticeId });
}

export function noticeAdd(data: any) {
  return httpRequest.postWithMsg<void>({ url: Api.root, data });
}

export function noticeUpdate(data: any) {
  return httpRequest.putWithMsg<void>({ url: Api.root, data });
}

export function noticeRemove(noticeIds: IDS) {
  return httpRequest.deleteWithMsg<void>({ url: Api.root + '/' + noticeIds });
}
