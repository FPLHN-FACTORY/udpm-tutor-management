<template>
  <div class="mt-4">
    <div class="flex justify-between items-center">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="hi-office-building" scale="2" />
        <span class="ml-2 text-2xl">Quản lý cơ sở</span>
      </h2>
    </div>
    <subject-filter @filter="handleFilter" />
    <subject-table
      class="p-4"
      :data-source="subjectData"
      :loading="isLoading"
      :pagination-params="params"
      :total-pages="totalPages"
      @update:pagination-params="handlePaginationChange"
      @handleOpenModalUpdate="handleOpenModalUpdate"
      @handleOpenModalAdd="handleOpenModalAdd"
    />
    <create-update-subject-modal
      :open="open"
      @handleClose="handleClose"
      @cancel="open = false"
      :subject-detail="subjectDetail || null"
      :is-loading-detail="isLoadingDetail || false"
    />
  </div>
</template>

<script lang="ts" setup>
import CreateUpdateSubjectModal from "@/pages/admin/subject/CreateUpdateSubjectModal.vue";
import SubjectFilter from "@/pages/admin/subject/SubjectFilter.vue";
import SubjectTable from "@/pages/admin/subject/SubjectTable.vue";
import { ParamsGetSubjects, SubjectResponse } from "@/services/api/subject.api";
import {
  useDetailSubject,
  useGetSubject,
} from "@/services/service/subject.action";
import { keepPreviousData } from "@tanstack/vue-query";
import { computed, ref } from "vue";

const params = ref<ParamsGetSubjects>({
  page: 1,
  size: 10,
});

const open = ref(false);

const subjectId = ref<string | null>(null);

const { data, isLoading } = useGetSubject(params, {
  refetchOnWindowFocus: false,
  placeholderData: keepPreviousData,
});

const { data: dataDetail, isLoading: isLoadingDetail } = useDetailSubject(
  subjectId,
  {
    refetchOnWindowFocus: false,
    enabled: () => !!subjectId.value,
  }
);

const handlePaginationChange = (newParams: ParamsGetSubjects) => {
  params.value = { ...params.value, ...newParams };
};

const handleFilter = (newParams: ParamsGetSubjects) => {
  params.value = { ...params.value, ...newParams };
};

const handleClose = () => {
  open.value = false;
  subjectId.value = null;
};

const handleOpenModalAdd = () => {
  open.value = true;
  subjectId.value = null;
};

const handleOpenModalUpdate = (record: SubjectResponse) => {
  subjectId.value = record.id;
  open.value = true;
};

const subjectData = computed(() => data?.value?.data?.data || []);
const totalPages = computed(() => data?.value?.data?.totalPages || 0);
const subjectDetail = computed(() =>
  subjectId.value
    ? {
        ...dataDetail.value?.data,
        subjectId: subjectId.value,
      }
    : null
);
</script>
