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
        />
      </div>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import { defineEmits, defineProps, ref, watch, computed } from 'vue';
import MajorFilter from '@/pages/admin/department/MajorFilter.vue';
import MajorTable from '@/pages/admin/department/MajorTable.vue';
import { ParamsGetMajor } from '@/services/api/admin/major.api';
import { useGetMajors } from '@/services/service/admin/major.action';
import { keepPreviousData } from '@tanstack/vue-query';
import { ParamsGetDepartment } from '@/services/api/admin/department.api';

const emit = defineEmits(['handleClose']);

const props = defineProps({
  open: Boolean,
  departmentId: Object as () => any,
});

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
