<template>
  <tm-app>
    <tm-sheet :border="1" :padding="[0]" :margin="[10, 10]" :round="3">
      <tm-cell
        :margin="[10, 0]"
        title="RuoYi Plus 文档地址"
        bottomBorder
        @click="openExternalLink('https://plus-doc.dromara.org/#/README')"
      ></tm-cell>
      <tm-cell
        :margin="[10, 0]"
        title="RuoYi Plus Vben 仓库地址"
        bottomBorder
        @click="openExternalLink('https://gitee.com/dapppp/ruoyi-plus-vben')"
      ></tm-cell>
      <tm-cell
        :margin="[10, 0]"
        title="TMUI 文档地址"
        bottomBorder
        @click="
          openExternalLink('https://tmui.design/start/%E5%BF%AB%E9%80%9F%E4%B8%8A%E6%89%8B.html')
        "
      ></tm-cell>
      <tm-cell
        :margin="[10, 0]"
        title="Uniapp 文档地址"
        @click="openExternalLink('https://uniapp.dcloud.net.cn/api/')"
      ></tm-cell>
    </tm-sheet>
  </tm-app>
</template>

<script setup lang="ts">
  import { openExternalLink } from '@/utils/page';
</script>

<style scoped></style>
