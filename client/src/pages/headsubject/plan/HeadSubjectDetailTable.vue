<template>
    <div class="mt-10 rounded-md flex h-full flex-col">
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
              <a-tooltip title="Nhập số lượng lớp tutor" color="#FFC26E">
                <a-button
                    class="flex items-center justify-center"
                    type="primary"
                    size="large"
                    @click="$emit('handleOpenModalAddNumberTutorClass', record)"
                    :icon="h(EditOutlined)"
                    :disabled="canUpdate"
                />
              </a-tooltip>
            </div>
            <div v-if="column.key === 'format'" class="text-center">
              <a-tag v-if="record.format === 0" color="success">ONLINE</a-tag>
              <a-tag v-else-if="record.format === 1" color="warning">OFFLINE</a-tag>
            </div>
          </template>
         </tutor-table>
        </div>
    </div>
  </template>

<script setup lang="ts">
import TutorTable from "@/components/ui/TutorTable/TutorTable.vue";
import { EditOutlined } from "@ant-design/icons-vue";
import { ColumnType } from "ant-design-vue/es/table";
import { h } from "vue";
import { TutorClassResponse } from "@/services/api/headsubject/plan.api.ts";

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
  "handleOpenModalAddNumberTutorClass",
  "handleApproveTutorClass"
]);

const columnsSubject: ColumnType[] = [
  {
      title: "STT",
      dataIndex: "orderNumber",
      key: "index",
      ellipsis: true,
  },
  {
    title: "Mã môn học",
    dataIndex: "subjectCode",
    key: "subjectCode",
    ellipsis: true,
  },
  {
    title: "Tên môn học",
    dataIndex: "subjectName",
    key: "subjectName",
    ellipsis: true,
  },
  {
    title: "Số lớp",
    dataIndex: "numberClasses",
    key: "numberClasses",
    ellipsis: true,
  },
  {
    title: "Số buổi",
    dataIndex: "numberLectures",
    key: "numberLectures",
    ellipsis: true,
    width: "200px",
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
  {
    title: "Hành động",
    key: "action",
    align: "center",
    width: "150px",
  },
];
</script>
