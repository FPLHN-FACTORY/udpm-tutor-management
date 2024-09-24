<template>
  <div class="mt-4">
    <div class="flex justify-between items-center">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="hi-office-building" scale="2" />
        <span class="m-2 text-3xl">Quản lý cơ sở</span>
      </h2>
    </div>
    <facility-filter
      @filter="handleFilter"
    />
    <facility-table
      @handle-open-modal-add="handleOpenModal"
      :data-source="facilityData"
      @handle-open-modal-update="handleOpenModalUpdate"
      :total-pages="totalPages"
      :pagination-params="facilityParams"
      @update:pagination-params="handleParentChangePagination"
      @syncSuccess="refetchFacilityData"
      :loading-sync="isLoadingSync"
      :loading="isLoading || isFetching"
    />
    <detail-facility-modal
      :open="openModal"
      @handle-close="handleCloseModal"
      :facility-detail="facilityDetailData || null"
      :is-loading-detail="isLoadingDetail"
    />
  </div>
</template>

<script lang="ts" setup>
import { computed, ref } from 'vue';
import FacilityFilter from './FacilityFilter.vue';
import FacilityTable from './FacilityTable.vue';
import DetailFacilityModal from './DetailFacilityModal.vue';
import { keepPreviousData } from '@tanstack/vue-query';
import { toast } from 'vue3-toastify';
import { useDetailFacility, useGetFacility } from '@/services/service/admin/facility.action';
import { ParamsGetFacility } from '@/services/api/admin/facility.api';
import { FacilityResponse } from '@/services/api/admin/department.api';

const openModal = ref(false);
const facilityId = ref<string | any>(null);
const isLoadingSync = ref<boolean>(false);

const facilityParams = ref<ParamsGetFacility>({
  page: 1,
  size: 5
})

const { data, isLoading,  isFetching, refetch } = useGetFacility(facilityParams, {
  refetchOnWindowFocus: false,
  placeholderData: keepPreviousData,
});
const { data: facilityDetail, isLoading: isLoadingDetail } = useDetailFacility(facilityId,
  {
    refetchOnWindowFocus: false,
    enabled: () => !!facilityId.value,
  }
)

const handleParentChangePagination = (newParams: ParamsGetFacility) => {
  facilityParams.value = { ...facilityParams.value, ...newParams }
}

const handleFilter = (newParams: ParamsGetFacility) => {
  facilityParams.value = { ...facilityParams.value, ...newParams }

}

const handleOpenModal = () => {
  openModal.value = true;
}

const handleOpenModalUpdate = (record: FacilityResponse) => {
  facilityId.value = record.id;
  openModal.value = true;
}

const handleCloseModal = () => {
  openModal.value = false;
  facilityId.value = null;
}

// Hàm refetch dữ liệu
const refetchFacilityData = () => {
  setTimeout(() => {
    refetch(); // Gọi lại hàm refetch để cập nhật dữ liệu
    isLoadingSync.value = false;
    toast.success("Đồng bộ cơ sở thành công");
  }, 2000)
  isLoadingSync.value = true;
};

const facilityData = computed(() => data?.value?.data?.data || []);
const facilityDetailData = computed(() =>
  facilityId.value ? {
    ...facilityDetail.value?.data,
    facilityId: facilityId.value
  } : {})
const totalPages = computed(() => data?.value?.data?.totalPages || 0)

</script>
