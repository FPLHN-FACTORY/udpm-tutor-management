<template>
  <div class="shadow-xl p-3 rounded-md flex h-full flex-col overflow-auto">
    <div class="flex justify-between items-center min-h-[56px]">
      <h2 class="flex items-center text-primary text-3xl font-semibold p-2">
        <span class="text-xl">Danh Sách Chuyên Ngành</span>
      </h2>
      <!-- <a-button
        type="primary"
        size="large"
        class="m-4 flex justify-between items-center"
        @click="handleSync"
        :disabled="isSyncing"
    >
      <v-icon name="bi-arrow-repeat" scale="1.5" class="me-1" />
      Đồng bộ
    </a-button> -->
    </div>
    <div class="flex h-0 flex-1 flex-col">
      <tutor-table
        wrapperClassName="min-h-[410px]"
        :columns="columnsMajorFacility"
        :data-source="dataSource"
        :loading="loading"
        :pagination-params="paginationParams || {}"
        :total-pages="totalPages || 0"
        @update:pagination-params="$emit('update:paginationParams', $event)"
      >
        <template #bodyCell="{ column, record }">
          <div v-if="column.key === 'status'" class="text-center">
            <a-tag v-if="record.status === 0" color="success">Hoạt động</a-tag>
            <a-tag v-else-if="record.status === 1" color="error">Không hoạt động</a-tag>
          </div>
        </template>
      </tutor-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import TutorTable from "@/components/ui/TutorTable/TutorTable.vue";
import { ERROR_MESSAGE } from "@/constants/message.constant";
import { queryKey } from "@/constants/queryKey";
import { MajorFacilityResponse } from "@/services/api/admin/major.api";
import { useMajorCampusSynchronize } from "@/services/service/admin/major.action";
import { useQueryClient } from "@tanstack/vue-query";
import { ColumnType } from "ant-design-vue/es/table";
import { toast } from "vue3-toastify";

defineProps({
  dataSource: Array as () => MajorFacilityResponse[],
  loading: Boolean,
  paginationParams: Object as () => any,
  totalPages: Number,
});

const emit = defineEmits([
  "update:paginationParams"
]);

const queryClient = useQueryClient();

const { mutate: onSync, isLoading: isSyncing } = useMajorCampusSynchronize();

const handleSync = async () => {
  try {
    await onSync();
    
    await queryClient.invalidateQueries({ queryKey: [queryKey.admin.majorFacility.majorFacilityList] });
    await queryClient.refetchQueries({ queryKey: [queryKey.admin.majorFacility.majorFacilityList] });

  } catch (error: any) {
    console.error("🚀 ~ handleSync ~ error:", error);
    toast.error(
        error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
    );
  }
};

const columnsMajorFacility: ColumnType[] = [
  {
    title: "STT",
    dataIndex: "orderNumber",
    key: "index",
    ellipsis: true,
  },
  {
    title: "Chuyên ngành",
    dataIndex: "majorName",
    key: "majorName",
    ellipsis: true,
  },
  {
    title: "Chủ nhiệm bộ môn",
    dataIndex: "headMajorCodeName",
    key: "headMajorCodeName",
    ellipsis: true,
  },
];
</script>
