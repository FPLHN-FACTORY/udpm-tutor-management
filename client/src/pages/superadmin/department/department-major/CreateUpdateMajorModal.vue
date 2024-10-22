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
  useCreateMajor,
  useUpdateMajor,
} from "@/services/service/superadmin/major.action";
import { ExclamationCircleOutlined } from "@ant-design/icons-vue";
import { Form, Modal } from "ant-design-vue";
import { computed, createVNode, reactive, watch } from "vue";
import { toast } from "vue3-toastify";

interface MajorForm {
  majorName: string;
  majorCode: string;
}

const props = defineProps({
  open: Boolean,
  majorDetail: Object as () => any | null,
  isLoadingDetail: Boolean,
  departmentId: String,
});

const emit = defineEmits(["handleClose"]);

const { mutate: createMajor } = useCreateMajor();

const { mutate: updateMajor } = useUpdateMajor();

const modelRef = reactive<MajorForm>({
  majorCode: "",
  majorName: "",
});

const rulesRef = reactive({
  majorCode: [
    {
      required: true,
      message: "Vui lòng nhập mã chuyên ngành",
      trigger: "blur",
    },
  ],
  majorName: [
    {
      required: true,
      message: "Vui lòng nhập tên chuyên ngành",
      trigger: "blur",
    },
  ],
});

const { resetFields, validate, validateInfos } = Form.useForm(
  modelRef,
  rulesRef
);

const modalTitle = computed(() =>
  props.majorDetail ? "Cập nhật chuyên ngành" : "Thêm chuyên ngành"
);

const okText = computed(() => (props.majorDetail ? "Cập nhật" : "Thêm"));

watch(
  () => props.majorDetail,
  (newVal) => {
    if (newVal) {
      Object.assign(modelRef, {
        majorName: newVal.majorName,
        majorCode: newVal.majorCode,
      });
    } else {
      resetFields();
    }
  },
  { immediate: true }
);

const formFields = computed(() => [
  {
    label: "Mã ngành",
    name: "majorCode",
    component: "a-input",
    props: { placeholder: "Ví dụ: CNTT" },
  },
  {
    label: "Tên ngành",
    name: "majorName",
    component: "a-input",
    props: { placeholder: "Ví dụ: Công nghệ thông tin" },
  },
]);

const handleCreateMajor = async () => {
  try {
    await validate();
    const actionParams = {
      majorCode: modelRef.majorCode,
      majorName: modelRef.majorName,
      departmentId: props.departmentId || "",
    };

    createMajor(actionParams, {
      onSuccess: () => {
        toast.success("Tạo chuyên ngành thành công!");
        handleClose();
      },
      onError: (error: any) => {
        toast.error(
          error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
        );
      },
    });
  } catch (error: any) {
    console.error("🚀 ~ handleCreateMajor ~ error:", error);
    toast.error(
      error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
    );
  }
};

const handleUpdateMajor = async () => {
  try {
    await validate();
    const actionParams = {
      majorId: props.majorDetail.id,
      params: {
        majorCode: modelRef.majorCode,
        majorName: modelRef.majorName,
        departmentId: props.departmentId || "",
      },
    };

    updateMajor(actionParams, {
      onSuccess: () => {
        toast.success("Cập nhật chuyên ngành thành công!");
        handleClose();
      },
      onError: (error: any) => {
        toast.error(
          error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
        );
      },
    });
  } catch (error: any) {
    console.error("🚀 ~ handleUpdateMajor ~ error:", error);
    toast.error(
      error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
    );
  }
};

const handleAddOrUpdate = () => {
  Modal.confirm({
    content: "Bạn chắc chắn muốn thêm chứ",
    icon: createVNode(ExclamationCircleOutlined),
    centered: true,
    async onOk() {
      if (props.majorDetail) {
        await handleUpdateMajor();
      } else {
        await handleCreateMajor();
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
