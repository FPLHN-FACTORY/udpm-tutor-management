<template>
    <div class="mt-4">
        <div class="flex justify-between items-center">
            <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
                <v-icon name="hi-office-building" scale="2" />
                <span class="m-2 text-3xl">Quản lý lớp tutor</span>
            </h2>
        </div>
        <tutor-class-filter @filter="handleFilter" />
        <tutor-class-table
            :data-source="tutorClassData"
            :loading="isLoading || isFetching"
            :pagination-params="params"
            :total-pages="totalPages"
            @update:pagination-params="handleChangePagination"
        />
    </div>
</template>

<script lang="ts" setup>
import { useAuthStore } from '@/stores/auth';
import TutorClassFilter from './TutorClassFilter.vue';
import TutorClassTable from './TutorClassTable.vue';
import { computed, ref } from 'vue';
import { ParamsGetTutorClass } from '@/services/api/teacher/tutor-class.api';
import { useGetTutorClassByTeacher } from '@/services/service/teacher/tutor-class.action';
import { keepPreviousData } from '@tanstack/vue-query';

const { user } = useAuthStore();

const params = ref<ParamsGetTutorClass>({
    page: 1,
    size: 5,
    subjectId: ""
})

const handleChangePagination = (newParams: ParamsGetTutorClass) => {
  params.value = { ...params.value, ...newParams }
}

const handleFilter = (newParams: ParamsGetTutorClass) => {
  params.value = { ...params.value, ...newParams, subjectId: newParams.subjectId || null }

}


const { data, isLoading, isFetching } = useGetTutorClassByTeacher(user?.userId || null, params, {
    refetchOnWindowFocus: false,
    placeholderData: keepPreviousData,
    enabled: !!user?.userId
})

const tutorClassData = computed(() => data.value?.data.data || []);
const totalPages = computed(() => data.value?.data.totalPages || 1);
</script>