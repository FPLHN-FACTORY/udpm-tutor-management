<template>
  <div>
    <div class="flex justify-end mb-5" v-if="!canUpdate">
      <a-button @click="handleUpdateStudentPlan" type="primary">Lưu Tất Cả</a-button>
    </div>
    <tutor-table
        :columns="columns"
        :data-source="tempDataSource"
        :row-selection="rowSelection"
        :loading="loading"
        :pagination-params="paginationParams || {}"
        :total-pages="totalPages || 0"
        :scroll="{ x: 'max-content' }"
        @update:pagination-params="$emit('update:paginationParams', $event)"
    >
      <template #headerCell="{ column }">
        <template v-if="column.key === 'studentTutor'">
          <div class="flex items-center justify-between">
            <span>
              Sinh viên tutor
            </span>
            <a-button
                class="flex items-center justify-center border-none bg-transparent text-current"
                @click="$emit('handleOpenModalAdd')"
                :icon="h(PlusCircleOutlined)"
                :disabled="canUpdate"
            />
          </div>
        </template>
      </template>
      <template #bodyCell="{ column, record }">
        <div v-if="column.key === 'studentTutor'">
          <a-select
              v-model:value="record.studentTutor"
              show-search
              placeholder="Chọn sinh viên"
              style="width: 100%"
              :options="studentOption"
              :filter-option="(input, option) => option.label.toLowerCase().includes(input.toLowerCase())"
              :disabled="canUpdate"
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
              style="width: 100%"
              :bordered="false"
              disabled
          />
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
              :disabled="canUpdate"
          />
        </div>
        <div v-else-if="column.key === 'link'">
          <a-input
              v-model:value="record.link"
              placeholder="Nhập link"
              :disabled="canUpdate"
          />
        </div>
      </template>
    </tutor-table>
  </div>
</template>

<script lang="ts" setup>
import {ref, defineProps, watch, h, defineEmits} from 'vue';
import { ColumnType } from "ant-design-vue/es/table";
import { TutorClassDetailResponse, UpdateTutorClassDetailParams } from "@/services/api/planner/tutor-class.api.ts";
import { useUpdateStudentPlan } from "@/services/service/planner/tutor-class.action.ts";
import { toast } from "vue3-toastify";
import { ERROR_MESSAGE } from "@/constants/message.constant.ts";
import { FormatCommonOptionsResponse } from "@/services/api/common.api.ts";
import { confirmModal } from "@/utils/common.helper.ts";
import {PlusCircleOutlined} from "@ant-design/icons-vue";
import TutorTable from "@/components/ui/TutorTable/TutorTable.vue";

const props = defineProps({
  dataSource: {
    type: Array as () => TutorClassDetailResponse[],
    default: () => []
  },
  loading: Boolean,
  paginationParams: Object,
  totalPages: Number,
  teacherOption: Array as () => FormatCommonOptionsResponse[],
  studentOption: Array as () => FormatCommonOptionsResponse[],
  canUpdate: Boolean
});

const emit = defineEmits([
  "handleOpenModalAdd",
  "update:paginationParams",
]);

const shiftOptions = Array.from({ length: 6 }, (_, index) => ({
  value: `Ca ${index + 1}`,
  label: `Ca ${index + 1}`,
}));

// Tạo biến tạm để lưu trữ dữ liệu thay đổi
const tempDataSource = ref([...props?.dataSource?.map((record, index) => ({
  ...record,
  key: index.toString()
}))]);

watch(() => props.dataSource, (newData) => {
  tempDataSource.value = newData.map((record, index) => ({
    ...record,
    key: index.toString()
  }));
});

const columns: ColumnType[] = [
  {
    title: "Mã lớp",
    dataIndex: "tutorClassCode",
    key: "tutorClassCode", ellipsis: true,
    width: "100px"
  },
  {
    title: "Sinh viên tutor",
    dataIndex: "studentTutor",
    key: "studentTutor",
    ellipsis: true,
    width: "100px"
  },
  {
    title: "Giảng viên tutor",
    dataIndex: "teacherTutor",
    key: "teacherTutor",
    ellipsis: true,
    width: "100px"
  },
  {
    title: "Ca học",
    dataIndex: "shift",
    key: "shift",
    ellipsis: true,
    width: "70px"
  },
  {
    title: "Phòng",
    dataIndex: "room",
    key: "room",
    ellipsis: true,
    width: "100px"
  },
  {
    title: "Link",
    dataIndex: "link",
    key: "link",
    ellipsis: true,
    width: "100px"
  }
];

interface DataItem {
  key: string;
  id: string;
  tutorClassCode: number;
  studentTutor: string;
  teacherTutor: string;
  shift: string;
  room: string;
  startTime: number;
  endTime: number;
}

const selectedRecords = ref<UpdateTutorClassDetailParams[]>([]);
const selectedIds = ref<string[]>([]);
const { mutate: updateStudentPlan } = useUpdateStudentPlan();

const rowSelection = ref({
  onChange: (selectedRowKeys: (string | number)[], selectedRows: DataItem[]) => {
    console.log(selectedRowKeys);
    selectedIds.value = selectedRows.map(record => record.id);
  },
});

// Hàm xử lý cập nhật
const handleUpdateStudentPlan = async () => {
  const message = 'Bạn chắc chắn muốn lưu các lớp tutor này chứ!'; // Thông điệp xác nhận

  confirmModal(message, () => {
    try {
      selectedRecords.value = tempDataSource.value
          .filter(record => selectedIds.value.includes(record.id))
          .map(record => ({
            id: record.id,
            studentId: record.studentTutor,
            room: record.room,
            link: record.link
          }));

      if (selectedRecords.value.length === 0) {
        toast.warning("Vui lòng chọn ít nhất một lớp tutor cần lưu");
        return;
      }

      updateStudentPlan(selectedRecords.value, {
        onSuccess: () => {
          toast.success("Cập nhật lớp tutor thành công");
        },
        onError: (error: any) => {
          toast.error(
              error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
          );
        },
      });
    } catch (error) {
      console.error("Error in adding/updating plan:", error);
    }
  });
};
</script>
