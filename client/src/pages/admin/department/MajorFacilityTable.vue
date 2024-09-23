<template>
  <div class="shadow-xl p-3 rounded-md flex h-full flex-col overflow-auto">
    <div class="flex justify-between items-center min-h-[56px]">
      <h2 class="flex items-center text-primary text-3xl font-semibold p-2">
        <span class="text-xl">Danh Sách Chuyên Ngành Thuộc Bộ Môn Cơ Sở</span>
      </h2>
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
import { MajorFacilityResponse } from "@/services/api/admin/major.api";
import { ColumnType } from "ant-design-vue/es/table";

const props = defineProps({
  dataSource: Array as () => MajorFacilityResponse[],
  loading: Boolean,
  paginationParams: Object as () => any,
  totalPages: Number,
});

const emit = defineEmits([
  "update:paginationParams",
]);

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
  {
    title: "Trạng thái",
    dataIndex: "status",
    key: "status",
    ellipsis: true,
  },
];
</script>
