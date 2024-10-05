<template>
  <div class="mt-4">
    <div class="flex justify-between items-center mb-5">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="hi-office-building" scale="2" />
        <span class="m-2 text-3xl">Chi tiết kế hoạch</span>
      </h2>
    </div>
    <div class="shadow-md p-3 rounded-md mx-3 my-15">
      <div class="p-5">
        <h2 class="text-center text-xl font-semibold mb-4">Thông tin chi tiết kế hoạch</h2>
        <a-descriptions title="" class="p-4" v-if="plan">
          <a-descriptions-item label="Tên kế hoạch">{{ plan.planName }}</a-descriptions-item>
          <a-descriptions-item label="Block">{{ formatBlockName(plan.blockName) }}</a-descriptions-item>
          <a-descriptions-item label="Trạng thái"><a-tag :color="getTagColor(plan.status)">{{ getTagStatus(plan.status)
            }}</a-tag></a-descriptions-item>
          <a-descriptions-item label="Số môn tutor">{{ plan.numberSubjects }}</a-descriptions-item>
          <a-descriptions-item label="Số lớp tutor">{{ plan.numberClasses }}</a-descriptions-item>
          <a-descriptions-item label="Số sinh viên tham gia">0</a-descriptions-item>
          <a-descriptions-item label="Số giảng viên tham gia">0</a-descriptions-item>
          <a-descriptions-item label="Trưởng môn đã thêm môn">0</a-descriptions-item>
          <a-descriptions-item label="Trưởng môn chưa thêm môn">0</a-descriptions-item>
          <a-descriptions-item label="Ngày bắt đầu">{{ getDateFormat(plan?.startTime, false) }}</a-descriptions-item>
          <a-descriptions-item label="Ngày kết thúc">{{ getDateFormat(plan?.endTime, false) }}</a-descriptions-item>
          <a-descriptions-item label="Đánh giá">0</a-descriptions-item>
        </a-descriptions>
      </div>
    </div>
    <div class="shadow-md p-3 rounded-md mx-3 mt-10">
      <div class="p-5">
        <div class="flex justify-end items-center gap-2">
          <p>Chế độ xem</p>
          <a-switch
              v-model:checked="canUpdate"
              :checked-children="'All'"
              :un-checked-children="'Only Me'"
          />
        </div>
        <a-tabs v-model:activeKey="activeKey">
          <a-tab-pane key="1" tab="Danh sách môn tutor">
            <head-subject-detail-table
                :data-source="tutorClass"
                :loading="isLoadingTutorClassData"
                :pagination-params="paramsTutorClass"
                :total-pages="totalPagesTutorClass"
                :plan="plan"
                :canUpdate="canPerformUpdate"
                @update:pagination-params="handlePaginationTutorClassChange"
                @handleOpenModalAddNumberTutorClass="handleOpenModalAdd"
            />
          </a-tab-pane>
          <a-tab-pane key="2" tab="Danh sách lớp tutor">
            <head-subject-detail-filter
                @filter="handleFilter"
                :teacherOption="teacherOption"
            />
            <tutor-class-detail-table
                :data-source="tutorClassDetail"
                :loading="isLoadingTutorClassDetailData"
                :pagination-params="paramsTutorClassDetail"
                :total-pages="totalPagesTutorClassDetail"
                :teacherOption="teacherOption"
                :studentOption="studentOption"
                :canUpdate="canPerformUpdate"
                @update:pagination-params="handlePaginationTutorClassDetailChange"
            />
          </a-tab-pane>
        </a-tabs>
        <add-number-tutor-class
            :open="open"
            @handleClose="handleClose"
            @cancel="open = false"
            :tutorClass="detailTutorClass"
            :planId="planId"
            :subjectId="subjectId"
            :isLoading="isLoadingDetailTutorClass"
        />
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import HeadSubjectDetailTable from "@/pages/headsubject/plan/TutorClassTable.vue";
import AddNumberTutorClass from "@/pages/headsubject/plan/AddNumberTutorClass.vue";
import TutorClassDetailTable from "@/pages/headsubject/plan/TutorClassDetailTable.vue";
import { ParamsGetPlans, TutorClassResponse } from "@/services/api/headsubject/plan.api.ts";
import { ParamsGetTutorClass, ParamsGetTutorClassDetail } from "@/services/api/headsubject/tutor-class.api.ts";
import { ParamsStaffSearchByRole } from "@/services/api/common.api.ts";
import { useRoute } from "vue-router";
import { computed, ref, watch } from "vue";
import { useGetPlanInfoById } from "@/services/service/headsubject/plan.action.ts";
import { formatBlockName, getDateFormat, getTagColor, getTagStatus } from "@/utils/common.helper.ts";
import { keepPreviousData } from "@tanstack/vue-query";
import { useAuthStore } from "@/stores/auth.ts";
import { useGetDetailTutorClass, useGetTutorClass, useListTutorClassDetail } from "@/services/service/headsubject/tutor-class.action.ts";
import {useGetStaffByRoleOptions, useGetStudentTutorOptions} from "@/services/service/common.action.ts";

