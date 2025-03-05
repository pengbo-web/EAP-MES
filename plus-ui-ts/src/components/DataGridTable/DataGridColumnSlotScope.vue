<template>
  <el-table-column slot="fixed" v-bind="$attrs" show-overflow-tooltip v-on="$listeners">
    <template slot="header" slot-scope="scope">
      <slot :column="scope.column" name="header">
        {{ scope.column.label }}
        <el-popover placement="top" width="200" height="200" trigger="click">
          <span slot="reference" style="cursor: pointer;margin-left:10px">
            <i :style="value ? 'color: red' : 'color: blue'" class="el-icon-search" style="font-size:16px" />
          </span>
          <el-input v-model="value" size="small" clearable placeholder="请输入筛选条件" @change="zChange"/>
        </el-popover>
      </slot>
    </template>
    <template slot-scope="scope">
      <slot :row="scope.row">
        {{ scope.row[prop] }}
      </slot>
    </template>
  </el-table-column>
</template>

<script>

export default {
  name: 'DataGridColumnslotScope',
  inheritAttrs: false, // v-bind="$attrs"传递属性后，浏览器查看html代码能看见visible，title等属性；设置inheritAttrs: false可以隐藏此类属性
  model: {
    prop: 'pValue', // 指定自己的value属性接受数值
    event: 'inputE'// 指定自己的change事件回送数据给父组件
  },
  props: {
    prop: {
      type: String,
      required: true
    },
    // 根据自己model的要求定义pfoo属性
    pValue: {
      type: String,
      default: ''
    }

  },
  data() {
    return {
      value: this.pValue
    }
  },
  computed: {
  },
  watch: {
    pValue: {
      handler(newValue) {
        this.value = newValue
      }
    }
  },
  beforeCreate() {
  },
  created() {
  },
  mounted() {
  },
  methods: {
    zChange(value) {
      this.$emit('inputE', value)
      this.$emit('change', value)
    }
  }
}
</script>
<style lang="scss" scoped>

// ::v-deep .el-select{
//     >.el-input .el-input__inner{
//         padding-left:6px;
//     }
// }

// ::v-deep .el-input .el-input__inner{
//     padding:0 6px;
// }

// ::v-deep .el-input--suffix .el-input__inner {
//     padding-right: 30px;
// }

</style>
