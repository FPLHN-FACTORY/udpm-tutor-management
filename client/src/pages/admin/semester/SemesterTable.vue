<template>
  <div class="shadow-xl p-3 m-3 rounded-md flex h-full flex-col overflow-auto">
    <div class="flex justify-between items-center min-h-36">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="bi-list-ul" scale="2" />
        <span class="ml-2 text-2xl">Danh sách học kỳ</span>
      </h2>
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
          <div v-if="column.key === 'startTime'" class="space-x-2">
            <span>{{ getDateFormat(record?.startTime) }}</span>
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

defineProps({
  dataSource: Array<SemesterResponse>,
  loading: Boolean,
  paginationParams: Object,
  totalPages: Number,
});

defineEmits(["update:paginationParams"]);

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
    title: "Năm học",
    dataIndex: "semesterYear",
    key: "semesterYear",
    ellipsis: true,
  },
  {
    title: "Ngày bắt đầu",
    dataIndex: "startTime",
    key: "startTime",
    ellipsis: true,
  },
];
</script>
