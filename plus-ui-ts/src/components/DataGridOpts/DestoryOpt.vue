<template>
  <div style="margin-left:6px;margin-right:6px;">
    <el-button
      v-bind="$attrs"
      class="filter-item"
      type="danger"
      icon="el-icon-s-release"
      @click="confirmDestory">注销</el-button>
    <el-dialog :visible.sync="dialog" title="确认要注销记录?" append-to-body width="400px">
      <el-form label-width="100px">
        <el-form-item label="选择方式">
          <el-radio v-model="isSubmit" :label="false">暂存</el-radio>
          <el-radio v-model="isSubmit" :label="true">提交</el-radio>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="busRmk" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="cancel">取消</el-button>
        <el-button type="primary" @click="doMultiDestory">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'DestoryOpt',
  props: {
    sup_this: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      dialog: false,
      isSubmit: false,
      busRmk: null
    }
  },
  methods: {
    cancel() {
      this.dialog = false
    },
    confirmDestory() {
      const _thisParent = this.sup_this
      const selectIds = []
      _thisParent.selectedGridRows.forEach(row => {
        selectIds.push(row.busId)
      })
      if (selectIds.length === 0) {
        this.$message({
          message: '请选择记录',
          type: 'warning'
        })
        return
      }
      this.dialog = true
    },
    doMultiDestory() {
      const _thisParent = this.sup_this
      const selectIds = []
      _thisParent.selectedGridRows.forEach(row => {
        selectIds.push(row.busId)
      })
      this.cancel()
      this.$emit('doMultiDestory', selectIds, this.isSubmit, this.busRmk)
    }
  }
}
</script>

