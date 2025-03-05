<template>
    <div>
      <svg ref="barcode" width="200" height="60" ></svg>
    </div>
  </template>
  
  <script lang="ts">
  import { defineComponent, onMounted, ref } from 'vue';
  import JsBarcode from 'jsbarcode';
  
  export default defineComponent({
    props: {
      barcodeValue: {
        type: String,
        required: true
      }
      ,
    barcodeWidth: {
      type: Number,
      default: 2
    },
    barcodeHeight: {
      type: Number,
      default: 100
    }
    },
    setup(props) {
      const barcode = ref<SVGSVGElement | null>(null);
  
      onMounted(() => {
		  console.log(props.barcodeValue)
        if (barcode.value) {
          JsBarcode(barcode.value, props.barcodeValue,{width: 1.6, // 设置条码宽度
  height: 120,
  format: "CODE128"});
        }
      });
  
      return { barcode };
    }
  });
  </script>