<template>
  <div class="shadow-xl p-3 m-3 rounded-md flex h-full flex-col overflow-auto">
    <div class="flex justify-between items-center min-h-36">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="bi-list-ul" scale="2" />
        <span class="ml-2 text-2xl">Danh sách bộ môn</span>
      </h2>
      <a-button
        type="primary"
        size="large"
        class="m-4 flex justify-between items-center"
        @click="handleSync"
    >
      <v-icon name="bi-arrow-repeat" scale="1.5" class="me-1" />
      Đồng bộ
    </a-button>
    </div>
    <div class="flex h-0 flex-1 flex-col">
      <tutor-table
          wrapperClassName="min-h-[410px]"
          :columns="columnsDepartment"
          :data-source="dataSource"
          :loading="loading"
          :pagination-params="paginationParams || {}"
          :total-pages="totalPages || 0"
          @update:pagination-params="$emit('update:paginationParams', $event)"
      >
        <template #bodyCell="{ column, record }">
          <div v-if="column.key === 'action'" class="space-x-2 text-center">
            <a-tooltip title="Chi tiết chuyên ngành thuộc bộ môn" color="#FFC26E">
              <a-button
                type="primary"
                size="regular"
                @click="$emit('handleOpenMajorListModal', record)"
                :icon="h(BookOutlined)"
              />
            </a-tooltip>
            <a-tooltip title="Chi tiết bộ môn" color="#FFC26E">
              <a-button
                type="primary"
                size="regular"
                @click="$emit('handleOpenDepartmentDetailModal', record)"
                :icon="h(EyeOutlined)"
              />
            </a-tooltip>
            <a-tooltip title="Chi tiết bộ môn theo cơ sở" color="#FFC26E">
              <a-button
                type="primary"
                size="regular"
                @click="$emit('handleOpenDepartmentsFacilityListModal', record)"
                :icon="h(GoldOutlined)"
              />
            </a-tooltip>
          </div>
          <div v-else-if="column.key === 'departmentStatus'" class="text-center">
            <a-tag v-if="record.departmentStatus === 0" color="success">Hoạt động</a-tag>
            <a-tag v-else-if="record.departmentStatus === 1" color="error">Không hoạt động</a-tag>
          </div>
        </template>
      </tutor-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import TutorTable from "@/components/ui/TutorTable/TutorTable.vue";
import { DepartmentResponse } from "@/services/api/admin/department.api";
import {  useDepartmentSynchronize } from "@/services/service/admin/department.action";
import { BookOutlined, EyeOutlined, GoldOutlined } from "@ant-design/icons-vue";
import { ColumnType } from "ant-design-vue/es/table";
import { h } from "vue";
import { useMajorSynchronize } from "@/services/service/admin/major.action";
import { toast } from "vue3-toastify";
import {ERROR_MESSAGE} from "@/constants/message.constant.ts";

defineProps({
  dataSource: Array as () => DepartmentResponse[],
  loading: Boolean,
  paginationParams: Object as () => any,
  totalPages: Number,
});

const emit = defineEmits([
  "update:paginationParams",
  "handleOpenDepartmentDetailModal",
  "handleOpenModalAdd",
  "handleOpenMajorListModal",
  "handleOpenDepartmentsFacilityListModal",
  "syncSuccess"
]);

const { mutate: onSyncDepartment } = useDepartmentSynchronize();
const { mutate: onSyncMajor } = useMajorSynchronize();
const handleSync = async () => {
  try {
      onSyncDepartment(undefined, {
        onSuccess: () => {
        },
        onError: (error) => {
          toast.error(
              error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
          )
          return;
        },
      });

      onSyncMajor(undefined, {
        onSuccess: () => {
          toast.success("Đồng bộ bộ môn, chuyên ngành  thành công");
        },
        onError: (error) => {
          toast.error(
              error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
          )
        },
      });

    emit("syncSuccess")

  } catch (error) {
    console.log("Có lỗi xảy ra trong quá trình đồng bộ: " + error.message);
  }
};

const columnsDepartment: ColumnType[] = [
  {
    title: "STT",
    dataIndex: "orderNumber",
    key: "index",
    ellipsis: true,
  },
  {
    title: "Mã bộ môn",
    dataIndex: "departmentCode",
    key: "departmentCode",
    ellipsis: true,
  },
  {
    title: "Tên bộ môn",
    dataIndex: "departmentName",
    key: "departmentName",
    ellipsis: true,
  },
  // {
  //   title: "Trạng thái",
  //   dataIndex: "departmentStatus",
  //   key: "departmentStatus",
  //   ellipsis: true,
  // },
  {
    title: "Hành động",
    key: "action",
    align: "center",
    width: "150px",
  },
];
</script>
