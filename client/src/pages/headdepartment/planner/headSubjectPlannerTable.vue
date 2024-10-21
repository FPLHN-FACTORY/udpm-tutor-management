<template>
  <div class="shadow-xl p-3 m-3 rounded-md flex h-full flex-col overflow-auto">
    <header class="flex justify-between items-center min-h-36">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="bi-list-ul" scale="2" />
        <span class="ml-2 text-2xl">Danh sách người lập kế hoạch</span>
      </h2>
    </header>
    <main class="flex flex-1 h-0 flex-col">
      <tutor-table wrapperClassName="min-h-[410px]" :columns="columnsSubject" :data-source="dataSource"
        :loading="loading" :pagination-params="paginationParams || {}" :total-pages="totalPages || 0"
        @update:pagination-params="$emit('update:paginationParams', $event)">
        <template #bodyCell="{ column, record }">
          <div v-if="column.key === 'action'" class="flex items-center justify-center space-x-2">
            <div v-if="record.isAssigned === 0">
              <a-tooltip title="Phân công" color="#FFC26E">
                <a-button class="flex items-center justify-center" type="primary" size="large"
                  @click="handleAssignedPlanner(record)" :icon="h(UserSwitchOutlined)" />
              </a-tooltip>
            </div>
            <div v-if="record.isAssigned === 1">
              <a-tooltip title="Hủy phân công" color="#FFC26E">
                <a-button class="flex items-center justify-center" type="primary" size="large"
                  @click="handleUnAssignedPlanner(record)" :icon="h(DeleteOutlined)" />
              </a-tooltip>
            </div>
          </div>
          <div v-else-if="column.key === 'isAssigned'" class="space-x-2">
            <a-tag :color="record.isAssigned === 1 ? 'green' : 'red'">
              {{ record.isAssigned === 1 ? 'Đã phân công' : 'Chưa phân công' }}
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
import { SubjectResponse } from "@/services/api/admin/subject.api";
import { useAssignedPlanner, useUnassignedPlanner } from "@/services/service/headdepartment/planner.action";
import { useAuthStore } from "@/stores/auth";
import { ExclamationCircleOutlined, UserSwitchOutlined, DeleteOutlined } from "@ant-design/icons-vue";
import { Modal } from "ant-design-vue";
import { ColumnType } from "ant-design-vue/es/table";
import { computed, createVNode, h, reactive, watch } from "vue";
import { toast } from "vue3-toastify";

const auth = useAuthStore();
const userInfo = computed(() => auth.user);

// Định nghĩa props nhận từ component cha
const props = defineProps({
  dataSource: Array<SubjectResponse>,
  loading: Boolean,
  paginationParams: Object,
  totalPages: Number,
  semesterId: String,
  checkAssign: Boolean,
});

interface AssignedPlannerForm {
  currentDepartmentCode: string,
  currentFacilityCode: string,
  currentUserId: string,
  currentSemesterId: string,
  currentBlockId: string,
}

const modelRef = reactive<AssignedPlannerForm>({
  currentFacilityCode: userInfo.value?.facilityCode,
  currentDepartmentCode: userInfo.value?.departmentCode,
  currentUserId: userInfo.value?.userId,
  currentSemesterId: userInfo.value?.semesterId,
  currentBlockId: userInfo.value?.blockId,
});

const { mutate: assignPlanner } = useAssignedPlanner();
const { mutate: unAssignPlanner } = useUnassignedPlanner();

watch(() => props.dataSource, (newData) => {
  if (newData && (!props.dataSource || props.dataSource.length === 0 || props.dataSource.every(item => item.isAssigned === 0))) {
    toast.warning('Hiện tại chưa có người lập kế hoạch, vui lòng phân công người lập kế hoạch.');
  }
});

const handleAssignedPlanner = (record: SubjectResponse) => {
  Modal.confirm({
    content: 'Bạn chắc chắn muốn phân công chứ',
    icon: createVNode(ExclamationCircleOutlined),
    centered: true,
    async onOk() {
      try {
        const params = {
          ...modelRef,
        };

        await assignPlanner({ id: record.id, params }); 

        toast.success('Phân công thành công!');
        handleClose(); 
      } catch (error: any) {
        console.error("🚀 ~  ~ error:", error);
        toast.error(
          error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
        );
      }
    },
    cancelText: 'Huỷ',
    onCancel() {
      Modal.destroyAll();
    },
  });
};

const handleUnAssignedPlanner = (record: SubjectResponse) => {
  Modal.confirm({
    content: 'Bạn chắc chắn muốn hủy phân công chứ',
    icon: createVNode(ExclamationCircleOutlined),
    centered: true,
    async onOk() {
      try {
        await unAssignPlanner({ id: record.id }); 

        toast.success('Hủy phân công thành công!');
        handleClose(); 
      } catch (error: any) {
        console.error("🚀 ~  ~ error:", error);
        toast.error(
          error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
        );
      }
    },
    cancelText: 'Huỷ',
    onCancel() {
      Modal.destroyAll();
    },
  });
};

const emit = defineEmits([
  "update:paginationParams",
  'assignPlanner',
  'handleClose',
  'handleOpenCreatePlanner'
]);

const handleClose = () => {
  emit("handleClose");
};

const columnsSubject: ColumnType[] = [
  { title: "STT", dataIndex: "orderNumber", key: "index", ellipsis: true },
  { title: "Mã nhân viên", dataIndex: "staffCode", key: "staffCode", ellipsis: true },
  { title: "Tên nhân viên", dataIndex: "staffName", key: "staffName", ellipsis: true },
  { title: "Email FPT", dataIndex: "emailFPT", key: "emailFPT", ellipsis: true, width: "120px" },
  { title: "Email FE", dataIndex: "emailFE", key: "emailFE", ellipsis: true, width: "200px", align: "center" },
  { title: "Phân công", dataIndex: "isAssigned", key: "isAssigned", ellipsis: true, align: "center" },
  { title: "Hành động", key: "action", align: "center", width: "150px" },
];
</script>