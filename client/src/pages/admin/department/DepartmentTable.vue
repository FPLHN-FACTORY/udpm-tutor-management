<template>
  <div class="shadow-xl p-3 m-3 rounded-md flex h-full flex-col overflow-auto">
    <div class="flex justify-between items-center min-h-36">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="bi-list-ul" scale="2" />
        <span class="ml-2 text-2xl">Danh sách bộ môn</span>
      </h2>
      <a-button
          type="primary"
          @click="$emit('handleOpenModalAdd')"
          size="large"
          class="m-4"
      >
        Tạo bộ môn
      </a-button>
    </div>
    <div class="flex h-0 flex-1 flex-col">
      <tutor-table
          wrapperClassName="min-h-[410px]"
          :columns="columnsDepartment"
          :data-source="dataSource"
          :loading="loading"
          :pagination-params="paginationParams || {}"
          :total-pages="totalPages || 0"
          @update:pagination-params="$emit('update:paginationParams', $event)"
      >
        <template #bodyCell="{ column, record }">
          <div v-if="column.key === 'action'" class="space-x-2 flex items-center justify-center">
            <a-tooltip title="Chỉnh sửa môn học" color="#FFC26E">
              <a-button
                  class="flex items-center justify-center"
                  type="primary"
                  size="large"
                  @click="$emit('handleOpenModalUpdate', record)"
                  :icon="h(EditOutlined)"
              />
            </a-tooltip>
          </div>
        </template>
      </tutor-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import TutorTable from "@/components/ui/TutorTable/TutorTable.vue";
import { DepartmentResponse } from "@/services/api/department.api";
import { EditOutlined } from "@ant-design/icons-vue";
import { ColumnType } from "ant-design-vue/es/table";
import { h } from "vue";

defineProps({
  dataSource: Array<DepartmentResponse>,
  loading: Boolean,
  paginationParams: Object,
  totalPages: Number,
});

defineEmits([
  "update:paginationParams",
  "handleOpenModalUpdate",
  "handleOpenModalAdd",
]);

const columnsDepartment: ColumnType[] = [
  {
    title: "STT",
    dataIndex: "orderNumber",
    key: "index",
    ellipsis: true,
  },
  {
    title: "Mã bộ môn",
    dataIndex: "departmentCode",
    key: "departmentCode",
    ellipsis: true,
  },
  {
    title: "Tên bộ môn",
    dataIndex: "departmentName",
    key: "departmentName",
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
