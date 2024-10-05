<template>
  <div>
    <a-modal :open="open" title="Chi Tiết Sử Dụng Chức Năng" @cancel="handleClose" destroyOnClose centered
      :footer="null" width="800px">
      <a-tabs style="max-height: 600px; overflow-y: auto;">
        <a-tab-pane key="currentInfo" tab="Thông tin hiện tại">
          <div v-if="props.isLoadingDetail" class="flex justify-center items-center">
            <a-spin />
          </div>
          <div v-else>
            <div class="shadow-md p-1 rounded-md my-15">
              <div>
                <a-descriptions class="p-1">
                  <a-descriptions-item :span="2" label="Tên người dùng">{{ props.operationLogDetail?.userName
                    }}</a-descriptions-item>
                  <a-descriptions-item :span="2" label="Mã người dùng">{{ props.operationLogDetail?.code }}</a-descriptions-item>
                  <a-descriptions-item :span="2" label="Email">{{ props.operationLogDetail?.email }}</a-descriptions-item>
                  <a-descriptions-item :span="2" label="Máy trạm">{{ props.operationLogDetail?.workstation
                    }}</a-descriptions-item>
                  <a-descriptions-item :span="2" label="Thời gian thực hiện">{{
                    getDateFormat(props.operationLogDetail?.createdDate, true) }}</a-descriptions-item>
                  <a-descriptions-item label="Chức năng"><a-tag
                      :color="getTagColorLog(props.operationLogDetail?.typeFunction)">{{
                        getTagStatusLog(props.operationLogDetail?.typeFunction)
                      }}</a-tag></a-descriptions-item>
                </a-descriptions>
              </div>
            </div>
          </div>
        </a-tab-pane>
        <a-tab-pane key="request" tab="Request">
          <div>
            <pre>{{ parsedRequest }}</pre>
          </div>
        </a-tab-pane>
        <a-tab-pane key="response" tab="Response">
          <div>
            <pre>{{ parsedResponse }}</pre>
          </div>
        </a-tab-pane>
      </a-tabs>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import { getDateFormat, getTagColorLog, getTagStatusLog } from "@/utils/common.helper";
import { defineProps, defineEmits, computed, watch } from "vue";

const props = defineProps({
  open: Boolean,
  operationLogDetail: Object as () => any | null,
  isLoadingDetail: Boolean,
});

const emit = defineEmits(["handleCloseModal"]);

// Ví dụ về text request và response
const parsedRequest = computed(() => {
  try {
    return JSON.stringify(JSON.parse(props.operationLogDetail?.request || ''), null, 2);
  } catch (e) {
    return '{}';
  }
});

const parsedResponse = computed(() => {
  try {
    return JSON.stringify(JSON.parse(props.operationLogDetail?.response || ''), null, 2);
  } catch (e) {
    return '{}';
  }
});


const handleClose = () => {
  emit("handleCloseModal");
};

watch(
  () => props.operationLogDetail,
  (newVal) => {
    if (newVal) {
      console.log(newVal)
    }
  },
  { immediate: true }
);
</script>

