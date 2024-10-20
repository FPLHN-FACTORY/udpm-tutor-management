<template>
  <div class="shadow-xl p-3 m-3 rounded-md flex h-full flex-col overflow-auto">
    <div class="flex justify-between items-center min-h-36">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="bi-list-ul" scale="2" />
        <span class="ml-2 text-2xl">Danh sách buổi học</span>
      </h2>
    </div>
    <div class="flex justify-end mb-5">
      <a-button @click="handleTutorClassDetail" type="primary">Lưu Tất Cả</a-button>
    </div>
    <div class="flex h-0 flex-1 flex-col">
      <tutor-table
          wrapperClassName="min-h-[410px]"
          :columns="columnsTutorClass"
          :data-source="tempDataSource"
          :row-selection="rowSelection"
          :loading="props.loading"
          :is-pagination="false"
           pagination-params=""
      >
        <template #bodyCell="{ column, record }">
          <div v-if="column.key === 'action'" class="space-x-2 flex items-center justify-center">
            <a-tooltip title="Điểm danh" color="#FFC26E">
              <a-button
                  type="primary"
                  size="large"
                  class="flex items-center justify-center"
                  :icon="h(EyeOutlined)"
                  @click="goToDetail(record.id)"
              />
            </a-tooltip>
            <a-tooltip title="Thông tin tài liệu buổi học" color="#FFC26E">
              <a-button
                  type="primary"
                  size="large"
                  class="flex items-center justify-center"
                  @click="$emit('handleOpenModalAdd', record)"
                  :icon="h(PlusSquareOutlined)"
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
            />
          </div>
          <div v-else-if="column.key === 'startTime'">
            <a-date-picker
                :value="record.startTime ? dayjs(record.startTime) : null"
                placeholder="Chọn ngày"
                :bordered="false"
                :format="'DD/MM/YYYY'"
                @change="(date) => record.startTime = date ? date.valueOf() : null"
            />
          </div>
          <div v-else-if="column.key === 'format'" class="text-center">
            <a-select
                v-model:value="record.format"
                show-search
                :options="formatOptions"
                :filter-option="(input, option) => option.label.toLowerCase().includes(input.toLowerCase())"
                style="width: 100%"
                :bordered="false"
            />
          </div>
          <div v-else-if="column.key === 'evidenceLink'" >
            <div v-if="record.evidenceLink || record.recordLink" class="text-center">
              <a
                  v-if="record.evidenceLink"
                  :href="record.evidenceLink"
                  target="_blank"
                  rel="noopener noreferrer"
              >
                Tại đây
              </a>
              <a
                  v-else-if="record.recordLink"
                  :href="record.recordLink"
                  target="_blank"
                  rel="noopener noreferrer"
              >
                Tại đây
              </a>
            </div>

          </div>
        </template>
      </tutor-table>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {EyeOutlined, PlusSquareOutlined} from '@ant-design/icons-vue';
import { ColumnType } from 'ant-design-vue/es/table';
import {h, ref, watch} from 'vue';
import TutorTable from '@/components/ui/TutorTable/TutorTable.vue';
import {confirmModal} from "@/utils/common.helper.ts";
import {
  LectureResponse, UpdateLectureParams,
} from "@/services/api/teacher/tutor-class.api.ts";
import {useUpdateLecture} from "@/services/service/teacher/tutor-class.action.ts";
import {toast} from "vue3-toastify";
import {ERROR_MESSAGE} from "@/constants/message.constant.ts";
import dayjs from "dayjs";
import { useRouter } from 'vue-router';

const router = useRouter();
function goToDetail(lectureId: string) {
  router.push({ name: 'tcStudentAttendance', params: { lectureId } });
}

defineEmits([
  "handleOpenModalUpdate",
  "handleOpenModalAdd",
]);


const props = defineProps({
  dataSource: {
    type: Array as () => LectureResponse[],
    default: () => []  // Đảm bảo rằng dataSource luôn là một mảng, tránh trường hợp undefined
  },
  loading: Boolean
})

interface DataItem {
  id: string,
  lectureContent: string,
  shift: string,
  name: string,
  recordLink: string,
  lectureType: string,
  exerciseLink: string,
  evidenceLink: string,
  startTime: number,
  lectureStatus: string,
  format: string,
  status: string,
}

const selectedRecords = ref<UpdateLectureParams[]>([]);
const selectedIds = ref<string[]>([]);
const { mutate: updateLecture } = useUpdateLecture();
const tempDataSource = ref([...props?.dataSource?.map((record, index) => ({
  ...record,
  key: index.toString()
}))]);

const shiftOptions = Array.from({ length: 10 }, (_, index) => ({
  value: `Ca ${index + 1}`,
  label: `Ca ${index + 1}`,
}));

const formatOptions = [
  { value: 'ONLINE', label: 'ONLINE' },
  { value: 'OFFLINE', label: 'OFFLINE' },
];

const handleTutorClassDetail = async () => {
  const message = 'Bạn chắc chắn muốn lưu các lớp tutor này chứ!'; // Thông điệp xác nhận

  confirmModal(message, () => {
    try {
      selectedRecords.value = tempDataSource.value
          .filter(record => selectedIds.value.includes(record.id))
          .map(record => ({
            id: record.id,
            shift: record.shift,
            startTime: record.startTime,
            format: record.format
          }));

      if (selectedRecords.value.length === 0) {
        toast.warning("Vui lòng chọn ít nhất một buổi học cần lưu!");
        return;
      }

      updateLecture(selectedRecords.value, {
        onSuccess: () => {
          toast.success("Cập nhật buổi học thành công");
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

watch(() => props.dataSource, (newData) => {
  tempDataSource.value = newData.map((record, index) => ({
    ...record,
    key: index.toString()
  }));
});

const rowSelection = ref({
  onChange: (selectedRowKeys: (string | number)[], selectedRows: DataItem[]) => {
    console.log(selectedRowKeys);
    selectedIds.value = selectedRows.map(record => record.id);
  },
});

const columnsTutorClass: ColumnType[] = [
  {
    title: "STT",
    dataIndex: "orderNumber",
    key: "index",
    ellipsis: true,
    width: "50px"
  },
  {
    title: "Buổi học",
    dataIndex: "name",
    key: "name",
    ellipsis: true,
    width: "140px",
  },
  {
    title: "Ca học",
    dataIndex: "shift",
    key: "shift",
    ellipsis: true,
    width: "100px",
  },
  {
    title: "Ngày",
    dataIndex: "startTime",
    key: "startTime",
    ellipsis: true,
    width: "150px",
  },
  {
    title: "Sĩ số",
    dataIndex: "studentCount",
    key: "studentCount",
    ellipsis: true,
    width: "100px"
  },
  {
    title: "Tài nguyên",
    dataIndex: "evidenceLink",
    key: "evidenceLink",
    ellipsis: true,
    width: "150px",
    align: "center",
  },
  {
    title: "Hình thức",
    dataIndex: "format",
    key: "format",
    ellipsis: true,
    width: "100px"
  },
  {
    title: "Trạng thái",
    dataIndex: "status",
    key: "status",
    ellipsis: true,
    width: "100px"
  },
  {
    title: "Hành động",
    key: "action",
    align: "center",
    width: "200px",
  },
];
</script>