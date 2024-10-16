<template>
    <div class="mt-4">
        <div class="flex justify-between items-center">
            <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
                <v-icon name="hi-office-building" scale="2" />
                <span class="m-2 text-3xl">Quản lý buổi học</span>
            </h2>
        </div>
        <div class="shadow-md p-3 rounded-md mx-3 my-15">
          <div class="p-5">
            <h2 class="text-center text-xl font-semibold mb-4">Thông tin chi tiết lớp tutor</h2>
            <a-descriptions title="" class="p-4">
              <a-descriptions-item label="Mã lớp">x</a-descriptions-item>
              <a-descriptions-item label="Môn học"></a-descriptions-item>
              <a-descriptions-item label="Trợ giảng"></a-descriptions-item>
              <a-descriptions-item label="Hình thức"></a-descriptions-item>
              <a-descriptions-item label="Sĩ số"></a-descriptions-item>
              <a-descriptions-item label="Số SV nghỉ"></a-descriptions-item>
              <a-descriptions-item label="Tiến trình"></a-descriptions-item>
            </a-descriptions>
          </div>
        </div>
        <tutor-class-detail-table
            :data-source="lectureListData"
            :loading="isLoadingLectureList"
        />
    </div>
</template>

<script lang="ts" setup>
import TutorClassDetailTable from './TutorClassDetailTable.vue';
import { computed } from 'vue';
import {
  useGetLectureByTutorClassDetail,
} from '@/services/service/teacher/tutor-class.action';
import { keepPreviousData } from '@tanstack/vue-query';
import {useRoute} from "vue-router";

const route = useRoute();
let tutorClassDetailId = computed(() => {
  const id = route.params.tutorClassDetailId;
  return Array.isArray(id) ? id[0] : id || null;
});

const { data: lectureList, isLoading: isLoadingLectureList } = useGetLectureByTutorClassDetail(tutorClassDetailId, {
  refetchOnWindowFocus: false,
  placeholderData: keepPreviousData,
  enabled: !!tutorClassDetailId
})

const lectureListData = computed(() => lectureList.value?.data || []);
</script>