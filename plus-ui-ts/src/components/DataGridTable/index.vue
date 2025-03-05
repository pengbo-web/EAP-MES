<template>
  <div class="datagrid-box" element-loading-background="rgba(0, 0, 0, 0)">
    <!--表格渲染-->
    <el-table
      :ref="tableRef"
      :id="id"
      :cell-class-name="tableCellClassName"
      :summary-method="getDetailSummary"
      v-bind="$attrs"
      :highlight-current-row="highlightCurrentRow"
      :stripe="stripe"
      border
      v-on="$listeners"
      @sort-change="customGridSortChange">
      <el-table-column v-if="multiSelect" type="selection" width="45" align ="center"/>
      <el-table-column v-if="showRowNumber" :show-overflow-tooltip="true" :index="customIndex" label="序号" type="index" width="50" align ="center"/>
      <el-table-column v-else-if="showRowComplexNumber" :show-overflow-tooltip="true" label="序号" type="index" width="50" align ="center">
        <template slot-scope="scope">
          <a title="查看历史版本记录" @click="toViewHistory(scope.row)">{{ scope.$index + 1 }}</a>
        </template>
      </el-table-column>
      <slot />
      <slot v-for="(columnprop) in customDefineColumns" :name="columnprop.fieldProp" />
    </el-table>
    <!--分页组件-->
    <el-pagination
      v-if="showPage"
      :total="total"
      :page-sizes="[20, 50, 100, 200]"
      :current-page="1"
      background
      style="margin-top: 3px;"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="curSizeChange"
      @current-change="curPageChange"/>
    <a v-if="showCustomField" class="user-custom-field" @click="userCustomField">
      <i class="el-icon-s-grid" />
    </a>
    <DataGridColumnDefine v-if="showCustomField" ref="DataGridColumnDefine" :grid-id="gridId" @refreshGridInfo="refreshGridInfo" />
  </div>
</template>

<script>
import { findByGridIdAndUser } from '@/api/user'
import DataGridColumnDefine from './DataGridColumnDefine'

