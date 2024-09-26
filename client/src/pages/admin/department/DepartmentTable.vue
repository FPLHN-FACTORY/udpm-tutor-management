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
        :disabled="isSyncing"
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
import { ERROR_MESSAGE } from "@/constants/message.constant";
import { DepartmentResponse } from "@/services/api/admin/department.api";
import { useDepartmentCampusSynchronize, useDepartmentSynchronize } from "@/services/service/admin/department.action";
import { BookOutlined, EyeOutlined, GoldOutlined } from "@ant-design/icons-vue";
import { ColumnType } from "ant-design-vue/es/table";
import { computed, h } from "vue";
import { toast } from "vue3-toastify";
import {useQueryClient} from "@tanstack/vue-query";
import { queryKey } from "@/constants/queryKey";
import { useMajorCampusSynchronize, useMajorSynchronize } from "@/services/service/admin/major.action";
import { useAuthStore } from "@/stores/auth";
import { useStaffSynchronize } from "@/services/service/admin/staff.action";

const auth = useAuthStore();
const userInfo = computed(() => auth.user);

const props = defineProps({
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
  "syncSuccess",
]);

const queryClient = useQueryClient();

const { mutate: onSyncDepartment, isLoading: isSyncing } = useDepartmentSynchronize();
const { mutate: onSyncMajor } = useMajorSynchronize();
const { mutate: onSyncStaff } = useStaffSynchronize();
const { mutate: onSyncDepartmentCampus } = useDepartmentCampusSynchronize();
const { mutate: onSyncCampusSynchronize } = useMajorCampusSynchronize();

const handleSync = async () => {
  try {

    // await onSyncStaff(userInfo.value?.facilityCode);
    await onSyncDepartment();
    toast.success("Đồng bộ bộ môn thành công");

    await onSyncMajor();
    toast.success("Đồng bộ chuyên ngành thành công");

    await onSyncDepartmentCampus();
    toast.success("Đồng bộ bộ môn theo cơ sở thành công");

    await onSyncCampusSynchronize();
    toast.success("Đồng bộ chuyên ngành theo cơ sở thành công");

    await queryClient.invalidateQueries({ queryKey: [queryKey.admin.department.departmentList] });
    await queryClient.refetchQueries({ queryKey: [queryKey.admin.department.departmentList] });

    emit('syncSuccess');
  } catch (error: any) {
    console.error("🚀 ~ handleSync ~ error:", error);
    toast.error(
        error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
    );
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
