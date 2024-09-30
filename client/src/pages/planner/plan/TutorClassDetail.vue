<template>
  <div class="mt-4">
    <div class="flex justify-between items-center mb-5">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="hi-office-building" scale="2" />
        <span class="m-2 text-3xl">Chi Tiết Lớp Tutor</span>
      </h2>
    </div>

    <div class="shadow-md p-3 rounded-md mx-3 mt-10">
      <div class="p-5">
        <tutor-class-detail-table
            :data-source="planData"
            :tutorClassDetailData="tutorClassDetailData"
            :loading="isLoading"
            :pagination-params="params"
            :total-pages="totalPages"
            @update:pagination-params="handlePaginationChange"
        />
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ParamsGetTutorClassDetail } from '@/services/api/headsubject/tutor-class.api.ts';
import {useGetDetailTutorClass, useListTutorClassDetail} from '@/services/service/headsubject/tutor-class.action.ts';
import { keepPreviousData } from '@tanstack/vue-query';
import { computed, ref } from 'vue';
import { useRoute } from 'vue-router';
import TutorClassDetailTable from '@/pages/planner/plan/TutorClassDetailTable.vue';
import { ParamsGetPlans } from '@/services/api/headsubject/plan.api.ts';

const route = useRoute();
let tutorClassId = computed(() => {
  const id = route.params.tutorClassId;
  return Array.isArray(id) ? id[0] : id || null;
});
const params = ref<ParamsGetTutorClassDetail>({
  page: 1,
  size: 10,
  tutorClassId: tutorClassId.value,
});

const { data, isLoading } = useListTutorClassDetail(params, {
  refetchOnWindowFocus: false,
  placeholderData: keepPreviousData,
  enabled: () => !!tutorClassId.value,
});

const { data: tutorClassDetail } = useGetDetailTutorClass(tutorClassId, {
  refetchOnWindowFocus: false,
  enabled: () => !!tutorClassId.value,
});

const handlePaginationChange = (newParams: ParamsGetPlans) => {
  params.value = { ...params.value, ...newParams };
};

const planData = computed(() => data?.value?.data?.data || []);
const tutorClassDetailData = computed(() => tutorClassDetail?.value?.data || []);
const totalPages = computed(() => data?.value?.data?.totalPages || 0);

</script>