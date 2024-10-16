<template>
  <a-dropdown :trigger="['click']" v-model:open="visible">
    <a-badge :count="totalNotify" :overflow-count="5">
      <BellOutlined @click.prevent class="text-lg" />
    </a-badge>
    <template #overlay>
      <a-menu class="min-w-96">
        <div class="my-2 ml-2">
          <span class="text-xl font-semibold">Thông báo</span>
        </div>
        <div class="max-h-[480px] overflow-auto">
          <div v-for="item in notificationData" :key="item.id">
            <a-menu-item
              :class="item.status ? '' : 'hover:bg-blue-200 bg-blue-100'"
              @click="handleReadNotification(item)"
            >
              <div class="flex justify-between items-center">
                <div class="flex gap-x-3">
                  <div class="size-12 rounded-full overflow-hidden">
                    <img
                      class="size-full object-cover"
                      src="/images/plan.jpg"
                    />
                  </div>
                  <div>
                    <p class="break-words w-80">{{ item.content }}</p>
                    <small
                      :class="item.status ? '' : 'text-blue-700 font-bold'"
                    >
                      {{ getDateFormat(item.createdDate, true) }}
                    </small>
                  </div>
                </div>
                <div
                  class="size-2 rounded-full bg-blue-700"
                  v-if="!item.status"
                ></div>
              </div>
            </a-menu-item>
            <a-menu-divider />
          </div>
        </div>
        <a-menu-item v-if="notificationData.length == 0" class="flex">
          <span class="m-auto">Bạn chưa có thông báo nào.</span>
        </a-menu-item>
        <a-menu-item v-if="notificationData.length > 5">
          <div class="flex">
            <a-button
              :loading="isLoading || isFetching"
              class="m-auto"
              @click="handleShowMoreNotification"
              >Hiển thị thêm thông báo</a-button
            >
          </div>
        </a-menu-item>
      </a-menu>
    </template>
  </a-dropdown>
</template>
<script lang="ts" setup>
import { useStomp } from "@/composable/useStomp";
import { WEBSOCKET_TOPIC } from "@/constants/common";
import { VITE_BASE_URL_SERVER } from "@/constants/url";
import {
  NotificationResponse,
  ParamsGetNotification,
  ParamsReadNotification,
} from "@/services/api/notification/notification.api";
import {
  useGetCountNotifications,
  useGetNotifications,
  useReadNotification,
} from "@/services/service/notification/notification.action";
import { useAuthStore } from "@/stores/auth";
import { getDateFormat } from "@/utils/common.helper";
import { BellOutlined } from "@ant-design/icons-vue";
import { keepPreviousData } from "@tanstack/vue-query";
import { computed, onMounted, ref } from "vue";

const { keyWord } = defineProps({
  keyWord: String,
});

const { user, accessToken } = useAuthStore();

const { messages, subscribe } = useStomp(
  `${VITE_BASE_URL_SERVER}/ws`,
  accessToken as string
);

const visible = ref(false);

const params = ref<ParamsGetNotification>({
  page: 1,
  size: 5,
  keyWord: keyWord || "",
  facilityCode: user?.facilityCode || "",
  departmentCode: user?.departmentCode || "",
  userId: user?.userId || "",
});

const paramsRead = ref<ParamsReadNotification>({
  userId: user?.userId || "",
});

const {
  data: listNotificationData,
  refetch: refetchNotificationData,
  isLoading,
  isFetching,
} = useGetNotifications(params, {
  refetchOnWindowFocus: false,
  placeholderData: keepPreviousData,
});

const { data: totalNotificationData, refetch: refetchCountNotification } =
  useGetCountNotifications(params, {
    refetchOnWindowFocus: false,
    placeholderData: keepPreviousData,
  });

const handleShowMoreNotification = () => {
  params.value = { ...params.value, size: params.value.size + 5 };
};

const { mutate: readNotification } = useReadNotification();

const handleReadNotification = (record: NotificationResponse) => {
  //Chỉ đọc những thông báo chưa đọc
  if (!record.status) {
    readNotification({ id: record.id, params: paramsRead });
    refetchNotificationData();
    refetchCountNotification();
  }
};

const notificationData = computed(
  () => listNotificationData.value?.data.data || []
);

const totalNotify = computed(() => totalNotificationData.value?.data || null);

subscribe(WEBSOCKET_TOPIC.NOTIFICATION, (message) => {
  if (message.roles.includes(keyWord)) {
    refetchNotificationData();
    refetchCountNotification();
  }
  messages.value.push(message);
});

onMounted(() => {
  if (keyWord === "GIANG_VIEN") {
    params.value = { ...params.value, keyWord: user?.userId || "" };
  }
});
</script>
