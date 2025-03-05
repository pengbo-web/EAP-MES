import { httpRequest } from '@/utils/http';
import { PageQuery, PageResult } from '@/api/base';
import { OnlineUser } from './model';

enum Api {
  root = '/monitor/online',
  onlineList = '/monitor/online/list',
}

export function onlineList(data?: PageQuery) {
  return httpRequest.get<PageResult<OnlineUser>>({ url: Api.onlineList, data });
}

export function forceLogout(tokenId: string) {
  return httpRequest.delete<void>(
    { url: Api.root + '/' + tokenId },
    { successMessageMode: 'toast' },
  );
}
