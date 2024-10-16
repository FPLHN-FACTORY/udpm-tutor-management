<template>
    <div class="mt-4">
      <div class="flex justify-between items-center">
        <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
          <v-icon name="hi-office-building" scale="2" />
          <span class="m-2 text-3xl">Quản lý người lập kế hoạch</span>
        </h2>
      </div>
      <head-subject-planner-filter :params="params" @filter="handleFilterChange" />
      <head-subject-planner-table
          :data-source="headSubjectPlannerData"
          :loading="isLoading || isFetching"
          :pagination-params="params"
          :total-pages="totalPages"
          @update:pagination-params="handlePaginationChange"
          @handleOpenCreatePlanner="handleOpenCreatePlanner"
      />
      <create-planner 
        :open="open"
        @handleClose="handleClose"
        @cancel="open = false"
      />
    </div>
  </template>
  
<script lang="ts" setup>
import { ref, computed } from "vue";
import { keepPreviousData } from "@tanstack/vue-query";
import { useAuthStore } from "@/stores/auth.ts";
import headSubjectPlannerTable from "./headSubjectPlannerTable.vue";
import HeadSubjectPlannerFilter from "./HeadSubjectPlannerFilter.vue";
import CreatePlanner from "./CreatePlanner.vue";
import { ParamsGetListPlaner } from "@/services/api/headsubject/plan.api";
import { useGetSubjectPlanner } from "@/services/service/headsubject/plan.action";

  // Khởi tạo Auth Store và lấy thông tin người dùng
  const auth = useAuthStore();
  const userInfo = computed(() => auth.user);
  const open = ref(false);
  
  // Khởi tạo các params cho head subjects và subjects
  const params = ref<ParamsGetListPlaner>({
    page: 1,
    size: 10,
    currentFacilityCode: userInfo.value?.facilityCode,
    currentDepartmentCode: userInfo.value?.departmentCode,
    currentUserId: userInfo.value?.userId,
    currentSemesterId: userInfo.value?.semesterId,
    currentBlockId: userInfo.value?.blockId,
  });
  
  // Lấy dữ liệu cho head subjects và subjects
  const { data, isLoading, isFetching } = useGetSubjectPlanner(params, {
    refetchOnWindowFocus: false,
    placeholderData: keepPreviousData,
  });
  
  // Xử lý thay đổi pagination và filter
  const handlePaginationChange = (newParams: ParamsGetListPlaner) => {
    params.value = { ...params.value, ...newParams };
  };
  
  const handleFilterChange = (newParams: ParamsGetListPlaner) => {
    params.value = { ...params.value, ...newParams };
  };

  const handleOpenCreatePlanner = () => {
    open.value = true;
  };

  const handleClose = () => {
  open.value = false;
};

  
  // Tính toán dữ liệu cho bảng
  const headSubjectPlannerData = computed(() => data?.value?.data?.data || []);
  const totalPages = computed(() => data?.value?.data?.totalPages || 0);
  
</script>
  