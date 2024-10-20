<template>
  <div>
    <a-modal
        :open="open"
        title="Thêm sinh viên"
        @cancel="handleClose"
        @ok="handleAddStudent"
        ok-text="Thêm"
        cancel-text="Hủy"
        destroyOnClose
        centered
    >
      <div>
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
import { computed, reactive } from "vue";
import { ERROR_MESSAGE } from "@/constants/message.constant.ts";
import { toast } from "vue3-toastify";
import {useCreateStudentTutor} from "@/services/service/planner/tutor-class.action.ts";
import {CreateStudentTutorParams} from "@/services/api/planner/tutor-class.api.ts";

defineProps({
  open: Boolean,
});

const emit = defineEmits(["handleClose"]);

const { mutate: createStudentTutor } = useCreateStudentTutor();

// Reactive model reference
const modelRef = reactive<CreateStudentTutorParams>({
  name: "",
  code: "",
  email: "",
});

// Validation rules
const rulesRef = reactive({
  name: [{ required: true, message: "Vui lòng nhập tên viên", trigger: "blur" }],
  code: [{ required: true, message: "Vui lòng nhập mã sinh viên", trigger: "blur" }],
  email: [{ required: true, message: "Vui lòng nhập email sinh viên", trigger: "blur" }],
});

const { resetFields, validate, validateInfos } = Form.useForm(modelRef, rulesRef);

// Form fields configuration
const formFields = computed(() => [
  {
    label: "Tên sinh viên",
    name: "name",
    component: "a-input",
    props: {
      placeholder: "Nhập tên sinh viên",
    },
  },
  {
    label: "Mã sinh viên",
    name: "code",
    component: "a-input",
    props: {
      placeholder: "Nhập mã sinh viên",
    },
  },
  {
    label: "Email",
    name: "email",
    component: "a-input",
    props: {
      placeholder: "Nhập email sinh viên",
    },
  },
]);

// Handle form submission
const handleAddStudent = async () => {
  try {
    await validate(); // Kiểm tra tính hợp lệ

    const payload = {
      ...modelRef
    };

    createStudentTutor(payload, {
      onSuccess: () => {
        toast.success("Thêm sinh viên thành công");
        handleClose(); // Đóng modal
      },
      onError: (error: any) => {
        toast.error(
            error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
        );
      },
    });
  } catch (error) {
    console.error("Error in adding/updating plan:", error); // Ghi log lỗi
  }
};

// Handle modal close
const handleClose = () => {
  emit("handleClose");
  resetFields(); // Reset fields only after the modal is closed
};
</script>
