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
      <div
          v-if="props.isLoading"
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
                v-bind="validateInfos[field.name]"
            >
              <component
                  :is="field.component"
                  v-bind="field.props"
                  v-model:value="modelRef[field.name]"
              >
                <template
                    v-if="field.options"
                    v-for="option in field.options"
                    :key="option.value"
                >
                  <a-select-option :value="option.value">
                    {{ option.label }}
                  </a-select-option>
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
import {Form, Modal} from "ant-design-vue";
import {reactive, computed, watch, createVNode} from "vue";
import {toast} from "vue3-toastify";
import {ERROR_MESSAGE} from "@/constants/message.constant.ts";
import {ExclamationCircleOutlined} from "@ant-design/icons-vue";
import {
  useCreateTutorClass,
  useUpdateTutorClass
} from "@/services/service/headsubject/tutor-class.action.ts";

interface TutorClassForm {
  numberOfClasses: number;
  numberOfLectures: number;
  format: string;
  planId?: string;
  subjectId?: string;
}

const props = defineProps({
  open: Boolean,
  isLoading: Boolean,
  planId: String,
  subjectId: String,
  tutorClass: Object as () => any | null,
});

const emit = defineEmits(["handleClose", "resetTable"]);
const { mutate: createTutorClass } = useCreateTutorClass();
const { mutate: updateTutorClass } = useUpdateTutorClass();

const modelRef = reactive<TutorClassForm>({
  numberOfClasses: 1,
  numberOfLectures: 8,
  format: "ONLINE",
  planId: props.planId,
  subjectId: props.subjectId,
});

const rulesRef = reactive({
  numberOfClasses: [
    { required: true, message: "Vui lòng nhập số lớp", trigger: "blur" },
  ],
  numberOfLectures: [
    { required: true, message: "Vui lòng nhập số buổi", trigger: "blur" },
  ],
  format: [
    { required: true, message: "Vui lòng chọn hình thức", trigger: "blur" },
  ],
});

const { resetFields, validate, validateInfos } = Form.useForm(
    modelRef,
    rulesRef
);

const modalTitle = computed(() =>
    props.tutorClass ? "Cập nhật lớp môn" : "Thêm lớp môn"
);

const okText = computed(() =>
    props.tutorClass ? "Cập nhật lớp môn thành công" : "Thêm lớp môn học thành công"
);

watch(
    () => props.tutorClass,
    (newVal) => {
      if (newVal) {
        Object.assign(modelRef, {
          numberOfClasses: newVal.numberClasses,
          numberOfLectures: newVal.numberLectures,
          format: newVal.format === 0 ? "ONLINE" : "OFFLINE",
        });
      } else {
        resetFields();
      }
    },
    { immediate: true }
);

const formFields = computed(() => [
  {
    label: "Số lớp",
    name: "numberOfClasses",
    component: "a-input",
    props: {
      type: "number", // Thiết lập type thành number
      placeholder: "Nhập số lớp",
      min: 1,
      disabled: !!props.tutorClass,
    },
  },
  {
    label: "Số buổi",
    name: "numberOfLectures",
    component: "a-input",
    props: {
      type: "number", // Thiết lập type thành number
      placeholder: "Nhập số buổi",
      min: 1,
    },
  },
  {
    label: "Hình thức",
    name: "format",
    component: "a-select",
    options: [
      { value: "ONLINE", label: "ONLINE" },
      { value: "OFFLINE", label: "OFFLINE" },
    ],
    props: { placeholder: "Chọn hình thức" },
  },
]);

const handleAddOrUpdate = () => {
  Modal.confirm({
    content: 'Bạn chắc chắn muốn cập nhật chứ',
    icon: createVNode(ExclamationCircleOutlined),
    centered: true,
    async onOk() {
      try {
        await validate(); // Kiểm tra tính hợp lệ

        const payload = {
          ...modelRef,
          subjectId: props.subjectId
        };

        // Tạo biến để giữ thông tin về hành động (cập nhật hay tạo mới)
        const actionParams = props.tutorClass
            ? {
              id: props.tutorClass.id,
              params: payload,
            }
            : payload;

        // Gọi hàm phù hợp dựa vào tutorClass
        const action = props.tutorClass ? updateTutorClass : createTutorClass;
        const message = props.tutorClass ? "Cập nhật môn học thành công!" : "Tạo môn học thành công!";

        action(actionParams, {
          onSuccess: () => {
            toast.success(message);
            handleClose();
          },
          onError: (error: any) => {
            toast.error(
                error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
            )
          },
        })
      } catch (error: any) {
        console.error("🚀 ~ handleAddOrUpdate ~ error:", error);
        toast.error(
            error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
        );
      }
    },
    cancelText: 'Huỷ',
    onCancel() {
      Modal.destroyAll();
    },
  });
}

const handleClose = () => {
  emit("handleClose");
  resetFields();
};
</script>
