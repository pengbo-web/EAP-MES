/**
 * @description: Request result set
 */
export enum ResultEnum {
  /**
   * 请求成功
   */
  SUCCESS = 200,
  /**
   * 登录超时
   */
  TIMEOUT = 401,
}

export enum RequestMsgEnum {
  SUCCESS = '请求成功',
  TIMEOUT = '登录超时',
  FAIL = '请求出错，请稍候重试',
}

/**
 * @description: request method
 */
export enum RequestEnum {
  GET = 'GET',
  POST = 'POST',
  PUT = 'PUT',
  DELETE = 'DELETE',
}

/**
 * @description:  contentType
 */
export enum ContentTypeEnum {
  // json
  JSON = 'application/json;charset=UTF-8',
  // form-data qs
  FORM_URLENCODED = 'application/x-www-form-urlencoded;charset=UTF-8',
  // form-data  upload
  FORM_DATA = 'multipart/form-data;charset=UTF-8',
}
