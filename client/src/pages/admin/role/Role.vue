<template>
  <div class="mt-4">
    <div class="flex justify-between items-center">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="hi-office-building" scale="2" />
        <span class="m-2 text-3xl">Quản lý chức vụ</span>
      </h2>
    </div>
    <role-filter :params="params" @filter="handleFilterChange" />
    <role-table
      :data-source="roles"
      :loading="isLoading || isFetching"
      :pagination-params="params"
      :total-pages="totalPages"
      @handlePaginationChange="handlePaginationChange"
    />
  </div>
</template>

<script lang="ts" setup>
import { ParamsGetRoles } from "../../../services/api/admin/role.api";
import { useGetRoles } from "../../../services/service/admin/role.action";
import RoleTable from "@/pages/admin/role/RoleTable.vue";
import RoleFilter from "@/pages/admin/role/RoleFilter.vue";
import { keepPreviousData } from "@tanstack/vue-query";
import { computed, ref } from "vue";

const params = ref<ParamsGetRoles>({
  page: 1,
  size: 10,
});

const {
  data: roleData,
  isLoading,
  isFetching,
} = useGetRoles(params, {
  refetchOnWindowFocus: false,
  placeholderData: keepPreviousData,
});

const handlePaginationChange = (paginationParams: ParamsGetRoles) => {
  params.value = paginationParams;
};

const handleFilterChange = (filterParams: ParamsGetRoles) => {
  params.value = { ...params.value, ...filterParams };
};

const roles = computed(() => roleData?.value?.data?.data || []);
const totalPages = computed(() => roleData?.value?.data?.totalPages || 0);
</script>