const route = useRoute();
let planId = computed(() => {
  const id = route.params.planId;
  return Array.isArray(id) ? id[0] : id || null;
});

const auth = useAuthStore();
const userInfo = computed(() => auth.user);
const open = ref(false);
const activeKey = ref('1');
const canUpdate = ref(false);
const subjectId = ref<string | null>(null);
const tutorClassId = ref<string | null>(null);

const paramsTutorClass = ref<ParamsGetTutorClass>({
  page: 1,
  size: 10,
  planId: planId.value,
  facilityId: userInfo.value?.facilityId,
  userId: userInfo.value?.userId,
  semesterId: userInfo.value?.semesterId,
});

const paramsTutorClassDetail = ref<ParamsGetTutorClassDetail>({
  page: 1,
  size: 10,
  planId: planId.value,
  facilityId: userInfo.value?.facilityId,
  userId: userInfo.value?.userId,
  semesterId: userInfo.value?.semesterId,
  teacherId: null,
  query: null,
});

const paramsGetStaffOption = ref<ParamsStaffSearchByRole>({
  departmentCode: userInfo.value?.departmentCode,
  facilityCode: userInfo.value?.facilityCode,
});

const { data: tutorClassData, isLoading: isLoadingTutorClassData } = useGetTutorClass(paramsTutorClass, {
  refetchOnWindowFocus: false,
  placeholderData: keepPreviousData,
  enabled: () => !!planId.value,
});

const { data: detailTutorClassData, isLoading: isLoadingDetailTutorClass } = useGetDetailTutorClass(tutorClassId, {
  refetchOnWindowFocus: false,
  enabled: () => !!tutorClassId.value,
});

const { data: teacherOptionData } = useGetStaffByRoleOptions(paramsGetStaffOption, {
  refetchOnWindowFocus: false,
});

const { data: studentOptionData } = useGetStudentTutorOptions( {
  refetchOnWindowFocus: false,
});

const { data: tutorClassDetailData, isLoading: isLoadingTutorClassDetailData } = useListTutorClassDetail(paramsTutorClassDetail, {
  refetchOnWindowFocus: false,
  placeholderData: keepPreviousData,
});

const handlePaginationTutorClassChange = (newParams: ParamsGetPlans) => {
  paramsTutorClass.value = { ...paramsTutorClass.value, ...newParams };
};

const handlePaginationTutorClassDetailChange = (newParams: ParamsGetPlans) => {
  paramsTutorClassDetail.value = { ...paramsTutorClassDetail.value, ...newParams };
};

const handleFilter = (newParams: ParamsGetPlans) => {
  paramsTutorClassDetail.value = { ...paramsTutorClassDetail.value, ...newParams };
};

const { data: planInfo } = useGetPlanInfoById(planId.value, {
  refetchOnWindowFocus: false,
  enabled: () => !!planId.value,
});

const handleOpenModalAdd = async (record: TutorClassResponse) => {
  tutorClassId.value = record.id;
  subjectId.value = record.subjectId;
  open.value = true;
};

const handleClose = () => {
  open.value = false;
  tutorClassId.value = null;
  subjectId.value = null;
};

watch(canUpdate, (newValue) => {
  if (newValue) {
    paramsTutorClass.value.userId = null; // Set userId to null
    paramsTutorClassDetail.value.userId = null; // Set userId to null
  } else {
    paramsTutorClass.value.userId = userInfo.value?.userId; // Reset to original userId
    paramsTutorClassDetail.value.userId = userInfo.value?.userId; // Reset to original userId
  }
});

const plan = computed(() => planInfo.value?.data || null);
const tutorClass = computed(() => tutorClassData?.value?.data?.data || []);
const tutorClassDetail = computed(() => tutorClassDetailData?.value?.data?.data || []);
const detailTutorClass = computed(() => detailTutorClassData?.value?.data || null);
const teacherOption = computed(() => teacherOptionData?.value?.data.map(teacher => ({
  value: teacher.id,
  label: teacher.name
})) || []);
const totalPagesTutorClass = computed(() => tutorClassData?.value?.data?.totalPages || 0);
const totalPagesTutorClassDetail = computed(() => tutorClassDetailData?.value?.data?.totalPages || 0);
const canPerformUpdate = computed(() => {
  return (plan.value?.status != 'PLANNING') || (plan.value?.status === 'PLANNING' && canUpdate.value);
});
const studentOption = computed(() => studentOptionData?.value?.data.map(teacher => ({
  value: teacher.id,
  label: teacher.name
})) || []);
</script>