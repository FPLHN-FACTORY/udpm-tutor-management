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
                <tutor-class-detal-table 
                    :data-source="planData" 
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
import { ParamsGetTutorClassDetail } from '@/services/api/headsubject/headsubject.api';
import { useListTutorClassDetail } from '@/services/service/headsubject/headsubject.action';
import { keepPreviousData } from '@tanstack/vue-query';
import { computed, ref } from 'vue';
import { useRoute } from 'vue-router';
import TutorClassDetalTable from './TutorClassDetalTable.vue';
import { ParamsGetPlans } from '@/services/api/planner/plan.api';

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

const handlePaginationChange = (newParams: ParamsGetPlans) => {
  params.value = { ...params.value, ...newParams };
};

const planData = computed(() => data?.value?.data?.data || []);
const totalPages = computed(() => data?.value?.data?.totalPages || 0);

</script>