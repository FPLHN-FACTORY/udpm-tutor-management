<template>
  <div class="shadow-xl p-3 m-3 rounded-md flex h-full flex-col overflow-auto">
    <div class="flex justify-between items-center min-h-36">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="bi-list-ul" scale="2" />
        <span class="ml-2 text-2xl">Danh sách sinh viên</span>
      </h2>
    </div>
    <div class="flex h-0 flex-1 flex-col">
      <tutor-table
        wrapperClassName="min-h-[410px]"
        :columns="columnsTutorClass"
        :data-source="dataSource"
        :loading="props.loading"
        :is-pagination="false"
      >
        <template #bodyCell="{ column, record }">
          <div v-if="column.key === 'checked'" class="space-x-2 flex items-center justify-center">
            <a-tooltip>
              <a-switch   
                style="font-size: 42px !important;"
                v-model:checked="record.checked"
                class="custom-switch"
                :checked-children="record.checked ? 'Có mặt' : 'Vắng mặt'"
                :un-checked-children="!record.checked ? 'Vắng mặt' : 'Có mặt'"
                :style="record.checked ? { backgroundColor: '#4caf50' } : { backgroundColor: '#f44336' }"
                componentSize="large"
              />
            </a-tooltip>
          </div>
          <div v-if="column.key === 'note'" class="space-x-2 flex items-center justify-center">
            <a-tooltip title="Ghi chú" color="#FFC26E">
              <a-input
                type="text"
                v-model="record.note"
                class="note-input"
                placeholder="Nhập ghi chú tại đây"
              />
            </a-tooltip>
          </div>
          <div v-if="column.key === 'statusAttendance'" class="space-x-2 flex items-center justify-center">
            <span 
              :style="{ color: record.statusAttendance === 'Có mặt' ? 'green' : (record.statusAttendance === 'Vắng mặt' ? 'red' : 'orange') }"
            >
              {{ record.statusAttendance }}
            </span>
          </div>
        </template>
      </tutor-table>
    </div>
    <div class="flex h-0 flex-1 flex-col justify-end">
      <div class="flex justify-end">
        <a-button
          type="primary"
          @click="handleSaveAttendance"
          size="large"
          class="m-4"
        >
          Lưu điểm danh
        </a-button>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ColumnType } from 'ant-design-vue/es/table';
import { computed, ref, watchEffect } from 'vue';
import TutorTable from '@/components/ui/TutorTable/TutorTable.vue';
import { CreateAttendanceRequest, StudentAttendanceResponse } from '@/services/api/teacher/student-attendance.api';
import { useAuthStore } from '@/stores/auth';
import { confirmModal, getDateFormat } from '@/utils/common.helper';
import { useCreateAttendance } from '@/services/service/teacher/student-attendance.action';
import { toast } from 'vue3-toastify';
import { useRoute } from 'vue-router';

const route = useRoute();
const lectureId = computed(() => {
  const id = route.params.lectureId;
  return Array.isArray(id) ? id[0] : id || null;
});

const { mutate: createAttendance } = useCreateAttendance();
const { user } = useAuthStore();
const dataSource = ref<StudentAttendanceResponse[]>([]);
const selectedRecords = ref<CreateAttendanceRequest[]>([]);
const props = defineProps({
  dataSource: {
    type: Array as () => StudentAttendanceResponse[],
    default: () => []
  },
  loading: Boolean,
  lectureId: String
});

// Hàm để chuyển đổi dữ liệu
const transformDataSource = (newData: StudentAttendanceResponse[]) => {
  return newData.map((record, index) => ({
    key: `${lectureId.value}-${index}`,
    studentCode: record.studentCode,
    studentName: record.studentName,
    attendanceTime: record.attendanceTime ?? "Chưa điểm danh",
    note: record.note ?? '',
    checked: record.isAttendance ?? true,
    attendanceBy: user?.fullName,
    attendanceDate: record.attendanceTime ? getDateFormat(record.attendanceTime, true) : getDateFormat(Date.now(), false),
    room: record.room,
    orderNumber: record.orderNumber,
    studentId: record.studentId,
    statusAttendance: record.isAttendance === null 
      ? 'Chưa điểm danh'
      : record.isAttendance 
      ? 'Có mặt' 
      : 'Vắng mặt'
  }));
};

watchEffect(() => {
  if (Array.isArray(props.dataSource)) {
    dataSource.value = transformDataSource(props.dataSource);
  }
});

const handleSaveAttendance = async () => {
  const message = 'Bạn chắc chắn muốn lưu điểm danh không!';
  confirmModal(message, async () => {
    try {
      if (!props.lectureId || !dataSource.value.length) {
        toast.error("Dữ liệu điểm danh không hợp lệ hoặc thiếu thông tin buổi học.");
        return;
      }

      selectedRecords.value = dataSource.value.map(record => ({
        studentId: record.studentId, 
        isPresent: record.checked,    
        note: record.note,  
        lectureId: props.lectureId || ''
      }));

      await createAttendance(selectedRecords.value, {
        onSuccess: () => {
          toast.success("Cập nhật điểm danh thành công.");
        },
        onError: (error: any) => {
          toast.error(
            error?.response?.data?.message || "Đã có lỗi xảy ra khi cập nhật điểm danh."
          );
        },
      });

    } catch (error) {
      console.error("Error saving attendance:", error);
      toast.error("Đã xảy ra lỗi trong quá trình lưu điểm danh.");
    }
  });
};

const columnsTutorClass: ColumnType[] = [
  {
    title: "STT",
    dataIndex: "orderNumber",
    key: "index",
    ellipsis: true,
    width: "50px"
  },
  {
    title: "Tên lớp",
    dataIndex: "room",
    key: "room",
    ellipsis: true,
    width: "140px"
  },
  {
    title: "Mã sinh viên",
    dataIndex: "studentCode",
    key: "studentCode",
    ellipsis: true,
    width: "120px"
  },
  {
    title: "Tên sinh viên",
    dataIndex: "studentName",
    key: "studentName",
    ellipsis: true,
    width: "150px"
  },
  {
    title: "Trạng thái",
    dataIndex: "statusAttendance",
    key: "statusAttendance",
    ellipsis: true,
    width: "150px",
    align: "center"
  },
  {
    title: "Người điểm danh",
    dataIndex: "attendanceBy",
    key: "attendanceBy",
    ellipsis: true,
    width: "150px"
  },
  {
    title: "Ngày điểm danh",
    dataIndex: "attendanceDate",
    key: "attendanceDate",
    ellipsis: true,
    width: "150px"
  },
  {
    title: "Điểm danh",
    key: "checked",
    align: "center",
    width: "140px" 
  },
  {
    title: "Ghi chú",
    key: "note",
    align: "center",
    width: "200px"
  }
];
</script>

<style scoped>
.note-input {
  height: 30px;
}
</style>
