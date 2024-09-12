<template>
  <div class="mt-4">
    <div class="flex justify-between items-center">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="hi-office-building" scale="2" />
        <span class="m-2 text-3xl">Quản lý cơ sở</span>
      </h2>
    </div>
    <facility-filter @filter="handleFilter" />
    <facility-table @handle-open-modal-add="handleOpenModal" :data-source="facilityData"
      @handle-open-modal-update="handleOpenModalUpdate" :total-pages="parentTotalPages"
      :pagination-params="facilityParams" @update:pagination-params="handleParentChangePagination" />
    <detail-facility-modal :open="openModal" @handle-close="handleCloseModal"
      :facility-detail="facilityDetailData || null" :facility-childs="facilityChildData"
      :pagination-params="facilityChildParams" :total-pages="childTotalPages" @update:pagination-params="handleChildChangePagination" />
  </div>
</template>

<script lang="ts" setup>
import { computed, ref } from 'vue';
import FacilityFilter from './FacilityFilter.vue';
import FacilityTable from './FacilityTable.vue';
import DetailFacilityModal from './DetailFacilityModal.vue';
import { useDetailFacility, useGetFacility } from '@/services/service/facility.action';
import { FacilityResponse, ParamsGetFacility } from '@/services/api/facility.api';
import { keepPreviousData } from '@tanstack/vue-query';
import { useGetFacilityChild } from '@/services/service/facility-child.action';
import { ParamsGetFacilityChild } from '@/services/api/facility-child.api';

const openModal = ref(false);
const facilityId = ref<string | any>(null);

const facilityParams = ref<ParamsGetFacility>({
  page: 1,
  size: 5
})

const facilityChildParams = ref<ParamsGetFacilityChild>({
  page: 1,
  size: 5
})

const { data: listFacilities } = useGetFacility(facilityParams, {
  refetchOnWindowFocus: false,
  placeholderData: keepPreviousData,
});
const { data: facilityDetail } = useDetailFacility(facilityId,
  {
    refetchOnWindowFocus: false,
    enabled: () => !!facilityId.value,
  }
)
const { data: listFacilityChilds } = useGetFacilityChild(
  facilityChildParams,
  facilityId,
  {
    refetchOnWindowFocus: false,
    enabled: () => !!facilityId.value,
  }
)

const handleParentChangePagination = (newParams: ParamsGetFacility) => {
  facilityParams.value = { ...facilityParams.value, ...newParams }
}

const handleChildChangePagination = (newParams: ParamsGetFacilityChild) => {
  facilityChildParams.value = { ...facilityChildParams.value, ...newParams }
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

const facilityData = computed(() => listFacilities?.value?.data?.data || []);
const facilityDetailData = computed(() =>
  facilityId.value ? {
    ...facilityDetail.value?.data,
    facilityId: facilityId.value
  } : {})
const parentTotalPages = computed(() => listFacilities?.value?.data?.totalPages || 0)

const facilityChildData = computed(() => listFacilityChilds?.value?.data?.data || []);
const childTotalPages = computed(() => listFacilityChilds?.value?.data?.totalPages || 0);

</script>
