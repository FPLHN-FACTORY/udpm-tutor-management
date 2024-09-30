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
          <a-descriptions-item label="Tên kế hoạch" >{{ plan.planName }}</a-descriptions-item>
          <a-descriptions-item label="Block">{{ formatBlockName(plan.blockName) }}</a-descriptions-item>
          <a-descriptions-item label="Trạng thái"><a-tag :color="getTagColor(plan.status)">{{ getTagStatus(plan.status) }}</a-tag></a-descriptions-item>
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
        <tutor-class-table
            :data-source="planData"
            :loading="isLoading || isFetching"
            :pagination-params="params"
            :total-pages="totalPages"
            @update:pagination-params="handlePaginationChange"
        />
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import TutorClassTable from "@/pages/headdepartment/plan/TutorClassTable.vue";
import { useRoute } from "vue-router";
import { computed, ref } from "vue";
import { useGetPlanInfoById } from "@/services/service/headdepartment/plan.action.ts";
import { formatBlockName, getDateFormat, getTagColor, getTagStatus} from "@/utils/common.helper.ts";
import { ParamsGetPlans } from "@/services/api/headdepartment/plan.api.ts";
import { keepPreviousData } from "@tanstack/vue-query";
import { useGetTutorClass } from "@/services/service/headdepartment/tutor-class.action.ts";

const route = useRoute();
const planId = computed(() => {
  const id = route.params.planId;
  return Array.isArray(id) ? id[0] : id || null;
});

const params = ref<ParamsGetTutorClass>({
  page: 1,
  size: 10,
  planId: planId.value,
});

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

const plan = computed(() => planInfo.value?.data || null);
const planData = computed(() => data?.value?.data?.data || []);
const totalPages = computed(() => data?.value?.data?.totalPages || 0);
</script>
