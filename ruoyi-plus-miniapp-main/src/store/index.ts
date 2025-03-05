import { createPinia } from 'pinia';
import piniaPersist from 'pinia-plugin-persist-uni';

export const store = createPinia();
store.use(piniaPersist);

export default store;
