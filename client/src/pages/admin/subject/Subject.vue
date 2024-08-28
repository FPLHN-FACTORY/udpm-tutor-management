<template>
  <div class="mt-4">
    <div class="flex justify-between items-center">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="hi-office-building" scale="2" />
        <span class="ml-2 text-2xl">Quản lý cơ sở</span>
      </h2>
      <a-button type="primary" @click="open = true" class="m-4"
        >Thêm mới</a-button
      >
    </div>
    <subject-filter @filter="handleFilter" />
    <subject-table
      class="p-4"
      :data-source="subjectData"
      :loading="isLoading"
      :pagination-params="params"
      :total-pages="totalPages"
      @update:pagination-params="handlePaginationChange"
    />
    <create-update-subject-modal
      :open="open"
      @update:open="open = $event"
      @ok="handleOk"
      :subjectId="subjectId"
    />
  </div>
</template>

<script lang="ts" setup>
import CreateUpdateSubjectModal from "@/pages/admin/subject/CreateUpdateSubjectModal.vue";
import SubjectFilter from "@/pages/admin/subject/SubjectFilter.vue";
import SubjectTable from "@/pages/admin/subject/SubjectTable.vue";
import { ParamsGetSubjects } from "@/services/api/subject.api";
import { useGetSubject } from "@/services/service/subject.action";
import { keepPreviousData } from "@tanstack/vue-query";
import { computed, ref } from "vue";

const params = ref<ParamsGetSubjects>({
  page: 1,
  size: 10,
});

const open = ref(false);

const subjectId = ref<string>();

const handleOk = () => {
  open.value = false;
};

const { data, isLoading } = useGetSubject(params, {
  refetchOnWindowFocus: false,
  placeholderData: keepPreviousData,
});

const handlePaginationChange = (newParams: ParamsGetSubjects) => {
  params.value = { ...params.value, ...newParams };
};

const handleFilter = (newParams: ParamsGetSubjects) => {
  params.value = { ...params.value, ...newParams };
};

const subjectData = computed(() => data?.value?.data?.data || []);
const totalPages = computed(() => data?.value?.data?.totalPages || 0);
</script>
