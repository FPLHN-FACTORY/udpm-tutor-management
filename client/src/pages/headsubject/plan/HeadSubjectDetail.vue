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
        <head-subject-detail-table
            :data-source="planData"
            :loading="isLoading || isFetching"
            :pagination-params="params"
            :total-pages="totalPages"
            @update:pagination-params="handlePaginationChange"
            @handleOpenModalAddNumberTutorClass="handleOpenModalAdd"
            @handleApproveTutorClass="handleApproveTutorClass"/>
        <add-number-tutor-class
            :open="open"
            @resetTable="resetTable"
            @handleClose="handleClose"
            @cancel="open = false"
            :planId="planId || null"
            :subjectId="subjectId"
            :numberClasses="numberClasses"
        />
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import HeadSubjectDetailTable from "@/pages/headsubject/plan/HeadSubjectDetailTable.vue";
import AddNumberTutorClass from "@/pages/headsubject/plan/AddNumberTutorClass.vue";
import { useRoute } from "vue-router";
import { computed, ref, watch } from "vue";
import { useDetailPlan, useGetPlanInfoById } from "@/services/service/headsubject/plan.action.ts";
import { formatBlockName, getDateFormat, getTagColor, getTagStatus } from "@/utils/common.helper.ts";
import {ParamsGetPlans, TutorClassResponse} from "@/services/api/headsubject/plan.api.ts";
import { keepPreviousData } from "@tanstack/vue-query";
import { useAuthStore } from "@/stores/auth.ts";
import { ParamsGetTutorClass } from "@/services/api/headsubject/tutor-class.api.ts";
import {
  useGetTutorClass,
  useUpdateStatusApproveTutorClassHeadSubject
} from "@/services/service/headsubject/tutor-class.action.ts";
import {toast} from "vue3-toastify";
import {ERROR_MESSAGE} from "@/constants/message.constant.ts";

const route = useRoute();
let planId = computed(() => {
  const id = route.params.planId;
  return Array.isArray(id) ? id[0] : id || null;
});

const auth = useAuthStore();
const userInfo = computed(() => auth.user);
const open = ref(false);

const params = ref<ParamsGetTutorClass>({
  page: 1,
  size: 10,
  planId: planId.value,
  facilityId: userInfo?.facilityId,
  staffId: userInfo?.userId,
  semesterId: userInfo?.semesterId,
});

const subjectId = ref<string | null>(null);
const tutorClassId = ref<string | null>(null);
const numberClasses = ref<number | null>(null);

watch(userInfo, (newValue) => {
  params.value.facilityId = newValue.facilityId;
  params.value.semesterId = newValue.semesterId;
  params.value.staffId = newValue.userId;
}, { immediate: true });

const { data, isLoading, isFetching } = useGetTutorClass(params, {
  refetchOnWindowFocus: false,
  placeholderData: keepPreviousData,
  enabled: () => !!planId.value,
});

const handlePaginationChange = (newParams: ParamsGetPlans) => {
  params.value = { ...params.value, ...newParams };
};

const { data: planInfo } = useGetPlanInfoById(planId.value, {
  refetchOnWindowFocus: false,
  enabled: () => !!planId.value,
});

const handleOpenModalAdd = async (record: TutorClassResponse) => {
  subjectId.value = record.subjectId;
  numberClasses.value = record.numberClasses || null;
  tutorClassId.value = record.id;
  open.value = true;
};

const handleClose = () => {
  open.value = false;
  tutorClassId.value = null;
  subjectId.value = null;
  numberClasses.value = null;
};

const { mutate: updateStatus } = useUpdateStatusApproveTutorClassHeadSubject();

const handleApproveTutorClass = async (id: string) => {
  try {
    updateStatus(id, {
      onSuccess: () => {
        toast.success("Xắc nhận phê duyệt lớp môn thành công");
        handleClose();
      },
      onError: (error) => {
        toast.error(
            error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
        )
      },
    });
  } catch (error) {
    console.error(error);
  }
};

const plan = computed(() => planInfo.value?.data || null);
const planData = computed(() => data?.value?.data?.data || []);
const totalPages = computed(() => data?.value?.data?.totalPages || 0);
</script>