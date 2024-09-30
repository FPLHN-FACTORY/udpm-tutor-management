<template>
  <div class="mt-10 rounded-md flex h-full flex-col">
    <h2 class="text-center text-xl font-semibold mb-7">Danh sách lớp tutor của môn học
      <span class="text-xl font-semibold text-green-600">({{ tutorClassDetailData.subjectName }})</span>
    </h2>
    <div class="flex h-0 flex-1 flex-col">
      <tutor-table
          wrapperClassName="min-h-[410px]"
          :columns="columnsTutorClassDetail"
          :data-source="dataSource"
          :loading="loading"
          :pagination-params="paginationParams || {}"
          :total-pages="totalPages || 0"
          @update:pagination-params="$emit('update:paginationParams', $event)"
      >
        <template #bodyCell="{ column, record }">
          <div v-if="column.key === 'action'" class="space-x-2 flex items-center justify-center">
          </div>
          <div v-if="column.key === 'status'" class="text-center">
            <a-tag v-if="record.status === 0" color="success">Đã duyệt</a-tag>
            <a-tag v-else-if="record.status === 1" color="error">Chưa duyệt</a-tag>
          </div>
          <div v-else-if="column.key === 'startTime'" >
            {{ record.startTime? getDateFormat(record.startTime, false) : '' }}
          </div>
          <div v-else-if="column.key === 'endTime'" >
            {{ record.endTime? getDateFormat(record.endTime, false) : '' }}
          </div>
          <div v-else-if="column.key === 'studentTutor'" >
            {{ record.studentTutor || 'Chưa phân công' }}
          </div>
          <div v-else-if="column.key === 'teacherTutor'" >
            {{ record.teacherTutor || 'Chưa phân công' }}
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
import {getDateFormat} from "@/utils/common.helper.ts";

defineProps({
  dataSource: Array<TutorClassDetailResponse>,
  loading: Boolean,
  paginationParams: Object,
  totalPages: Number,
  tutorClassDetailData: Object as () => any | null,
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
    width: "150px",
  },
  {
    title: "Ca học",
    dataIndex: "shift",
    key: "shift",
    ellipsis: true,
    width: "100px",
  },
  {
    title: "Ngày bắt đầu",
    dataIndex: "startTime",
    key: "startTime",
    ellipsis: true,
    width: "120px",
  },
  {
    title: "Ngày kết thúc",
    dataIndex: "endTime",
    key: "endTime",
    ellipsis: true,
    width: "120px",
  },
  {
    title: "Hành động",
    key: "action",
    align: "center",
    width: "150px",
  },
];
</script>
