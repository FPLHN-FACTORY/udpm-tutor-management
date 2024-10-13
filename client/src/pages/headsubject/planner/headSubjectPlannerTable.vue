<template>
    <div class="shadow-xl p-3 m-3 rounded-md flex h-full flex-col overflow-auto">
      <header class="flex justify-between items-center min-h-36">
        <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
          <v-icon name="bi-list-ul" scale="2" />
          <span class="ml-2 text-2xl">Danh sách người lập kế hoạch</span>
        </h2>
        <a-button
          type="primary"
          size="large"
          class="m-4 flex justify-between items-center"
          @click="$emit('handleOpenCreatePlanner')"
        >
          <v-icon name="bi-person-plus" scale="1" class="me-1" />
          Tạo người lập kế hoạch
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
            <div v-if="column.key === 'action'" class="flex items-center justify-center space-x-2">
                <div v-if="record.isAssigned === 0">
                <a-tooltip title="Phân công" color="#FFC26E">
                    <a-button
                    class="flex items-center justify-center"
                    type="primary"
                    size="large"
                    @click="handleAssignedPlanner(record)" 
                    :icon="h(UserSwitchOutlined)"
                    />
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
import { useAssignedPlannerResponse } from "@/services/service/headsubject/plan.action";
  import { useAuthStore } from "@/stores/auth";
  import { EditOutlined, ExclamationCircleOutlined, UserSwitchOutlined } from "@ant-design/icons-vue";
  import { Modal } from "ant-design-vue";
  import { ColumnType } from "ant-design-vue/es/table";
  import { computed, createVNode, h, reactive } from "vue";
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
    currentSemesterId: string,
    currentBlockId: string,
    currentFacilityCode: string,
    currentDepartmentCode: string,
    currentUserId: string,
  }
  
  const modelRef = reactive<AssignedPlannerForm>({
    currentFacilityCode: userInfo.value?.facilityCode,
    currentDepartmentCode: userInfo.value?.departmentCode,
    currentUserId: userInfo.value?.userId,
    currentSemesterId: userInfo.value?.semesterId,
    currentBlockId: userInfo.value?.blockId,
  });
  
  // Sử dụng hook để gọi hàm phân công
  const { mutate: assignPlanner } = useAssignedPlannerResponse();
  
  const handleAssignedPlanner = (record: SubjectResponse) => {
    Modal.confirm({
      content: 'Bạn chắc chắn muốn phân công chứ',
      icon: createVNode(ExclamationCircleOutlined),
      centered: true,
      async onOk() {
        try {
          const params = {
            ...modelRef, // Thêm các thông tin cần thiết vào params
            // Nếu cần thêm thông tin từ record, hãy thêm vào đây
          };
  
          await assignPlanner({ id: record.id, params }); // Gọi hàm phân công
  
          toast.success('Phân công thành công!');
          handleClose(); // Đóng modal hoặc thực hiện hành động khác nếu cần
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
  
  // Khởi tạo emit để phát sự kiện
  const emit = defineEmits([
    "update:paginationParams",
    'assignPlanner',
    'handleClose',
    'handleOpenCreatePlanner'
  ]);
  
  const handleClose = () => {
    emit("handleClose");
  };
  
  // Cấu hình cột của bảng danh sách trưởng môn
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
  