<template>
  <div class="shadow-xl p-3 rounded-md flex h-full flex-col overflow-auto">
    <div class="flex justify-between items-center min-h-[56px]">
      <h2 class="flex items-center text-primary text-3xl font-semibold p-2">
        <span class="text-xl">Danh Sách Chuyên Ngành</span>
      </h2>
      <a-button
        type="primary"
        size="large"
        class="m-4 flex justify-between items-center"
        @click="$emit('handleOpenModalAdd')"
      >
        Thêm chuyên ngành
      </a-button>
    </div>
    <div class="flex h-0 flex-1 flex-col">
      <tutor-table
        wrapperClassName="min-h-[410px]"
        :columns="columnsMajor"
        :data-source="dataSource"
        :loading="loading"
        :pagination-params="paginationParams || {}"
        :total-pages="totalPages || 0"
        @update:pagination-params="$emit('update:paginationParams', $event)"
      >
        <template #bodyCell="{ column, record }">
          <div v-if="column.key === 'majorStatus'" class="text-center">
            <a-tag v-if="record.majorStatus === 0" color="success"
              >Hoạt động</a-tag
            >
            <a-tag v-else-if="record.majorStatus === 1" color="error"
              >Không hoạt động</a-tag
            >
          </div>
          <div
            v-else-if="column.key === 'action'"
            class="space-x-2 text-center"
          >
            <a-button
              type="primary"
              size="regular"
              @click="$emit('handleOpenModalUpdate', record)"
              :icon="h(EyeOutlined)"
            />
          </div>
        </template>
      </tutor-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import TutorTable from "@/components/ui/TutorTable/TutorTable.vue";
import { MajorResponse } from "@/services/api/superadmin/major.api";
import { EyeOutlined } from "@ant-design/icons-vue";
import { ColumnType } from "ant-design-vue/es/table";
import { h } from "vue";

defineProps({
  dataSource: Array as () => MajorResponse[],
  loading: Boolean,
  paginationParams: Object as () => any,
  totalPages: Number,
});

const emit = defineEmits([
  "update:paginationParams",
  "handleOpenModalUpdate",
  "handleOpenModalAdd",
  "syncSuccess",
]);

const columnsMajor: ColumnType[] = [
  {
    title: "STT",
    dataIndex: "orderNumber",
    key: "index",
    ellipsis: true,
  },
  {
    title: "Mã Chuyên Ngành",
    dataIndex: "majorCode",
    key: "majorCode",
    ellipsis: true,
  },
  {
    title: "Tên Chuyên Ngành",
    dataIndex: "majorName",
    key: "majorName",
    ellipsis: true,
  },
  {
    title: "Hành động",
    key: "action",
    align: "center",
    width: "150px",
    ellipsis: true,
  },
];
</script>
