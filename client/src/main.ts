import { VueQueryPlugin } from "@tanstack/vue-query";
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
import { router } from "@/routes/router";

(window as any).global = window;

const AllIcon = Object.values({ ...AllIcons });
addIcons(...AllIcon);

const app = createApp(App);

app.use(router);
app.use(Antd);
app.use(Vue3Toastify, {
  autoClose: 3000,
} as ToastContainerOptions);
app.use(VueQueryPlugin);
app.use(createPinia());
app.component("v-icon", OhVueIcon);

app.mount("#app");
