<template>
  <div class="mt-4">
    <div class="flex justify-between items-center">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="hi-office-building" scale="2" />
        <span class="m-2 text-3xl">Quản lý nhân viên</span>
      </h2>
    </div>
    <StaffFilter @filter="handleFilter" />
    <staff-table
        :data-source="staffData"
        :loading="isLoading || isFetching"
        :pagination-params="params"
        :total-pages="totalPages"
        @update:pagination-params="handlePaginationChange"
    />
  </div>
</template>

<script lang="ts" setup>
import StaffTable from "@/pages/admin/staff/StaffTable.vue";
import StaffFilter from "@/pages/admin/staff/StaffFilter.vue";
import {computed, ref} from "vue";
import {ParamsGetStaff} from "@/services/api/admin/staff.api.ts";
import {useGetStaff} from "@/services/service/admin/staff.action.ts";
import {keepPreviousData} from "@tanstack/vue-query";
import {ParamsGetDepartment} from "@/services/api/admin/department.api.ts";

const params = ref<ParamsGetStaff>({
  page: 1,
  size: 10,
});

const { data, isLoading, isFetching } = useGetStaff(params, {
  refetchOnWindowFocus: false,
  placeholderData: keepPreviousData,
});

const handlePaginationChange = (newParams: ParamsGetStaff) => {
  params.value = { ...params.value, ...newParams };
};


const handleFilter = (newParams: ParamsGetDepartment) => {
  params.value = { ...params.value, ...newParams };
};

const staffData = computed(() => data?.value?.data?.data || []);
const totalPages = computed(() => data?.value?.data?.totalPages || 0);
</script>
