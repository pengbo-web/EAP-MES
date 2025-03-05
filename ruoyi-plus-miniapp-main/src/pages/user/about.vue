<template>
  <tm-app>
    <tm-sheet :margin="[10, 20, 10, 0]" :round="3" :padding="[24, 24, 24, 0]" :border="2">
      <tm-text :font-size="28" _class="text-weight-b" label="项目信息"></tm-text>
      <tm-divider color="blue"></tm-divider>
      <tm-cell
        :margin="[0, 0]"
        :padding="[0, 24]"
        :titleFontSize="30"
        :border="2"
        title="名称"
        :right-text="projectInfo.name"
        right-icon=""
      >
      </tm-cell>
      <tm-cell
        :margin="[0, 0]"
        :padding="[0, 24]"
        :titleFontSize="30"
        :border="2"
        title="版本"
        :right-text="projectInfo.version"
        right-icon=""
      >
      </tm-cell>
      <tm-cell
        :margin="[0, 0]"
        :padding="[0, 24]"
        :titleFontSize="30"
        title="编译时间"
        :right-text="projectInfo.lastBuildTime"
        right-icon=""
      >
      </tm-cell>
    </tm-sheet>

    <tm-sheet :margin="[10, 20, 10, 0]" :round="3" :padding="[24, 24, 24, 0]" :border="2">
      <tm-text :font-size="28" _class="text-weight-b" label="生产环境依赖"></tm-text>
      <tm-divider color="green"></tm-divider>
      <tm-cell
        v-for="(key, index) in Object.keys(dependencies)"
        :key="index"
        :margin="[0, 0]"
        :padding="[0, 24]"
        :titleFontSize="30"
        :border="index === dependenciesSize - 1 ? 0 : 2"
        :title="key"
        :right-text="dependencies[key]"
        right-icon=""
      >
      </tm-cell>
    </tm-sheet>

    <tm-sheet :margin="[10, 20, 10, 10]" :round="3" :padding="[24, 24, 24, 0]" :border="2">
      <tm-text :font-size="28" _class="text-weight-b" label="开发环境依赖"></tm-text>
      <tm-divider color="yellow"></tm-divider>
      <tm-cell
        v-for="(key, index) in Object.keys(devDependencies)"
        :key="index"
        :margin="[0, 0]"
        :padding="[0, 24]"
        :titleFontSize="30"
        :border="index === devDependenciesSize - 1 ? 0 : 2"
        :title="key"
        :right-text="devDependencies[key]"
        right-icon=""
      >
      </tm-cell>
    </tm-sheet>
  </tm-app>
</template>

<script setup lang="ts">
  const { pkg, lastBuildTime } = __APP_INFO__;
  const { dependencies, devDependencies } = pkg;
  const dependenciesSize = Object.keys(dependencies).length;
  const devDependenciesSize = Object.keys(devDependencies).length;

  removeSubVersion(dependencies);
  removeSubVersion(devDependencies);

  const projectInfo = {
    name: pkg.name,
    version: pkg.version,
    lastBuildTime,
  };
  /**
   * 去除子版本号 比如3.0.0-xxxxx -> 3.0.0
   * @param obj obj
   */
  function removeSubVersion(obj: any) {
    Object.keys(obj).forEach((key) => {
      const value = obj[key];
      if (value.includes('-')) {
        obj[key] = value.split('-')[0];
      }
    });
  }
</script>

<style scoped></style>
