<template>
  <div class=" mt-5 rounded-md flex h-full flex-col">
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
          <div v-if="column.key === 'studentTutor'">
            <a-select
              v-model:value="record.studentTutor"
              placeholder="Chọn sinh viên"
              style="width: 100%"
              :options="studentOption"
              show-search
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
              :bordered="false"
              disabled
            />
          </div>
        </template>
      </tutor-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import TutorTable from "@/components/ui/TutorTable/TutorTable.vue";
import { ColumnType } from "ant-design-vue/es/table";
import { TutorClassDetailResponse } from "@/services/api/headdepartment/tutor-class.api.ts";
import {defineProps} from "vue";
import {FormatCommonOptionsResponse} from "@/services/api/common.api.ts";

defineProps({
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
    title: "Phòng",
    dataIndex: "room",
    key: "room",
    ellipsis: true,
    width: "80px",
  },
];
</script>
