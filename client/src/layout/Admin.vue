<script lang="ts" setup>
import { ROUTES_CONSTANTS } from "@/constants/path";
import { useAuthStore } from "@/stores/auth";
import {
  BookOutlined,
  BuildOutlined,
  CalendarOutlined,
  IdcardOutlined,
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  UserOutlined,
} from "@ant-design/icons-vue";
import { throttle } from "lodash";
import { computed, ref } from "vue";
import { useRoute, useRouter } from "vue-router";

const auth = useAuthStore();

const collapsed = ref<boolean>(false);

const sidebarWidth = ref<number>(250);

const resizing = ref<boolean>(false);

const route = useRoute();

const router = useRouter();

const itemsAdmin = computed(() => [
  {
    key: "1",
    icon: CalendarOutlined,
    text: "Quản lý học kỳ",
    path: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.SEMESTER.path}`,
  },
  {
    key: "2",
    icon: IdcardOutlined,
    text: "Quản lý chức vụ",
    path: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.ROLE.path}`,
  },
  {
    key: "3",
    icon: UserOutlined,
    text: "Quản lý nhân viên",
    path: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.STAFF.path}`,
  },
  {
    key: "4",
    icon: BookOutlined,
    text: "Quản lý môn học",
    path: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.SUBJECT.path}`,
  },
  {
    key: "5",
    icon: BuildOutlined,
    text: "Quản lý bộ môn",
    path: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.DEPARTMENT.path}`,
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

const startResizing = (event: MouseEvent) => {
  event.preventDefault();
  resizing.value = true;
  document.addEventListener("mousemove", throttledResizeSidebar);
  document.addEventListener("mouseup", stopResizing);
};

const throttledResizeSidebar = throttle((event: MouseEvent) => {
  if (resizing.value) {
    const newWidth = Math.min(Math.max(event.clientX, 200), 400);
    sidebarWidth.value = newWidth;
  }
}, 50);

const stopResizing = () => {
  if (resizing.value) {
    console.log("Stop Resizing");
    resizing.value = false;
    document.removeEventListener("mousemove", throttledResizeSidebar);
    document.removeEventListener("mouseup", stopResizing);
  }
};
</script>

<template>
  <a-layout class="min-h-screen">
    <a-layout-sider
      v-model:collapsed="collapsed"
      :width="collapsed ? 80 : sidebarWidth"
      class="bg-white relative"
    >
      <div class="logo">
        <a href="/" class="logo">
          <img
            src="/images/logo-udpm-dark.png"
            alt="logo-udpm"
            class="p-2 mt-3"
            :width="collapsed ? 200 : 200"
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
      <div class="resizable-handle" @mousedown="startResizing"></div>
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
          <a-dropdown arrow placement="bottomRight">
            <div class="flex items-center cursor-pointer h-8">
              <a-avatar
                v-if="userInfo?.pictureUrl"
                :src="userInfo?.pictureUrl"
                size="large"
              >
                {{ userInfo?.fullName[0] }}
              </a-avatar>
              <span class="ml-2 truncate">{{ userInfo?.fullName }}</span>
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
      <a-layout-content class="mx-4 bg-white">
        <router-view />
      </a-layout-content>
      <a-layout-footer class="text-center">
        FPL - UDPM ©2021 Created by BIT
      </a-layout-footer>
    </a-layout>
  </a-layout>
</template>

<style scoped>
.resizable-handle {
  width: 3px;
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  background-color: #e0e0e0;
  cursor: col-resize;
}
</style>
