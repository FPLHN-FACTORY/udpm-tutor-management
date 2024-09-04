<template>
  <div class="background-permission">
    <div class="content-container">
      <div class="logo-container">
        <img src="/images/Logo_FPT.png" alt="Ảnh logo FPT" class="logo-image" />
        <img
          src="/images/logo-udpm-dark.png"
          alt="Ảnh logo UDPM"
          class="logo-image"
        />
      </div>
      <div class="roles-container">
        <div
          v-for="role in filteredRoles"
          :key="role.code"
          class="role-card flex flex-col"
        >
          <img
            :src="role.image"
            :alt="'Ảnh ' + role.displayName"
            class="permission-image"
            @click="navigateToRole(role)"
          />
          <a-button
            type="primary"
            class="btn-primary"
            @click="navigateToRole(role)"
          >
            <span class="truncate">{{ role.displayName }}</span>
          </a-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ROLES } from "@/constants/roles";
import { useAuthStore } from "@/stores/auth";
import { computed, onMounted } from "vue";
import { useRouter } from "vue-router";

const roleMappings = computed(() => [
  {
    code: ROLES.ADMIN,
    displayName: "Admin",
    image: "/images/BanDaoTao.png",
    route: "/admin",
  },
  {
    code: ROLES.HEAD_DEPARTMENT,
    displayName: "Chủ nhiệm bộ môn",
    image: "/images/TM_CNBM.png",
    route: "/head-department",
  },
  {
    code: ROLES.HEAD_DEPARTMENT,
    displayName: "Trưởng môn",
    image: "/images/TM_CNBM.png",
    route: "/head-subject",
  },
  {
    code: ROLES.TEACHER,
    displayName: "Giảng viên",
    image: "/images/GV.png",
    route: "/teacher",
  },
]);

const authStore = useAuthStore();

const router = useRouter();

const filteredRoles = computed(() => {
  if (!authStore.user) return [];
  return roleMappings.value.filter((role) =>
    authStore?.user?.rolesCodes.includes(role.code)
  );
});

const navigateToRole = (role: { route: string }) => {
  router.push(role.route);
};

onMounted(() => {
  if (!authStore.isAuthenticated) {
    router.push({ name: "login" });
  }
});
</script>

<style scoped>
.background-permission {
  height: 100vh;
  position: relative;
  width: 100%;
}

.background-permission:before {
  content: "";
  background: url(/images/bg-simple.jpg) no-repeat center center / cover;
  filter: blur(1px);
  height: 100%;
  position: absolute;
  width: 100%;
  opacity: 0.8;
  z-index: -1;
}

.content-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: calc(100vh - 120px);
  padding: 10px 40px;
}

.logo-container {
  display: flex;
  justify-content: space-between;
  width: 100%;
  max-width: 300px;
}

.logo-image {
  width: 55%;
}

.roles-container {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 20px;
  margin-top: 40px;
}

.role-card {
  text-align: center;
  cursor: pointer;
}

.role-card:hover .permission-image {
  transform: scale(1.1);
}

.permission-image {
  width: 200px;
  transition: transform 0.3s ease;
}

.btn-primary {
  display: block;
  margin-top: 10px;
}

@media (max-width: 768px) {
  .roles-container {
    flex-direction: column;
    align-items: center;
  }

  .permission-image {
    width: 150px;
  }

  .btn-primary {
    max-width: 150px;
    font-size: 14px;
  }
}

@media (max-width: 576px) {
  .permission-image {
    width: 120px;
  }

  .btn-primary {
    max-width: 120px;
    font-size: 12px;
  }
}
</style>
