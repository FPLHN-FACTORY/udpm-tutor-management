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
      @handle-open-modal-add="handleOpenModalAdd"
      :data-source="facilityData"
      @handle-open-modal-update="handleOpenModalUpdate"
      :total-pages="totalPages"
      :pagination-params="facilityParams"
      @update:pagination-params="handleChangePagination"
      :loading="isLoading || isFetching"
    />
    <create-update-facility-modal
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
import CreateUpdateFacilityModal from './CreateUpdateFacilityModal.vue';
import { keepPreviousData } from '@tanstack/vue-query';
import { useDetailFacility, useGetFacility } from '@/services/service/admin/facility.action';
import { ParamsGetFacility } from '@/services/api/admin/facility.api';
import { FacilityResponse } from '@/services/api/admin/facility.api';

const openModal = ref(false);
const facilityId = ref<string | any>(null);

const facilityParams = ref<ParamsGetFacility>({
  page: 1,
  size: 5
})

const { data, isLoading,  isFetching } = useGetFacility(facilityParams, {
  refetchOnWindowFocus: false,
  placeholderData: keepPreviousData,
});
const { data: facilityDetail, isLoading: isLoadingDetail } = useDetailFacility(facilityId,
  {
    refetchOnWindowFocus: false,
    enabled: () => !!facilityId.value,
  }
)

const handleChangePagination = (newParams: ParamsGetFacility) => {
  facilityParams.value = { ...facilityParams.value, ...newParams }
}

const handleFilter = (newParams: ParamsGetFacility) => {
  facilityParams.value = { ...facilityParams.value, ...newParams }

}

const handleOpenModalAdd = () => {
  openModal.value = true;
  facilityId.value = null;
}

const handleOpenModalUpdate = (record: FacilityResponse) => {
  facilityId.value = record.id;
  openModal.value = true;
}

const handleCloseModal = () => {
  openModal.value = false;
  facilityId.value = null;
}


const facilityData = computed(() => data?.value?.data?.data || []);
const facilityDetailData = computed(() =>
  facilityId.value ? {
    ...facilityDetail.value?.data,
    facilityId: facilityId.value
  } : null)
const totalPages = computed(() => data?.value?.data?.totalPages || 0)

</script>
