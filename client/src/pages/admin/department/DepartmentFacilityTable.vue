<template>
  <div class="shadow-xl p-3 rounded-md flex h-full flex-col overflow-auto">
    <div class="flex justify-between items-center min-h-[56px]">
      <h2 class="flex items-center text-primary text-3xl font-semibold p-2">
        <span class="text-xl">Danh Sách Cơ Sở Bộ Môn</span>
      </h2>
    </div>
    <div class="flex h-0 flex-1 flex-col">
      <tutor-table
        wrapperClassName="min-h-[410px]"
        :columns="columnsDepartmentFacility"
        :data-source="dataSource"
        :loading="loading"
        :pagination-params="paginationParams || {}"
        :total-pages="totalPages || 0"
        @update:pagination-params="$emit('update:paginationParams', $event)"
      >
        <template #bodyCell="{ column, record }">
          <div v-if="column.key === 'action'" class="space-x-2 text-center">
            <a-tooltip title="Danh sách chuyên ngành thuộc cơ sở của bộ môn" color="#FFC26E">
              <a-button
                type="primary"
                size="regular"
                @click="$emit('handleGetMajorFacility', record)"
                :icon="h(EyeOutlined)"
              />
            </a-tooltip>
          </div>
          <div v-else-if="column.key === 'departmentFacilityStatus'" class="text-center">
            <a-tag v-if="record.departmentFacilityStatus === 0" color="success">
              Hoạt động
            </a-tag>
            <a-tag v-else-if="record.departmentFacilityStatus === 1" color="error">
              Không hoạt động
            </a-tag>
          </div>
        </template>
      </tutor-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import TutorTable from "@/components/ui/TutorTable/TutorTable.vue";
import { FacilityResponse } from "@/services/api/department.api";
import { EyeOutlined } from "@ant-design/icons-vue";
import { ColumnType } from "ant-design-vue/es/table";
import { h } from "vue";

const props = defineProps({
  dataSource: Array as () => FacilityResponse[],
  loading: Boolean,
  paginationParams: Object as () => any,
  totalPages: Number,
});

const emit = defineEmits([
  "update:paginationParams",
  "handleGetMajorFacility"
]);

const columnsDepartmentFacility: ColumnType[] = [
  {
    title: "STT",
    dataIndex: "orderNumber",
    key: "index",
    ellipsis: true,
  },
  {
    title: "Tên cơ sở",
    dataIndex: "facilityName",
    key: "facilityName",
    ellipsis: true,
  },
  {
    title: "Chủ nhiệm bộ môn",
    dataIndex: "profileStaff",
    key: "profileStaff",
    ellipsis: true,
  },
  {
    title: "Trạng thái",
    dataIndex: "departmentFacilityStatus",
    key: "departmentFacilityStatus",
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
