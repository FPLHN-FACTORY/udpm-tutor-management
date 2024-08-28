<script lang="ts" setup>
import {
  BookOutlined,
  BuildOutlined,
  CalendarOutlined,
  HomeOutlined,
  IdcardOutlined,
  UserOutlined,
} from "@ant-design/icons-vue";
import { ref } from "vue";
const collapsed = ref<boolean>(false);
const selectedKeys = ref<string[]>(["1"]);

const itemsAdmin = [
  {
    key: "1",
    icon: CalendarOutlined,
    text: "Quản lý học kỳ - block",
  },
  {
    key: "2",
    icon: IdcardOutlined,
    text: "Quản lý chức vụ",
  },
  {
    key: "3",
    icon: UserOutlined,
    text: "Quản lý nhân viên",
  },
  {
    key: "4",
    icon: BookOutlined,
    text: "Quản lý môn học",
  },
  {
    key: "5",
    icon: HomeOutlined,
    text: "Quản lý cơ sở",
  },
  {
    key: "6",
    icon: BuildOutlined,
    text: "Quản lý bộ môn",
  },
];
</script>

<template>
  <a-layout style="min-height: 100vh">
    <a-layout-sider v-model:collapsed="collapsed" class="bg-white">
      <div class="logo">
        <a href="/" class="logo">
          <img
            src="/images/logo-udpm-dark.png"
            alt="logo-udpm"
            class="p-2 mt-3"
          />
        </a>
      </div>
      <a-menu v-model:selectedKeys="selectedKeys" theme="light" mode="inline">
        <a-menu-item v-for="item in itemsAdmin" :key="item.key">
          <component :is="item.icon" />
          <span class="text-sm">
            <a-tooltip :title="item.text" placement="right">
              {{ item.text }}
            </a-tooltip>
          </span>
        </a-menu-item>
      </a-menu>
    </a-layout-sider>
    <a-layout>
      <a-layout-header class="bg-white">
        <div class="user-info flex items-center justify-end">
          <user-outlined />
          <span class="ml-2">Admin</span>
        </div>
      </a-layout-header>
      <a-layout-content class="mx-4">
        <div class="min-h-[calc(100vh-9.5rem)] bg-white mt-4">
          <router-view />
        </div>
      </a-layout-content>
      <a-layout-footer style="text-align: center">
        FPL - UDPM ©2021 Created by BIT
      </a-layout-footer>
    </a-layout>
  </a-layout>
</template>

<style scoped></style>
