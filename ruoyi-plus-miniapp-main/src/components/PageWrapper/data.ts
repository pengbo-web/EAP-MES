import { PageQuery, PageResult } from '@/api/base';

export interface Props {
  /**
   * 卡片高度 必填
   */
  cardHeight: number;
  /**
   * 是否开启搜索 即页面最上方的筛选
   */
  enableSearch?: boolean;
  /**
   * 是否显示日期搜索
   */
  showDateSearch?: boolean;
  /**
   * 是否显示状态搜索
   */
  showStatusSearch?: boolean;
  /**
   * 请求list的api
   * @param data page请求参数
   * @returns
   */
  api: (data?: PageQuery) => Promise<PageResult<any>>;
  /**
   * 虚拟滚动组件的v-for index不唯一  需要传一个item的唯一Id用来遍历
   */
  idProp: string;
  /**
   * 是否开启下拉功能 需要在pages.json设置
   */
  enablePulldown?: boolean;
  /**
   * 每页的条数  默认10
   */
  pageSize?: number;
  /**
   * list到底加载的等待时间 ms 不加等待的话样式会乱
   */
  wait?: number;
  /**
   * 搜索的字段
   */
  search: Record<string, string | number | boolean>;
}