export default {
  name: 'DataGridTable',
  components: { DataGridColumnDefine },
  inheritAttrs: false, // v-bind="$attrs"传递属性后，浏览器查看html代码能看见visible，title等属性；设置inheritAttrs: false可以隐藏此类属性
  props: {
    tableRef: {
      type: String,
      required: false,
      default: 'dataGridTable'
    },
    showRowComplexNumber: {
      type: Boolean,
      required: false,
      default: false
    },
    id: {
      type: String,
      required: false,
      default: 'tableId'
    },
    showRowNumber: {
      type: Boolean,
      required: false,
      default: false
    },
    showCellClass: {
      type: Boolean,
      required: false,
      default: false
    },
    multiSelect: {
      type: Boolean,
      required: false,
      default: true
    },
    sup_this: {
      type: Object,
      required: false,
      default: null
    },
    total: {
      type: Number,
      required: false,
      default: 0
    },
    highlightCurrentRow: {
      type: Boolean,
      required: false,
      default: true
    },
    stripe: {
      type: Boolean,
      required: false,
      default: true
    },
    showPage: {
      type: Boolean,
      required: false,
      default: true
    },
    customCellClassColumn: {
      type: String,
      required: false,
      default: 'selfStateName'
    },
    customCellFieldColumn: {
      type: String,
      required: false,
      default: 'selfState'
    },
    gridId: {
      type: String,
      required: false,
      default: ''
    },
    allDefineColumns: {
      type: Array,
      required: false,
      default: () => []
    },
    summaryField: {
      type: String,
      required: false,
      default: null
    }
  },
  data() {
    return {
      customDefineColumns: [],
      loadGridFlag: true
    }
  },
  computed: {
    showCustomField() {
      return this.allDefineColumns.length !== 0 && this.gridId
    }
  },
  watch: {
    allDefineColumns: {
      handler(val, oldVal) {
        if (val && val.length > 0 && (String(val) !== String(oldVal) || this.loadGridFlag)) {
          this.loadGridFlag = false
          this.loadUserCustomField(true)
        }
      },
      immediate: true
    }
  },
  created() {
  },
  mounted() {
    if (this.showRowNumber || this.showRowComplexNumber) {
      setTimeout(() => {
        this.$forceUpdate()
        this.$refs[this.tableRef].doLayout()
      }, 300)
    }
  },
  methods: {
    getDetailSummary(param) {
      if (!this.summaryField) {
        return
      }
      const { columns, data } = param
      const sums = []
      const summaryField = this.summaryField
      columns.forEach((column, index) => {
        const fieldIndex = summaryField.indexOf(column.property)
        if (fieldIndex <= 0) {
          return
        }
        if (fieldIndex === 1) {
          sums[index] = '合计'
          return
        }
        const values = data.map(item => Number(item[column.property]))
        if (!values.every(value => isNaN(value))) {
          sums[index] = values.reduce((prev, curr) => {
            const value = Number(curr)
            if (!isNaN(value)) {
              return prev + curr
            } else {
              return prev
            }
          }, 0)
        } else {
          sums[index] = 'N/A'
        }
      })
      return sums
    },
    refreshGridInfo() {
      this.loadUserCustomField(false)
    },
    loadUserCustomField(isInit) {
      const _this = this
      if (!_this.showCustomField) {
        return
      }
      if (!isInit) {
        setTimeout(() => {
          _this.customDefineColumns = []
        }, 10)
      }
      const tmpDefineColumns = {}
      _this.allDefineColumns.forEach(column => {
        tmpDefineColumns[column] = true
      })
      findByGridIdAndUser(this.gridId).then(res => {
        const resData = res.data
        const tmpColumns = []
        resData.forEach(row => {
          if (row.fieldVisible === 1) {
            tmpColumns.push(row)
          }
          delete tmpDefineColumns[row.fieldProp]
        })
        // 处理新增加的字段, 放到后面
        for (const tmpColumn in tmpDefineColumns) {
          tmpColumns.push({ fieldProp: tmpColumn })
        }
        _this.customDefineColumns = tmpColumns
        if (resData.length > 0) {
          setTimeout(() => {
            _this.setFieldInfo()
          }, 50)
        }
      })
    },
    setFieldInfo() {
      const mainGridTable = this.$refs[this.tableRef]
      if (!mainGridTable) {
        return
      }
      const userDisplayColumns = {}
      this.customDefineColumns.forEach(row => {
        userDisplayColumns[row.fieldProp] = row
      })
      mainGridTable.columns.forEach(e => {
        if (!e.property || e.type !== 'default') {
          return
        }
        const fieldInfo = userDisplayColumns[e.property]
        if (fieldInfo) {
          if (fieldInfo.fieldLabel) {
            e.label = fieldInfo.fieldLabel
          }
          if (fieldInfo.fieldWidth && fieldInfo.fieldWidth > 0) {
            e.width = fieldInfo.fieldWidth //  e.realWidth = '500px';//minWidth
          }
        }
      })
    },
    userCustomField() {
      const _this = this
      const _thisForm = _this.$refs.DataGridColumnDefine
      const mainGridTable = _this.$refs[this.tableRef]
      const tmpDefineColumns = {}
      _this.allDefineColumns.forEach(column => {
        tmpDefineColumns[column] = true
      })
      const allDisplayColumns = []
      let i = 0
      mainGridTable.columns.forEach(e => {
        if (!e.property || e.type !== 'default') {
          return
        }
        if (!tmpDefineColumns[e.property]) {
          return
        }
        i++
        const fieldObj = {}
        fieldObj.fieldProp = e.property
        fieldObj.fieldLabel = e.label
        fieldObj.fieldWidth = e.width || e.realWidth
        fieldObj.fieldVisible = 1
        fieldObj.fieldSort = i
        allDisplayColumns.push(fieldObj)
      })
      _thisForm.initDataOfBefore()
      _thisForm.viewMainData(allDisplayColumns)
    },
    tableCellClassName({ row, column, rowIndex, columnIndex }) {
      if (!this.showCellClass || column.property !== this.customCellClassColumn) {
        return ''
      }
      switch (row[this.customCellFieldColumn]) {
        case '10': return 'valid-row'
        case '0': return 'draft-row'
        case '-10': return 'invalid-row'
        case '5': return 'wait-row'
        case '4': return 'approve_not_pass-row'
        case '8': return 'waitfeedback-row'
        case '7': return 'manualprocess-row'
      }
      return ''
    },
    tableRowClassName({ row, rowIndex }) {
      if (!this.showCellClass) {
        return ''
      }
      switch (row[this.customCellFieldColumn]) {
        case '10': return 'valid-row'
        case '-10': return 'invalid-row'
        // case 0: return 'draft-row'
        case '5': return 'wait-row'
        case '4': return 'approve_not_pass-row'
      }
      return 'draft-row'
    },
    customIndex(index) {
      return index + 1
    },
    curSizeChange(size) {
      this.sup_this.sizeChange(size)
    },
    curPageChange(page) {
      this.sup_this.pageChange(page)
    },
    customGridSortChange({ column, prop, order }) {
      this.sup_this.gridSortChange({ column, prop, order })
    },
    toViewHistory(rowData) {
      this.$emit('toViewHistory', rowData)
    }
  }
}
</script>

<style lang="scss" scoped>
.datagrid-box {
    position: relative;
    width: 100%;
    height: 100%;
  // height: calc(100vh - 42px);
      display: flex;
    flex-direction: column;
  box-sizing: border-box;
//   overflow: hidden;
min-height: 100px;
  overflow: auto;
  .el-table{
    flex: auto;
    display: flex;
    flex-direction: column;
  }
}
.user-custom-field {
  position: absolute;
  display: block;
  width: 26px;
  height: 26px;
  top: 0;
  right: 5px;
  cursor: pointer;
  background: #9fbbb4;
  z-index: 1;
  border-radius: 0 0 6px 0;
  text-align: center;
  line-height: 26px;
  color: #fff;
  font-size: 18px;
}
::v-deep .el-table__body-wrapper {
    flex: auto;
}
</style>
