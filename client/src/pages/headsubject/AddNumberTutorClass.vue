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
import { UpdateHeadSubjectTutorDetailParams } from "@/services/api/headsubject/headsubject.api";
import { useGetDetailTutorClass, useUpdateHeadSubjectTutorDetail } from "@/services/service/headsubject/headsubject.action";
import { Form } from "ant-design-vue";
import { reactive, ref, computed, PropType, watch } from "vue";

const props = defineProps({
  open: Boolean,
  isLoadingDetail: Boolean,
  tutorClassId: {
    type: String as PropType<string | null>,
    required: false,
    default: null,
  },
});


const tutorClassIdRef = ref(props.tutorClassId);

watch(
  () => props.tutorClassId,
  (newData) => {
    tutorClassIdRef.value = newData; 
    if (newData) {

    }
  },
  { immediate: true }
);

const { data: tutorClass } = useGetDetailTutorClass(tutorClassIdRef, {
  refetchOnWindowFocus: false,
  enabled: () => !!tutorClassIdRef.value,
});


watch(
  tutorClass,
  (newData) => {
    if (newData && newData.data) {
      modelRef.numberOfClasses = newData.data.numberOfClasses; 
      console.log("Number of Classes:", modelRef.numberOfClasses); 
    }
  },
  { immediate: true }
);



const emit = defineEmits(["handleClose", "resetTable"]);

const modelRef = reactive({
  numberOfClasses: 0,
});

const rulesRef = reactive({
  numberOfClasses: [{ required: true, message: "Vui lòng nhập số lớp", trigger: "blur" }],
});

const { resetFields, validate, validateInfos } = Form.useForm(modelRef, rulesRef);

const modalTitle = computed(() => "Nhập số lớp");
const okText = computed(() => "Xác nhận");

// Use the mutation hook at the top level
const { mutate } = useUpdateHeadSubjectTutorDetail();

const handleAddOrUpdate = async () => {
  try {
    await validate();

    const params: UpdateHeadSubjectTutorDetailParams = {
      numberOfClasses: modelRef.numberOfClasses,
      tutorClassId: tutorClassIdRef.value || "", 
    };

    await mutate(params); // Execute the mutation

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
