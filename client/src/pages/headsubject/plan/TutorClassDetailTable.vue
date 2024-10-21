<template>
  <div class="mt-5 rounded-md flex h-full flex-col">
    <div class="flex h-0 flex-1 flex-col">
      <tutor-table
          wrapperClassName="min-h-[410px]"
          :columns="columnsTutorClassDetail"
          :data-source="dataSource"
          :loading="loading"
          :pagination-params="paginationParams || {}"
          :total-pages="totalPages || 0"
          :scroll="{ x: 'max-content' }"
          @update:pagination-params="$emit('update:paginationParams', $event)"
      >
        <template #bodyCell="{ column, record }">
          <div v-if="column.key === 'action'" class="space-x-2 flex items-center ">
            <a-tooltip title="Xóa lớp tutor" color="#FFC26E">
              <a-button
                  class="flex items-center justify-center"
                  type="primary"
                  size="large"
                  @click="handleDeleteTutorClassDetail(record.id)"
                  :icon="h(MinusCircleOutlined)"
              />
            </a-tooltip>
            <a-tooltip title="Thêm lớp tutor" color="#FFC26E">
              <a-button
                  v-if="isFirstClassOfSubject(record)"
                  class="flex items-center justify-center"
                  type="primary"
                  size="large"
                  @click="handleAddTutorClassDetail(record.id)"
                  :icon="h(PlusCircleOutlined)"
              />
            </a-tooltip>
          </div>
          <div v-else-if="column.key === 'shift'">
            <a-select
                v-model:value="record.shift"
                show-search
                placeholder="Chọn ca"
                :options="shiftOptions"
                :filter-option="(input, option) => option.label.toLowerCase().includes(input.toLowerCase())"
                style="width: 100%"
                :bordered="false"
                disabled
            />
          </div>
          <div v-else-if="column.key === 'room'">
            <a-input
                v-model:value="record.room"
                placeholder="Nhập phòng"
                :bordered="false"
                disabled
            />
          </div>
          <div v-else-if="column.key === 'studentTutor'">
            <a-select
                v-model:value="record.studentTutor"
                placeholder="Chọn sinh viên"
                style="width: 100%"
                show-search
                :options="studentOption"
                :filter-option="(input, option) => option.label.toLowerCase().includes(input.toLowerCase())"
                :bordered="false"
                disabled
            >
            </a-select>
          </div>
          <div v-else-if="column.key === 'teacherTutor'">
            <a-select
                v-model:value="record.teacherTutor"
                show-search
                placeholder="Chọn giảng viên"
                :options="teacherOption"
                :filter-option="(input, option) => option.label.toLowerCase().includes(input.toLowerCase())"
                @change="(value) => updateTeacher(record.id, value)"
                style="width: 100%"
                :disabled="canUpdate"
            >
            </a-select>
          </div>
        </template>
      </tutor-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import TutorTable from "@/components/ui/TutorTable/TutorTable.vue";
import { TutorClassDetailResponse } from "@/services/api/headsubject/tutor-class.api.ts";
import {computed, createVNode, defineProps, h} from "vue";
import {
  ExclamationCircleOutlined,
  MinusCircleOutlined,
  PlusCircleOutlined
} from "@ant-design/icons-vue";
import {
  useAddTutorClassDetail,
  useDeleteTutorClassDetail,
  useUpdateTutorClassDetail
} from "@/services/service/headsubject/tutor-class.action.ts";
import { Modal } from "ant-design-vue";
import { toast } from "vue3-toastify";
import { ERROR_MESSAGE } from "@/constants/message.constant.ts";
import {FormatCommonOptionsResponse} from "@/services/api/common.api.ts";

const props = defineProps({
  dataSource: Array as () => TutorClassDetailResponse[],
  loading: Boolean,
  paginationParams: Object,
  totalPages: Number,
  studentOption: Array as () => FormatCommonOptionsResponse[],
  teacherOption: Array as () => FormatCommonOptionsResponse[],
  canUpdate: Boolean
});

const shiftOptions = Array.from({ length: 10 }, (_, index) => ({
  value: `Ca ${index + 1}`,
  label: `Ca ${index + 1}`,
}));

const columnsTutorClassDetail = computed(() => [
  {
    title: "STT",
    dataIndex: "orderNumber",
    key: "index",
    ellipsis: true,
    width: "40px",
  },
  {
    title: "Mã lớp",
    dataIndex: "tutorClassCode",
    key: "tutorClassCode",
    ellipsis: true,
    width: "100px",
  },
  {
    title: "Sinh viên tutor",
    dataIndex: "studentTutor",
    key: "studentTutor",
    ellipsis: true,
    width: "100px",
  },
  {
    title: "Giảng viên tutor",
    dataIndex: "teacherTutor",
    key: "teacherTutor",
    ellipsis: true,
    width: "100px",
  },
  {
    title: "Ca học",
    dataIndex: "shift",
    key: "shift",
    ellipsis: true,
    width: "80px",
  },
  {
    title: "Phòng",
    dataIndex: "room",
    key: "room",
    ellipsis: true,
    width: "100px",
  },
  ...(props.canUpdate ? [] : [
    {
      title: "Hành động",
      key: "action",
      align: "center",
      width: "100px",
    },
  ]),
]);

const { mutate: updateTutorClassDetail } = useUpdateTutorClassDetail();
const { mutate: deleteTutorClassDetail } = useDeleteTutorClassDetail();
const { mutate: addTutorClassDetail } = useAddTutorClassDetail();

const isFirstClassOfSubject = (record) => {
  const subjectId = record.subjectId; // Thay đổi theo thuộc tính thực tế của bạn
  const firstClassIndex = props?.dataSource?.findIndex(item => item.subjectId === subjectId);
  return firstClassIndex === props?.dataSource?.indexOf(record);
};

const handleUpdateTeacher = (tutorClassDetailId: string, staffId: string) => {
  try {
    const params = {
      staffId: staffId
    };
    updateTutorClassDetail({
      id: tutorClassDetailId,
      params: params,
    }, {
      onSuccess: () => {
        toast.success("Cập nhật giảng viên tutor thành công!",{
          autoClose: 1000,
        });
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
}

const handleDeleteTutorClassDetail = (tutorClassDetailId: string) => {
  Modal.confirm({
    content: 'Bạn chắc chắn muốn xóa lớp tutor này chứ!',
    icon: createVNode(ExclamationCircleOutlined),
    centered: true,
    async onOk() {
      try {
        deleteTutorClassDetail(
            tutorClassDetailId,
            {
              onSuccess: () => {
                toast.success("Xóa lớp tutor thành công!");
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
}

const handleAddTutorClassDetail = (tutorClassDetailId: string) => {
  Modal.confirm({
    content: 'Bạn chắc chắn muốn thêm lớp tutor này chứ!',
    icon: createVNode(ExclamationCircleOutlined),
    centered: true,
    async onOk() {
      try {
        addTutorClassDetail(
            tutorClassDetailId,
            {
              onSuccess: () => {
                toast.success("Thêm lớp tutor thành công!");
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
}

const updateTeacher = (tutorClassDetailId: string, teacherId: string) => {
  handleUpdateTeacher(tutorClassDetailId, teacherId);
};
</script>
