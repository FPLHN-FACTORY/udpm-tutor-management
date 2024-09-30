<template>
  <div class="mt-10 rounded-md flex h-full flex-col">
    <h2 class="text-center text-xl font-semibold mb-7">Danh sách môn học</h2>
      <div class="flex h-0 flex-1 flex-col">
        <tutor-table
        wrapperClassName="min-h-[410px]"
        :columns="columnsSubject"
        :data-source="dataSource"
        :loading="loading"
        :pagination-params="paginationParams || {}"
        :total-pages="totalPages || 0"
        @update:pagination-params="$emit('update:paginationParams', $event)"
      >
        <template #bodyCell="{ column, record }">
          <div v-if="column.key === 'action'" class="space-x-2 flex items-center justify-center">
            <a-tooltip title="Chi tiết lớp môn" color="#FFC26E">
              <a-button
                  class="flex items-center justify-center"
                  type="primary"
                  size="large"
                  @click="goToDetail(record.id)"
                  :icon="h(EyeOutlined)"
              />
            </a-tooltip>
          </div>
        </template>
       </tutor-table>
      </div>
  </div>
</template>

<script setup lang="ts">
import TutorTable from "@/components/ui/TutorTable/TutorTable.vue";
import {EyeOutlined} from "@ant-design/icons-vue";
import { ColumnType } from "ant-design-vue/es/table";
import { h } from "vue";
import {router} from "@/routes/router.ts";
import {TutorClassResponse} from "@/services/api/planner/tutor-class.api.ts";

defineProps({
  dataSource: Array<TutorClassResponse>,
  loading: Boolean,
  paginationParams: Object,
  totalPages: Number,
});

defineEmits([
  "update:paginationParams",
  "handleOpenModalUpdate",
  "handleOpenModalAdd",
]);

const goToDetail = (tutorClassId: string) => {
  router.push({ name: 'detailTutorClassDetail', params: { tutorClassId } });
}


const columnsSubject: ColumnType[] = [
  {
    title: "STT",
    dataIndex: "orderNumber",
    key: "index",
    ellipsis: true,
  },
  {
    title: "Tên môn học",
    dataIndex: "subjectName",
    key: "subjectName",
    ellipsis: true,
  },
  {
    title: "Số lớp",
    dataIndex: "numberClasses",
    key: "numberClasses",
    ellipsis: true,
  },
  {
    title: "Trưởng môn",
    dataIndex: "headSubject",
    key: "headSubject",
    ellipsis: true,
    width: "200px",
    align: "center",
  },
  {
    title: "Hành động",
    key: "action",
    align: "center",
    width: "150px",
  },
];
</script>
