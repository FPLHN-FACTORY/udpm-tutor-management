<template>
  <div class="shadow-xl p-3 m-3 rounded-md flex h-full flex-col overflow-auto">
    <div class="flex justify-between items-center min-h-36">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="bi-list-ul" scale="2" />
        <span class="ml-2 text-2xl">Danh sách kế hoạch</span>
      </h2>
    </div>
    <div class="flex h-0 flex-1 flex-col">
      <tutor-table
        wrapperClassName="min-h-[410px]"
        :columns="columnsSubject"
        :data-source="dataSource"
        :loading="loading"
        :pagination-params="paginationParams || {}"
        :total-pages="totalPages || 0"
        @update:pagination-params="$emit('update:paginationParams', $event)"
      >
        <template #bodyCell="{ column, record }">
          <div v-if="column.key === 'action'" class="space-x-2 flex items-center justify-center">
            <a-tooltip v-if="record.status === 'PLANNER_APPROVED'" title="Phê duyệt kế hoạch" color="#FFC26E">
              <a-button
                class="flex items-center justify-center"
                type="primary"
                size="large"
                @click="handleApprovePlan(record.id)"
                :icon="h(CheckCircleOutlined)"
              />
            </a-tooltip>
            <a-tooltip v-if="record.status === 'PLANNER_APPROVED'" title="Từ chối kế hoạch" color="#FFC26E">
              <a-button
                class="flex items-center justify-center"
                type="primary"
                size="large"
                @click="openRejectModal(record.id)"
                :icon="h(StopOutlined)"
              />
            </a-tooltip>
            <a-tooltip title="Chi tiết kế hoạch" color="#FFC26E">
              <a-button
                class="flex items-center justify-center"
                type="primary"
                size="large"
                @click="goToDetail(record.id)"
                :icon="h(EyeOutlined)"
              />
            </a-tooltip>
          </div>
          <div v-else-if="column.key === 'departmentName'" class="text-center">
            <p>{{ record.departmentName + " - " + record.facilityName }}</p>
          </div>
          <div v-else-if="column.key === 'status'" class="text-center">
            <a-tag :color="getTagColor(record.status)">{{ getTagStatus(record.status) }}</a-tag>
          </div>
          <div v-else-if="column.key === 'blockName'">
            <p>{{ formatBlockName(record.blockName) }}</p>
          </div>
          <div v-else-if="column.key === 'timeAdditionSubject'">
            <p>{{ getDateFormat(record.startTime, false) + ' - ' + getDateFormat(record.endTime, false) }}</p>
          </div>
        </template>
      </tutor-table>
    </div>

    <!-- Modal từ chối kế hoạch -->
    <a-modal
      title="Nhập lý do từ chối"
      v-model:open="isRejectModalOpen"
      @cancel="closeRejectModal"
      @ok="handleRejectPlan"
      ok-text="Xác nhận"
      cancel-text="Huỷ"
    >
      <a-input
        v-model:value="rejectReason"
        placeholder="Nhập lý do từ chối"
      />
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import TutorTable from "@/components/ui/TutorTable/TutorTable.vue";
import { CheckCircleOutlined, EyeOutlined, StopOutlined } from "@ant-design/icons-vue";
import { ColumnType } from "ant-design-vue/es/table";
import { h, ref } from "vue";
import { PlanResponse } from "@/services/api/headdepartment/plan.api.ts";
import { useRouter } from "vue-router";
import { confirmModal, formatBlockName, getDateFormat, getTagColor, getTagStatus } from "@/utils/common.helper.ts";
import { useApprovePlan, useRejectPlan } from "@/services/service/headdepartment/plan.action.ts";
import { toast } from "vue3-toastify";
import { ERROR_MESSAGE } from "@/constants/message.constant.ts";

defineProps({
  dataSource: Array as () => PlanResponse[],
  loading: Boolean,
  paginationParams: Object,
  totalPages: Number,
});

defineEmits([
  "update:paginationParams",
  "handleOpenModalUpdate",
  "handleOpenModalAdd",
]);

const router = useRouter();
const isRejectModalOpen = ref(false);
const rejectReason = ref("");
const currentPlanId = ref("");

const goToDetail = (planId: string) => {
  router.push({ name: 'hDPlDetailPlan', params: { planId } });
}

const { mutate: approvePlan } = useApprovePlan();
const { mutate: rejectPlan } = useRejectPlan();

const handleApprovePlan = (id: string) => {
  const message = 'Bạn chắc chắn muốn phê duyệt kế hoạch này chứ!'; // Thông điệp xác nhận

  confirmModal(message, () => {
    try {
      approvePlan(id, {
        onSuccess: () => {
          toast.success("Phê duyệt thành công!");
        },
        onError: (error: any) => {
          toast.error(
              error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
          )
        },
      });
    } catch (error: any) {
      console.log(error);
    }
  });
};

const openRejectModal = (id: string) => {
  currentPlanId.value = id;
  isRejectModalOpen.value = true;
};

const closeRejectModal = () => {
  isRejectModalOpen.value = false;
  rejectReason.value = ""; // Reset lý do
};

const handleRejectPlan = () => {
  const message = 'Bạn chắc chắn muốn từ chối kế hoạch này?'; // Thông điệp xác nhận

  confirmModal(message, async () => {
    try {
      rejectPlan({ planId: currentPlanId.value, reason: rejectReason.value }, {
        onSuccess: () => {
          toast.success("Từ chối thành công!");
          closeRejectModal(); // Đóng modal
        },
        onError: (error: any) => {
          toast.error(
            error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
          )
        },
      });
    } catch (error: any) {
      console.log(error);
    }
  });
};

const columnsSubject: ColumnType[] = [
  {
    title: "STT",
    dataIndex: "orderNumber",
    key: "index",
    ellipsis: true,
    width: "80px",
    align: "center",
  },
  {
    title: "Tên kế hoạch",
    dataIndex: "planName",
    key: "planName",
    ellipsis: true,
    width: "120px",
  },
  {
    title: "Block",
    dataIndex: "blockName",
    key: "blockName",
    ellipsis: true,
    width: "80px",
  },
  {
    title: "Bộ môn",
    dataIndex: "departmentName",
    key: "departmentName",
    ellipsis: true,
    width: "120px",
  },
  {
    title: "Số môn tutor",
    dataIndex: "numberSubjects",
    key: "numberSubjects",
    ellipsis: true,
    width: "100px",
    align: "center",
  },
  {
    title: "Thời gian thêm môn",
    dataIndex: "timeAdditionSubject",
    key: "timeAdditionSubject",
    ellipsis: true,
    width: "200px",
    align: "center",
  },
  {
    title: "Trạng thái",
    dataIndex: "status",
    key: "status",
    ellipsis: true,
    width: "150px",
    align: "center",
  },
  {
    title: "Hành động",
    key: "action",
    align: "center",
    width: "150px",
  },
];
</script>
