<template>
    <div id="printable-area">
      <div v-for="(item, index) in printData" :key="index" class="print-item">
        <div class="centered-content" style="font-size: 40px; margin-top: 55px">
          图号:<span style="border-bottom: 1px solid black; display: inline-block; width: 300px;">{{ item.figureNumber }}</span><br>
          名称:<span style="border-bottom: 1px solid black; display: inline-block; width: 300px;">{{ item.name }}</span><br>
          编号:<span style="border-bottom: 1px solid black; display: inline-block; width: 300px; ">{{ item.number }}</span><br>
          研制阶段:<span style="border-bottom: 1px solid black; display: inline-block; width: 300px; ">{{ item.developmentPhase }}</span><br>
          出厂时间:<span style="border-bottom: 1px solid black; display: inline-block; width: 300px; ">{{ item.factoryTime }}</span><br>
        </div>
        <br>
        <div v-if="index < printData.length - 1" class="page-break"></div>
      </div>
    </div>
  </template>
  
  <script>
  import {defineComponent} from 'vue';
  
  export default defineComponent({
    props: {
      queryParam: Array
    },
    setup(props) {
      const printData = props.queryParam;
      return { printData };
    }
  });
  </script>
  
  <style>
  @media print {
    body {
      background: none !important;
      text-align: center; /* 让 body 内容居中 */
    }
    @page {
      size: 15cm 12cm; /* 设置纸张大小为A4 */
      margin:0; /* 设置页边距 */
    }
    .noprint {
      display: none;
    }
    body {
      background: none !important;
    }
    .page-break {
      page-break-before: always;
    }
  
  }
  </style>