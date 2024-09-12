<template>
  <div class="mt-4">
    <div class="flex justify-between items-center">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="hi-office-building" scale="2" />
        <span class="m-2 text-3xl">Quản lý bộ môn</span>
      </h2>
    </div>
    <department-filter @filter="handleFilter" />
    <department-table
        :data-source="departmentData"
        :loading="isLoading || isFetching"
        :pagination-params="params"
        :total-pages="totalPages"
        @update:pagination-params="handlePaginationChange"
        @handleOpenModalUpdate="handleOpenModalUpdate"
        @handleOpenModalAdd="handleOpenModalAdd"
    />
    <create-update-department-modal
        :open="open"
        @handleClose="handleClose"
        @cancel="open = false"
        :department-detail="departmentDetail || null"
        :is-loading-detail="isLoadingDetail || false"
    />
  </div>
</template>

<script lang="ts" setup>
import CreateUpdateDepartmentModal from "@/pages/admin/department/CreateUpdateDepartmentModal.vue";
import DepartmentFilter from "@/pages/admin/department/DepartmentFilter.vue";
import DepartmentTable from "@/pages/admin/department/DepartmentTable.vue";
import {
  DepartmentResponse,
  ParamsGetDepartment,
} from "@/services/api/department.api";
import {
  useDetailDepartment,
  useGetDepartment,
} from "@/services/service/department.action";
import { keepPreviousData } from "@tanstack/vue-query";
import { computed, ref } from "vue";

const params = ref<ParamsGetDepartment>({
  page: 1,
  size: 10,
});

const open = ref(false);

const departmentId = ref<string | null>(null);

const { data, isLoading, isFetching } = useGetDepartment(params, {
  refetchOnWindowFocus: false,
  placeholderData: keepPreviousData,
});

const { data: dataDetail, isLoading: isLoadingDetail } = useDetailDepartment(
    departmentId,
    {
      refetchOnWindowFocus: false,
      enabled: () => !!departmentId.value,
    }
);

const handlePaginationChange = (newParams: ParamsGetDepartment) => {
  params.value = { ...params.value, ...newParams };
};

const handleFilter = (newParams: ParamsGetDepartment) => {
  params.value = { ...params.value, ...newParams };
};

const handleClose = () => {
  open.value = false;
  departmentId.value = null;
};

const handleOpenModalAdd = () => {
  open.value = true;
  departmentId.value = null;
};

const handleOpenModalUpdate = (record: DepartmentResponse) => {
  departmentId.value = record.id;
  open.value = true;
};

const departmentData = computed(() => data?.value?.data?.data || []);
const totalPages = computed(() => data?.value?.data?.totalPages || 0);
const departmentDetail = computed(() =>
    dataDetail.value?.data
        ? {
          ...dataDetail.value?.data,
          departmentId: departmentId?.value,
        }
        : null
);
</script>
