<template>
  <div class="mt-5 rounded-md flex h-full flex-col">
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
          <div v-if="column.key === 'format'" class="text-center">
            <a-tag :color="getTagFormat(record.format)"> {{record.format}}</a-tag>
          </div>
        </template>
      </tutor-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import TutorTable from "@/components/ui/TutorTable/TutorTable.vue";
import { ColumnType } from "ant-design-vue/es/table";
import { TutorClassResponse } from "@/services/api/headdepartment/plan.api.ts";
import {getTagFormat} from "@/utils/common.helper.ts";

defineProps({
  dataSource: Array<TutorClassResponse>,
  loading: Boolean,
  paginationParams: Object,
  totalPages: Number,
  plan: Object as () => any | null,
  canUpdate: Boolean
});

defineEmits([
  "update:paginationParams",
]);

const columnsSubject: ColumnType[] = [
  {
    title: "STT",
    dataIndex: "orderNumber",
    key: "index",
    ellipsis: true,
    width: "50px",
  },
  {
    title: "Mã môn học",
    dataIndex: "subjectCode",
    key: "subjectCode",
    ellipsis: true,
    width: "250px",
  },
  {
    title: "Tên môn học",
    dataIndex: "subjectName",
    key: "subjectName",
    ellipsis: true,
    width: "250px",
  },
  {
    title: "Số lớp",
    dataIndex: "numberClasses",
    key: "numberClasses",
    ellipsis: true,
    align: "center",
  },
  {
    title: "Số buổi",
    dataIndex: "numberLectures",
    key: "numberLectures",
    ellipsis: true,
    align: "center",
  },
  {
    title: "Hình thức",
    dataIndex: "format",
    key: "format",
    ellipsis: true,
    width: "200px",
    align: "center",
  },
];
</script>
