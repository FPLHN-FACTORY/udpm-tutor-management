<template>
    <div class="mt-4">
      <div class="flex justify-between items-center">
        <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
          <v-icon name="hi-office-building" scale="2" />
          <span class="m-2 text-3xl">Quản lý lịch sử đăng nhập</span>
        </h2>
      </div>
      <user-activity-log-filter @filter="handleFilter" />
      <user-activity-log-table
        :data-source="UserActivityLogData"
        :loading="isLoading || isFetching"
        :pagination-params="params"
        :total-pages="totalPages"
        @update:pagination-params="handlePaginationChange"
        @handleOpenModalDetail="handleOpenModalDetail"
      />
    </div>
  </template>
  
  <script lang="ts" setup>
import UserActivityLogTable from '@/pages/operationlogs/UserActivityLogTable.vue';
import UserActivityLogFilter from '@/pages/operationlogs/UserActivityLogFilter.vue';
import { computed, ref } from 'vue';
import { useDetailOperationLogs, useGetUserActivityLog } from '@/services/service/operationlog/operationlog.action';
import { keepPreviousData } from '@tanstack/vue-query';
import { OperationLogResponse, ParamsGetUserActivityLog } from '@/services/api/operationlog/operationlog.api';

const params = ref<ParamsGetUserActivityLog>({
  page: 1,
  size: 10,
});

const open = ref(false);
const operationLogId = ref<string | null>(null);

const { data, isLoading, isFetching } = useGetUserActivityLog(params, {
  refetchOnWindowFocus: false,
  placeholderData: keepPreviousData,
});

const { data: dataDetail, isLoading: isLoadingDetail } = useDetailOperationLogs(
  operationLogId,
  {
    refetchOnWindowFocus: false,
    enabled: () => !!operationLogId.value,
  }
);

const handlePaginationChange = (newParams: ParamsGetUserActivityLog) => {
  params.value = { ...params.value, ...newParams };
};

const handleFilter = (newParams: ParamsGetUserActivityLog) => {
  params.value = { ...params.value, ...newParams };
};

const handleCloseModal = () => {
  open.value = false;
  operationLogId.value = null;
};

const handleOpenModalDetail = (record: OperationLogResponse) => {
  console.log(record)
  operationLogId.value = record.id;
  open.value = true;
};

const UserActivityLogData = computed(() => data?.value?.data?.data || []);
const totalPages = computed(() => data?.value?.data?.totalPages || 0);
const operationLogDetail = computed(() => dataDetail.value?.data || null);
  </script>
  