import { DictData } from '@/api/system/dict/dictData.model';
import { defineStore } from 'pinia';

interface DictMap {
  [key: string]: DictData[];
}

interface DictState {
  dictMap: DictMap;
}

export const useDictStore = defineStore('miniapp-dict', {
  state: (): DictState => ({
    dictMap: {},
  }),
  actions: {
    getDict(dictType: string) {
      return this.dictMap[dictType];
    },
    setDict(dictType: string, dictValue: DictData[]) {
      this.dictMap[dictType] = dictValue;
    },
    reset() {
      this.dictMap = {};
    },
  },
  persist: {
    enabled: true,
    // #ifdef H5
    H5Storage: window.localStorage,
    // #endif
  },
});
