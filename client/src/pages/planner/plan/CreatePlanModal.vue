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
import { useCreatePlan, useUpdatePlan } from "@/services/service/planner/plan.action.ts";
import { useAuthStore } from "@/stores/auth.ts";
import { filterOption, formatBlockName } from "@/utils/common.helper.ts";
import { getBlockOptions } from "@/services/api/common.api.ts";
import { ERROR_MESSAGE } from "@/constants/message.constant.ts";
import { toast } from "vue3-toastify";
import dayjs from "dayjs";

const auth = useAuthStore();
const userInfo = computed(() => auth.user);

const props = defineProps({
  open: Boolean,
  planDetail: Object as () => any | null,
  isLoadingDetail: Boolean,
  semesterOptions: Array as () => any,
  semesterId: String,
  blockId: String,
});

const emit = defineEmits(["handleClose"]);

const { mutate: createPlan } = useCreatePlan();
const { mutate: updatePlan } = useUpdatePlan();

interface PlanForm {
  blockId: string | null,
  description: string | null,
  semesterId: string | null,
  timeAdditionSubject: [dayjs.Dayjs, dayjs.Dayjs] | null,
  departmentCode: string | null,
  facilityCode: string | null,
  userCode: string | null,
}

// Reactive model reference
const modelRef = reactive<PlanForm>({
  blockId: "",
  description: "",
  semesterId: "",
  timeAdditionSubject: [dayjs(), dayjs()],
  departmentCode: userInfo.value?.departmentCode || "",
  facilityCode: userInfo.value?.facilityCode || "",
  userCode: userInfo.value?.userCode || "",
});

// Validation rules
const rulesRef = reactive({
  semesterId: [{ required: true, message: "Vui lòng chọn học kỳ", trigger: "blur" }],
  blockId: [{ required: true, message: "Vui lòng chọn block", trigger: "blur" }],
  timeAdditionSubject: [{ required: true, message: "Vui lòng chọn thời gian thêm môn", trigger: "blur" }],
});

const { resetFields, validate, validateInfos } = Form.useForm(modelRef, rulesRef);

const modalTitle = computed(() => (props.planDetail ? "Cập nhật kế hoạch" : "Thêm kế hoạch"));
const okText = computed(() => (props.planDetail ? "Cập nhật kế hoạch" : "Thêm kế hoạch"));

// Block options
const blockOptions = ref([{ value: "", label: "-- Chọn Block --" }]);

// Watch for changes in planDetail to populate form
watch(
    () => props.planDetail,
    (newVal) => {
      if (newVal) {
        Object.assign(modelRef, {
          blockId: newVal.blockId,
          description: newVal.description,
          semesterId: newVal.semesterId,
          timeAdditionSubject: [dayjs(newVal.startTime), dayjs(newVal.endTime)],
        });
        if (newVal.semesterId) {
          fetchBlockOptions(newVal.semesterId);
        }
      }
    },
    { immediate: true }
);

// Function to fetch block options
const fetchBlockOptions = async (semesterId: string) => {
  try {
    const response = await getBlockOptions(semesterId);
    blockOptions.value = response.data.map(block => ({
      value: block.id,
      label: formatBlockName(block.name),
    }));
  } catch (error) {
    console.error("Error fetching block options:", error);
    blockOptions.value = [{ value: "", label: "-- Chọn Block --" }];
  }
};

// Watch for semesterId changes to refetch block options
watch(
    () => modelRef.semesterId,
    (newSemesterId) => {
      if (newSemesterId) {
        fetchBlockOptions(newSemesterId);
      } else {
        // Reset block options if semesterId is empty
        modelRef.blockId = ""; // Reset blockId if needed
        blockOptions.value = [{ value: "", label: "-- Chọn Block --" }];
      }
    }
);

watch(
    () => props.open,
    (openNew) => {
      if (openNew) {
        resetFields();
        if(!props.planDetail){
          modelRef.semesterId = props.semesterId;// Default semesterId
          modelRef.blockId = props.blockId;// Default blockId
        }
      }
    }
);

// Form fields configuration
const formFields = computed(() => [
  {
    label: "Học kỳ",
    name: "semesterId",
    component: "a-select",
    props: {
      placeholder: "Chọn học kỳ",
      loading: false,
      showSearch: true,
      filterOption: filterOption,
    },
    options: props.semesterOptions,
  },
  {
    label: "Block",
    name: "blockId",
    component: "a-select",
    props: { placeholder: "Chọn block" },
    options: blockOptions.value,
  },
  {
    label: "Thời Gian Thêm Môn",
    name: "timeAdditionSubject",
    component: "a-range-picker",
    props: {
      class: "w-full",
      format: "DD/MM/YYYY",
    },
  },
  {
    label: "Mô tả",
    name: "description",
    component: "a-input",
    props: { placeholder: "Nhập mô tả" },
  },
]);

// Handle form submission
const handleAddOrUpdate = async () => {
  try {
    await validate(); // Kiểm tra tính hợp lệ

    const [startTime, endTime] = modelRef.timeAdditionSubject;

    const payload = {
      ...modelRef,
      startTime: startTime ? startTime.valueOf() : null, // Chuyển đổi sang long
      endTime: endTime ? endTime.valueOf() : null, // Chuyển đổi sang long
    };

    // Tạo biến để giữ thông tin về hành động (cập nhật hay tạo mới)
    const actionParams = props.planDetail
        ? {
          planId: props.planDetail.planId,
          params: payload,
        }
        : payload;

    // Gọi hàm phù hợp dựa vào planDetail
    const action = props.planDetail ? updatePlan : createPlan;
    const message = props.planDetail ? "Cập nhật kế hoạch thành công!" : "Tạo kế hoạch thành công!";

    action(actionParams, {
      onSuccess: () => {
        toast.success(message); // Hiển thị thông báo thành công
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
