import { router } from "@/routes/router";
import { VueQueryPlugin, VueQueryPluginOptions } from "@tanstack/vue-query";
import Antd from "ant-design-vue";
import "ant-design-vue/dist/reset.css";
import { OhVueIcon, addIcons } from "oh-vue-icons";
import * as AllIcons from "oh-vue-icons/icons";
import { createPinia } from "pinia";
import { createApp } from "vue";
import Vue3Toastify, { type ToastContainerOptions } from "vue3-toastify";
import "vue3-toastify/dist/index.css";
import App from "./App.vue";
import "./index.scss";

(window as any).global = window;

const AllIcon = Object.values({ ...AllIcons });
addIcons(...AllIcon);

const app = createApp(App);

const vueQueryConfig: VueQueryPluginOptions = {
  queryClientConfig: {
    defaultOptions: {
      queries: {
        refetchOnWindowFocus: false,
        retry: 1,
        staleTime: 1000 * 60 * 5, // 5 minutes
      },
    },
  },
};

app.use(router);
app.use(Antd);
app.use(Vue3Toastify, {
  autoClose: 3000,
} as ToastContainerOptions);
app.use(VueQueryPlugin, vueQueryConfig);
app.use(createPinia());
app.component("v-icon", OhVueIcon);

app.mount("#app");
