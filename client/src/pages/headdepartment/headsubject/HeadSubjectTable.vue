<template>
  <div class="shadow-xl p-3 m-3 rounded-md flex h-full flex-col overflow-auto">
    <header class="flex justify-between items-center min-h-36">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="bi-list-ul" scale="2" />
        <span class="ml-2 text-2xl">Danh sách trưởng môn</span>
      </h2>
      <a-button
        v-if="checkAssign"
        type="primary"
        @click="handleSyncHeadSubjectAttach"
        size="large"
        class="m-4 flex justify-between items-center"
        :loading="isSyncing"
      >
        <v-icon name="bi-arrow-counterclockwise" scale="2" />
        Đồng bộ dữ liệu từ học kỳ trước
      </a-button>
    </header>
    <main class="flex flex-1 h-0 flex-col">
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
          <div
            v-if="column.key === 'action'"
            class="flex items-center justify-center space-x-2"
          >
            <a-tooltip title="Chỉnh sửa trưởng môn" color="#FFC26E">
              <a-button
                class="flex items-center justify-center"
                type="primary"
                size="large"
                @click="$emit('handleOpenModalUpdate', record)"
                :icon="h(EditOutlined)"
              />
            </a-tooltip>
            <a-tooltip title="Chi tiết trưởng môn" color="#FFC26E">
              <a-button
                class="flex items-center justify-center"
                type="primary"
                size="large"
                @click="$emit('handleOpenModalDetail', record)"
                :icon="h(EyeOutlined)"
              />
            </a-tooltip>
          </div>
          <div v-else-if="column.key === 'isAssigned'" class="space-x-2">
            <a-tag :color="record.isAssigned === 1 ? 'green' : 'red'">
              {{ record.isAssigned === 1 ? "Đã phân công" : "Chưa phân công" }}
            </a-tag>
          </div>
        </template>
      </tutor-table>
    </main>
  </div>
</template>

<script setup lang="ts">
import TutorTable from "@/components/ui/TutorTable/TutorTable.vue";
import { ERROR_MESSAGE } from "@/constants/message.constant";
import { HeadOfSubjectResponse } from "@/services/api/headdepartment/head-subject.api";
import { useSyncHeadSubjectAttach } from "@/services/service/headdepartment/head-subject.action.ts";
import { EditOutlined, EyeOutlined } from "@ant-design/icons-vue";
import { ColumnType } from "ant-design-vue/es/table";
import { h, ref } from "vue";
import { toast } from "vue3-toastify";

const props = defineProps({
  dataSource: Array<HeadOfSubjectResponse>,
  loading: Boolean,
  paginationParams: Object,
  totalPages: Number,
  semesterId: String,
  checkAssign: Boolean,
});

const emit = defineEmits([
  "update:paginationParams",
  "handleOpenModalDetail",
  "handleOpenModalUpdate",
]);

const isSyncing = ref(false);

const { mutateAsync: onSync } = useSyncHeadSubjectAttach();

const handleSyncHeadSubjectAttach = async () => {
  isSyncing.value = true;
  await onSync(props.semesterId || "", {
    onSuccess: () => {
      isSyncing.value = false;
      toast.success("Đồng bộ dữ liệu thành công");
    },
    onError: (error: any) => {
      isSyncing.value = false;
      toast.error(
        error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
      );
    },
  });
  isSyncing.value = false;
};

const columnsSubject: ColumnType[] = [
  { title: "STT", dataIndex: "orderNumber", key: "index", ellipsis: true },
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
    width: "250px",
  },
  {
    title: "Email FPT",
    dataIndex: "emailFPT",
    key: "emailFPT",
    ellipsis: true,
    width: "120px",
  },
  {
    title: "Email FE",
    dataIndex: "emailFE",
    key: "emailFE",
    ellipsis: true,
    width: "200px",
    align: "center",
  },
  {
    title: "Phân công",
    dataIndex: "isAssigned",
    key: "isAssigned",
    ellipsis: true,
    align: "center",
  },
  {
    title: "Môn đang phụ trách",
    dataIndex: "assignedCount",
    key: "assignedCount",
    ellipsis: true,
    align: "center",
    width: "150px",
  },
  { title: "Hành động", key: "action", align: "center", width: "150px" },
];
</script>
