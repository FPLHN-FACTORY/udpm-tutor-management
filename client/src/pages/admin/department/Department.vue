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
      @handleOpenDepartmentDetailModal="handleOpenDepartmentDetailModal"
      @handleOpenMajorListModal="handleOpenMajorListModal"
      @handleOpenDepartmentsFacilityListModal="handleOpenDepartmentsFacilityListModal"
    />
    <department-detail-modal
      :open="openDepartmentDetailModal"
      @handleClose="handleCloseDepartmentDetailModal"
      :department-detail="departmentDetail"
      :is-loading-detail="isLoadingDetail"
    />
    <major-list-modal
      :open="openMajorListModal"
      @handleClose="handleCloseMajorListModal"
      :department-id="departmentId || null"
    />
    <department-facility-modal
      :open="openDepartmentsFacilityListModal"
      :title="title"
      @handleClose="handleCloseDepartmentsFacilityListModal"
      :department-id="departmentId || null"
    />
  </div>
</template>

<script lang="ts" setup>
import { computed, ref } from 'vue';
import { keepPreviousData } from '@tanstack/vue-query';

import DepartmentDetailModal from '@/pages/admin/department/DepartmentDetailModal.vue';
import DepartmentFilter from '@/pages/admin/department/DepartmentFilter.vue';
import DepartmentTable from '@/pages/admin/department/DepartmentTable.vue';
import MajorListModal from '@/pages/admin/department/MajorListModal.vue';
import DepartmentFacilityModal from '@/pages/admin/department/DepartmentFacilityModal.vue';

import { useDetailDepartment, useGetDepartment } from '../../../services/service/admin/department.action';
import { DepartmentFacilityResponse, DepartmentResponse, ParamsGetDepartment } from '../../../services/api/admin/department.api';
import { MajorResponse } from '../../../services/api/admin/major.api';

const params = ref<ParamsGetDepartment>({ page: 1, size: 10 });
const openDepartmentDetailModal = ref(false);
const departmentId = ref<string | null>(null);
const title = ref<string>('');

const { data, isLoading, isFetching } = useGetDepartment(params, {
  refetchOnWindowFocus: false,
  placeholderData: keepPreviousData,
});

const { data: dataDetail, isLoading: isLoadingDetail } = useDetailDepartment(departmentId, {
  refetchOnWindowFocus: false,
  enabled: computed(() => !!departmentId.value && openDepartmentDetailModal.value),
});

const handlePaginationChange = (newParams: ParamsGetDepartment) => {
  params.value = { ...params.value, ...newParams };
};

const handleFilter = (newParams: ParamsGetDepartment) => {
  params.value = { ...params.value, ...newParams };
};

const handleCloseDepartmentDetailModal = () => {
  openDepartmentDetailModal.value = false;
  departmentId.value = null;
};

const handleOpenDepartmentDetailModal = (record: DepartmentResponse) => {
  departmentId.value = record.id;
  openDepartmentDetailModal.value = true;
};

const departmentData = computed(() => data?.value?.data?.data || []);
const totalPages = computed(() => data?.value?.data?.totalPages || 0);
const departmentDetail = computed(() =>
  dataDetail.value?.data ? { ...dataDetail.value?.data, departmentId: departmentId?.value } : null
);

const openMajorListModal = ref(false);
const openDepartmentsFacilityListModal = ref(false);

const handleCloseMajorListModal = () => {
  openMajorListModal.value = false;
  departmentId.value = null;
};

const handleOpenMajorListModal = (record: MajorResponse) => {
  departmentId.value = record.id;
  openMajorListModal.value = true;
};

const handleCloseDepartmentsFacilityListModal = () => {
  openDepartmentsFacilityListModal.value = false;
  departmentId.value = null;
};

const handleOpenDepartmentsFacilityListModal = (record: DepartmentFacilityResponse) => {
  departmentId.value = record.id;
  title.value = record.departmentCode + "_" + record.departmentName;
  openDepartmentsFacilityListModal.value = true;
};
</script>
