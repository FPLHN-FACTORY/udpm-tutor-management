<template>
  <div class="shadow-xl p-3 m-3 rounded-md">
    <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
      <v-icon name="bi-list-ul" scale="2" />
      <span class="ml-2 text-xl">Danh sách môn học</span>
    </h2>
    <tutor-table
      class="p-4 min-h-[310px]"
      :columns="columnsSubject"
      :data-source="dataSource"
      :loading="loading"
      :pagination-params="paginationParams"
      :total-pages="totalPages"
      @update:pagination-params="$emit('update:paginationParams', $event)"
    >
      <template #bodyCell="{ column, record }">
        <div v-if="column.key === 'action'" class="space-x-2 text-center">
          <a-button
            type="primary"
            @click="editRecord(record)"
            :icon="h(EditOutlined)"
          />
          <a-button
            type="primary"
            @click="deleteRecord(record)"
            :icon="h(RedoOutlined)"
          />
        </div>
        <div v-else-if="column.key === 'subjectType'" class="text-center">
          <a-tag color="warning">{{ record.subjectType }}</a-tag>
        </div>
        <div v-else-if="column.key === 'createdDate'" class="text-center">
          {{ getDateFormat(record.createdDate, false) }}
        </div>
      </template>
    </tutor-table>
  </div>
</template>

<script setup lang="ts">
import TutorTable from "@/components/ui/TutorTable/TutorTable.vue";
import { SubjectResponse } from "@/services/api/subject.api";
import { getDateFormat } from "@/utils/common.helper";
import { EditOutlined, RedoOutlined } from "@ant-design/icons-vue";
import { ColumnType } from "ant-design-vue/es/table";
import { defineEmits, defineProps, h } from "vue";

defineProps({
  dataSource: Array<SubjectResponse>,
  loading: Boolean,
  paginationParams: Object,
  totalPages: Number,
});

defineEmits(["update:paginationParams"]);

const columnsSubject: ColumnType[] = [
  {
    title: "STT",
    dataIndex: "orderNumber",
    key: "index",
    ellipsis: true,
  },
  {
    title: "Mã môn học",
    dataIndex: "subjectCode",
    key: "subjectCode",
    ellipsis: true,
  },
  {
    title: "Tên môn học",
    dataIndex: "subjectName",
    key: "subjectName",
    ellipsis: true,
  },
  {
    title: "Bộ môn",
    dataIndex: "departmentName",
    key: "departmentName",
    ellipsis: true,
    width: "120px",
  },
  {
    title: "Loại môn học",
    dataIndex: "subjectType",
    key: "subjectType",
    ellipsis: true,
    width: "200px",
    align: "center",
  },
  {
    title: "Ngày tạo",
    dataIndex: "createdDate",
    key: "createdDate",
    ellipsis: true,
    width: "100px",
  },
  {
    title: "Hành động",
    key: "action",
    align: "center",
    width: "150px",
  },
];

const editRecord = (record) => {
  console.log("Edit", record);
};

const deleteRecord = (record) => {
  console.log("Delete", record);
};
</script>
