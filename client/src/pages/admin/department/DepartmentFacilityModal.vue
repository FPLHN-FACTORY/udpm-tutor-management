<template>
  <a-modal
    :open="open"
    @cancel="handleClose"
    :width="1000"
    destroyOnClose
    centered
    :footer="null"
  >
    <template #title>
      <div class="flex items-center">
        <a-button v-if="currentView === 'major'" type="primary" @click="goBack" size="small" class="me-4">
          quay lại
        </a-button>
        <span class="custom-modal-title text-xl font-bold block">
          {{ currentView === 'department'
            ? `Chi Tiết Cơ Sở - Bộ Môn : ${title}`
            : `${titleMajorFacility}` }}
        </span>
      </div>
    </template>

    <div v-if="isLoading" class="flex justify-center items-center">
      <a-spin />
    </div>

    <div v-else>
      <component :is="currentFilter" @filter="handleFilter" />
      <component
        :is="currentComponent"
        :data-source="dataSource"
        :loading="isLoading || isFetching"
        :pagination-params="currentView === 'department'
          ? params
          : paramsMajorFacility"
        :total-pages="totalPages"
        @update:pagination-params="handlePaginationChange"
        @handleGetMajorFacility="handleGetMajorFacility"
      />
    </div>
  </a-modal>
</template>

<script lang="ts" setup>
import { defineEmits, defineProps, ref, watch, computed, watchEffect } from 'vue';
import DepartmentFacilityTable from '@/pages/admin/department/DepartmentFacilityTable.vue';
import DepartmentFacilityFilter from '@/pages/admin/department/DepartmentFacilityFilter.vue';
import MajorFacilityFilter from '@/pages/admin/department/MajorFacilityFilter.vue';
import MajorFacilityTable from '@/pages/admin/department/MajorFacilityTable.vue';
import { DepartmentFacilityResponse, ParamsGetDepartmentFacility } from '@/services/api/admin/department.api';
import { useGetDepartmentFacility } from '@/services/service/admin/department.action';
import { useGetMajorsFacility } from '@/services/service/admin/major.action';
import { MajorFacilityResponse, ParamsGetMajorFacility } from '@/services/api/admin/major.api';
import { keepPreviousData } from '@tanstack/vue-query';

const emit = defineEmits(['handleClose']);

const props = defineProps({
  open: Boolean,
  departmentId: Object as () => any,
  title: String
});

const currentView = ref('department');
const params = ref<ParamsGetDepartmentFacility>({ page: 1, size: 10 });
const paramsMajorFacility = ref<ParamsGetMajorFacility>({ page: 1, size: 10 });
const currentDepartmentId = ref(props.departmentId);
const currentMajorFacilityId = ref<string | null>(null);
const titleMajorFacility = ref<string | null>(null);

const handleClose = () => {
  emit('handleClose');
};

const handleFilter = (newParams: ParamsGetDepartmentFacility | ParamsGetMajorFacility) => {
  if (currentView.value === 'department') {
    params.value = { ...params.value, ...newParams as ParamsGetDepartmentFacility };
  } else {
    paramsMajorFacility.value = { ...paramsMajorFacility.value, ...newParams as ParamsGetMajorFacility };
  }
};

const handlePaginationChange = (newParams: ParamsGetDepartmentFacility | ParamsGetMajorFacility) => {
  if (currentView.value === 'department') {
    params.value = { ...params.value, ...newParams as ParamsGetDepartmentFacility };
  } else {
    paramsMajorFacility.value = { ...paramsMajorFacility.value, ...newParams as ParamsGetMajorFacility };
    console.log("paramsMajorFacility", paramsMajorFacility);
  }
};

const goBack = () => {
  currentView.value = 'department';
  currentMajorFacilityId.value = null;
};

const handleGetMajorFacility = (record: MajorFacilityResponse) => {
  titleMajorFacility.value = `Chi Tiết Bộ Môn : ${props.title} - Cơ sở: ${record.facilityCode}_${record.facilityName}`;
  currentMajorFacilityId.value = String(record.departmentFacilityId);
  currentView.value = 'major';
};

const { data: departmentData, isLoading: isDepartmentLoading, isFetching: isDepartmentFetching } = useGetDepartmentFacility(currentDepartmentId, params, {
  refetchOnWindowFocus: false,
  placeholderData: keepPreviousData,
  enabled: computed(() => !!props.open && !!currentDepartmentId.value && currentView.value === 'department'),
});

const { data: majorData, isLoading: isMajorLoading, isFetching: isMajorFetching } = useGetMajorsFacility(paramsMajorFacility, {
  refetchOnWindowFocus: false,
  placeholderData: keepPreviousData,
  enabled: computed(() => !!paramsMajorFacility.value.departmentFacilityId && currentView.value === 'major'),
});

const isLoading = computed(() => currentView.value === 'department' ? isDepartmentLoading.value : isMajorLoading.value);
const isFetching = computed(() => currentView.value === 'department' ? isDepartmentFetching.value : isMajorFetching.value);

const totalPages = computed(() => {
  if (currentView.value === 'department') {
    return departmentData?.value?.data?.totalPages || 0;
  } else {
    console.log("majorData", majorData);
    return majorData?.value?.data?.majorFacilities?.totalPages || 0;
  }
});

const dataSource = computed(() => {
  if (currentView.value === 'department') {
    console.log("Data for department", departmentData?.value?.data?.data);
    return (departmentData?.value?.data?.data as DepartmentFacilityResponse[]) || [];
  } else {
    return (majorData?.value?.data?.majorFacilities?.data as MajorFacilityResponse[]) || [];
  }
});

watch(
  () => props.departmentId,
  (newVal) => {
    if (newVal) {
      currentDepartmentId.value = newVal;
    }
  },
  { immediate: true }
);

watchEffect(() => {
  if (currentMajorFacilityId.value && currentView.value === 'major') {
    paramsMajorFacility.value = {
      ...paramsMajorFacility.value,
      departmentFacilityId: currentMajorFacilityId.value,
    };
  }
});

const currentComponent = computed(() => {
  return currentView.value === 'department' ? DepartmentFacilityTable : MajorFacilityTable;
});

const currentFilter = computed(() => {
  return currentView.value === 'department' ? DepartmentFacilityFilter : MajorFacilityFilter;
});
</script>
