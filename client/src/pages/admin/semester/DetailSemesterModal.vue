<template>
  <div>
    <a-modal
        :open="open"
        title="Chi tiết môn học"
        @cancel="handleClose"
        destroyOnClose
        centered
        :footer="null"
    >
      <div
          v-if="props.isLoadingDetail"
          class="flex justify-center items-center"
      >
        <a-spin />
      </div>
      <div v-else>
        <a-form layout="vertical">
          <template v-for="field in formFields">
            <a-form-item
                :label="field.label"
                :name="field.name"
            >
              <component
                  :is="field.component"
                  v-bind="field.props"
                  :value="modelRef[field.name]"
                  :readonly="true"
              >
              </component>
            </a-form-item>
          </template>
        </a-form>
      </div>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import {defineProps, defineEmits, reactive, computed, watch} from "vue";
import dayjs from "dayjs";

interface SubjectForm {
  semesterName: string;
  startTime: dayjs.Dayjs | null;
  endTime: dayjs.Dayjs | null;
  block1Time: dayjs.Dayjs | null;
  block2Time: dayjs.Dayjs | null;
}

const props = defineProps({
  open: Boolean,
  semesterDetail: Object as () => any | null,
  isLoadingDetail: Boolean,
});

const emit = defineEmits(["handleCloseModal"]);

const modelRef = reactive<SubjectForm>({
  semesterName: props.semesterDetail?.semesterName || "",
  startTime: props.semesterDetail?.startTime || null,
  endTime: props.semesterDetail?.endTime || null,
  block1Time: props.semesterDetail?.endTime || null,
  block2Time: props.semesterDetail?.endTime || null,
});

const formFields = computed(() => [
  {
    label: "Tên học kỳ",
    name: "semesterName",
    component: "a-input",
  },
  {
    label: "Thời gian bắt đầu học kỳ",
    name: "startTime",
    component: "a-date-picker",
    props: {
      class: "w-full",
      format: "DD/MM/YYYY",
    },
  },
  {
    label: "Thời gian kết thúc học kỳ",
    name: "endTime",
    component: "a-date-picker",
    props: {
      class: "w-full",
      format: "DD/MM/YYYY",
    },
  },
  {
    label: "Thời gian block 1",
    name: "block1Time",
    component: "a-range-picker",
    props: {
      class: "w-full",
      format: "DD/MM/YYYY",
    },
  },
  {
    label: "Thời gian block 2",
    name: "block2Time",
    component: "a-range-picker",
    props: {
      class: "w-full",
      format: "DD/MM/YYYY",
    },
  },
]);

const handleClose = () => {
  emit("handleCloseModal");
};

watch(
    () => props.semesterDetail,
    (newVal) => {
      if (newVal) {
        Object.assign(modelRef, {
          semesterName: newVal.semesterName + ' ' + newVal.semesterYear,
          startTime: dayjs(newVal.startTime),
          endTime: dayjs(newVal.endTime),
          block1Time: [dayjs(newVal.startTimeFirstBlock), dayjs(newVal.endTimeFirstBlock)],
          block2Time: [dayjs(newVal.startTimeSecondBlock), dayjs(newVal.endTimeSecondBlock)],
        });
      }
    },
    { immediate: true }
);

</script>
