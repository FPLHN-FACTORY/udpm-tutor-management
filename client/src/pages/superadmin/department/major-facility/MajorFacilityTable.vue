<template>
  <div class="shadow-xl p-3 rounded-md flex h-full flex-col overflow-auto">
    <div class="flex justify-between items-center min-h-[56px]">
      <h2
        class="flex justify-between items-center text-primary text-3xl font-semibold p-2"
      >
        <span class="text-xl">Danh Sách Chuyên Ngành</span>
      </h2>
      <a-button
        type="primary"
        size="large"
        class="m-4 flex justify-between items-center"
        @click="$emit('handleOpenModalMajorFacilityAdd')"
      >
        Thêm
      </a-button>
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
            <a-tag v-else-if="record.status === 1" color="error"
              >Không hoạt động</a-tag
            >
          </div>
          <div
            v-else-if="column.key === 'action'"
            class="space-x-2 text-center"
          >
            <a-tooltip title="Chỉnh sửa" color="#FFC26E">
              <a-button
                type="primary"
                size="regular"
                @click="$emit('handleOpenModalMajorFacilityUpdate', record)"
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
import { MajorFacilityResponse } from "@/services/api/superadmin/major.api";
import { EditOutlined } from "@ant-design/icons-vue";
import { ColumnType } from "ant-design-vue/es/table";
import { h } from "vue";

defineProps({
  dataSource: Array as () => MajorFacilityResponse[],
  loading: Boolean,
  paginationParams: Object as () => any,
  totalPages: Number,
});

const emit = defineEmits([
  "update:paginationParams",
  "handleOpenModalMajorFacilityAdd",
  "handleOpenModalMajorFacilityUpdate",
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
    title: "Hành động",
    dataIndex: "action",
    key: "action",
    ellipsis: true,
  },
];
</script>
