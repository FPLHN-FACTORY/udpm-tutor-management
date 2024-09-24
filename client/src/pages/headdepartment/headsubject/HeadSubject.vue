<template>
  <div class="mt-4">
    <div class="flex justify-between items-center">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="hi-office-building" scale="2" />
        <span class="m-2 text-3xl">Quản lý trưởng môn</span>
      </h2>
    </div>
    <head-subject-filter :params="params" @filter="handleFilterChange" />
    <head-subject-table
        :data-source="headSubjectData"
        :loading="isLoading || isFetching"
        :pagination-params="params"
        :total-pages="totalPages"
        :semester-id="semesterId"
        :check-assign="checkAssign"
        @update:pagination-params="handlePaginationChange"
        @handle-open-modal-detail="handleOpenModalDetail"
        @handle-open-modal-update="handleOpenModalUpdate"
    />
    <detail-head-subject-modal
        :open="open"
        @handle-close="handleCloseModal"
        :subjects="subjectData"
        :is-loading-subject="isLoadingSubject"
        :semester-id="semesterId"
        :head-subject-id="headSubjectId"
        :user-detail="userDetail"
        :pagination-params="paramsSubjects"
        :total-pages="totalPageSubject"
        @update:pagination-params="handleSubjectPaginationChange"
    />
    <update-head-subject-modal
        :open="openWithAssign"
        @handle-close="handleCloseModalWithAssign"
        :subjects="subjectWithAssignData"
        :is-loading-subject="isLoadingSubjectWithAssign"
        :semester-id="semesterId"
        :head-subject-id="headSubjectId"
        :pagination-params="paramsSubjectsWithAssign"
        :total-pages="totalPageSubjectWithAssign"
        @update:pagination-params="handleSubjectWithAssignPaginationChange"
    />
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, watch } from "vue";
import { keepPreviousData } from "@tanstack/vue-query";
import { useAuthStore } from "@/stores/auth.ts";
import {
  useCheckCurrentSemesterHasHeadSubject,
  useGetHeadOfSubject,
  useGetSubjectByHeadOfSubject,
  useGetSubjectsWithAssignByHeadOfSubject
} from "@/services/service/headdepartment/head-subject.action.ts";
import {
  HeadOfSubjectResponse,
  ParamsGetHeadOfSubjects,
  ParamsGetSubjectsByHeadSubject,
  ParamsGetSubjectsWithAssign
} from "@/services/api/headdepartment/head-subject.api.ts";
import HeadSubjectTable from "@/pages/headdepartment/headsubject/HeadSubjectTable.vue";
import HeadSubjectFilter from "@/pages/headdepartment/headsubject/HeadSubjectFilter.vue";
import DetailHeadSubjectModal from "@/pages/headdepartment/headsubject/DetailHeadSubjectModal.vue";
import UpdateHeadSubjectModal from "@/pages/headdepartment/headsubject/UpdateHeadSubjectModal.vue";

// Khởi tạo Auth Store và lấy thông tin người dùng
const auth = useAuthStore();
const userInfo = computed(() => auth.user);
const semesterId = ref<string | null>(null);

// Khởi tạo các params cho head subjects và subjects
const params = ref<ParamsGetHeadOfSubjects>({
  page: 1,
  size: 10,
  currentFacilityCode: userInfo.value?.facilityCode,
  currentDepartmentCode: userInfo.value?.departmentCode,
  currentUserId: userInfo.value?.userId,
});

const paramsSubjects = ref<ParamsGetSubjectsByHeadSubject>({
  page: 1,
  size: 10,
  currentFacilityCode: userInfo.value?.facilityCode,
  currentDepartmentCode: userInfo.value?.departmentCode,
  currentSemesterId: "",
});

const paramsSubjectsWithAssign = ref<ParamsGetSubjectsWithAssign>({
  page: 1,
  size: 10,
  facilityCode: userInfo.value?.facilityCode,
  departmentCode: userInfo.value?.departmentCode,
  currentSemesterId: "",
});

// Trạng thái modal và dữ liệu hiển thị
const open = ref(false);
const openWithAssign = ref(false);
const headSubjectId = ref<string | null>(null);
const userDetail = ref<string | null>(null);

