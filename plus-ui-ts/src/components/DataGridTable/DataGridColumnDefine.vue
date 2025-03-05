<template>
  <CustomDialog :drag-dialog="dialogInfo">
    <div :style="{'text-align': 'right'}" class="action-bar">
      <div style="float:left; horiz-align: center; text-align:center;">
        <span>自定义列信息</span>
      </div>
      <span v-loading="loading" class="bar-btn" @click="saveColumn">
        <i class="el-icon-check" />
        保存
      </span>
      <span v-loading="loading" class="bar-btn delete-btn" @click="deleteColumn">
        <i class="el-icon-delete" />
        恢复系统默认
      </span>
      <span class="bar-btn delete-btn" @click="cancel()">
        <i class="el-icon-circle-close" />
        关闭
      </span>
    </div>
    <el-table
      v-if="gridLoad"
      :data="form.customFieldList"
      :max-height="maxHeight"
      style="width: 100%;">
      <el-table-column label="系统字段名" prop="fieldProp" width="120" />
      <el-table-column label="自定义显示名称" width="180">
        <template slot-scope="scope">
          <el-input v-model="scope.row.fieldLabel" placeholder="请输入名称" />
        </template>
      </el-table-column>
      <el-table-column label="是否显示" width="80">
        <template slot-scope="scope">
          <el-checkbox v-model="scope.row.fieldVisible" :true-label="1" :false-label="0" />
        </template>
      </el-table-column>
      <el-table-column label="显示顺序" width="120" sortable prop="fieldSort">
        <template slot-scope="scope">
          <el-input-number v-model.number="scope.row.fieldSort" :min="0" :max="99" controls-position="right" style="width: 80px;" />
        </template>
      </el-table-column>
      <el-table-column label="列宽度">
        <template slot-scope="scope">
          <el-input-number v-model.number="scope.row.fieldWidth" :min="30" :max="999" :step="10" controls-position="right" style="width: 200px;" />
        </template>
      </el-table-column>
    </el-table>
  </CustomDialog>
</template>
<script>
import { deleteByGridIdAndUser, findByGridIdAndUser, saveCustomField } from '@/api/user'

export default {
  name: 'DataGridColumnDefine',
  components: { },
  props: {
    gridId: {
      type: String,
      required: true,
      default: ''
    }
  },
  data() {
    return {
      dialogInfo: {
        name: this.gridId,
        title: '自定义列维护',
        showFooter: false,
        showHeader: false,
        height: '99',
        width: '60'
      },
      form: {
        customFieldList: [],
        paramBean: {
          gridId: this.gridId
        }
      },
      maxHeight: window.innerHeight - 60,
      loading: false,
      gridLoad: false
    }
  },
  watch: {},
  created() {},
  mounted() {
  },
  beforeDestroy() {
  },
  methods: {
    displayDialog() {
      const { name } = this.dialogInfo
      this.$nextTick(() => {
        this.$modal.show(name)
      })
    },
    hiddenDialog() {
      const { name } = this.dialogInfo
      this.$nextTick(() => {
        this.$modal.hide(name)
      })
    },
    cancel() {
      this.hiddenDialog()
      this.gridLoad = false
      this.form.customFieldList = []
    },
    initDataOfBefore() {
      this.gridLoad = true
      this.form.customFieldList = []
    },
    viewMainData(allDisplayColumns) {
      this.displayDialog()
      const tmpFieldList = {}
      // 默认定义的
      allDisplayColumns.forEach(row => {
        tmpFieldList[row.fieldProp] = row
      })
      findByGridIdAndUser(this.gridId).then(res => {
        const data = res.data
        if (data.length > 0) {
          res.data.forEach(row => {
            tmpFieldList[row.fieldProp] = row
          })
        }
        const customFieldList = []
        for (const key in tmpFieldList) {
          customFieldList.push(tmpFieldList[key])
        }
        this.form.customFieldList = customFieldList
      })
    },
    saveColumn() {
      this.doSingleSave()
    },
    deleteColumn() {
      const _this = this
      this.$confirm('确认要恢复系统默认?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        _this.loading = true
        deleteByGridIdAndUser(this.gridId).then(res => {
          _this.loading = false
          _this.cancel()
          _this.$emit('refreshGridInfo')
        }).catch(err => {
          _this.loading = false
          console.log(err)
        })
      })
    },
    doSingleSave() {
      const _this = this
      _this.loading = true
      saveCustomField(_this.form).then(res => {
        _this.loading = false
        _this.cancel()
        _this.$emit('refreshGridInfo')
      }).catch(err => {
        _this.loading = false
        console.log(err)
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/styles/mixin.scss';
@include action-bar;
::v-deep .el-drawer__header {
  display: none;
}
</style>
