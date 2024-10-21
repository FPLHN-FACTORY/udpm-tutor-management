<template>
  <div class="shadow-xl p-3 m-3 rounded-md flex h-full flex-col overflow-auto">
    <div class="flex justify-between items-center min-h-36">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="bi-list-ul" scale="2" />
        <span class="ml-2 text-2xl">Danh sách kế hoạch</span>
      </h2>
      <a-button
          type="primary"
          @click="$emit('handleOpenModalAdd')"
          size="large"
          class="m-4"
      >
        Tạo kế hoạch
      </a-button>
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
            <a-tooltip v-if="record.status === 'IN_PROGRESS'" title="Import sinh viên" color="#FFC26E">
              <input
                  type="file"
                  accept=".xls, .xlsx"
                  @change="handleFileChange"
                  style="display: none"
                  ref="fileInput"
              />
              <a-button
                  class="flex items-center justify-center"
                  type="primary"
                  size="large"
                  @click="selectFile(record.id)"
                  :icon="h(UploadOutlined)"
              />
            </a-tooltip>
            <a-tooltip v-if="record.status === 'PLANNING'" title="Phê duyệt kế hoạch" color="#FFC26E">
              <a-button
                  class="flex items-center justify-center"
                  type="primary"
                  size="large"
                  @click="handleApprovePlan(record.id)"
                  :icon="h(CheckCircleOutlined)"
                  :loading="isPendingApprove"
              />
            </a-tooltip>
            <a-tooltip v-if="record.status === 'HEAD_DEPARTMENT_APPROVED'" title="Bắt đầu triển khai" color="#FFC26E">
              <a-button
                  class="flex items-center justify-center"
                  type="primary"
                  size="large"
                  @click="handleStartPlan(record.id)"
                  :icon="h(CheckCircleOutlined)"
                  :loading="isPendingStart"
              />
            </a-tooltip>
            <a-tooltip title="Chi tiết kế hoạch" color="#FFC26E">
              <a-button
                  class="flex items-center justify-center"
                  type="primary"
                  size="large"
                  @click="goToDetail(record.id)"
                  :icon="h(EyeOutlined)"
              />
            </a-tooltip>
            <a-tooltip v-if="record.status === 'PLANNING'" title="Chỉnh sửa kế hoạch" color="#FFC26E">
              <a-button
                  class="flex items-center justify-center"
                  type="primary"
                  size="large"
                  @click="$emit('handleOpenModalUpdate', record)"
                  :icon="h(EditOutlined)"
              />
            </a-tooltip>
            <a-tooltip title="Xuất file kế hoạch" color="#FFC26E">
              <a-button
                  class="flex items-center justify-center"
                  type="primary"
                  size="large"
                  @click="$emit('handleOpenModalUpdate', record)"
                  :icon="h(DownloadOutlined)"
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
          <div v-else-if="column.key === 'timeAdditionSubject'" >
            <p>{{ getDateFormat(record.startTime,false) + ' - ' + getDateFormat(record.endTime,false)}}</p>
          </div>
        </template>
      </tutor-table>

    </div>
  </div>
</template>

<script setup lang="ts">
import TutorTable from "@/components/ui/TutorTable/TutorTable.vue";
import {
  CheckCircleOutlined,
  DownloadOutlined,
  EditOutlined,
  ExclamationCircleOutlined,
  EyeOutlined,
  UploadOutlined
} from "@ant-design/icons-vue";
import { ColumnType } from "ant-design-vue/es/table";
import {createVNode, h, ref} from "vue";
import { PlanResponse } from "@/services/api/planner/plan.api.ts";
import {useRouter} from "vue-router";
import {confirmModal, formatBlockName, getDateFormat, getTagColor, getTagStatus} from "@/utils/common.helper.ts";
import {
  useApprovePlan,
  useCheckApprovePlan,
  useStartPlan,
  useUploadFileStudent
} from "@/services/service/planner/plan.action.ts";
import {toast} from "vue3-toastify";
import {ERROR_MESSAGE} from "@/constants/message.constant.ts";
import {Modal} from "ant-design-vue";