// Lấy dữ liệu cho head subjects và subjects
const { data, isLoading, isFetching, refetch } = useGetHeadOfSubject(params, {
  refetchOnWindowFocus: false,
  placeholderData: keepPreviousData,
});

const { data: listSubjectByHeadSubjectData, isLoading: isLoadingSubject } = useGetSubjectByHeadOfSubject(
    paramsSubjects,
    headSubjectId,
    {
      refetchOnWindowFocus: false,
      enabled: () => !!headSubjectId.value,
    }
);

const { data: listSubjectWithAssignData, isLoading: isLoadingSubjectWithAssign } = useGetSubjectsWithAssignByHeadOfSubject(
    paramsSubjectsWithAssign,
    headSubjectId,
    {
      refetchOnWindowFocus: false,
      enabled: () => !!headSubjectId.value,
    }
);

// Kiểm tra xem học kỳ hiện tại có trưởng bộ môn không
const { data: checkAssignData, refetch: refetchCheckAssign } = useCheckCurrentSemesterHasHeadSubject(semesterId, {
  refetchOnWindowFocus: false,
  enabled: () => !!semesterId.value,
});

// Xử lý thay đổi pagination và filter
const handlePaginationChange = (newParams: ParamsGetHeadOfSubjects) => {
  params.value = { ...params.value, ...newParams };
};

const handleSubjectPaginationChange = (newParams: ParamsGetSubjectsByHeadSubject) => {
  paramsSubjects.value = { ...paramsSubjects.value, ...newParams };
};

const handleSubjectWithAssignPaginationChange = (newParams: ParamsGetSubjectsWithAssign) => {
  paramsSubjectsWithAssign.value = { ...paramsSubjectsWithAssign.value, ...newParams };
};

const handleFilterChange = (newParams: ParamsGetHeadOfSubjects) => {
  params.value = { ...params.value, ...newParams };
};

// Xử lý đóng mở modal
const handleCloseModal = () => {
  paramsSubjects.value.page = 1;
  open.value = false;
  headSubjectId.value = null;
};

const handleCloseModalWithAssign = () => {
  paramsSubjectsWithAssign.value.page = 1;
  openWithAssign.value = false;
  headSubjectId.value = null;
};

// Xử lý mở modal chi tiết và cập nhật
const handleOpenModalDetail = (record: HeadOfSubjectResponse) => {
  headSubjectId.value = record.id;
  userDetail.value = `Danh sách môn học: ${record.staffCode} - ${record.staffName}`;
  open.value = true;
};

const handleOpenModalUpdate = (record: HeadOfSubjectResponse) => {
  headSubjectId.value = record.id;
  openWithAssign.value = true;
};

// Tính toán dữ liệu cho bảng
const headSubjectData = computed(() => data?.value?.data?.data || []);
const subjectData = computed(() => listSubjectByHeadSubjectData?.value?.data?.data || []);
const subjectWithAssignData = computed(() => listSubjectWithAssignData?.value?.data?.data || []);
const checkAssign = computed(() => checkAssignData?.value?.data);
const totalPages = computed(() => data?.value?.data?.totalPages || 0);
const totalPageSubject = computed(() => listSubjectByHeadSubjectData?.value?.data?.totalPages || 0);
const totalPageSubjectWithAssign = computed(() => listSubjectWithAssignData?.value?.data?.totalPages || 0);

// Theo dõi sự thay đổi của modal và semester ID
watch(
    () => [openWithAssign.value, params.value.currentSemesterId],
    async ([newOpenWithAssign, newSemesterId]) => {
      if (newSemesterId) {
        semesterId.value = newSemesterId;
        paramsSubjects.value.currentSemesterId = newSemesterId;
        paramsSubjectsWithAssign.value.currentSemesterId = newSemesterId;
        await refetchCheckAssign(); // Gọi refetch sau khi cập nhật
      }
      if (!newOpenWithAssign) {
        await refetch(); // Gọi refetch nếu modal không mở
      }
    },
    { immediate: true }
);
</script>
