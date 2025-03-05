<template>
  <el-button-group class="crud-opts-right">
    <slot name="left" />
    <el-button
      v-if="searchEnable"
      size="mini"
      plain
      type="info"
      icon="el-icon-search"
      @click="toggleSearch()"
    />
    <el-button
      size="mini"
      icon="el-icon-refresh"
      @click="clickRefresh()"
    />
    <slot name="right" />
    <!-- <el-popover
      placement="bottom-end"
      width="150"
      trigger="click"
    >
      <el-button
        slot="reference"
        size="mini"
        icon="el-icon-s-grid"
      >
        <i
          class="fa fa-caret-down"
          aria-hidden="true"
        />
      </el-button>
      <el-checkbox
      >
        全选
      </el-checkbox>
      <el-checkbox
        v-for="item in sup_this.customProps.tableColumns"
        :key="item.label"
        v-model="item.visible"
      >
        {{ item.label }}
      </el-checkbox>
    </el-popover>-->
  </el-button-group>
</template>

<script>
export default {
  name: 'DataGridOpts',
  props: {
    sup_this: {
      type: Object,
      required: true
    },
    searchEnable: {
      type: Boolean,
      required: false,
      default: true
    }
  },
  methods: {
    toggleSearch() {
      this.$emit('searchClick') // searchClick()
    },
    clickRefresh() {
      this.$emit('refreshClick') // this.refreshClick()
    }
  }
}
</script>

