<template>
  <tm-radio-group :align="align" :direction="direction" :model="model" v-model="modelValueRef">
    <tm-radio
      v-for="(item, index) in dictDataRef"
      :key="index"
      outlined
      :value="item.value"
      :label="item.label"
    ></tm-radio>
  </tm-radio-group>
</template>

<script lang="ts">
  import { defineComponent, ref } from 'vue';
  import type { DictData } from '@/api/system/dict/dictData.model';
  import { PropType } from 'vue';
  import { computed } from 'vue';
  import { watch } from 'vue';

  export default defineComponent({
    name: 'DictRadioGroup',
    emits: ['update:modelValue'],
    props: {
      dictValue: {
        type: Array as PropType<DictData[]>,
        default: [],
      },
      modelValue: [String, Number],
      model: {
        type: String as PropType<'button' | 'radio'>,
        default: 'radio',
      },
      direction: {
        type: String as PropType<'row' | 'col' | 'customCol'>,
        default: 'row',
      },
      align: {
        type: String as PropType<'left' | 'center' | 'right'>,
        default: 'left',
      },
    },
    setup(props, { emit }) {
      const dictDataRef = computed(() => {
        return props.dictValue.map((item) => ({ label: item.dictLabel, value: item.dictValue }));
      });

      const modelValueRef = ref(props.modelValue);

      watch(modelValueRef, (val) => {
        modelValueRef.value = val;
        emit('update:modelValue', val);
      });

      return {
        dictDataRef,
        modelValueRef,
      };
    },
  });
</script>

<style scoped></style>
