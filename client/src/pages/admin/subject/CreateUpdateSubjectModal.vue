<template>
  <div>
    <a-modal
      :open="open"
      title="Basic Modal"
      @ok="handleOkInternal"
      @update:open="updateOpen"
    >
      <a-spin v-if="isLoadingDetail" />
      <div v-else>
        <a-form>
          <a-form-item label="Mã môn học" name="subjectCode">
            <a-input />
          </a-form-item>
          <a-form-item label="Tên môn học" name="subjectName">
            <a-input />
          </a-form-item>
          <a-form-item label="Bộ môn" name="departmentId">
            <a-input />
          </a-form-item>
          <a-form-item label="Loại môn học" name="subjectType">
            <a-input />
          </a-form-item>
          <a-form-item label="Ngày tạo" name="createdDate">
            <a-input />
          </a-form-item>
        </a-form>
      </div>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import { useDetailSubject } from "@/services/service/subject.action";
import { keepPreviousData } from "@tanstack/vue-query";
import { computed, defineEmits, defineProps, reactive, watch } from "vue";

const props = defineProps({
  open: Boolean,
  subjectId: (String as any) || null,
});

const emit = defineEmits(["update:open"]);

const handleOkInternal = () => {
  emit("update:open", false);
};

const updateOpen = (value: boolean) => {
  emit("update:open", value);
};

const { data: dataDetail, isLoading: isLoadingDetail } = useDetailSubject(
  props.subjectId,
  {
    refetchOnWindowFocus: false,
    placeholderData: keepPreviousData,
    enabled: !!props.subjectId,
  }
);

const formState = reactive({
  subjectCode: "",
  subjectName: "",
  departmentId: "",
  subjectType: "",
  createdDate: 0,
});

const subjectDetail = computed(() => dataDetail?.value?.data);

watch(subjectDetail, (newVal) => {
  if (newVal) {
    formState.subjectCode = newVal.subjectCode;
    formState.subjectName = newVal.subjectName;
    formState.departmentId = newVal.departmentId;
    formState.subjectType = newVal.subjectType;
    formState.createdDate = newVal.createdDate;
  }
});
</script>
