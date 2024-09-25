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
import {useCreatePlan, useUpdatePlan} from "@/services/service/planner/plan.action.ts";
import { useAuthStore } from "@/stores/auth.ts";
import { filterOption } from "@/utils/common.helper.ts";
import { getBlockOptions } from "@/services/api/common.api.ts";

const auth = useAuthStore();
const userInfo = computed(() => auth.user);

const props = defineProps({
  open: Boolean,
  planDetail: Object as () => any | null,
  isLoadingDetail: Boolean,
  semesterOptions: Array as () => any,
});

const emit = defineEmits(["handleClose"]);

const { mutate: createPlan } = useCreatePlan();
const { mutate: updatePlan } = useUpdatePlan();

interface PlanForm {
  blockId: string | null,
  description: string | null,
  semesterId: string | null,
  departmentCode: string | null,
  facilityCode: string | null,
  userCode: string | null,
}

// Reactive model reference
const modelRef = reactive<PlanForm>({
  blockId: "",
  description: "",
  semesterId: "",
  departmentCode: userInfo.value?.departmentCode || "",
  facilityCode: userInfo.value?.facilityCode || "",
  userCode: userInfo.value?.userCode || "",
});

// Validation rules
const rulesRef = reactive({
  semesterId: [{ required: true, message: "Vui lòng chọn học kỳ", trigger: "blur" }],
  blockId: [{ required: true, message: "Vui lòng chọn block", trigger: "blur" }],
  description: [{ required: true, message: "Vui lòng nhập mô tả", trigger: "blur" }],
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
        });
        // Call API to fetch block options if semesterId is available
        if (newVal.semesterId) {
          fetchBlockOptions(newVal.semesterId);
        }
      } else {
        resetFields();
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
      label: block.name,
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
    label: "Mô tả",
    name: "description",
    component: "a-input",
    props: { placeholder: "Nhập mô tả" },
  },
]);

// Handle form submission
const handleAddOrUpdate = async () => {
  try {
    console.log(modelRef)
    const payload = {
      ...modelRef,
    };
    await validate();
    props.planDetail
        ? updatePlan({
          planId: props.planDetail.planId,
          // @ts-ignore
          params: payload,
        })
        : // @ts-ignore
        createPlan(payload);
    emit("handleClose"); // Close the modal
  } catch (error) {
    console.error("Error in adding/updating plan:", error);
  }
};

// Handle modal close
const handleClose = () => {
  emit("handleClose");
  resetFields(); // Reset fields only after the modal is closed
};

</script>
