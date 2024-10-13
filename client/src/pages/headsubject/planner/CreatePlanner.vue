<template>
    <div>
      <a-modal
        :open="props.open"
        :title="modalTitle"
        @cancel="handleClose"
        @ok="handleAdd"
        :ok-text="okText"
        destroyOnClose
        centered
      >
        <div v-if="props.isLoading" class="flex justify-center items-center">
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
  import { Form, Modal } from "ant-design-vue";
  import { reactive, computed, createVNode } from "vue";
  import { toast } from "vue3-toastify";
  import { ERROR_MESSAGE } from "@/constants/message.constant.ts";
  import { ExclamationCircleOutlined } from "@ant-design/icons-vue";
import { useCreatePlanner } from "@/services/service/headsubject/plan.action";
import { useAuthStore } from "@/stores/auth";

const auth = useAuthStore();
const userInfo = computed(() => auth.user);

  interface PlannerForm {
    name: string;
    staffCode: string;
    emailFe: string;
    emailFpt: string;
    currentSemesterId: string;
    currentBlockId: string;
    currentFacilityCode: string;
    currentDepartmentCode: string;
    currentUserId: string;
  }
  
  const props = defineProps({
    open: Boolean,
    isLoading: Boolean,
    planId: String,
    subjectId: String,
  });
  
  const emit = defineEmits(["handleClose", "resetTable"]);
  const { mutate: createPlanner } = useCreatePlanner();
  
  const modelRef = reactive<PlannerForm>({
    name: "",
    staffCode: "",
    emailFe: "",
    emailFpt: "",
    currentFacilityCode: userInfo.value?.facilityCode,
    currentDepartmentCode: userInfo.value?.departmentCode,
    currentUserId: userInfo.value?.userId,
    currentSemesterId: userInfo.value?.semesterId,
    currentBlokId: userInfo.value?.blockId,
  });
  
  const rulesRef = reactive({
    name: [
      { required: true, message: "Vui lòng nhập tên người lập kế hoạch", trigger: "blur" },
    ],
    staffCode: [
      { required: true, message: "Vui lòng nhập mã người lập kế hoạch", trigger: "blur" },
    ],
    emailFe: [
      { required: true, type: "email", message: "Vui lòng nhập Email Fe hợp lệ", trigger: "blur" },
    ],
    emailFpt: [
      { required: true, type: "email", message: "Vui lòng nhập Email FPT hợp lệ", trigger: "blur" },
    ],
  });
  
  const { resetFields, validate, validateInfos } = Form.useForm(modelRef, rulesRef);
  
  const modalTitle = computed(() => "Thêm người lập kế hoạch");
  const okText = computed(() => "Tạo lớp môn học thành công");
  
  const formFields = computed(() => [
    {
      label: "Tên người lập kế hoạch",
      name: "name",
      component: "a-input",
      props: {
        type: "text",
        placeholder: "Nhập tên người lập kế hoạch",
      },
    },
    {
      label: "Mã người lập kế hoạch",
      name: "staffCode",
      component: "a-input",
      props: {
        type: "text",
        placeholder: "Nhập mã người lập kế hoạch",
      },
    },
    {
      label: "Email Fe",
      name: "emailFe",
      component: "a-input",
      props: {
        type: "text",
        placeholder: "Nhập Email Fe",
      },
    },
    {
      label: "Email FPT",
      name: "emailFpt",
      component: "a-input",
      props: {
        type: "text",
        placeholder: "Nhập Email FPT",
      },
    },
  ]);
  
  const handleAdd = () => {
    Modal.confirm({
      content: "Người dùng này sẽ được phân công. Bạn chắc chắn muốn tạo mới chứ?",
      icon: createVNode(ExclamationCircleOutlined),
      centered: true,
      async onOk() {
        try {
          await validate(); // Kiểm tra tính hợp lệ tất cả các trường
  
          const payload = {
            ...modelRef
          };
  
          createPlanner(payload, {
            onSuccess: () => {
              toast.success("Thêm người tạo kế hoạch thành công!");
              handleClose();
            },
            onError: (error: any) => {
              toast.error(
                error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
              );
            },
          });
        } catch (error: any) {
          console.error("🚀 ~ handleAdd ~ error:", error);
          toast.error(
            error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
          );
        }
      },
      cancelText: "Huỷ",
      onCancel() {
        Modal.destroyAll();
      },
    });
  };
  
  const handleClose = () => {
    emit("handleClose");
    resetFields();
  };
  </script>
  