<template>
  <div
    class="bg-gray-200 bg-cover h-screen"
    :style="{ backgroundImage: `url(${backgroundImage})` }"
  >
    <div
      class="flex justify-center items-center"
      :style="{ height: 'calc(100vh - 64px)' }"
    >
      <div class="card border-0">
        <div>
          <div class="my-4 flex justify-center items-center">
            <img :src="logo" alt="Logo" class="mb-2 w-48" />
            <img :src="logoUDPM" alt="Logo" class="mb-2 w-48" />
          </div>
          <div class="flex justify-center items-center flex-col">
            <button
              @click="handleLogin"
              :style="buttonStyleLogin"
              type="button"
              class="mt-5 flex justify-center items-center p-3"
              :disabled="isLoginProcessing"
            >
              <v-icon name="fc-google" class="mr-2" />
              <span>Đăng nhập với Google</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { URL_FRONTEND, URL_OAUTH2_GOOGLE } from "@/constants/url";
import { computed, ref } from "vue";
import backgroundImage from "/images/bg-simple.jpg";
import logoUDPM from "/images/logo-udpm-dark.png";
import logo from "/images/Logo_FPT.png";

const isLoginProcessing = ref(false);

const handleLogin = () => {
  console.log(
    "URL_OAUTH2_GOOGLE + URL_FRONTEND",
    URL_OAUTH2_GOOGLE + URL_FRONTEND
  );
  window.location.href = URL_OAUTH2_GOOGLE + URL_FRONTEND;
};

const buttonStyleLogin = computed(() => ({
  backgroundColor: "#253741",
  color: "#fff",
  borderRadius: "5px",
  fontSize: "16px",
  boxShadow: "0 0 4px #253741",
}));
</script>

<style scoped></style>
