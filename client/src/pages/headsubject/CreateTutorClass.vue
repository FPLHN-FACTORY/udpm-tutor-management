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
          <template v-for="field in formFields" :key="field.name">
            <a-form-item
              :label="field.label"
              :name="field.name"
              v-bind="validateInfos[field.name]"
            >
            <component
              :is="field.component"
              v-bind="field.props"
              v-model:value="modelRef[field.name]"
            >
              <template v-if="field.options" v-for="option in field.options" :key="option.value">
                <a-select-option :value="option.value">{{ option.label }}</a-select-option>
              </template>
            </component>
            </a-form-item>
          </template>
        </a-form>
      </div>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import { Form } from "ant-design-vue";
import { computed, reactive, ref, watch } from "vue";
import { filterOption } from "@/utils/common.helper.ts";
import { useCreateOrUpdateHeadSubject } from "@/services/service/headsubject/headsubject.action";


const props = defineProps({
  open: Boolean,
  planDetail: Object as () => any | null,
  isLoadingDetail: Boolean,
  subjectOptions: Array as () => any,
  planId: String
});


const emit = defineEmits(["handleClose", "resetTable"]);

const modelRef = reactive({
  planeId: '',
  subjectId: '',
  numberOfClasses: 0,
  planFormat: 0,
});

const rulesRef = reactive({
  subjectId: [{ required: true, message: "Vui lòng chọn môn học", trigger: "blur" }],
  planFormat: [{ required: true, message: "Vui lòng chọn hình thức", trigger: "blur" }],
});

const { resetFields, validate, validateInfos } = Form.useForm(modelRef, rulesRef);
const modalTitle = computed(() => "Thêm môn học và hình thức");
const okText = computed(() => "Thêm môn học");

const formFields = computed(() => [
  {
    label: "Môn học",
    name: "subjectId",
    component: "a-select",
    props: {
      placeholder: "Chọn học kỳ",
      loading: false,
      showSearch: true,
      filterOption: filterOption,
    },
    options: props.subjectOptions,
  },
  {
    label: "Hình thức",
    name: "planFormat",
    component: "a-select",
    props: { placeholder: "Chọn hình thức" },
    options: [
      { value: 0, label: 'Online' },
      { value: 1, label: 'Offline' },
    ],
    required: true,
  },
]);

const { mutateAsync } = useCreateOrUpdateHeadSubject()

const handleAddOrUpdate = async () => {
  try {
    await validate();

    const params = {
      planeId: props.planId || '',
      subjectId: modelRef.subjectId || '',
      numberOfClasses:  modelRef.numberOfClasses || 0,
      planFormat:  modelRef.planFormat || 0
    };

    await mutateAsync(params);
    emit("handleClose");
    emit("resetTable");
  } catch (error) {
    console.log("Lỗi khi thêm/cập nhật lớp học:", error);
  }
};

watch(
    () => props.planDetail,
    (newVal) => {
      if (newVal) {
        modelRef.planeId = newVal.planeId || null; 
        modelRef.subjectId = newVal.subjectId || null; 
        modelRef.numberOfClasses = newVal.numberOfClasses || 0; 
      }
    },
    { immediate: true }
);

watch(
  () => props.subjectOptions,
  (newOptions) => {
    if (newOptions) {
      formFields.value[0].options = newOptions; 
    }
  }
);
const handleClose = () => {
  emit("handleClose");
  resetFields(); 
};

</script>
