<template>
  <div class="mt-4">
    <div class="flex justify-between items-center">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="hi-clipboard-check" scale="2" />
        <span class="m-2 text-3xl">Điểm danh </span>
      </h2>

      <div class="flex justify-center items-center me-3">
        <a-button @click="goBack" type="primary">Trở lại</a-button>
      </div>
    </div>
    <student-attendance-table
      :data-source="attendanceListData"
      :loading="isLoadingStudentAttendanceList"
      :lectureId="lectureId"
    />
  </div>
</template>

<script lang="ts" setup>
import { useRoute, useRouter } from 'vue-router';
import StudentAttendanceTable from './StudentAttendanceTable.vue';
import { computed, watch } from 'vue';
import { useGetStudentAttendance } from '@/services/service/teacher/student-attendance.action';
import { keepPreviousData } from '@tanstack/vue-query';

const route = useRoute();
const routerBack = useRouter();
let lectureId = computed(() => {
  const id = route.params.lectureId;
  return Array.isArray(id) ? id[0] : id || null;
});

const { data: studentAttendanceList, isLoading: isLoadingStudentAttendanceList, refetch: refetchStudentAttendance } = useGetStudentAttendance(lectureId, {
  refetchOnWindowFocus: false,
  placeholderData: keepPreviousData,
  enabled: !!lectureId
})

const attendanceListData = computed(() => studentAttendanceList.value?.data || []);

const goBack = () => {
  routerBack.back();
};

watch(lectureId, () => {
  if (lectureId.value) {
    refetchStudentAttendance();
  }
});
</script>