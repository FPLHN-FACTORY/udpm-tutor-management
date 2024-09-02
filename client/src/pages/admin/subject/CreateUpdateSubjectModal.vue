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
import { useGetDepartmentOptions } from "@/services/service/common.action";
import {
  useCreateSubject,
  useUpdateSubject,
} from "@/services/service/subject.action";
import { Form } from "ant-design-vue";
import dayjs from "dayjs";
import { computed, reactive, watch } from "vue";
import { toast } from "vue3-toastify";

interface SubjectForm {
  subjectCode: string;
  subjectName: string;
  departmentId: string;
  subjectType: string;
  startDate: dayjs.Dayjs | null;
}

const props = defineProps({
  open: Boolean,
  subjectDetail: Object as () => any | null,
  isLoadingDetail: Boolean,
});

const emit = defineEmits(["handleClose"]);

const { mutate: createSubject } = useCreateSubject();
const { mutate: updateSubject } = useUpdateSubject();
const { data: departmentOptionsData } = useGetDepartmentOptions();

const departmentOptions = computed(() =>
  departmentOptionsData?.value?.data.map((dept: any) => ({
    value: dept.departmentId,
    label: dept.departmentName,
  }))
);

const modelRef = reactive<SubjectForm>({
  subjectCode: "",
  subjectName: "",
  departmentId: departmentOptions.value?.[0]?.value || "",
  subjectType: "TRADITIONAL",
  startDate: dayjs(),
});

const rulesRef = reactive({
  subjectCode: [
    { required: true, message: "Vui lòng nhập mã môn học", trigger: "blur" },
  ],
  subjectName: [
    { required: true, message: "Vui lòng nhập tên môn học", trigger: "blur" },
  ],
  departmentId: [
    { required: true, message: "Vui lòng chọn bộ môn", trigger: "blur" },
  ],
  subjectType: [
    { required: true, message: "Vui lòng chọn loại môn học", trigger: "blur" },
  ],
  startDate: [
    { required: true, message: "Vui lòng chọn ngày tạo", trigger: "blur" },
  ],
});

const { resetFields, validate, validateInfos } = Form.useForm(
  modelRef,
  rulesRef
);

const modalTitle = computed(() =>
  props.subjectDetail ? "Cập nhật môn học" : "Thêm môn học"
);

const okText = computed(() =>
  props.subjectDetail ? "Cập nhật môn học" : "Thêm môn học"
);

watch(
  () => props.subjectDetail,
  (newVal) => {
    if (newVal) {
      Object.assign(modelRef, {
        subjectCode: newVal.subjectCode,
        subjectName: newVal.subjectName,
        departmentId: newVal.departmentId,
        subjectType: newVal.subjectType,
        startDate: dayjs(newVal.createdDate),
      });
    } else {
      resetFields();
    }
  },
  { immediate: true }
);

const formFields = computed(() => [
  {
    label: "Mã môn học",
    name: "subjectCode",
    component: "a-input",
    props: { placeholder: "Nhập mã môn học" },
  },
  {
    label: "Tên môn học",
    name: "subjectName",
    component: "a-input",
    props: { placeholder: "Nhập tên môn học" },
  },
  {
    label: "Bộ môn",
    name: "departmentId",
    component: "a-select",
    props: {
      placeholder: "Chọn bộ môn",
      loading: false,
    },
    options: departmentOptions.value,
  },
  {
    label: "Loại môn học",
    name: "subjectType",
    component: "a-select",
    props: { placeholder: "Chọn loại môn học" },
    options: [
      { value: "TRADITIONAL", label: "Truyền thống" },
      { value: "ONLINE", label: "Online" },
      { value: "BLEND", label: "Blend" },
    ],
  },
  {
    label: "Ngày tạo",
    name: "startDate",
    component: "a-date-picker",
    props: {
      placeholder: "Chọn ngày tạo",
      class: "w-full",
      disabled: true,
      format: "DD/MM/YYYY",
    },
  },
]);

const handleAddOrUpdate = async () => {
  try {
    await validate();
    const payload = {
      ...modelRef,
      startDate: dayjs(modelRef.startDate).toDate().getTime(),
    };
    props.subjectDetail
      ? updateSubject({
          subjectId: props.subjectDetail.subjectId,
          params: payload,
        })
      : createSubject(payload);
    toast.success(
      props.subjectDetail
        ? "Cập nhật môn học thành công"
        : "Thêm môn học thành công"
    );
    emit("handleClose");
  } catch (error: any) {
    console.error("🚀 ~ handleAddOrUpdate ~ error:", error);
    toast.error(
      error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
    );
  }
};

const handleClose = () => {
  emit("handleClose");
  resetFields();
};
</script>
