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
              <v-icon
                name="fc-google"
                v-if="urlRedirect === '' || isLoginProcessing"
                class="mr-2"
              />
              <FcGoogle v-else class="mr-2" />
              <span>Đăng nhập với Google</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import backgroundImage from "/images/bg-simple.jpg";
import logo from "/images/Logo_FPT.png";
import logoUDPM from "/images/logo-udpm-dark.png";
export default {
  data() {
    return {
      backgroundImage,
      logo,
      logoUDPM,
      urlRedirect: "",
      isLoginProcessing: false,
    };
  },
  methods: {
    handleLogin() {
      // Hàm xử lý đăng nhập
    },
  },
  computed: {
    buttonStyleLogin() {
      return {
        backgroundColor: "#253741",
        color: "#fff",
        borderRadius: "5px",
        fontSize: "16px",
        boxShadow: "0 0 4px #253741",
      };
    },
  },
};
</script>

<style scoped></style>
