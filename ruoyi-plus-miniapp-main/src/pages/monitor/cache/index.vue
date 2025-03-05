<template>
  <tm-app>
    <tm-card :margin="[20, 20, 20, 10]" :padding="[25, 0]">
      <template #title>
        <tm-text label="redis信息" class="text-weight-b" :font-size="28"></tm-text>
      </template>
      <template #content>
        <tm-descriptions class="fulled" :label-width="200" :column="1" text :round="2">
          <tm-descriptions-item
            :font-size="26"
            v-for="(item, index) in redisInfo"
            :key="index"
            :label="item.label"
            :value="item.value"
          ></tm-descriptions-item>
        </tm-descriptions>
      </template>
    </tm-card>
    <!-- #ifdef MP -->
    <tm-alert
      color="orange"
      linear="right"
      :content="{
        icon: 'tmicon-alert',
        content: '小程序由于打包体积原因, 图表不做展示',
      }"
      :height="80"
    ></tm-alert>
    <!-- #endif -->
    <!-- 小程序不显示图表 打包太大了 -->
    <!-- #ifndef MP -->
    <!-- 内存占用 -->
    <tm-card :margin="[20, 20, 20, 10]" :padding="[25, 0]">
      <template #title>
        <tm-text label="内存信息" class="text-weight-b" :font-size="28"></tm-text>
      </template>
      <template #content>
        <tm-chart
          ref="memoryChartDom"
          :height="550"
          :width="660"
          @onInit="memoryChartInit"
        ></tm-chart>
      </template>
    </tm-card>
    <!-- 命令统计 -->
    <tm-card :margin="[20, 20, 20, 10]" :padding="[25, 0]">
      <template #title>
        <tm-text label="命令统计" class="text-weight-b" :font-size="28"></tm-text>
      </template>
      <template #content>
        <tm-chart
          ref="commandChartDom"
          :height="600"
          :width="660"
          @onInit="commandChartInit"
        ></tm-chart>
      </template>
    </tm-card>
    <!-- #endif -->
  </tm-app>
</template>

<script setup lang="ts">
  import { redisCacheInfo } from '@/api/monitor/cache';
  import { onLoad, onPullDownRefresh } from '@dcloudio/uni-app';
  import { ref } from 'vue';

  const redisInfo = ref<any[]>([]);
  const memoryUsed = ref<string>('0');
  const commondState = ref<any>();

  async function loadInfo() {
    const ret = await redisCacheInfo();
    const { info, dbSize, commandStats } = ret;
    // 设置memory 图表要用
    memoryUsed.value = (parseInt(info.used_memory) / 1024 / 1024).toFixed(2);
    // 命令统计
    commondState.value = commandStats;
    redisInfo.value = [
      { label: '平台', value: info.os },
      { label: 'redis版本', value: info.redis_version },
      { label: 'redis模式', value: info.redis_mode },
      { label: 'tcp端口', value: info.tcp_port },
      { label: '客户端数', value: info.connected_clients },
      { label: '运行时间', value: info.uptime_in_days + '天' },
      { label: '使用内存', value: memoryUsed.value + 'MB' },
      { label: '使用CPU', value: info.used_cpu_user_children + '%' },
      { label: 'AOF是否开启', value: info.aof_enabled === '0' ? '否' : '是' },
      { label: 'RDB是否成功', value: info.rdb_last_bgsave_status },
      { label: 'RDB是否成功', value: info.rdb_last_bgsave_status },
      { label: 'Key数量', value: String(dbSize) },
      {
        label: '网络入口/出口',
        value: info.instantaneous_input_kbps + 'kbps/' + info.instantaneous_output_kbps + 'kbps',
      },
    ];
    console.log(ret);
  }

  onLoad(async () => {
    await loadInfo();
  });

  onPullDownRefresh(async () => {
    await loadInfo().finally(() => {
      uni.stopPullDownRefresh();
    });
  });

  /**
   * 小程序打包太大了 图表不做显示
   */
  // #ifndef MP
  import tmChart from '@/tmui/components/tm-chart/tm-chart.vue';
  import { ECharts, EChartsOption } from 'echarts';
  /**
   * 内存占用图表ref
   */
  const memoryChartDom = ref<InstanceType<typeof tmChart> | null>(null);
  /**
   * 命令统计图表ref
   */
  const commandChartDom = ref<InstanceType<typeof tmChart> | null>(null);

  function memoryChartInit(chart: ECharts) {
    const value = memoryUsed.value;
    const option: EChartsOption = {
      tooltip: {
        formatter: '{b} {a} : ' + value,
      },
      series: [
        {
          name: '峰值',
          type: 'gauge',
          min: 0,
          max: 1000,
          detail: {
            formatter: value,
          },
          data: [
            {
              value: parseFloat(value),
              name: '内存消耗',
            },
          ],
        },
      ],
    };
    chart.setOption(option);
  }

  function commandChartInit(chart: ECharts) {
    const option: EChartsOption = {
      tooltip: {
        trigger: 'item',
        formatter: '{a} {b} : {c} ({d}%)',
      },
      series: [
        {
          name: '命令',
          type: 'pie',
          roseType: 'radius',
          radius: [15, 95],
          center: ['50%', '38%'],
          data: commondState.value,
          animationEasing: 'cubicInOut',
          animationDuration: 1000,
        },
      ],
    };
    chart.setOption(option);
  }
  // #endif
</script>

<style scoped></style>
