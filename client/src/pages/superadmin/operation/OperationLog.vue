<template>
  <div class="mt-4">
    <div class="flex justify-between items-center">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="hi-office-building" scale="2" />
        <span class="m-2 text-3xl">Quản lý lịch sử hoạt động</span>
      </h2>
    </div>
    <operation-log-filter @filter="handleFilter" />
    <operation-log-table
      :data-source="operationLogData"
      :loading="isLoading || isFetching"
      :pagination-params="params"
      :total-pages="totalPages"
      @update:pagination-params="handlePaginationChange"
      @handleOpenModalDetail="handleOpenModalDetail"
    />
    <operation-log-detail
      :open="open"
      :operation-log-detail="operationLogDetail || null"
      :loading="isLoadingDetail"
      @handleCloseModal="handleCloseModal"
    />
  </div>
</template>

<script lang="ts" setup>
import OperationLogTable from "@/pages/superadmin/operation/OperationLogTable.vue";
import OperationLogFilter from "@/pages/superadmin/operation/OperationLogFilter.vue";
import OperationLogDetail from "@/pages/superadmin/operation/OperationLogDetail.vue";
import { computed, ref } from "vue";
import {
  useDetailOperationLogs,
  useGetOperationLogs,
} from "@/services/service/superadmin/operationlog.action";
import { keepPreviousData } from "@tanstack/vue-query";
import {
  OperationLogResponse,
  ParamsGetOperationLogs,
} from "@/services/api/superadmin/operationlog.api";

const params = ref<ParamsGetOperationLogs>({
  page: 1,
  size: 10,
});

const open = ref(false);
const operationLogId = ref<string | null>(null);

const { data, isLoading, isFetching } = useGetOperationLogs(params, {
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

const handlePaginationChange = (newParams: ParamsGetOperationLogs) => {
  params.value = { ...params.value, ...newParams };
};

const handleFilter = (newParams: ParamsGetOperationLogs) => {
  params.value = { ...params.value, ...newParams };
};

const handleCloseModal = () => {
  open.value = false;
  operationLogId.value = null;
};

const handleOpenModalDetail = (record: OperationLogResponse) => {
  console.log(record);
  operationLogId.value = record.id;
  open.value = true;
};

const operationLogData = computed(() => data?.value?.data?.data || []);
const totalPages = computed(() => data?.value?.data?.totalPages || 0);
const operationLogDetail = computed(() => dataDetail.value?.data || null);
</script>
