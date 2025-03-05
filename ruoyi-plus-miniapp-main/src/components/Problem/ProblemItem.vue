<template>
  <tm-collapse-item :name="name">
    <template #title>
      <view class="flex flex-row flex-row-center-center">
        <tm-text :label="title" :fontSize="30"></tm-text>
        <tm-tag size="xs" :label="tagLabel" :color="tagColor"></tm-tag>
      </view>
    </template>
    <view class="pa-24">
      <tm-text :font-size="28" :label="content"></tm-text>
    </view>
  </tm-collapse-item>
</template>

<script setup lang="ts">
  interface Problem {
    name: string;
    title: string;
    tagLabel: string;
    tagColor: string;
    content: string;
  }

  const props = withDefaults(defineProps<Problem>(), {
    name: 'xxx',
    title: '',
    tagColor: 'red',
    tagLabel: '',
    content: '',
  });
</script>

<style scoped></style>
