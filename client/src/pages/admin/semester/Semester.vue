<template>
  <div class="mt-4">
    <div class="flex justify-between items-center">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="hi-office-building" scale="2" />
        <span class="m-2 text-3xl">Quản lý học kỳ</span>
      </h2>
    </div>
    <semester-table
      :data-source="semesterData"
      :loading="isLoading || isFetching"
      :pagination-params="params"
      :total-pages="totalPages"
      @update:pagination-params="handlePaginationChange"
      @handleOpenModalUpdate="handleOpenModalUpdate"
    />
  </div>
</template>

<script lang="ts" setup>
import SemesterTable from "@/pages/admin/semester/SemesterTable.vue";
import {
  SemesterResponse,
  ParamsGetSemester,
} from "@/services/api/semester.api";
import {
  useDetailSemester,
  useGetSemester,
} from "@/services/service/semester.action";
import { keepPreviousData } from "@tanstack/vue-query";
import { computed, ref } from "vue";

const params = ref<ParamsGetSemester>({
  page: 1,
  size: 10,
});

const open = ref(false);

const semesterId = ref<string | null>(null);

const { data, isLoading, isFetching } = useGetSemester(params, {
  refetchOnWindowFocus: false,
  placeholderData: keepPreviousData,
});

const { data: dataDetail, isLoading: isLoadingDetail } = useDetailSemester(
  semesterId,
  {
    refetchOnWindowFocus: false,
    enabled: () => !!semesterId.value,
  }
);

const handlePaginationChange = (newParams: ParamsGetSemester) => {
  params.value = { ...params.value, ...newParams };
};

const handleFilter = (newParams: ParamsGetSemester) => {
  params.value = { ...params.value, ...newParams };
};

const handleClose = () => {
  open.value = false;
  semesterId.value = null;
};

const handleOpenModalUpdate = (record: SemesterResponse) => {
  semesterId.value = record.id;
  open.value = true;
};

const semesterData = computed(() => data?.value?.data?.data || []);
const totalPages = computed(() => data?.value?.data?.totalPages || 0);
const semesterDetail = computed(() =>
  dataDetail.value?.data
    ? {
        ...dataDetail.value?.data,
        semesterId: semesterId?.value,
      }
    : null
);
</script>
