import { httpRequest } from '@/utils/http';
import { PageQuery, ID, IDS, PageResult } from '@/api/base';
import { Post } from './model';

enum Api {
  root = '/system/post',
  postSelect = '/system/post/optionselect',
  postList = '/system/post/list',
  postExport = '/system/post/export',
}

export function postList(data?: PageQuery & AnyObject) {
  return httpRequest.get<PageResult<Post>>({ url: Api.postList, data });
}

export function postExport() {
  return httpRequest.post<Blob>(
    { url: Api.postExport, responseType: 'blob' },
    { isTransformResponse: false },
  );
}

export function postInfo(postId: ID) {
  return httpRequest.get<Post>({ url: Api.root + '/' + postId });
}

export function postAdd(data: any) {
  return httpRequest.postWithMsg<void>({ url: Api.root, data });
}

export function postUpdate(data: any) {
  return httpRequest.putWithMsg<void>({ url: Api.root, data });
}

export function postRemove(postIds: IDS) {
  return httpRequest.deleteWithMsg<void>({ url: Api.root + '/' + postIds });
}

/**
 * 这个方法未使用过
 * @returns
 */
export function postOptionSelect() {
  return httpRequest.get({ url: Api.postSelect });
}
