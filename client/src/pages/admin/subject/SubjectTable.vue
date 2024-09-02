<template>
  <div class="shadow-xl p-3 m-3 rounded-md flex h-full flex-col overflow-auto">
    <div class="flex justify-between items-center min-h-36">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="bi-list-ul" scale="2" />
        <span class="ml-2 text-2xl">Danh sách môn học</span>
      </h2>
      <a-button
        type="primary"
        @click="$emit('handleOpenModalAdd')"
        size="large"
        class="m-4"
      >
        Tạo môn học
      </a-button>
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
          <div v-if="column.key === 'action'" class="space-x-2 text-center">
            <a-tooltip title="Chỉnh sửa môn học" color="#FFC26E">
              <a-button
                type="primary"
                size="large"
                @click="$emit('handleOpenModalUpdate', record)"
                :icon="h(EditOutlined)"
              />
            </a-tooltip>
          </div>
          <div v-else-if="column.key === 'subjectType'" class="text-center">
            <a-tag color="warning">{{ record.subjectType }}</a-tag>
          </div>
          <div v-else-if="column.key === 'createdDate'" class="text-center">
            {{ getDateFormat(record.createdDate, false) }}
          </div>
        </template>
      </tutor-table>
      </div>
  </div>
</template>

<script setup lang="ts">
import TutorTable from "@/components/ui/TutorTable/TutorTable.vue";
import { SubjectResponse } from "@/services/api/subject.api";
import { EditOutlined } from "@ant-design/icons-vue";
import { ColumnType } from "ant-design-vue/es/table";
import { getDateFormat } from "@/utils/common.helper";
import { h } from "vue";

defineProps({
  dataSource: Array<SubjectResponse>,
  loading: Boolean,
  paginationParams: Object,
  totalPages: Number,
});

defineEmits([
  "update:paginationParams",
  "handleOpenModalUpdate",
  "handleOpenModalAdd",
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
    title: "Bộ môn",
    dataIndex: "departmentName",
    key: "departmentName",
    ellipsis: true,
    width: "120px",
  },
  {
    title: "Loại môn học",
    dataIndex: "subjectType",
    key: "subjectType",
    ellipsis: true,
    width: "200px",
    align: "center",
  },
  {
    title: "Ngày tạo",
    dataIndex: "createdDate",
    key: "createdDate",
    ellipsis: true,
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
