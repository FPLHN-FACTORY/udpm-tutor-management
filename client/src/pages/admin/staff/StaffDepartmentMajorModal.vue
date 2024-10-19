<template>
  <div>
    <a-modal
      :open="open"
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
      <template #title>
        <span class="text-xl font-semibold mb-4">{{ modalTitle }}</span>
      </template>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import { ERROR_MESSAGE } from "@/constants/message.constant";
import { ParamsMajorOption } from "@/services/api/common.api";
import {
  useCreateStaffDeparmentMajor,
  useUpdateStaffDeparmentMajor,
} from "@/services/service/admin/staff.action";
import {
  useGetDepartmentOptions,
  useGetMajorOptions,
} from "@/services/service/common.action";
import { useAuthStore } from "@/stores/auth";
import { ExclamationCircleOutlined } from "@ant-design/icons-vue";
import { Form, Modal } from "ant-design-vue";
import { computed, createVNode, reactive, ref, watch } from "vue";
import { useRoute } from "vue-router";
import { toast } from "vue3-toastify";

const auth = useAuthStore();
const userInfo = computed(() => auth.user);
const route = useRoute();
const staffId = computed(() => {
  const id = route.params.staffId;
  return Array.isArray(id) ? id[0] : id || null;
});

interface DepartmentFacilityForm {
  idFacility: string;
  idDepartment: string;
  idMajor: string;
}

const props = defineProps({
  open: Boolean,
  staffDepartmentMajorDetail: Object as () => any | null,
  isLoadingDetail: Boolean,
});

const emit = defineEmits(["handleClose"]);

const paramsGetMajorOption = ref<ParamsMajorOption>({
  departmentId: null,
  facilityId: null,
});

const { data: departmentOptionsData } = useGetDepartmentOptions();

const { data: majorOptionsData, refetch: refechMajorOptions } =
  useGetMajorOptions(paramsGetMajorOption, {
    refetchOnWindowFocus: false,
    enabled: () => !!paramsGetMajorOption.value.departmentId,
  });

const { mutate: createDeparmentMajor } = useCreateStaffDeparmentMajor();

const { mutate: updateDeparmentMajor } = useUpdateStaffDeparmentMajor();

watch(paramsGetMajorOption, () => {
  if (paramsGetMajorOption.value.departmentId) {
    refechMajorOptions();
  }
});

const modelRef = reactive<DepartmentFacilityForm>({
  idFacility: "",
  idDepartment: "",
  idMajor: "",
});

const rulesRef = reactive({
  idDepartment: [
    { required: true, message: "Vui lòng chọn bộ môn", trigger: "blur" },
  ],
  idMajor: [
    { required: true, message: "Vui lòng chọn chuyên ngành", trigger: "blur" },
  ],
});

const { resetFields, validate, validateInfos } = Form.useForm(
  modelRef,
  rulesRef
);

const modalTitle = computed(() =>
  props.staffDepartmentMajorDetail
    ? "Cập nhật bộ môn, chuyên ngành theo cơ sở"
    : "Thêm bộ môn, chuyên ngành theo cơ sở"
);

const okText = computed(() =>
  props.staffDepartmentMajorDetail ? "Cập nhật" : "Thêm"
);

watch(
  () => props.staffDepartmentMajorDetail,
  (newVal) => {
    if (newVal) {
      Object.assign(modelRef, {
        idFacility: newVal.facilityId,
        idDepartment: newVal.departmentId,
        idMajor: newVal.majorId,
      });
      paramsGetMajorOption.value = {
        departmentId: newVal.departmentId,
        facilityId: userInfo.value?.facilityId || "",
      };
    } else {
      resetFields();
    }
  },
  { immediate: true }
);

const formFields = computed(() => [
  {
    label: "Chọn bộ môn",
    name: "idDepartment",
    component: "a-select",
    props: {
      placeholder: "Tìm kiếm bộ môn",
      onChange: handleDepartmentChange,
    },
    options: departmentOptions.value,
  },
  {
    label: "Chọn chuyên ngành",
    name: "idMajor",
    component: "a-select",
    props: { placeholder: "Tìm kiếm chuyên nghành" },
    options: majorOptions.value,
  },
]);

const handleDepartmentChange = (departmentId: string) => {
  paramsGetMajorOption.value = {
    departmentId: departmentId,
    facilityId: userInfo.value?.facilityId || "",
  };
  modelRef.idMajor = "";
};

const handleAddOrUpdate = () => {
  Modal.confirm({
    content: "Bạn chắc chắn muốn cập nhật chứ",
    icon: createVNode(ExclamationCircleOutlined),
    centered: true,
    async onOk() {
      try {
        await validate();
        const actionParams = props.staffDepartmentMajorDetail
          ? {
              idDepartment: modelRef.idDepartment,
              idFacility: userInfo.value?.facilityId || "",
              idMajor: modelRef.idMajor,
              idStaff: staffId.value,
              idStaffMajorFacility:
                props.staffDepartmentMajorDetail.staffMajorFacilityId,
            }
          : {
              idDepartment: modelRef.idDepartment,
              idFacility: userInfo.value?.facilityId || "",
              idMajor: modelRef.idMajor,
              idStaff: staffId.value,
            };

        const action = props.staffDepartmentMajorDetail
          ? updateDeparmentMajor
          : createDeparmentMajor;
        const message = props.staffDepartmentMajorDetail
          ? "Cập nhật bộ môn, chuyên ngành theo cơ sở thành công!"
          : "Tạo bộ môn, chuyên ngành theo cơ sở thành công!";

        action(actionParams, {
          onSuccess: () => {
            toast.success(message);
            handleClose();
          },
          onError: (error: any) => {
            toast.error(
              error?.response?.data?.message ||
                ERROR_MESSAGE.SOMETHING_WENT_WRONG
            );
          },
        });
      } catch (error: any) {
        console.error("🚀 ~ handleAddOrUpdate ~ error:", error);
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

const departmentOptions = computed(() => {
  return departmentOptionsData?.value?.data?.map((item) => ({
    value: item.id,
    label: item.name,
  }));
});

const majorOptions = computed(() => {
  return majorOptionsData?.value?.data?.map((item) => ({
    value: item.id,
    label: item.name,
  }));
});
</script>
