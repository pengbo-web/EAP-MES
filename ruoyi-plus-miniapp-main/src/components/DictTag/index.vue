<template>
  <view>
    <tm-tag
      :margin="[0]"
      :padding="[6, 0]"
      v-if="color"
      :color="color"
      :label="label"
      :font-size="fontSize"
    ></tm-tag>
    <tm-text v-if="!color" :label="label" :font-size="fontSize"></tm-text>
  </view>
</template>

<script lang="ts">
  import { defineComponent } from 'vue';
  import type { DictData } from '@/api/system/dict/dictData.model';
  import { PropType, computed } from 'vue';
  import { isHexColor } from '@/utils/is';
  import { tagTypes } from './data';

  export default defineComponent({
    name: 'DictTag',
    props: {
      dictValue: {
        type: Array as PropType<DictData[]>,
        default: [],
      },
      value: [String, Number],
      fontSize: {
        type: Number,
        default: 24,
      },
    },
    setup(props, ctx) {
      const color = computed(() => {
        const current = props.dictValue.filter((item) => item.dictValue == props.value)[0];
        // cssClass在element-plus中是作为css类名使用  在这里是作为16进制颜色使用
        const cssClass = current?.cssClass;
        // listClass是tag组件的color使用 优先级应该低于自定义颜色
        const listClass = current?.listClass;
        // 都不存在的话返回空颜色
        if (!listClass && !cssClass) {
          return '';
        }
        // 优先返回自定义颜色
        // update 2023-12-21 由于官方使用这个是作为css类名 这里用作自定义颜色  需要判断
        // 是16进制颜色  直接返回
        if (isHexColor(cssClass)) {
          return cssClass;
        }
        // 这里做了antd - element-plus的兼容
        return tagTypes[listClass].color;
      });

      const label = computed<string>(() => {
        const current = props.dictValue.filter((item) => item.dictValue == props.value)[0];
        if (current) {
          return String(current.dictLabel);
        }
        return 'unknown';
      });

      return {
        color,
        label,
      };
    },
  });
</script>

<style scoped></style>
