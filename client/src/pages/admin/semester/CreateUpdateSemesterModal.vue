<template>
  <div>
    <a-modal :open="open" :title="modalTitle" @cancel="handleClose" @ok="handleAddOrUpdate" destroyOnClose centered
      :ok-text="okText">
      <div v-if="props.isLoadingDetail" class="flex justify-center items-center">
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
              ></component>
            </a-form-item>
          </template>
        </a-form>
      </div>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import { defineProps, defineEmits, reactive, computed, watch, createVNode } from "vue";
import dayjs from "dayjs";
import { Form, Modal } from "ant-design-vue";
import { ExclamationCircleOutlined } from "@ant-design/icons-vue";
import { toast } from "vue3-toastify";
import { ERROR_MESSAGE } from "@/constants/message.constant";
import { useCreateSemester, useUpdateSemester } from "@/services/service/admin/semester.action";

interface SubjectForm {
  semesterName: string;
  startTime: dayjs.Dayjs | null;
  endTime: dayjs.Dayjs | null;
  block1Time: dayjs.Dayjs[];  // Block 1 là mảng chứa thời gian bắt đầu và kết thúc
  block2Time: dayjs.Dayjs[];  // Block 2 cũng là mảng chứa thời gian bắt đầu và kết thúc
}

const props = defineProps({
  open: Boolean,
  semesterDetail: Object as () => any | null,
  isLoadingDetail: Boolean,
});

const emit = defineEmits(["handleCloseModal"]);

const { mutate: createSemester } = useCreateSemester();
const { mutate: updateSemester } = useUpdateSemester();

const modelRef = reactive<SubjectForm>({
  semesterName: props.semesterDetail?.semesterName || "",
  startTime: props.semesterDetail?.startTime || null,
  endTime: props.semesterDetail?.endTime || null,
  block1Time: [props.semesterDetail?.startTimeFirstBlock, props.semesterDetail?.endTimeFirstBlock],
  block2Time: [props.semesterDetail?.startTimeSecondBlock, props.semesterDetail?.endTimeSecondBlock],
});

const rulesRef = reactive({
  semesterName: [{ required: true, message: "Vui lòng nhập mã môn học", trigger: "blur" }],
  startTime: [{ required: true, message: "Vui lòng chọn thời gian bắt đầu", trigger: "blur" }],
  endTime: [{ required: true, message: "Vui lòng chọn thời gian kết thúc", trigger: "blur" }],
  block1Time: [{ required: true, message: "Vui lòng chọn thời gian block 1", trigger: "blur" }],
  block2Time: [{ required: true, message: "Vui lòng chọn thời gian block 2", trigger: "blur" }],
});

const { resetFields, validate, validateInfos } = Form.useForm(modelRef, rulesRef);

const formFields = computed(() => [
  {
    label: "Tên học kỳ",
    name: "semesterName",
    component: "a-input",
    props: {
      placeholder: "Tên học kỳ",
    },
  },
  {
    label: "Thời gian bắt đầu học kỳ",
    name: "startTime",
    component: "a-date-picker",
    props: {
      class: "w-full",
      format: "DD/MM/YYYY",
    },
  },
  {
    label: "Thời gian kết thúc học kỳ",
    name: "endTime",
    component: "a-date-picker",
    props: {
      class: "w-full",
      format: "DD/MM/YYYY",
    },
  },
  {
    label: "Thời gian block 1",
    name: "block1Time",
    component: "a-range-picker",
    props: {
      class: "w-full",
      format: "DD/MM/YYYY",
    },
  },
  {
    label: "Thời gian block 2",
    name: "block2Time",
    component: "a-range-picker",
    props: {
      class: "w-full",
      format: "DD/MM/YYYY",
    },
  },
]);

// Watch để theo dõi sự thay đổi của block1Time và tự động tính toán block2Time
watch(
  () => modelRef.block1Time,
  (newBlock1Time) => {
    if (newBlock1Time && modelRef.endTime) {
      // Giả sử thời gian block 2 bắt đầu sau khi block 1 kết thúc 1 ngày
      const block2StartTime = dayjs(newBlock1Time[1]).add(1, 'day');
      const block2EndTime = dayjs(modelRef.endTime);

      // Gán giá trị mới cho block2Time
      modelRef.block2Time = [block2StartTime, block2EndTime];
    }
  }
);

const handleAddOrUpdate = () => {
  Modal.confirm({
    content: 'Bạn chắc chắn muốn thêm chứ',
    icon: createVNode(ExclamationCircleOutlined),
    centered: true,
    async onOk() {
      try {
        await validate(); // Kiểm tra tính hợp lệ
        const block1EndTime = dayjs(modelRef.block1Time[1]).toDate().getTime();
        // Tạo biến để giữ thông tin về hành động (cập nhật hay tạo mới)
        const actionParams = props.semesterDetail
          ? {
            semesterId: props.semesterDetail.id,
            params: {
              endTime: dayjs(modelRef.endTime).toDate().getTime(),
              endTimeBlock1: block1EndTime,
              semesterName: modelRef.semesterName,
              startTime: dayjs(modelRef.startTime).toDate().getTime(),
            },
          }
          : {
            endTime: dayjs(modelRef.endTime).toDate().getTime(),
            endTimeBlock1: block1EndTime,
            semesterName: modelRef.semesterName,
            startTime: dayjs(modelRef.startTime).toDate().getTime(),
          };

        // Gọi hàm phù hợp dựa vào semesterDetail
        const action = props.semesterDetail ? updateSemester : createSemester;
        const message = props.semesterDetail ? "Cập nhật học kì thành công!" : "Tạo học kì thành công!";

        action(actionParams, {
          onSuccess: () => {
            toast.success(message);
            handleClose();
          },
          onError: (error: any) => {
            toast.error(
              error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
            )
          },
        })
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
  emit("handleCloseModal");
};

const modalTitle = computed(() =>
  props.semesterDetail ? "Cập nhật học kì" : "Thêm học kì"
);

const okText = computed(() =>
  props.semesterDetail ? "Cập nhật học kì" : "Thêm học kì"
);

watch(
  () => props.semesterDetail,
  (newVal) => {
    if (newVal) {
      Object.assign(modelRef, {
        semesterName: newVal.semesterName,
        startTime: dayjs(newVal.startTime),
        endTime: dayjs(newVal.endTime),
        block1Time: [dayjs(newVal.startTimeFirstBlock), dayjs(newVal.endTimeFirstBlock)],
        block2Time: [dayjs(newVal.startTimeSecondBlock), dayjs(newVal.endTimeSecondBlock)],
      });
    } else {
      resetFields();
    }
  },
  { immediate: true }
);

</script>
