<script setup lang="ts">
import themeConfig from "./theme/themeConfig";
import { VueQueryDevtools } from "@tanstack/vue-query-devtools";
</script>

<template>
  <vue-query-devtools />
  <a-config-provider :theme="themeConfig">
    <router-view />
  </a-config-provider>
</template>
