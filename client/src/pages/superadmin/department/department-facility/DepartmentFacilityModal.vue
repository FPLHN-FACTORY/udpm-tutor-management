<template>
  <a-modal :open="open" @cancel="handleClose" :width="1000" destroyOnClose centered :footer="null">
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
        ? params : paramsMajorFacility" :total-pages="totalPages"
        @update:pagination-params="handlePaginationChange"
        @handleGetMajorFacility="handleGetMajorFacility"
        @dataSynced="refreshDepartmentFacilityData"
        @handleOpenModalAdd="handleOpenModalAdd"
        @handleOpenModalUpdate="handleOpenModalUpdate"
        @handleOpenModalMajorFacilityAdd="handleOpenModalMajorFacilityAdd"
        @handleOpenModalMajorFacilityUpdate="handleOpenModalMajorFacilityUpdate" />
      <create-update-department-facility-modal
        :open="openModalDepartmentFacility"
        @handle-close="handleCloseModalDepartmentFacility"
        :department-facility-detail="departmentFacilityDetail"
        :department-id="departmentId"
      />
      <create-update-major-facility-modal
        :open="openModalMajorFacility"
        @handle-close="handleCloseModalMajorFacility"
        :major-facility-detail="majorFacilityDetailData"
        :department-id="departmentId"
        :department-facility-id="currentMajorFacilityId || ''"
        :head-major-id="headDepartmentId || ''"
      />
    </div>
  </a-modal>
</template>

<script lang="ts" setup>
import { defineEmits, defineProps, ref, watch, computed, watchEffect } from 'vue';
import DepartmentFacilityTable from './DepartmentFacilityTable.vue';
import DepartmentFacilityFilter from './DepartmentFacilityFilter.vue';
import MajorFacilityFilter from '../major-facility/MajorFacilityFilter.vue';
import MajorFacilityTable from '../major-facility/MajorFacilityTable.vue';
import { DepartmentFacilityResponse, ParamsGetDepartmentFacility } from '@/services/api/admin/department.api';
import { useGetDepartmentFacility } from '@/services/service/admin/department.action';
import { useGetDetailMajorFacility, useGetMajorsFacility } from '@/services/service/admin/major.action';
import { MajorFacilityResponse, ParamsGetMajorFacility } from '@/services/api/admin/major.api';
import { keepPreviousData } from '@tanstack/vue-query';
import CreateUpdateDepartmentFacilityModal from './CreateUpdateDepartmentFacilityModal.vue';
import CreateUpdateMajorFacilityModal from '../major-facility/CreateUpdateMajorFacilityModal.vue';

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
const openModalDepartmentFacility = ref<boolean>(false);
const departmentFacilityDetail = ref<DepartmentFacilityResponse | null>(null)
const openModalMajorFacility = ref<boolean>(false);
const majorFacilityId = ref<string | null>(null)
const headDepartmentId = ref<string | null>(null)

const handleClose = () => {
  emit('handleClose');
  currentView.value = 'department';
  currentMajorFacilityId.value = null;
};

const handleOpenModalAdd = () => {
  openModalDepartmentFacility.value = true;
  departmentFacilityDetail.value = null;
}

const handleOpenModalUpdate = (record: DepartmentFacilityResponse) => {
  openModalDepartmentFacility.value = true;
  departmentFacilityDetail.value = record;
}

const handleCloseModalDepartmentFacility = () => {
  openModalDepartmentFacility.value = false;
  departmentFacilityDetail.value = null;
}

const handleOpenModalMajorFacilityAdd = () => {
  openModalMajorFacility.value = true;
  majorFacilityId.value = null;
}

const handleOpenModalMajorFacilityUpdate = (record: MajorFacilityResponse) => {
  majorFacilityId.value = record.id;
  openModalMajorFacility.value = true;
}

const handleCloseModalMajorFacility = () => {
  openModalMajorFacility.value = false;
  majorFacilityId.value = null;
}

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
  }
};

const goBack = () => {
  currentView.value = 'department';
  currentMajorFacilityId.value = null;
};
const handleGetMajorFacility = (record: MajorFacilityResponse) => {
  // titleMajorFacility.value = `Chi Tiết Bộ Môn : ${props.title} - Cơ sở: ${record.facilityCode}_${record.facilityName}`;
  titleMajorFacility.value = `Quản Lý Chuyên Ngành Theo Cơ Sở`;
  currentMajorFacilityId.value = String(record.departmentFacilityId);
  currentView.value = 'major';
  headDepartmentId.value = record.headOfDepartmentId
};

const { data: majorFacilityDetail } = useGetDetailMajorFacility(majorFacilityId,
  {
    refetchOnWindowFocus: false,
    enabled: () => !!majorFacilityId.value,
  }
)

const { data: departmentData, isLoading: isDepartmentLoading, isFetching: isDepartmentFetching, refetch: refetchDepartment } = useGetDepartmentFacility(currentDepartmentId, params, {
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
    return majorData?.value?.data?.majorFacilities?.totalPages || 0;
  }
});

const dataSource = computed(() => {
  if (currentView.value === 'department') {
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


const refreshDepartmentFacilityData = () => {
  refetchDepartment();
};

const majorFacilityDetailData = computed(() =>
  majorFacilityId.value ? {
    ...majorFacilityDetail.value?.data,
    id: majorFacilityId.value
  } : null)

</script>
