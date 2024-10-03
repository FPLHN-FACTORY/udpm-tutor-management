<template>
  <div class="mt-10 rounded-md flex h-full flex-col">
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
          <div v-if="column.key === 'action'" class="space-x-2 flex items-center justify-center">
            <a-tooltip title="Xóa lớp tutor" color="#FFC26E">
              <a-button
                  class="flex items-center justify-center"
                  type="primary"
                  size="large"
                  @click="handleDeleteTutorClassDetail(record.id)"
                  :icon="h(MinusCircleOutlined)"
                  :disabled="canUpdate"
              />
            </a-tooltip>
            <a-tooltip title="Thêm lớp tutor" color="#FFC26E">
              <a-button
                  class="flex items-center justify-center"
                  type="primary"
                  size="large"
                  @click="handleAddTutorClassDetail(record.id)"
                  :icon="h(PlusCircleOutlined)"
                  :disabled="canUpdate"
              />
            </a-tooltip>
          </div>
          <div v-else-if="column.key === 'time'">
            <a-range-picker
                :value="[record.startTime ? getDateFormat(record.startTime, false) : '',
                 record.endTime ? getDateFormat(record.endTime, false) : '']"
                disabled
            />
          </div>
          <div v-else-if="column.key === 'studentTutor'">
            <a-select
                v-model:value="record.studentTutor"
                placeholder="Chọn sinh viên"
                style="width: 100%"
                show-search
                :filter-option="(input, option) => option.label.toLowerCase().includes(input.toLowerCase())"
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
import { ColumnType } from "ant-design-vue/es/table";
import { TutorClassDetailResponse } from "@/services/api/headsubject/tutor-class.api.ts";
import { getDateFormat } from "@/utils/common.helper.ts";
import { createVNode, h } from "vue";
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

defineProps({
  dataSource: Array<TutorClassDetailResponse>,
  loading: Boolean,
  paginationParams: Object,
  totalPages: Number,
  students: Array,
  teacherOption: Array,
  canUpdate: Boolean
});

const columnsTutorClassDetail: ColumnType[] = [
  {
    title: "STT",
    dataIndex: "orderNumber",
    key: "index",
    ellipsis: true,
    width: "50px",
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
    width: "150px",
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
    title: "Thời gian",
    dataIndex: "time",
    key: "time",
    ellipsis: true,
    width: "250px",
  },
  {
    title: "Hành động",
    key: "action",
    align: "center",
    width: "150px",
  },
];

const { mutate: updateTutorClassDetail } = useUpdateTutorClassDetail();
const { mutate: deleteTutorClassDetail } = useDeleteTutorClassDetail();
const { mutate: addTutorClassDetail } = useAddTutorClassDetail();

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
        toast.success("Cập nhật giảng viên tutor thành công!");
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
