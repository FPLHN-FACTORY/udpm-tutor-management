<template>
  <div class="shadow-xl p-3 m-3 rounded-md flex h-full flex-col overflow-auto">
    <div class="flex justify-between items-center min-h-36">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="bi-list-ul" scale="2" />
        <span class="ml-2 text-2xl">Danh sách nhân viên</span>
      </h2>
      <a-popconfirm placement="bottom" ok-text="Yes" cancel-text="No" @confirm="handleSync">
        <template #title>
          <p>Bạn chắc chắn muốn đồng bộ chứ?</p>
        </template>
        <a-button
            type="primary"
            size="large"
            class="m-4"
        >
          Đồng bộ
        </a-button>
      </a-popconfirm>
    </div>
    <div class="flex h-0 flex-1 flex-col">
      <tutor-table
          wrapperClassName="min-h-[410px]"
          :columns="columnsStaff"
          :data-source="dataSource"
          :loading="loading"
          :pagination-params="paginationParams || {}"
          :total-pages="totalPages || 0"
          @update:pagination-params="$emit('update:paginationParams', $event)"
      >
        <template #bodyCell="{ column, record }">
          <div v-if="column.key === 'action'" class="space-x-2 text-center flex items-center justify-center">
            <a-tooltip title="Chi tiết nhân viên" color="#FFC26E">
              <a-button
                  class="flex items-center justify-center"
                  type="primary"
                  size="large"
                  :icon="h(EyeOutlined)"
                  @click="goToDetail(record.id)"
              />
            </a-tooltip>
          </div>
        </template>
      </tutor-table>
    </div>
    <router-view />
  </div>
</template>

<script setup lang="ts">
import TutorTable from "@/components/ui/TutorTable/TutorTable.vue";
import {StaffResponse} from "@/services/api/admin/staff.api.ts";
import {ColumnType} from "ant-design-vue/es/table";
import {EyeOutlined} from "@ant-design/icons-vue";
import {computed, h} from "vue";
import {useRouter} from "vue-router";
import {useStaffSynchronize} from "@/services/service/admin/staff.action.ts";
import {useAuthStore} from "@/stores/auth.ts";

const router = useRouter();
// Khởi tạo Auth Store và lấy thông tin người dùng
const auth = useAuthStore();
const userInfo = computed(() => auth.user);

function goToDetail(staffId: string) {
  router.push({ name: 'detailStaff', params: { staffId } });
}

defineProps({
  dataSource: Array<StaffResponse>,
  loading: Boolean,
  paginationParams: Object,
  totalPages: Number,
});

defineEmits([
  "update:paginationParams",
]);

const { mutate: onSync } = useStaffSynchronize();

// Handle button click
const handleSync = async () => {
    await onSync(userInfo.value?.facilityCode)
};

const columnsStaff: ColumnType[] = [
  {
    title: "STT",
    dataIndex: "orderNumber",
    key: "index",
    ellipsis: true,
  },
  {
    title: "Mã nhân viên",
    dataIndex: "staffCode",
    key: "staffCode",
    ellipsis: true,
  },
  {
    title: "Tên nhân viên",
    dataIndex: "staffName",
    key: "staffName",
    ellipsis: true,
  },
  {
    title: "Email FPT",
    dataIndex: "emailFpt",
    key: "emailFpt",
    ellipsis: true,
  },
  {
    title: "Email FE",
    dataIndex: "emailFe",
    key: "emailFe",
    ellipsis: true,
  },
  {
    title: "Hành động",
    key: "action",
    align: "center",
    width: "150px",
  },
];
</script>
