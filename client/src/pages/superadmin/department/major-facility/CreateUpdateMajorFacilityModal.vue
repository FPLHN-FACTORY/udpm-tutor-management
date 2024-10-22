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
import { ERROR_MESSAGE } from "@/constants/message.constant";
import { ParamsMajorOption } from "@/services/api/common.api";
import { useGetMajorOptions } from "@/services/service/common.action";
import {
  useCreateMajorFacility,
  useUpdateMajorFacility,
} from "@/services/service/superadmin/major.action";
import { ExclamationCircleOutlined } from "@ant-design/icons-vue";
import { Form, Modal } from "ant-design-vue";
import { computed, createVNode, reactive, ref, watch } from "vue";
import { toast } from "vue3-toastify";

interface MajorFacilityForm {
  majorId: string;
}

const props = defineProps({
  open: Boolean,
  majorFacilityDetail: Object as () => any | null,
  isLoadingDetail: Boolean,
  departmentFacilityId: String,
  headMajorId: String,
  departmentId: String,
});

const emit = defineEmits(["handleClose"]);

const paramsGetMajorOption = ref<ParamsMajorOption>({
  departmentId: props.departmentId || "",
  facilityId: null,
});

const { data: majorOptionsData } = useGetMajorOptions(paramsGetMajorOption, {
  refetchOnWindowFocus: false,
  enabled: () => !!props.departmentId && props.open,
});

const { mutate: createMajorFacility } = useCreateMajorFacility();

const { mutate: updateMajorFacility } = useUpdateMajorFacility();

const modelRef = reactive<MajorFacilityForm>({
  majorId: "",
});

const rulesRef = reactive({
  majorId: [
    { required: true, message: "Vui lòng chọn chuyên ngành", trigger: "blur" },
  ],
});

const { resetFields, validate, validateInfos } = Form.useForm(
  modelRef,
  rulesRef
);

const modalTitle = computed(() =>
  props.majorFacilityDetail
    ? "Cập nhật chuyên ngành theo cơ sở"
    : "Thêm chuyên ngành theo cơ sở"
);

const okText = computed(() =>
  props.majorFacilityDetail ? "Cập nhật" : "Thêm"
);

watch(
  () => props.majorFacilityDetail,
  (newVal) => {
    if (newVal) {
      Object.assign(modelRef, {
        majorId: newVal.majorId,
      });
    } else {
      resetFields();
    }
  },
  { immediate: true }
);

const formFields = computed(() => [
  {
    label: "Chuyên ngành",
    name: "majorId",
    component: "a-select",
    props: { placeholder: "Chọn chuyên ngành" },
    options: majorOptions.value,
  },
]);

const handleCreateMajorFacility = async () => {
  try {
    await validate();

    createMajorFacility(
      {
        departmentFacilityId: props.departmentFacilityId || "",
        headMajorId: props.headMajorId || "",
        majorId: modelRef.majorId,
      },
      {
        onSuccess: () => {
          toast.success("Tạo chuyên ngành theo cơ sở thành công!");
          handleClose();
        },
        onError: (error: any) => {
          toast.error(
            error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
          );
        },
      }
    );
  } catch (error: any) {
    console.error("🚀 ~ handleCreateMajorFacility ~ error:", error);
    toast.error(
      error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
    );
  }
};

const handleUpdateMajorFacility = async () => {
  try {
    await validate();

    updateMajorFacility(
      {
        majorFacilityId: props.majorFacilityDetail.id,
        params: {
          majorId: modelRef.majorId,
          headMajorId: props.headMajorId || "",
          departmentFacilityId: props.departmentFacilityId || "",
        },
      },
      {
        onSuccess: () => {
          toast.success("Cập nhật chuyên ngành theo cơ sở thành công!");
          handleClose();
        },
        onError: (error: any) => {
          toast.error(
            error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
          );
        },
      }
    );
  } catch (error: any) {
    console.error("🚀 ~ handleUpdateMajorFacility ~ error:", error);
    toast.error(
      error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
    );
  }
};

const handleAddOrUpdate = () => {
  Modal.confirm({
    content: "Bạn chắc chắn muốn cập nhật chứ",
    icon: createVNode(ExclamationCircleOutlined),
    centered: true,
    async onOk() {
      if (props.majorFacilityDetail) {
        await handleUpdateMajorFacility();
      } else {
        await handleCreateMajorFacility();
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

const majorOptions = computed(() => {
  return majorOptionsData?.value?.data?.map((item) => ({
    value: item.id,
    label: item.name,
  }));
});
</script>
