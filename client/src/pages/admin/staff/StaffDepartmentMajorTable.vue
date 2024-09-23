<template>
  <div class="shadow-2xl p-3 m-3 rounded-md flex h-full flex-col overflow-auto">
    <div class="flex justify-between items-center min-h-36">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="bi-list-ul" scale="2" />
        <span class="ml-2 text-2xl">Danh sách bộ môn, chuyên ngành theo cơ sở</span>
      </h2>
    </div>
    <div class="flex h-0 flex-1 flex-col">
      <tutor-table
          wrapperClassName="min-h-[410px]"
          :columns="columnsStaff"
          :data-source="dataSource"
          :loading="loading"
          :is-pagination="false"
      >
      </tutor-table>
    </div>
    <router-view />
  </div>
</template>

<script setup lang="ts">
import TutorTable from "@/components/ui/TutorTable/TutorTable.vue";
import {StaffResponse} from "@/services/api/admin/staff.api.ts";
import {ColumnType} from "ant-design-vue/es/table";

defineProps({
  dataSource: Array<StaffResponse>,
  loading: Boolean,
});

const columnsStaff: ColumnType[] = [
  {
    title: "STT",
    dataIndex: "orderNumber",
    key: "index",
    ellipsis: true,
  },
  {
    title: "Cơ sở",
    dataIndex: "facilityName",
    key: "facilityName",
    ellipsis: true,
  },
  {
    title: "Bộ môn",
    dataIndex: "departmentName",
    key: "departmentName",
    ellipsis: true,
  },
  {
    title: "Chuyên ngành",
    dataIndex: "majorName",
    key: "majorName",
    ellipsis: true,
  },
];
</script>
