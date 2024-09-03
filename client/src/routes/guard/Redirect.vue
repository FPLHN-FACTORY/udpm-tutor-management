<template>
  <div>
    <p>Redirecting...</p>
  </div>
</template>

<script setup lang="ts">
import { useAuthStore } from "@/stores/auth";
import { getUserInformation } from "@/utils/token.helper";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();

const state = route.query.state;
if (state) {
  const decodedState = atob(state as string);
  const { accessToken, refreshToken } = JSON.parse(decodedState);

  const userData = getUserInformation(accessToken);

  authStore.login(userData, {
    accessToken,
    refreshToken,
  });

  router.push({ name: "Dashboard" });
} else {
  router.push({ name: "Login" });
}
</script>
