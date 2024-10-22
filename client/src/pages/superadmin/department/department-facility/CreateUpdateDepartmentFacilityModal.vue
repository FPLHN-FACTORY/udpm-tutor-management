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
import { ParamsStaffOptions } from "@/services/api/common.api";
import {
  useGetFacilityOptions,
  useGetStaffOptions,
} from "@/services/service/common.action";
import {
  useCreateDepartmentFacility,
  useUpdateDepartmentFacility,
} from "@/services/service/superadmin/department.action";
import { useAuthStore } from "@/stores/auth";
import { ExclamationCircleOutlined } from "@ant-design/icons-vue";
import { Form, Modal } from "ant-design-vue";
import { computed, createVNode, reactive, ref, watch } from "vue";
import { toast } from "vue3-toastify";

const { user } = useAuthStore();

interface DepartmentFacilityForm {
  facilityId: string | null;
  headOfDepartmentId: string | null;
}

const props = defineProps({
  open: Boolean,
  departmentFacilityDetail: Object as () => any | null,
  isLoadingDetail: Boolean,
  departmentId: String,
});

const emit = defineEmits(["handleClose"]);

const paramsGetStaffOption = ref<ParamsStaffOptions>({});

const { data: staffOptionsData } = useGetStaffOptions(paramsGetStaffOption, {
  refetchOnWindowFocus: false,
});

const { data: facilityOptionsData } = useGetFacilityOptions();

const { mutate: createDepartmentFacility } = useCreateDepartmentFacility();

const { mutate: updateDepartmentFacility } = useUpdateDepartmentFacility();

const modelRef = reactive<DepartmentFacilityForm>({
  facilityId: null,
  headOfDepartmentId: null,
});

const rulesRef = reactive({
  facilityId: [
    { required: true, message: "Vui lòng chọn cơ sở", trigger: "blur" },
  ],
  headOfDepartmentId: [
    {
      required: true,
      message: "Vui lòng chọn chủ nhiệm bộ môn",
      trigger: "blur",
    },
  ],
});

const { resetFields, validate, validateInfos } = Form.useForm(
  modelRef,
  rulesRef
);

const modalTitle = computed(() =>
  props.departmentFacilityDetail
    ? "Cập nhật bộ môn theo cơ sở"
    : "Thêm bộ môn theo cơ sở"
);

const okText = computed(() =>
  props.departmentFacilityDetail ? "Cập nhật" : "Thêm"
);

watch(
  () => props.departmentFacilityDetail,
  (newVal) => {
    if (newVal) {
      Object.assign(modelRef, {
        headOfDepartmentId: newVal.headOfDepartmentId,
        facilityId: newVal.facilityId,
      });
    } else {
      resetFields();
    }
  },
  { immediate: true }
);

const formFields = computed(() => [
  {
    label: "Chọn chủ nhiệm bộ môn",
    name: "headOfDepartmentId",
    component: "a-select",
    props: { placeholder: "Tìm kiếm chủ nhiệm bộ môn" },
    options: staffOptions.value,
  },
  {
    label: "Chọn cơ sở",
    name: "facilityId",
    component: "a-select",
    props: { placeholder: "Tìm kiếm cơ sở" },
    options: facilityOptions.value,
  },
]);

const createDepartmentFacilityAction = async () => {
  try {
    const validateFields = await validate();

    createDepartmentFacility(
      {
        departmentId: props.departmentId || "",
        facilityId: validateFields.facilityId,
        headOfDepartmentId: modelRef.headOfDepartmentId || "",
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
    console.error("🚀 ~ createDepartmentFacilityAction ~ error:", error);
    toast.error(
      error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
    );
  }
};

const updateDepartmentFacilityAction = async () => {
  try {
    await validate();

    updateDepartmentFacility(
      {
        id: props.departmentFacilityDetail.departmentFacilityId,
        params: {
          departmentId: props.departmentId || "",
          facilityId: user?.facilityId || "",
          headOfDepartmentId: modelRef.headOfDepartmentId || "",
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
    console.error("🚀 ~ updateDepartmentFacilityAction ~ error:", error);
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
      if (props.departmentFacilityDetail) {
        await updateDepartmentFacilityAction();
      } else {
        await createDepartmentFacilityAction();
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

const staffOptions = computed(
  () =>
    staffOptionsData?.value?.data.map((staff) => ({
      value: staff.id,
      label: staff.name,
    })) || []
);

const facilityOptions = computed(
  () =>
    facilityOptionsData?.value?.data.map((facility) => ({
      value: facility.id,
      label: facility.name,
    })) || []
);
</script>
