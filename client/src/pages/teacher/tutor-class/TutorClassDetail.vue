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
          @handleOpenModalUpdate="handleOpenModalUpdate"
          @handleOpenModalAdd="handleOpenModalAdd"
        />
        <create-update-evidence
          :open="open"
          :evidenceDetail="evidenceDetail || null"
          :lectureId="lectureId"
          @handleClose="handleClose"
          @cancel="open = false"
          :is-loading-detail="isLoadingDetail"
        />
    </div>
</template>

<script lang="ts" setup>
import TutorClassDetailTable from './TutorClassDetailTable.vue';
import { computed, ref } from 'vue';
import {
  useGetEvidenceLectureDetail,
  useGetLectureByTutorClassDetail,
} from '@/services/service/teacher/tutor-class.action';
import { keepPreviousData } from '@tanstack/vue-query';
import {useRoute} from "vue-router";
import CreateUpdateEvidence from './CreateUpdateEvidence.vue';
import { LectureResponse } from '@/services/api/teacher/tutor-class.api';

const open = ref(false);
const route = useRoute();
const lectureId = ref<string | null>(null);
let tutorClassDetailId = computed(() => {
  const id = route.params.tutorClassDetailId;
  return Array.isArray(id) ? id[0] : id || null;
});

const handleOpenModalAdd = (record: LectureResponse) => {
  lectureId.value =  record.id
  open.value = true;
};

const handleOpenModalUpdate = (record: LectureResponse) => {
  open.value = true;
};


const { data: lectureList, isLoading: isLoadingLectureList } = useGetLectureByTutorClassDetail(tutorClassDetailId, {
  refetchOnWindowFocus: false,
  placeholderData: keepPreviousData,
  enabled: !!tutorClassDetailId && open.value == false
})

const { data: evidenceLectureDetail, isLoading: isLoadingDetail } = useGetEvidenceLectureDetail(lectureId,
  {
    refetchOnWindowFocus: false,
    enabled: () => !!lectureId.value,
  }
)

const handleClose = () => {
  open.value = false;
  lectureId.value = null;
};

const lectureListData = computed(() => lectureList.value?.data || []);
const evidenceDetail = computed(() =>
lectureId.value ? {
    ...evidenceLectureDetail.value?.data,
  } : null)

</script>