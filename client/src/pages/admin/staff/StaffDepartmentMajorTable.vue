<template>
  <div class="shadow-2xl p-3 m-3 rounded-md flex h-full flex-col overflow-auto">
    <div class="flex justify-between items-center min-h-36">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="bi-list-ul" scale="2" />
        <span class="ml-2 text-2xl">Danh sách bộ môn, chuyên ngành theo cơ sở</span>
      </h2>
      <a-button
          type="primary"
          size="large"
          class="m-4 flex justify-between items-center"
          @click="handleOpenModalAdd"
      >
        Thêm
      </a-button>
    </div>
    <div class="flex h-0 flex-1 flex-col">
      <tutor-table
          wrapperClassName="min-h-[410px]"
          :columns="columnsStaff"
          :data-source="dataSource"
          :loading="loading"
          :is-pagination="false"
      >
        <template #bodyCell="{ column, record }">
            <div v-if="column.key === 'action'" class="space-x-2 text-center">
              <a-tooltip title="Chỉnh sửa" color="#FFC26E">
                <a-button type="primary" size="regular" @click="handleOpenModalUpdate(record)"
                  :icon="h(EditOutlined)" />
              </a-tooltip>
            </div>
          </template>
      </tutor-table>
      <staff-department-major-modal
        :open="open"
        @handle-close="handleCloseModal"
        :staff-department-major-detail="staffDeparmentMajorDetailData"
        :is-loading-detail="isLoadingDetail"
      />
    </div>
    <router-view />
  </div>
</template>

<script setup lang="ts">
import TutorTable from "@/components/ui/TutorTable/TutorTable.vue";
import {DepartmentMajorResponse, StaffResponse} from "@/services/api/admin/staff.api.ts";
import {ColumnType} from "ant-design-vue/es/table";
import { computed, h, ref } from "vue";
import StaffDepartmentMajorModal from "./StaffDepartmentMajorModal.vue";
import { EditOutlined } from "@ant-design/icons-vue";
import { useGetDetailStaffDeparmentMajor } from "@/services/service/admin/staff.action";

defineProps({
  dataSource: Array<StaffResponse>,
  loading: Boolean,
});

const open = ref<boolean>(false);
const staffDepartmentMajorId = ref<string | null>(null);

const { data: staffDepartmentMajorDetail, isLoading: isLoadingDetail } = useGetDetailStaffDeparmentMajor(staffDepartmentMajorId,
  {
    refetchOnWindowFocus: false,
    enabled: () => !!staffDepartmentMajorId.value ,
  }
)

const handleOpenModalAdd = () => {
  staffDepartmentMajorId.value = null;
  open.value = true
}

const handleOpenModalUpdate = (record: DepartmentMajorResponse) => {
  console.log(record.id);
  
  staffDepartmentMajorId.value = record.id;
  open.value = true
}

const handleCloseModal = () => {
  staffDepartmentMajorId.value = null;
  open.value = false
}

const columnsStaff: ColumnType[] = [
  {
    title: "STT",
    dataIndex: "orderNumber",
    key: "index",
    ellipsis: true,
  },
  {
    title: "Bộ môn",
    dataIndex: "departmentName",
    key: "departmentName",
    ellipsis: true,
  },
  {
    title: "Chuyên ngành",
    dataIndex: "majorName",
    key: "majorName",
    ellipsis: true,
  },
  {
    title: "Hành động",
    dataIndex: "action",
    key: "action",
    ellipsis: true,
  },
];

const staffDeparmentMajorDetailData = computed(() =>
  staffDepartmentMajorId.value ? {
    ...staffDepartmentMajorDetail.value?.data,
    staffDepartmentMajorId: staffDepartmentMajorId.value
  } : null)
</script>
