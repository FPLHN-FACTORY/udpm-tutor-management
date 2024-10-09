<script lang="ts" setup>
import { useAuthStore } from "@/stores/auth";
import {
  BookOutlined,
  BuildOutlined,
  CalendarOutlined,
  AuditOutlined,
  HomeOutlined,
  IdcardOutlined,
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  UserOutlined,
  LoginOutlined
} from "@ant-design/icons-vue";
import { computed, ref } from "vue";
import { useRoute, useRouter } from "vue-router";

const auth = useAuthStore();

const collapsed = ref<boolean>(false);

const route = useRoute();

const router = useRouter();

const itemsAdmin = computed(() => [
  {
    key: "1",
    icon: CalendarOutlined,
    text: "Quản lý học kỳ",
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
]);

const userInfo = computed(() => auth.user);

const selectedKeys = computed(() => {
  const currentPath = route.path;
  const selectedItem = itemsAdmin.value.find((item) =>
    item.path.includes(currentPath)
  );
  return selectedItem ? [selectedItem.key] : [];
});

const handleLogout = () => {
  auth.logout();
  router.push("/login");
};
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
      <a-menu
        v-model:selectedKeys="selectedKeys"
        theme="light"
        mode="inline"
        :trigger="null"
      >
        <a-menu-item
          v-for="item in itemsAdmin"
          :key="item.key"
          :title="item.text"
        >
          <component :is="item.icon" class="text-xl" />
          <router-link :to="item.path" class="ml-3 text-sm inline">
            {{ collapsed ? "" : item.text }}
          </router-link>
        </a-menu-item>
      </a-menu>
    </a-layout-sider>
    <a-layout>
      <a-layout-header class="bg-white pl-3 mt-1">
        <div class="user-info flex items-center justify-between">
          <div class="cursor-pointer" @click="collapsed = !collapsed">
            <component
              :is="collapsed ? MenuUnfoldOutlined : MenuFoldOutlined"
              class="text-xl"
            />
          </div>
          <a-dropdown placement="bottomRight" arrow>
            <div class="flex items-center cursor-pointer">
              <a-avatar
                v-if="userInfo?.pictureUrl"
                :src="userInfo?.pictureUrl"
                size="large"
              >
                {{ userInfo?.fullName[0] }}
              </a-avatar>
              <span class="ml-2 truncate">
                {{ userInfo?.fullName }}
              </span>
            </div>
            <template #overlay>
              <a-menu>
                <a-menu-item key="logout" @click="handleLogout">
                  Đăng xuất
                </a-menu-item>
              </a-menu>
            </template>
          </a-dropdown>
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
