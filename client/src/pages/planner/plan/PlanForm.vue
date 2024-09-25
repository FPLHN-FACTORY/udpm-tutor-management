<template>
  <div class="mt-4">
    <div class="flex justify-between items-center mb-5">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="hi-office-building" scale="2" />
        <span class="m-2 text-3xl">Quản lý kế hoạch</span>
      </h2>
    </div>
    <div class="shadow-md p-3 rounded-md mx-3 my-15">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="bi-exclamation-circle" scale="2" />
        <span class="ml-2 text-2xl">Thông tin kế hoạch kỳ hiện tại</span>
      </h2>
      <div class="p-5">
        <a-descriptions title="">
          <a-descriptions-item label="Học kỳ">{{ semesterData?.planName }}</a-descriptions-item>
          <a-descriptions-item label="Bộ môn">{{ semesterData?.departmentName }}</a-descriptions-item>
          <a-descriptions-item label="Cơ sở">{{ semesterData?.facilityName }}</a-descriptions-item>
          <a-descriptions-item label="Ngày bắt đầu">{{ getDateFormat(semesterData?.startTime, false) }}</a-descriptions-item>
          <a-descriptions-item label="Ngày kết thúc">
            {{ getDateFormat(semesterData?.endTime, false) }}
          </a-descriptions-item>
        </a-descriptions>
        <div>
          <a-tabs v-model:activeKey="activeKey">
            <template v-for="(block, index) in planInfoData" :key="index">
              <a-tab-pane :tab="`Kế hoạch block ${index + 1}`" force-render>
                <a-descriptions title="" class="p-4">
                  <a-descriptions-item label="Trạng thái">{{ block.status }}</a-descriptions-item>
                  <a-descriptions-item label="Số môn tutor">{{ block.numberSubjects }}</a-descriptions-item>
                  <a-descriptions-item label="Số lớp tutor">{{ block.numberClasses }}</a-descriptions-item>
                  <a-descriptions-item label="Số sinh viên tham gia">0</a-descriptions-item>
                  <a-descriptions-item label="Số giảng viên tham gia">0</a-descriptions-item>
                  <a-descriptions-item label="Trưởng môn đã thêm môn">0</a-descriptions-item>
                  <a-descriptions-item label="Trưởng môn chưa thêm môn">0</a-descriptions-item>
                  <a-descriptions-item label="Đánh giá">0</a-descriptions-item>
                </a-descriptions>
              </a-tab-pane>
            </template>
          </a-tabs>
        </div>
      </div>
    </div>
    <plan-filter
        @filter="handleFilter"
        :semesterOptions="semesterOptions"
        :currentSemester="userInfo?.semesterId"
    />
    <plan-table
      :data-source="planData"
      :loading="isLoading || isFetching"
      :pagination-params="params"
      :total-pages="totalPages"
      @update:pagination-params="handlePaginationChange"
      @handleOpenModalUpdate="handleOpenModalUpdate"
      @handleOpenModalAdd="handleOpenModalAdd"
    />
    <create-plan-modal
        :open="open"
        @handleClose="handleClose"
        @cancel="open = false"
        :plan-detail="planDetail || null"
        :is-loading-detail="isLoadingDetail || false"
        :semesterOptions="semesterOptions"
    />
  </div>
</template>

<script lang="ts" setup>
import PlanTable from "@/pages/planner/plan/PlanTable.vue";
import PlanFilter from "@/pages/planner/plan/PlanFilter.vue";
import CreatePlanModal from "@/pages/planner/plan/CreatePlanModal.vue";
import { ParamsGetPlans, PlanResponse } from "@/services/api/planner/plan.api";
import {
  useDetailPlan, useGetPlanInfo,
  useGetPlans, useGetSemesterInfo,
} from "@/services/service/planner/plan.action";
import { keepPreviousData } from "@tanstack/vue-query";
import { computed, ref } from "vue";
import {useGetSemesterOptions} from "@/services/service/common.action.ts";
import {useAuthStore} from "@/stores/auth.ts";
import {getDateFormat} from "../../../utils/common.helper.ts";

const auth = useAuthStore();
const userInfo = computed(() => auth.user);
const open = ref(false);
const planId = ref<string | null>(null);
const activeKey = ref('1');

const params = ref<ParamsGetPlans>({
  page: 1,
  size: 10,
  semesterId: userInfo.value?.semesterId,
  departmentCode: userInfo.value?.departmentCode,
  facilityCode: userInfo.value?.facilityCode
});

const { data, isLoading, isFetching } = useGetPlans(params, {
  refetchOnWindowFocus: false,
  placeholderData: keepPreviousData,
});

const { data: semesterInfo } = useGetSemesterInfo(params.value, {
  refetchOnWindowFocus: false,
  enabled: () => !!params.value.semesterId,
});

const { data: planInfo } = useGetPlanInfo(params.value, {
  refetchOnWindowFocus: false,
  enabled: () => !!params.value.semesterId,
});

const { data: dataDetail, isLoading: isLoadingDetail } = useDetailPlan(
  planId,
  {
    refetchOnWindowFocus: false,
    enabled: () => !!planId.value,
  }
);

const handlePaginationChange = (newParams: ParamsGetPlans) => {
  params.value = { ...params.value, ...newParams };
};

const handleFilter = (newParams: ParamsGetPlans) => {
  params.value = { ...params.value, ...newParams };
};

const handleClose = () => {
  open.value = false;
  planId.value = null;
};

const handleOpenModalAdd = () => {
  open.value = true;
  planId.value = null;
};

const handleOpenModalUpdate = (record: PlanResponse) => {
  planId.value = record.id;
  open.value = true;
};

const { data: semesterOptionsData } = useGetSemesterOptions();

const semesterOptions = computed(() =>
    semesterOptionsData?.value?.data.map((semester) => ({
      value: semester.id,
      label: semester.name,
    }))
);

const planData = computed(() => data?.value?.data?.data || []);
const semesterData = computed(() => semesterInfo.value?.data);
const planInfoData = computed(() => planInfo.value?.data || [{id: 'x', name : 'x'},{id: 'y', name : 'x'} ]);
const totalPages = computed(() => data?.value?.data?.totalPages || 0);
const planDetail = computed(() =>
  planId.value
    ? {
        ...dataDetail.value?.data,
        planId: planId.value,
      }
    : null
);
</script>
