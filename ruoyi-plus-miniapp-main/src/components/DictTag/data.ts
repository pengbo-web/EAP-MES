interface TagType {
  [key: string]: { label: string; color: string };
}

export const tagTypes: TagType = {
  /** 由于和elementUI不同 用于替换颜色 */
  default: { label: '默认(default)', color: 'grey-2' },
  primary: { label: '主要(primary)', color: 'blue' },
  success: { label: '成功(success)', color: 'green' },
  info: { label: '信息(info)', color: 'grey-2' },
  warning: { label: '警告(warning)', color: 'amber' },
  danger: { label: '危险(danger)', color: 'red' },
  /** 自定义预设 color可以为16进制颜色 */
  pink: { label: 'pink', color: 'pink' },
  red: { label: 'red', color: 'red' },
  orange: { label: 'orange', color: 'orange' },
  green: { label: 'green', color: 'green' },
  cyan: { label: 'cyan', color: 'cyan' },
  purple: { label: 'purple', color: 'purple' },
};
