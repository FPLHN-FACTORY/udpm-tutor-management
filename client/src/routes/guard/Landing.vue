<template>
  <a-spin size="large" />
</template>

<script lang="ts" setup>
import { ROUTES_CONSTANTS } from "@/constants/path";
import { useAuthStore } from "@/stores/auth";
import { getExpireTime } from "@/utils/token.helper";
import dayjs from "dayjs";
import { onMounted } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();

const { accessToken } = useAuthStore();

onMounted(() => {
  if (!accessToken) {
    router.push({ name: ROUTES_CONSTANTS.LOGIN.name });
    return;
  }

  const expiredTimeAccessToken = getExpireTime(accessToken);

  if (dayjs().isAfter(expiredTimeAccessToken * 1000)) {
    router.push({ name: ROUTES_CONSTANTS.LOGIN.name });
    return;
  }

  router.push({ name: ROUTES_CONSTANTS.ROLE_SWITCH.name });
});
</script>
