<template>
  <div class="mt-4">
    <div class="flex justify-between items-center mb-5">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="hi-office-building" scale="2" />
        <span class="m-2 text-3xl">Quản lý kế hoạch</span>
      </h2>
    </div>
    <head-plan-filter :semester-options="semesterOptions" @filter="handleFilter" />
    <head-plan-table :data-source="planData" :loading="isLoading || isFetching" :pagination-params="params" :total-pages="totalPages"
      @update:pagination-params="handlePaginationChange" @update:approve-plan="handleRefetch" />
  </div>
</template>

<script lang="ts" setup>
import { useGetSemesterOptions } from "@/services/service/common.action";
import HeadPlanFilter from "./HeadPlanFilter.vue";
import HeadPlanTable from "./HeadPlanTable.vue";
import { computed, ref } from "vue";
import { ParamsGetPlans } from "@/services/api/headdepartment/plan.api.ts";
import { useGetPlans } from "@/services/service/headdepartment/head-plan.action";
import { keepPreviousData } from "@tanstack/vue-query";

const params = ref<ParamsGetPlans>({
  page: 1,
  size: 5
})

const { data: semesterOptionsData } = useGetSemesterOptions();

const { data, isLoading, isFetching, refetch } = useGetPlans(params, {
  refetchOnWindowFocus: false,
  placeholderData: keepPreviousData,
});

const handleFilter = (newParams: ParamsGetPlans) => {
  params.value = { ...params.value, ...newParams };
};

const handlePaginationChange = (newParams: ParamsGetPlans) => {
  params.value = { ...params.value, ...newParams };
};

const handleRefetch = () => {
  refetch();
}

const semesterOptions = computed(() =>
  semesterOptionsData?.value?.data.map((semester) => ({
    value: semester.id,
    label: semester.name,
  }))
);

const planData = computed(() => data?.value?.data?.data || []);
const totalPages = computed(() => data?.value?.data?.totalPages || 0);
</script>