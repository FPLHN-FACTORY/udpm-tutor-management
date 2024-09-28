<template>
    <div class="mt-10 rounded-md flex h-full flex-col">
      <h2 class="text-center text-xl font-semibold mb-7">Danh sách môn học</h2>
        <div class="flex h-0 flex-1 flex-col">
          <tutor-table
          wrapperClassName="min-h-[410px]"
          :columns="columnsTutorClassDetail"
          :data-source="dataSource"
          :loading="loading"
          :pagination-params="paginationParams || {}"
          :total-pages="totalPages || 0"
          @update:pagination-params="$emit('update:paginationParams', $event)"
        >
          <template #bodyCell="{ column, record }">
            <div v-if="column.key === 'action'" class="space-x-2 flex items-center justify-center">
        
            </div>
            <div v-else-if="column.key === 'format'" class="text-center">
              <p>{{ getFormatted(record.format) }}</p>
            </div>
            <div v-if="column.key === 'status'" class="text-center">
            <a-tag v-if="record.status === 0" color="success">Đã duyệt</a-tag>
            <a-tag v-else-if="record.status === 1" color="error">Chưa duyệt</a-tag>
          </div>
          </template>
         </tutor-table>
        </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import TutorTable from "@/components/ui/TutorTable/TutorTable.vue";
  import { ColumnType } from "ant-design-vue/es/table";
  import {getFormatted} from "@/utils/common.helper.ts";
import { TutorClassDetailResponse } from "@/services/api/headsubject/headsubject.api";

  defineProps({
    dataSource: Array<TutorClassDetailResponse>,
    loading: Boolean,
    paginationParams: Object,
    totalPages: Number,
  });
  
  defineEmits([
    "update:paginationParams",
    "handleOpenModalAddNumberTutorClass",
    "handlApproveTutorClass"
  ]);
  
  const columnsTutorClassDetail: ColumnType[] = [
    {
      title: "STT",
      dataIndex: "orderNumber",
      key: "index",
      ellipsis: true,
    },
    {
      title: "Tên môn học",
      dataIndex: "nameTutorClass",
      key: "nameTutorClass",
      ellipsis: true,
    },
    {
      title: "Số buổi",
      dataIndex: "numberOfLectures",
      key: "numberOfLectures",
      ellipsis: true,
    },
    {
      title: "Hành động",
      key: "action",
      align: "center",
      width: "150px",
    },
  ];
  </script>
  