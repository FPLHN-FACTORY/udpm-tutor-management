<template>
  <div>
    <a-modal
      :open="open"
      title="Cập nhật lớp tutor"
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
} from "@/services/service/admin/subject.action";
import { filterOption } from "@/utils/common.helper";
import { Form } from "ant-design-vue";
import dayjs from "dayjs";
import { computed, reactive, watch } from "vue";
import { toast } from "vue3-toastify";

interface SubjectForm {
  subjectCode: string;
  subjectName: string;
  departmentId: string | null;
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
  departmentOptionsData?.value?.data.map((dept) => ({
    value: dept.id,
    label: dept.name,
  }))
);

const modelRef = reactive<SubjectForm>({
  subjectCode: "",
  subjectName: "",
  departmentId: null,
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
    { required: true, message: "Vui lòng chọn ngày bắt đầu", trigger: "blur" },
  ],
});

const { resetFields, validate, validateInfos } = Form.useForm(
  modelRef,
  rulesRef
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
    label: "Giảng viên tutor",
    name: "staffName",
    component: "a-select",
    props: {
      placeholder: "Chọn giảng viên tutor",
      loading: false,
      showSearch: true,
      filterOption: filterOption,
    },
    // options: departmentOptions.value,
  },
  {
    label: "Sinh viên tutor",
    name: "studentName",
    component: "a-select",
    props: {
      placeholder: "Chọn sinh viên tutor",
      loading: false,
      showSearch: true,
      filterOption: filterOption,
    },
  },
  {
    label: "Ngày bắt đầu - kết thúc",
    name: "startEndTime",
    component: "a-select",
  },
  {
    label: "Ca",
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
    label: "Lớp",
    name: "startDate",
    component: "a-date-picker",
    props: {
      placeholder: "Chọn ngày tạo",
      class: "w-full",
      format: "DD/MM/YYYY",
    },
  },
]);

const handleAddOrUpdate = async () => {
  try {
    await validate(); // Kiểm tra tính hợp lệ

    const payload = {
      ...modelRef,
      startDate: dayjs(modelRef.startDate).toDate().getTime(),
    };

    // Tạo biến để giữ thông tin về hành động (cập nhật hay tạo mới)
    const actionParams = props.subjectDetail
        ? {
          subjectId: props.subjectDetail.subjectId,
          params: payload,
        }
        : payload;

    // Gọi hàm phù hợp dựa vào subjectDetail
    const action = props.subjectDetail ? updateSubject : createSubject;
    const message = props.subjectDetail ? "Cập nhật môn học thành công!" : "Tạo môn học thành công!";

    await action(actionParams, {
      onSuccess: () => {
        toast.success(message);
        handleClose();
      },
      onError: (error) => {
        toast.error(
            error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
        )
      },
    }); // Chờ kết quả của hành động

  } catch (error: any) {
    console.error("🚀 ~ handleAddOrUpdate ~ error:", error);
  }
};

const handleClose = () => {
  emit("handleClose");
  resetFields();
};
</script>