const router = useRouter();
const fileInput = ref<HTMLInputElement | null>(null);
const planId = ref<string>("");

const selectFile = (id: string) => {
  fileInput.value?.click();
  planId.value = id;
};

const handleFileChange = (event: Event) => {
  const target = event.target as HTMLInputElement;
  if (target.files && target.files.length > 0) {
    handleImportStudent(target.files[0]);
  }
};

const { mutate: uploadFileStudent } = useUploadFileStudent();

const handleImportStudent = (file: File) => {
  Modal.confirm({
    content: 'Bạn chắc chắn muốn import file chứ!',
    icon: createVNode(ExclamationCircleOutlined),
    centered: true,
    async onOk() {
      try {
        uploadFileStudent({file, planId}, {
          onSuccess: () => {
            toast.success("Import file sinh viên thành công");
          },
          onError: (error: any) => {
            toast.error(
                error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
            )
          },
        })
      } catch (error: any) {
        console.error("🚀 ~ handleAdd ~ error:", error);
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
}

const goToDetail = (planId: string) => {
  router.push({ name: 'pLPlDetailPlan', params: { planId } });
}

const { mutate: approvePlan, isPending: isPendingApprove } = useApprovePlan();
const { mutate: startPlan, isPending: isPendingStart } = useStartPlan();

const handleApprovePlan = async (id: string) => {
  const message = 'Bạn chắc chắn muốn phê duyệt kế hoạch này chứ!'; // Thông điệp xác nhận

  confirmModal(message, () => { // Thêm async ở đây
    try {
      const { data: checkApprove }  = useCheckApprovePlan(id); // Sử dụng await

      if (!checkApprove?.value?.data) {
        confirmModal("Chưa đến thời gian phê duyệt kế hoạch. Bạn có chắc chắn vẫn muốn phê duyệt kế hoạch này không?", async () => {
          approvePlan(id, {
            onSuccess: () => {
              toast.success("Phê duyệt thành công!");
            },
            onError: (error: any) => {
              toast.error(
                  error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
              );
            },
          });
        });
        return;
      }

      approvePlan(id, {
        onSuccess: () => {
          toast.success("Phê duyệt thành công!");
        },
        onError: (error: any) => {
          toast.error(
              error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
          );
        },
      });
    } catch (error: any) {
      console.log(error);
      toast.error("Đã xảy ra lỗi trong quá trình phê duyệt.");
    }
  });
};

const handleStartPlan = async (id: string) => {
  const message = 'Bạn chắc chắn muốn bắt đầu triển khai kế hoạch này chứ!'; // Thông điệp xác nhận
  confirmModal(message, () => { // Thêm async ở đây
    try {
      startPlan(id, {
        onSuccess: () => {
          toast.success("Triển khai kế hoạch thành công!");
        },
        onError: (error: any) => {
          toast.error(
              error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
          );
        },
      });
    } catch (error: any) {
      console.log(error);
      toast.error("Đã xảy ra lỗi trong quá trình bắt đầu kế hoạch.");
    }
  });
};

defineProps({
  dataSource: Array as () => PlanResponse[],
  loading: Boolean,
  paginationParams: Object,
  totalPages: Number,
});

defineEmits([
  "update:paginationParams",
  "handleOpenModalUpdate",
  "handleOpenModalAdd",
]);

const columnsSubject: ColumnType[] = [
  {
    title: "STT",
    dataIndex: "orderNumber",
    key: "index",
    ellipsis: true,
    width: "40px",
  },
  {
    title: "Tên kế hoạch",
    dataIndex: "planName",
    key: "planName",
    ellipsis: true,
    width: "120px",
  },
  {
    title: "Block",
    dataIndex: "blockName",
    key: "blockName",
    ellipsis: true,
    width: "80px",
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
    width: "100px",
    align: "center",
  },
  {
    title: "Thời gian thêm môn",
    dataIndex: "timeAdditionSubject",
    key: "timeAdditionSubject",
    ellipsis: true,
    width: "200px",
    align: "center",
  },
  {
    title: "Trạng thái",
    dataIndex: "status",
    key: "status",
    ellipsis: true,
    width: "150px",
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
