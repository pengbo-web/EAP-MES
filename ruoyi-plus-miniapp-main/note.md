### ts 中使用 type 里的所有 key 作为类型生成新的类型

```typescript
type Test = {
  a: string;
  b: number;
};

type NewTest = {
  [K in keyof Test]: Array<Test[K]>;
};
```

### ts 中获取类型

```ts
const value = ref({
  name: 'xxx',
  age: 10,
});
type ValueType = typeof value;

// 生成新的类型
type newType = {
  [key in keyof typeof value.value]: number[];
};
```
