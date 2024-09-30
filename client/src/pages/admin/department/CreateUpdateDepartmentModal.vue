<template>
  <div>
    <a-modal
      :open="open"
      :title="modalTitle"
      @cancel="handleClose"
      @ok="handleAddOrUpdate"
      :ok-text="okText"
      destroyOnClose
      centered
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
import { ERROR_MESSAGE } from "@/constants/message.constant";
import {
  useCreateDepartment,
  useUpdateDepartment,
} from "@/services/service/admin/department.action.ts";
import { ExclamationCircleOutlined } from "@ant-design/icons-vue";
import { Form, Modal } from "ant-design-vue";
import { computed, createVNode, reactive, watch } from "vue";
import { toast } from "vue3-toastify";

interface DepartmentForm {
  departmentCode: string;
  departmentName: string;
}

const props = defineProps({
  open: Boolean,
  departmentDetail: Object as () => any | null,
  isLoadingDetail: Boolean,
});

const emit = defineEmits(["handleClose"]);

const { mutate: createDepartment } = useCreateDepartment();

const { mutate: updateDepartment } = useUpdateDepartment();

const modelRef = reactive<DepartmentForm>({
  departmentCode: "",
  departmentName: "",
});

const rulesRef = reactive({
  departmentCode: [
    { required: true, message: "Vui lòng nhập mã bộ môn", trigger: "blur" },
  ],
  departmentName: [
    { required: true, message: "Vui lòng nhập tên bộ môn", trigger: "blur" },
  ],
});

const { resetFields, validate, validateInfos } = Form.useForm(
  modelRef,
  rulesRef
);

const modalTitle = computed(() =>
  props.departmentDetail ? "Cập nhật bộ môn" : "Thêm bộ môn"
);

const okText = computed(() => (props.departmentDetail ? "Cập nhật" : "Thêm"));

watch(
  () => props.departmentDetail,
  (newVal) => {
    if (newVal) {
      Object.assign(modelRef, {
        departmentCode: newVal.departmentCode,
        departmentName: newVal.departmentName,
      });
    } else {
      resetFields();
    }
  },
  { immediate: true }
);

const formFields = computed(() => [
  {
    label: "Mã bộ môn",
    name: "departmentCode",
    component: "a-input",
    props: { placeholder: "Nhập mã bộ môn" },
  },
  {
    label: "Tên bộ môn",
    name: "departmentName",
    component: "a-input",
    props: { placeholder: "Nhập tên bộ môn" },
  },
]);

const handleAddOrUpdate = () => {
  Modal.confirm({
    content: 'Bạn chắc chắn muốn thêm chứ',
    icon: createVNode(ExclamationCircleOutlined),
    centered: true,
    async onOk() {
      try {
        await validate();
        props.departmentDetail
          ? updateDepartment({
              departmentId: props.departmentDetail.departmentId,
              params: {
                departmentCode: modelRef.departmentCode,
                departmentName: modelRef.departmentName,
              },
            })
          : createDepartment({
              departmentCode: modelRef.departmentCode,
              departmentName: modelRef.departmentName,
            });
        toast.success(
          props.departmentDetail
            ? "Cập nhật bộ môn thành công"
            : "Thêm bộ môn thành công"
        );
        emit("handleClose");
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
};

const handleClose = () => {
  emit("handleClose");
  resetFields();
};
</script>
