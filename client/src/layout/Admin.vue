<script lang="ts" setup>
import {
  BookOutlined,
  BuildOutlined,
  CalendarOutlined,
  HomeOutlined,
  IdcardOutlined,
  UserOutlined,
} from "@ant-design/icons-vue";
import { computed, ref } from "vue";
import { useRoute } from "vue-router";

const collapsed = ref<boolean>(false);
const route = useRoute();

const itemsAdmin = [
  {
    key: "1",
    icon: CalendarOutlined,
    text: "Quản lý học kỳ - block",
    path: "/admin/semester",
  },
  {
    key: "2",
    icon: IdcardOutlined,
    text: "Quản lý chức vụ",
    path: "/admin/role",
  },
  {
    key: "3",
    icon: UserOutlined,
    text: "Quản lý nhân viên",
    path: "/admin/staff",
  },
  {
    key: "4",
    icon: BookOutlined,
    text: "Quản lý môn học",
    path: "/admin/subject",
  },
  {
    key: "5",
    icon: HomeOutlined,
    text: "Quản lý cơ sở",
    path: "/admin/facility",
  },
  {
    key: "6",
    icon: BuildOutlined,
    text: "Quản lý bộ môn",
    path: "/admin/department",
  },
];

const selectedKeys = computed(() => {
  const currentPath = route.path;
  const selectedItem = itemsAdmin.find((item) =>
    item.path.includes(currentPath)
  );
  return selectedItem ? [selectedItem.key] : [];
});
</script>

<template>
  <a-layout class="min-h-screen">
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
          <router-link :to="item.path" class="ml-2 text-sm">
            {{ item.text }}
          </router-link>
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
        <div class="min-h-[calc(100vh-9.5rem)] bg-white">
          <router-view />
        </div>
      </a-layout-content>
      <a-layout-footer class="text-center">
        FPL - UDPM ©2021 Created by BIT
      </a-layout-footer>
    </a-layout>
  </a-layout>
</template>

<style scoped></style>
