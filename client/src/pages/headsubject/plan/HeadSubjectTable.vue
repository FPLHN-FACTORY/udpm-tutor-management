<template>
    <div class="shadow-xl p-3 m-3 rounded-md flex h-full flex-col overflow-auto">
      <div class="flex justify-between items-center min-h-36">
        <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
          <v-icon name="bi-list-ul" scale="2" />
          <span class="ml-2 text-2xl">Danh sách kế hoạch</span>
        </h2>
      
      </div>
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
            <a-tooltip title="Chi tiết kế hoạch" color="#FFC26E">
              <a-button
                  class="flex items-center justify-center"
                  type="primary"
                  size="large"
                  @click="goToDetail(record.id)"
                  :icon="h(EyeOutlined)"
              />
            </a-tooltip>
            </div>
            <div v-else-if="column.key === 'departmentName'" class="text-center">
              <p>{{ record.departmentName + " - " + record.facilityName  }}</p>
            </div>
            <div v-else-if="column.key === 'status'" class="text-center">
              <a-tag :color="getTagColor(record.status)">{{ getTagStatus(record.status) }}</a-tag>
            </div>
            <div v-else-if="column.key === 'blockName'" >
              <p>{{ formatBlockName(record.blockName) }}</p>
            </div>
          </template>
         </tutor-table>
        </div>
    </div>
  </template>
  
<script setup lang="ts">
import TutorTable from "@/components/ui/TutorTable/TutorTable.vue";
import { ColumnType } from "ant-design-vue/es/table";
import {PlanResponse} from "@/services/api/headsubject/plan.api.ts";
import {formatBlockName, getTagColor, getTagStatus} from "@/utils/common.helper.ts";
import { h } from "vue";
import { EyeOutlined } from "@ant-design/icons-vue";
import { router } from "@/routes/router.ts";

  const goToDetail = (planId: string) => {
    router.push({ name: 'detailSubjectPlan', params: { planId } });
  }

  defineProps({
    dataSource: Array<PlanResponse>,
    loading: Boolean,
    paginationParams: Object,
    totalPages: Number,
  });

  defineEmits([
    "update:paginationParams",
    "handleOpenModalAdd",
  ]);

  const columnsSubject: ColumnType[] = [
    {
      title: "STT",
      dataIndex: "orderNumber",
      key: "index",
      ellipsis: true,
    },
    {
      title: "Tên kế hoạch",
      dataIndex: "planName",
      key: "planName",
      ellipsis: true,
    },
    {
      title: "Block",
      dataIndex: "blockName",
      key: "blockName",
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
      title: "Số môn tutor",
      dataIndex: "numberSubjects",
      key: "numberSubjects",
      ellipsis: true,
      width: "200px",
      align: "center",
    },
    {
      title: "Trạng thái",
      dataIndex: "status",
      key: "status",
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
  