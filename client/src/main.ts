import { createApp } from "vue";
import App from "./App.vue";
import Antd from "ant-design-vue";
import { createPinia } from "pinia";
import { router } from "./router";
import "ant-design-vue/dist/reset.css";
import "./index.css";
import { OhVueIcon, addIcons } from "oh-vue-icons";
import * as AllIcons from "oh-vue-icons/icons";
import { VueQueryPlugin } from "@tanstack/vue-query";

const AllIcon = Object.values({ ...AllIcons });
addIcons(...AllIcon);

const app = createApp(App);

app.use(router);
app.use(Antd);
app.use(VueQueryPlugin);
app.use(createPinia());
app.component("v-icon", OhVueIcon);

app.mount("#app");
