<template>
  <div class="shadow-2xl p-3 m-3 rounded-md flex h-full flex-col overflow-auto my-14">
    <div class="flex justify-between items-center min-h-36">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="bi-list-ul" scale="2" />
        <span class="ml-2 text-2xl">Danh sách chức vụ</span>
      </h2>
    </div>
    <div class="flex h-0 flex-1 flex-col">
      <tutor-table
          wrapperClassName="min-h-[410px]"
          :columns="columnsRoleStaff"
          :data-source="dataSource"
          :loading="loading"
          :is-pagination="false"
      >
      </tutor-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import TutorTable from "@/components/ui/TutorTable/TutorTable.vue";
import {RoleResponse} from "../../../services/api/admin/role.api.ts";
import {ColumnType} from "ant-design-vue/es/table";

defineProps({
  dataSource: Array<RoleResponse>,
  loading: Boolean,
});

const columnsRoleStaff: ColumnType[] = [
  {
    title: "STT",
    dataIndex: "orderNumber",
    key: "index",
    ellipsis: true,
  },
  {
    title: "Mã chức vụ",
    dataIndex: "roleCode",
    key: "roleCode",
    ellipsis: true,
  },
  {
    title: "Tên chức vụ",
    dataIndex: "roleName",
    key: "roleName",
    ellipsis: true,
  },
  {
    title: "Tên cơ sở",
    dataIndex: "facilityName",
    key: "facilityName",
    ellipsis: true,
  },
];
</script>
