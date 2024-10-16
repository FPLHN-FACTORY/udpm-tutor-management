<template>
  <div>
    <a-modal
      :open="open"
      @cancel="handleClose"
      :width="1000"
      destroyOnClose
      centered
      :footer="null"
    >
      <template #title>
        <span class="custom-modal-title text-2xl font-bold mb-1 block">
          Chi Tiết Chuyên Ngành
        </span>
      </template>

      <div v-if="isLoading" class="flex justify-center items-center">
        <a-spin />
      </div>

      <div v-else>
        <major-filter @filter="handleFilter" />
        <major-table
          :data-source="majorData"
          :loading="isLoading || isFetching"
          :pagination-params="params"
          :total-pages="totalPages"
          @update:pagination-params="handlePaginationChange"
          @syncSuccess="refetchMajorData"
          @handle-open-modal-update="handleOpenUpdateModal"
          @handle-open-modal-add="handleOpenAddModal"
        />
        <create-update-major-modal
          :open="openModalMajor"
          :major-detail="majorDetail"
          :department-id="departmentId"
          @handle-close="handleCloseMajorModal"
        />
      </div>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import { defineEmits, defineProps, ref, watch, computed } from 'vue';
import MajorFilter from './MajorFilter.vue';
import MajorTable from './MajorTable.vue';
import { MajorResponse, ParamsGetMajor } from '@/services/api/admin/major.api';
import { useGetMajors } from '@/services/service/admin/major.action';
import { keepPreviousData } from '@tanstack/vue-query';
import { ParamsGetDepartment } from '@/services/api/admin/department.api';
import CreateUpdateMajorModal from './CreateUpdateMajorModal.vue';

const emit = defineEmits(['handleClose']);

const props = defineProps({
  open: Boolean,
  departmentId: Object as () => any,
});

const majorDetail = ref<MajorResponse | null>(null)
const openModalMajor = ref<boolean>(false)

const handleOpenAddModal = () => {
  majorDetail.value = null;
  openModalMajor.value = true
}

const handleOpenUpdateModal = (record: MajorResponse) => {
  majorDetail.value = record;
  openModalMajor.value = true
}

const handleCloseMajorModal = () => {
  openModalMajor.value = false;
  majorDetail.value = null
}

const handleClose = () => {
  emit('handleClose');
};

const params = ref<ParamsGetMajor>({ page: 1, size: 10 });

const handleFilter = (newParams: ParamsGetMajor) => {
  params.value = { ...params.value, ...newParams };
};

const handlePaginationChange = (newParams: ParamsGetDepartment) => {
  params.value = { ...params.value, ...newParams };
};

const currentDepartmentId = ref(props.departmentId);

watch(
  () => props.departmentId,
  (newVal) => {
    if (newVal) {
      currentDepartmentId.value = newVal;
    }
  },
  { immediate: true }
);

const { data: majorDataResponse, isLoading, isFetching, refetch } = useGetMajors(
  currentDepartmentId,
  params,
  {
    refetchOnWindowFocus: false,
    placeholderData: keepPreviousData,
    enabled: computed(() => !!props.open && !!currentDepartmentId.value),
  }
);

const majorData = computed(() => majorDataResponse?.value?.data?.data || []);
const totalPages = computed(() => majorDataResponse?.value?.data?.totalPages || 0);

const refetchMajorData = () => {
  refetch();
};

</script>
