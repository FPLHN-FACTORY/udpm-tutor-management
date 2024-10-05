<template>
    <div class="shadow-xl p-3 m-3 rounded-md flex h-full flex-col overflow-auto">
      <div class="flex justify-between items-center min-h-36">
        <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
          <v-icon name="bi-list-ul" scale="2" />
          <span class="ml-2 text-2xl">Danh sách lịch sử đăng nhập</span>
        </h2>
      </div>
      <div class="flex h-0 flex-1 flex-col">
        <tutor-table
            wrapperClassName="min-h-[410px]"
            :columns="columnsUserActivityLog"
            :data-source="dataSource"
            :loading="loading"
            :pagination-params="paginationParams || {}"
            :total-pages="totalPages || 0"
            @update:pagination-params="$emit('update:paginationParams', $event)"
        >
          <template #bodyCell="{ column, record }">
            <div v-if="column.key === 'action'" class="space-x-2 text-center">
              <a-tooltip title="Chi tiết lịch sử" color="#FFC26E">
                <a-button
                  type="primary"
                  size="regular"
                  @click="$emit('handleOpenModalDetail', record)"
                  :icon="h(EyeOutlined)"
                />
              </a-tooltip>
            </div>
            <div v-else-if="column.key === 'status'" class="text-center">
                <a-tag v-if="record.status === '0'" color="success">Thành công</a-tag>
                <a-tag v-else-if="record.status === '1'" color="error">Thất bại</a-tag>
            </div>
            <div v-else-if="column.key === 'loginTime'" class="text-center">
                {{ getDateFormat(record.loginTime, true) }}
            </div>
            <div v-else-if="column.key === 'logoutTime'" class="text-center">
                {{ getDateFormat(record.logoutTime, true) }}
            </div>
          </template>
        </tutor-table>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import TutorTable from "@/components/ui/TutorTable/TutorTable.vue";
import { GetUserActivityLogResponse } from "@/services/api/operationlog/operationlog.api";
import { getDateFormat } from "@/utils/common.helper";
  import { EyeOutlined } from "@ant-design/icons-vue";
  import { ColumnType } from "ant-design-vue/es/table";
  import { h } from "vue";
  
  defineProps({
    dataSource: Array as () => GetUserActivityLogResponse[],
    loading: Boolean,
    paginationParams: Object as () => any,
    totalPages: Number,
  });
  
  const emit = defineEmits([
    "update:paginationParams",
    "handleOpenModalDetail"
  ]);
  

  const columnsUserActivityLog: ColumnType[] = [
    {
      title: "STT",
      dataIndex: "orderNumber",
      key: "index",
      ellipsis: true,
      width: "50px",
    },
    {
      title: "Tên người dùng",
      dataIndex: "name",
      key: "name",
      ellipsis: true,
      width: "150px",
    },
    {
      title: "Email",
      dataIndex: "email",
      key: "email",
      ellipsis: true,
    },
    {
      title: "Chức năng",
      dataIndex: "operation",
      key: "operation",
      ellipsis: true,
    },
    {
      title: "Thời gian đăng nhập",
      dataIndex: "loginTime",
      key: "loginTime",
      ellipsis: true,
    },
    {
      title: "Thời gian đăng xuất",
      dataIndex: "logoutTime",
      key: "logoutTime",
      ellipsis: true,
    },
    {
      title: "Trạng thái",
      dataIndex: "status",
      key: "status",
      width: "150px",
    },
    // {
    //     title: "Hành động",
    //     key: "action",
    //     align: "center",
    // },
  ];
  </script>
  