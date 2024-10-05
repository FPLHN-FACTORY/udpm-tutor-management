<template>
  <div class="shadow-xl p-3 m-3 rounded-md flex h-full flex-col overflow-auto">
    <div class="flex justify-between items-center min-h-36">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="bi-list-ul" scale="2" />
        <span class="ml-2 text-2xl">Danh sách lớp tutor</span>
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
          :pagination-params="props.paginationParams || {}"
          :total-pages="props.totalPages || 1"
          @update:pagination-params="$emit('update:paginationParams', $event)"
          :loading="props.loading"
      >
        <template #bodyCell="{ column, record }">
          <div v-if="column.key === 'action'" class="space-x-2 flex items-center justify-center">
            <a-button type="primary" size="large" class="flex items-center justify-center"
                      :icon="h(EditOutlined)" />
          </div>
          <div v-else-if="column.key === 'shift'">
            <a-select
                v-model:value="record.shift"
                show-search
                placeholder="Chọn ca"
                :options="shiftOptions"
                :filter-option="(input, option) => option.label.toLowerCase().includes(input.toLowerCase())"
                style="width: 100%"
            />
          </div>
          <div v-else-if="column.key === 'time'">
            <a-range-picker
                :value="[record.startTime ? dayjs(record.startTime) : null, record.endTime ? dayjs(record.endTime) : null]"
                @change="(dates) => handleTimeChange(record, dates)"
                :placeholder="['Ngày bắt đầu', 'Ngày kết thúc']"
                :format="'DD/MM/YYYY'"
            />
          </div>
        </template>
      </tutor-table>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { EditOutlined } from '@ant-design/icons-vue';
import { ColumnType } from 'ant-design-vue/es/table';
import {h, ref, watch} from 'vue';
import TutorTable from '@/components/ui/TutorTable/TutorTable.vue';
import {confirmModal} from "@/utils/common.helper.ts";
import {TutorClassResponse, UpdateTutorClassDetailParams} from "@/services/api/teacher/tutor-class.api.ts";
import {useUpdateTutorClassDetail} from "@/services/service/teacher/tutor-class.action.ts";
import {toast} from "vue3-toastify";
import {ERROR_MESSAGE} from "@/constants/message.constant.ts";
import dayjs from "dayjs";

const props = defineProps({
  dataSource: {
    type: Array as () => TutorClassResponse[],
    default: () => []  // Đảm bảo rằng dataSource luôn là một mảng, tránh trường hợp undefined
  },
  totalPages: Number,
  paginationParams: Object,
  loading: Boolean
})

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

const emit = defineEmits(['update:paginationParams'])
const selectedRecords = ref<UpdateTutorClassDetailParams[]>([]);
const selectedIds = ref<string[]>([]);
const { mutate: updateTutorClassDetail } = useUpdateTutorClassDetail();
const dateFormat = 'DD/MM/YYYY';
const tempDataSource = ref([...props?.dataSource?.map((record, index) => ({
  ...record,
  key: index.toString()
}))]);

const shiftOptions = Array.from({ length: 6 }, (_, index) => ({
  value: `Ca ${index + 1}`,
  label: `Ca ${index + 1}`,
}));

const handleTimeChange = (record, dates) => {
  console.log('Dates:', dates); // Kiểm tra xem dates có nhận giá trị không

  if (dates && dates.length) {
    record.startTime = dates[0] ? dates[0].valueOf() : null;
    record.endTime = dates[1] ? dates[1].valueOf() : null;
  } else {
    record.startTime = null;
    record.endTime = null;
  }

  console.log('Updated Record:', record); // Kiểm tra giá trị đã cập nhật
};



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
            endTime: record.endTime,
          }));

      if (selectedRecords.value.length === 0) {
        toast.warning("Vui lòng chọn ít nhất một lớp tutor cần lưu");
        return;
      }

      updateTutorClassDetail(selectedRecords.value, {
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

watch(() => props.dataSource, (newData) => {
  tempDataSource.value = newData.map((record, index) => ({
    ...record,
    key: index.toString()
  }));
});

const rowSelection = ref({
  onChange: (selectedRowKeys: (string | number)[], selectedRows: DataItem[]) => {
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
    title: "Mã lớp",
    dataIndex: "classCode",
    key: "classCode",
    ellipsis: true,
  },
  {
    title: "Tên môn học",
    dataIndex: "subjectName",
    key: "subjectName",
    ellipsis: true,
  },
  {
    title: "Trợ giảng",
    dataIndex: "studentName",
    key: "studentName",
    ellipsis: true,
  },
  {
    title: "Sĩ số",
    dataIndex: "totalStudent",
    key: "totalStudent",
    ellipsis: true,
  },
  {
    title: "Ca học",
    dataIndex: "shift",
    key: "shift",
    ellipsis: true,
    width: "70px"
  },
  {
    title: "Thời gian",
    dataIndex: "time",
    key: "time",
    ellipsis: true,
    width: "250px"
  },
  {
    title: "Hành động",
    key: "action",
    align: "center",
    width: "200px",
  },
];
</script>