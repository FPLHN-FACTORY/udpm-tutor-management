<template>
  <div class="shadow-xl p-3 m-3 rounded-md flex h-full flex-col overflow-auto">
    <div class="flex justify-between items-center min-h-36">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="bi-list-ul" scale="2" />
        <span class="ml-2 text-2xl">Danh sách học kỳ</span>
      </h2>
      <a-button
          type="primary"
          size="large"
          class="m-4 flex justify-between items-center"
          @click="handleSync"
          :disabled="isSyncing"
      >
        <v-icon name="bi-arrow-counterclockwise" scale="2" />
        Đồng bộ
      </a-button>
    </div>
    <div class="flex h-0 flex-1 flex-col">
      <tutor-table
        wrapperClassName="min-h-[410px]"
        :columns="columnsSemester"
        :data-source="dataSource"
        :loading="loading"
        :pagination-params="paginationParams || {}"
        :total-pages="totalPages || 0"
        @update:pagination-params="$emit('update:paginationParams', $event)"
      >
        <template #bodyCell="{ column, record }">
          <div v-if="column.key === 'semesterName'" class="space-x-2">
            <span>{{ record?.semesterName + " " + record?.semesterYear  }}</span>
          </div>
          <div v-if="column.key === 'startTime'" class="space-x-2">
            <span>{{ getDateFormat(record?.startTime) }}</span>
          </div>
          <div v-else-if="column.key === 'endTime'" class="space-x-2">
            <span>{{ getDateFormat(record?.endTime) }}</span>
          </div>
          <div v-else-if="column.key === 'firstBlock'" class="space-x-2">
            <span>{{ getDateFormat(record?.startTimeFirstBlock) + " - " + getDateFormat(record?.endTimeFirstBlock) }}</span>
          </div>
          <div v-else-if="column.key === 'secondBlock'" class="space-x-2">
            <span>{{ getDateFormat(record?.startTimeSecondBlock) + " - " + getDateFormat(record?.endTimeSecondBlock) }}</span>
          </div>
          <div v-if="column.key === 'action'" class="space-x-2 text-center flex items-center justify-center">
            <a-tooltip title="Chi tiết học kỳ" color="#FFC26E">
              <a-button class="flex items-center justify-center"
                        type="primary"
                        size="large"
                        :icon="h(EyeOutlined)"
                        @click="$emit('handleOpenModalDetail', record)" />
            </a-tooltip>
          </div>
        </template>
      </tutor-table>
    </div>
  </div>
</template>

<script lang="ts" setup>
import TutorTable from "@/components/ui/TutorTable/TutorTable.vue";
import { SemesterResponse } from "@/services/api/semester.api";
import { getDateFormat } from "@/utils/common.helper";
import { ColumnType } from "ant-design-vue/es/table";
import {h} from "vue";
import {EyeOutlined} from "@ant-design/icons-vue";
import {useSemesterSynchronize} from "@/services/service/semester.action.ts";
import {toast} from "vue3-toastify";
import {ERROR_MESSAGE} from "@/constants/message.constant.ts";
import {useQueryClient} from "@tanstack/vue-query";
import {queryKey} from "@/constants/queryKey.ts";

defineProps({
  dataSource: Array<SemesterResponse>,
  loading: Boolean,
  paginationParams: Object,
  totalPages: Number,
});
// Sử dụng useQueryClient để lấy queryClient
const queryClient = useQueryClient();

const emit = defineEmits(["update:paginationParams", "handleOpenModalDetail", "syncSuccess"]);

const { mutate: onSync, isLoading: isSyncing } = useSemesterSynchronize();

// Handle button click
const handleSync = async () => {
  try {
    await onSync(); // Chỉ gọi khi nhấn nút
    toast.success("Đồng bộ học kỳ và block thành công");

    // Chờ invalidate hoàn tất trước khi thực hiện refetch
    // Invalidating query và refetch ngay lập tức
    await queryClient.invalidateQueries({ queryKey: [queryKey.admin.semester.semesterList] });
    await queryClient.refetchQueries({ queryKey: [queryKey.admin.semester.semesterList] });

    emit('syncSuccess');
  } catch (error: any) {
    console.error("🚀 ~ handleSync ~ error:", error); // Log lỗi để dễ dàng debug
    toast.error(
        error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
    );
  }
};

const columnsSemester: ColumnType[] = [
  {
    title: "STT",
    dataIndex: "orderNumber",
    key: "orderNumber",
    ellipsis: true,
  },
  {
    title: "Tên học kỳ",
    dataIndex: "semesterName",
    key: "semesterName",
    ellipsis: true,
  },
  {
    title: "Ngày bắt đầu",
    dataIndex: "startTime",
    key: "startTime",
    ellipsis: true,
  },
  {
    title: "Ngày kết thúc",
    dataIndex: "endTime",
    key: "endTime",
    ellipsis: true,
  },
  {
    title: "Block 1",
    dataIndex: "firstBlock",
    key: "firstBlock",
    ellipsis: true,
  },
  {
    title: "Block 2",
    dataIndex: "secondBlock",
    key: "secondBlock",
    ellipsis: true,
  },
  {
    title: "Hành động",
    key: "action",
    align: "center",
    width: "200px",
  },
];
</script>
