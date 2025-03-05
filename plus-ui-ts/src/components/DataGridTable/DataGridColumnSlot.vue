<template>
  <el-table-column slot="fixed" v-bind="$attrs" show-overflow-tooltip v-on="$listeners">
    <template slot="header" slot-scope="scope">
      <slot :column="scope.column" name="header">
        {{ scope.column.label }}
      </slot>
    </template>
    <template slot-scope="scope">
      <slot :row="scope.row" :$index="scope.$index" :_self="scope._self"/>
    </template>
  </el-table-column>
</template>

<script>

export default {
  name: 'DataGridColumnslot',
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
