<template>
  <div class="mt-4">
    <div class="flex justify-between items-center mb-5">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="hi-office-building" scale="2" />
        <span class="m-2 text-3xl">Quản lý kế hoạch</span>
      </h2>
    </div>
    <div class="shadow-md p-3 rounded-md mx-3 mb-10">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="bi-exclamation-circle" scale="2" />
        <span class="ml-2 text-2xl">Thông tin kế hoạch kỳ hiện tại</span>
      </h2>
      <div class="p-5">
        <template v-if="userInfo?.semesterId">
          <a-descriptions title="">
            <a-descriptions-item label="Học kỳ">{{ semesterData?.planName }}</a-descriptions-item>
            <a-descriptions-item label="Bộ môn">{{ semesterData?.departmentName }}</a-descriptions-item>
            <a-descriptions-item label="Cơ sở">{{ semesterData?.facilityName }}</a-descriptions-item>
            <a-descriptions-item label="Ngày bắt đầu">{{ getDateFormat(semesterData?.startTime, false) }}</a-descriptions-item>
            <a-descriptions-item label="Ngày kết thúc">{{ getDateFormat(semesterData?.endTime, false) }}</a-descriptions-item>
          </a-descriptions>
          <div class="mt-4">
            <template v-if="planInfoData && planInfoData.length > 0">
              <a-tabs v-model:activeKey="activeKey">
                <template v-for="(plan, index) in planInfoData" :key="plan.id || index">
                  <a-tab-pane :tab="`Kế hoạch block ${formatBlockName(plan.blockName)}`" force-render>
                    <plan-form :plan="plan" />
                  </a-tab-pane>
                </template>
              </a-tabs>
            </template>
            <template v-else>
              <div class="p-4 text-center text-red-500">
                Chưa có kế hoạch trong học kỳ này
              </div>
            </template>
          </div>
        </template>
        <template v-else>
          <div class="p-4 text-center text-red-500">
            Bạn đang chưa có trong học kỳ nào
          </div>
        </template>
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
        :semesterId="userInfo?.semesterId || null"
        :blockId="userInfo?.blockId || null"
    />
  </div>
</template>

<script lang="ts" setup>
import PlanTable from "@/pages/planner/plan/PlanTable.vue";
import PlanForm from "@/pages/planner/plan/PlanForm.vue";
import PlanFilter from "@/pages/planner/plan/PlanFilter.vue";
import CreatePlanModal from "@/pages/planner/plan/CreatePlanModal.vue";
import { ParamsGetPlans, PlanResponse } from "@/services/api/planner/plan.api";
import {
  useDetailPlan, useGetPlanInfo,
  useGetPlans, useGetSemesterInfo,
} from "@/services/service/planner/plan.action";
import { keepPreviousData } from "@tanstack/vue-query";
import {computed, ref, watch} from "vue";
import { useGetSemesterOptions } from "@/services/service/common.action.ts";
import { useAuthStore } from "@/stores/auth.ts";
import {formatBlockName, getDateFormat} from "@/utils/common.helper.ts";

const auth = useAuthStore();
const userInfo = computed(() => auth.user);
const open = ref(false);
const planId = ref<string | null>(null);
const activeKey = ref('1'); // Default to the first tab (0-indexed)

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

const { data: planInfo, refetch: refetchPlanInfo } = useGetPlanInfo(params.value, {
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

watch(
    () => data.value,
    (newData) => {
      if (newData) {
        // Gọi refetch khi data thay đổi
        refetchPlanInfo();
      }
    },
    { immediate: true } // Optional: Call the function immediately on component mount
);

const planData = computed(() => data?.value?.data?.data || []);
const semesterData = computed(() => semesterInfo.value?.data);
const planInfoData = computed(() => planInfo.value?.data || null);
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
