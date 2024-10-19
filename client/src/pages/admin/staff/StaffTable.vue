<template>
  <div class="shadow-xl p-3 m-3 rounded-md flex h-full flex-col overflow-auto">
    <div class="flex justify-between items-center min-h-36">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="bi-list-ul" scale="2" />
        <span class="ml-2 text-2xl">Danh sách nhân viên</span>
      </h2>
     <div>
       <a-button
           type="primary"
           size="large"
           class="m-2"
           @click="handleDownloadTemplate"
       >
         Tải xuống mẫu
       </a-button>
       <input
           type="file"
           accept=".xls, .xlsx"
           @change="handleFileChange"
           style="display: none"
           ref="fileInput"
       />
       <a-button
           type="primary"
           size="large"
           @click="selectFile"
       >
         Import nhân viên
       </a-button>
       <a-button
           type="primary"
           size="large"
           class="m-2"
           @click="handleOpenModalAdd"
       >
         Thêm nhân viên
       </a-button>
     </div>
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
      <create-staff-modal
        :open="open"
        @handle-close-modal="handleCloseModalAdd"
      />
    </div>
    <router-view />
  </div>
</template>

<script setup lang="ts">
import TutorTable from "@/components/ui/TutorTable/TutorTable.vue";
import {downloadTemplateStaff, StaffResponse} from "@/services/api/admin/staff.api.ts";
import {ColumnType} from "ant-design-vue/es/table";
import {ExclamationCircleOutlined, EyeOutlined} from "@ant-design/icons-vue";
import {computed, createVNode, h, ref} from "vue";
import {useRouter} from "vue-router";
import CreateStaffModal from "./CreateStaffModal.vue";
import {Modal} from "ant-design-vue";
import {toast} from "vue3-toastify";
import {ERROR_MESSAGE} from "@/constants/message.constant.ts";
import {useUploadFileStaff} from "@/services/service/admin/staff.action.ts";
import {useAuthStore} from "@/stores/auth.ts";

const auth = useAuthStore();
const userInfo = computed(() => auth.user);
const router = useRouter();
const open = ref<boolean>(false)
const fileInput = ref<HTMLInputElement | null>(null);

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

const handleFileChange = (event: Event) => {
  const target = event.target as HTMLInputElement;
  if (target.files && target.files.length > 0) {
    handleImportStaff(target.files[0]);
  }
};

const selectFile = () => {
  fileInput.value?.click();
};

const { mutate: uploadFileStaff } = useUploadFileStaff();

const handleDownloadTemplate = async () => {
  try {
    const facilityId = userInfo.value?.facilityId;
    if (facilityId) { // Kiểm tra facilityId có tồn tại
      await downloadTemplateStaff(facilityId);
    }
  } catch (error) {
    // Handle error if necessary, e.g., show a message to the user
    console.error("Download failed:", error);
  }
};

const handleImportStaff = (file: File) => {
  Modal.confirm({
    content: 'Bạn chắc chắn muốn import file chứ!',
    icon: createVNode(ExclamationCircleOutlined),
    centered: true,
    async onOk() {
      try {

        uploadFileStaff(file, {
          onSuccess: () => {
            toast.success("Import file nhân viên thành công");
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

const handleOpenModalAdd = () => {
  open.value = true;
};

const handleCloseModalAdd = () => {
  open.value = false;
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
