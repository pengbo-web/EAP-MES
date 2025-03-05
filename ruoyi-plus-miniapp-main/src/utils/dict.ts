import type { DictData } from '@/api/system/dict/dictData.model';
import { dictDataInfo } from '@/api/system/dict/dictData';
import { reactive } from 'vue';
import { useDictStore } from '@/store/modules/dict';

export function getDict(dictType: string): DictData[] {
  const dictData = reactive<DictData[]>([]);
  const dictStore = useDictStore();
  const ret = dictStore.getDict(dictType);
  if (ret && ret.length > 0) {
    dictData.push(...ret);
  } else {
    dictDataInfo(dictType).then((resp) => {
      dictData.push(...resp);
      // 添加到stor
      dictStore.setDict(dictType, resp);
    });
  }

  return dictData;
}
