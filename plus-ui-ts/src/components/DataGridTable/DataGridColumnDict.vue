<template>
  <el-table-column v-bind="$attrs" v-on="$listeners" >
    <template slot-scope="scope">
      <slot :row="scope.row">
        <template v-for="item in dicts">
          <el-tag v-if="item.dictCode === scope.row[prop]" :type="item.attribute1" :key="item.id" size="mini">{{ item.dictName }}</el-tag>
        </template>
      </slot>
    </template>
  </el-table-column>
</template>

<script>

export default {
  name: 'DataGridColumnDict',
  inheritAttrs: false, // v-bind="$attrs"传递属性后，浏览器查看html代码能看见visible，title等属性；设置inheritAttrs: false可以隐藏此类属性
  props: {
    prop: {
      type: String,
      required: true
    },
    dicts: {
      type: Array,
      required: true
    }
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
