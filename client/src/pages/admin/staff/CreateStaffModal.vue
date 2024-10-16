<template>
  <div>
    <a-modal :open="open" title="Thêm nhân viên" @cancel="handleClose" @ok="handleAddStaff" destroyOnClose centered
      ok-text="Thêm nhân viên">
      <div>
        <a-form layout="vertical">
          <template v-for="field in formFields">
            <a-form-item :label="field.label" :name="field.name" v-bind="validateInfos[field.name]">
              <component :is="field.component" v-model:value="modelRef[field.name]"></component>
            </a-form-item>
          </template>
        </a-form>
      </div>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import { defineProps, defineEmits, reactive, computed, createVNode } from "vue";
import { Form, Modal } from "ant-design-vue";
import { ExclamationCircleOutlined } from "@ant-design/icons-vue";
import { toast } from "vue3-toastify";
import { ERROR_MESSAGE } from "@/constants/message.constant";
import { useCreateStaff } from "@/services/service/admin/staff.action";

export interface StaffForm {
  name: string;
  staffCode: string;
  emailFe: string;
  emailFpt: string;
}

defineProps({
  open: Boolean,
});

const emit = defineEmits(["handleCloseModal"]);

const { mutate: create } = useCreateStaff();

const modelRef = reactive<StaffForm>({
  name: "",
  staffCode: "",
  emailFe: "",
  emailFpt: "",
});

const rulesRef = reactive({
  name: [
    { required: true, message: "Vui lòng nhập tên", trigger: "blur" },
  ],
  staffCode: [
    { required: true, message: "Vui lòng nhập mã nhân viên", trigger: "blur" },
    { message: "Vui lòng nhập lại, mã nhân không hợp lệ", trigger: "blur", pattern: '^[^\s]+$' },
  ],
  emailFe: [
    { required: true, message: "Vui lòng nhập email FE", trigger: "blur" },
    { message: "Vui lòng nhập lại, email FE không hợp lệ", trigger: "blur", pattern: '^[A-Za-z0-9._%+-]+@fe\.edu\.vn$' },
  ],
  emailFpt: [
    { required: true, message: "Vui lòng nhập email FPT", trigger: "blur" },
    { message: "Vui lòng nhập lại, email FPT không hợp lệ", trigger: "blur", pattern: '^[A-Za-z0-9._%+-]+@fpt\.edu\.vn$' },
  ],
});

const { resetFields, validate, validateInfos } = Form.useForm(
  modelRef,
  rulesRef
);

const formFields = computed(() => [
  {
    label: "Họ và tên",
    name: "name",
    component: "a-input",
  },
  {
    label: "Mã nhân viên",
    name: "staffCode",
    component: "a-input",
  },
  {
    label: "Email FE",
    name: "emailFe",
    component: "a-input",
  },
  {
    label: "Email FPT",
    name: "emailFpt",
    component: "a-input",
  },
]);

const handleAddStaff = () => {
  Modal.confirm({
    content: 'Bạn chắc chắn muốn thêm chứ',
    icon: createVNode(ExclamationCircleOutlined),
    centered: true,
    async onOk() {
      try {
        await validate(); // Kiểm tra tính hợp lệ

        create(modelRef, {
          onSuccess: () => {
            toast.success("Thêm nhân viên thành công");
            handleClose();
          },
          onError: (error: any) => {
            toast.error(
              error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
            )
          },
        })

      } catch (error: any) {
        console.error("🚀 ~ handleAdd ~ error:", error);
        toast.error(
          error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
        );
      }
    },
    cancelText: 'Huỷ',
    onCancel() {
      Modal.destroyAll();
      resetFields();
    },
  });
}

const handleClose = () => {
  emit("handleCloseModal");
  resetFields();
};


</script>