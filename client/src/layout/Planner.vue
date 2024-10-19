<script lang="ts" setup>
import TutorNotification from "@/components/ui/TutorNotification/TutorNotification.vue";
import { useAuthStore } from "@/stores/auth.ts";
import {
  HistoryOutlined,
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  UserSwitchOutlined,
} from "@ant-design/icons-vue";
import { computed, ref } from "vue";
import { useRoute, useRouter } from "vue-router";

const auth = useAuthStore();

const userInfo = computed(() => auth.user);

const collapsed = ref<boolean>(false);

const route = useRoute();

const router = useRouter();

const itemsHeadDepartment = [
  {
    key: "1",
    icon: UserSwitchOutlined,
    text: "Quản lý kế hoạch",
    path: "/planner/plan",
  },
  {
    key: "2",
    icon: HistoryOutlined,
    text: "Quản lý lịch sử kế hoạch",
    path: "/planner/plan-log",
  },
];

const selectedKeys = computed(() => {
  const currentPath = route.path;
  const selectedItem = itemsHeadDepartment.find((item) =>
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
      <a-menu v-model:selectedKeys="selectedKeys" theme="light" mode="inline">
        <a-menu-item v-for="item in itemsHeadDepartment" :key="item.key">
          <component :is="item.icon" />
          <router-link :to="item.path" class="ml-2 text-sm">
            {{ item.text }}
          </router-link>
        </a-menu-item>
      </a-menu>
    </a-layout-sider>
    <a-layout class="gap-4">
      <a-layout-header class="bg-white pl-3 mt-1">
        <div class="user-info flex items-center justify-between">
          <div class="cursor-pointer" @click="collapsed = !collapsed">
            <component
              :is="collapsed ? MenuUnfoldOutlined : MenuFoldOutlined"
              class="text-xl"
            />
          </div>
          <div class="flex gap-x-2 items-center">
            <a-dropdown placement="bottomRight" arrow>
              <div class="flex items-center cursor-pointer h-8">
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
            <tutor-notification :role="'NGUOI_LAP_KE_HOACH'" />
          </div>
        </div>
      </a-layout-header>
      <a-layout-content class="mx-4 bg-white">
        <router-view />
      </a-layout-content>
      <a-layout-footer class="text-center">
        FPL - UDPM ©2021 Created by BIT
      </a-layout-footer>
    </a-layout>
  </a-layout>
</template>

<style scoped></style>
