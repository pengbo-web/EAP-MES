<template>
  <el-table-column v-bind="$attrs" show-overflow-tooltip v-on="$listeners"/>
</template>

<script>

export default {
  name: 'DataGridColumn',
  inheritAttrs: false, // v-bind="$attrs"传递属性后，浏览器查看html代码能看见visible，title等属性；设置inheritAttrs: false可以隐藏此类属性
  props: {
  },
  data() {
    return {
    }
  },
  computed: {
  },
  beforeCreate() {
  },
  mounted() {
  },
  methods: {
  }
}
</script>
