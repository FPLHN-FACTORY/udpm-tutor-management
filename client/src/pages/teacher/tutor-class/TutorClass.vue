<template>
    <div class="mt-4">
        <div class="flex justify-between items-center">
            <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
              <v-icon name="hi-office-building" scale="2" />
              <span class="m-2 text-3xl">Quản lý lớp tutor</span>
            </h2>
        </div>
        <tutor-class-filter
            @filter="handleFilter"
            :planOptions="planOptions"
            :planId="planId"
        />
        <tutor-class-table
          :data-source="tutorClassData"
          :loading="isLoading || isFetching"
          :pagination-params="params"
          :total-pages="totalPages"
          :canUpdate="!canUpdate"
          @update:pagination-params="handleChangePagination"
        />
    </div>
</template>

<script lang="ts" setup>
import { useAuthStore } from '@/stores/auth';
import TutorClassFilter from './TutorClassFilter.vue';
import TutorClassTable from './TutorClassTable.vue';
import { computed, ref, watch } from 'vue';
import {ParamsGetPlan, ParamsGetTutorClass} from '@/services/api/teacher/tutor-class.api';
import {
  useGetPlan,
  useGetTutorClassByTeacher
} from '@/services/service/teacher/tutor-class.action';
import { keepPreviousData } from '@tanstack/vue-query';

const auth = useAuthStore();
const userInfo = computed(() => auth.user);

const paramsPlan = ref<ParamsGetPlan>({
  departmentCode: userInfo.value?.departmentCode,
  facilityCode: userInfo.value?.facilityCode,
  blockId: userInfo.value?.blockId,
});

const { data: planOptionsData } = useGetPlan(paramsPlan);

const planOptions = computed(() => {
  return planOptionsData?.value?.data?.map((item) => ({
    value: item.id,
    label: item.name,
    isCurrent: item.isCurrent,
    planStatus: item.planStatus
  })) || [];
});

const planId = computed(() => {
  return planOptions.value.find((item) => item.isCurrent)?.value || null;
});

const params = ref<ParamsGetTutorClass>({
  page: 1,
  size: 5,
  subjectId: "",
  planId: planId.value,
  teacherId: userInfo.value?.userId
});

const handleChangePagination = (newParams: ParamsGetTutorClass) => {
  params.value = { ...params.value, ...newParams };
};

const handleFilter = (newParams: ParamsGetTutorClass) => {
  params.value = { ...params.value, ...newParams };
};

const { data, isLoading, isFetching } = useGetTutorClassByTeacher(params, {
  refetchOnWindowFocus: false,
  placeholderData: keepPreviousData,
  enabled: !!userInfo.value?.userId,
});

const tutorClassData = computed(() => data.value?.data.data || []);
const totalPages = computed(() => data.value?.data.totalPages || 1);
const canUpdate = computed(() => {
  const planOption = planOptions.value.find((item) => item.value === params.value.planId);
  return planOption ? planOption.planStatus !== 'IN_PROGRESS' && planOption.isCurrent == 1 : false;
});

watch(planId, (newPlanId) => {
  if (newPlanId) {
    params.value.planId = newPlanId;
  }
});
</script>
