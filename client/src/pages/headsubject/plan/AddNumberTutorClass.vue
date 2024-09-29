<template>
  <div>
    <a-modal
        :open="props.open"
        :title="modalTitle"
        @cancel="handleClose"
        @ok="handleAddOrUpdate"
        :ok-text="okText"
        destroyOnClose
        centered
    >
      <div v-if="props.isLoadingDetail" class="flex justify-center items-center">
        <a-spin />
      </div>
      <div v-else>
        <a-form layout="vertical">
          <a-form-item
              label="Số lớp"
              name="numberOfClasses"
              v-bind="validateInfos.numberOfClasses"
          >
            <a-input-number
                v-model:value="modelRef.numberOfClasses"
                :min="1"
                style="width: 100%;"
                placeholder="Nhập số lớp"
            />
          </a-form-item>
        </a-form>
      </div>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import { UpdateHeadSubjectTutorDetailParams } from "@/services/api/headsubject/tutor-class.api.ts";
import { useUpdateHeadSubjectTutorDetail } from "@/services/service/headsubject/tutor-class.action.ts";
import { Form } from "ant-design-vue";
import { reactive, computed, watch } from "vue";

const props = defineProps({
  open: Boolean,
  isLoadingDetail: Boolean,
  subjectId: String,
  planId: String,
  numberClasses: Number
});

const emit = defineEmits(["handleClose", "resetTable"]);

const modelRef = reactive({
  numberOfClasses: props.numberClasses,
});

const rulesRef = reactive({
  numberOfClasses: [
    { required: true, message: "Vui lòng nhập số lớp", trigger: "blur" },
    { type: 'number', min: 1, message: "Số lớp phải lớn hơn 0", trigger: "blur" },
  ],
});

// Để sử dụng các tính năng của form
const { resetFields, validate, validateInfos } = Form.useForm(modelRef, rulesRef);

const modalTitle = computed(() => "Nhập số lớp");
const okText = computed(() => "Xác nhận");

const { mutate } = useUpdateHeadSubjectTutorDetail();

watch(
    () => props.numberClasses,
    (newValue) => {
      if(newValue){
        modelRef.numberOfClasses = newValue; // Cập nhật số lớp khi props.numberClasses thay đổi
      }
    },
    { immediate: true }
);

const handleAddOrUpdate = async () => {
  try {
    await validate();
    const params: UpdateHeadSubjectTutorDetailParams = {
      numberOfClasses: modelRef.numberOfClasses,
      planeId: props.planId,
      subjectId: props.subjectId,
    };
    await mutate(params);
    emit("handleClose");
    emit("resetTable");
  } catch (error) {
    console.error("Validation failed:", error);
  }
};

const handleClose = () => {
  emit("handleClose");
  resetFields();
};
</script>
