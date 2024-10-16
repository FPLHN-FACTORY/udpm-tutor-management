<template>
  <div class="mt-4 p-2">
    <div class="flex justify-between items-center">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <span class="m-2 text-3xl">Quản lý chức vụ / bộ môn / chuyên ngành</span>
      </h2>
    </div>

    <div class="shadow-2xl p-3 rounded-md m-3">
      <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
        <v-icon name="bi-exclamation-circle" scale="2" />
        <span class="ml-2 text-2xl">Thông tin nhân viên</span>
      </h2>
      <div
          v-if="isLoadingDetail"
          class="flex justify-center items-center"
      >
        <a-spin />
      </div>
      <div v-else>
        <a-form
            :model="staffRef"
            @finish="handleUpdateStaff"
            layout="vertical"
            class="mb-4 grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-4 p-5"
            autocomplete="off"
        >
          <a-form-item
              label="Mã"
              name="staffCode"
              class="col-span-1 md:col-span-2 lg:col-span-1"
              :rules="rulesStaffRef.staffCode"
          >
            <a-input
                v-model:value="staffRef.staffCode"
            />
          </a-form-item>
          <a-form-item
              label="Tên"
              name="name"
              class="col-span-1 md:col-span-2 lg:col-span-1"
              :rules="rulesStaffRef.name"
          >
            <a-input
                v-model:value="staffRef.name"
            />
          </a-form-item>
          <a-form-item
              label="Email FPT"
              name="emailFpt"
              class="col-span-1 md:col-span-2 lg:col-span-1"
              :rules="rulesStaffRef.emailFpt"
          >
            <a-input
                v-model:value="staffRef.emailFpt"
            />
          </a-form-item>
          <a-form-item
              label="Email FE"
              name="emailFe"
              class="col-span-1 md:col-span-2 lg:col-span-1"
              :rules="rulesStaffRef.emailFe"
          >
            <a-input
                v-model:value="staffRef.emailFe"
            />
          </a-form-item>
          <a-form-item class="col-span-1 md:col-span-2 lg:col-span-4 flex justify-end">
            <a-button type="primary" html-type="submit">Lưu</a-button>
          </a-form-item>
        </a-form>
      </div>
    </div>

    <StaffRoleTable
        :data-source="staffRole"
        :loading="isLoadingStaffRole"
        @update:staff-role="handleRefechStaffRole"
        
    />

    <StaffDepartmentMajorTable
        :data-source="staffDepartmentMajor"
        :loading="isLoadingStaffDepartmentMajor"
    />
  </div>
</template>

<script setup lang="ts">
import StaffRoleTable from "@/pages/admin/staff/StaffRoleTable.vue";
import StaffDepartmentMajorTable from "@/pages/admin/staff/StaffDepartmentMajorTable.vue";
import { useRoute } from 'vue-router';
import {useDetailStaff, useGetStaffDepartmentMajor, useGetStaffRole, useUpdateStaff} from "@/services/service/admin/staff.action.ts";
import {computed, createVNode, reactive, watch} from "vue";
import { StaffForm } from "./CreateStaffModal.vue";
import { Modal } from "ant-design-vue";
import { ExclamationCircleOutlined } from "@ant-design/icons-vue";
import { toast } from "vue3-toastify";
import { ERROR_MESSAGE } from "@/constants/message.constant";

const route = useRoute();
const staffId = computed(() => {
  const id = route.params.staffId;
  // Kiểm tra nếu id là mảng thì lấy phần tử đầu tiên, nếu là string thì trả về, nếu không thì null
  return Array.isArray(id) ? id[0] : id || null;
});

const { data: dataDetail, isLoading: isLoadingDetail } = useDetailStaff(
    staffId,
    {
      refetchOnWindowFocus: false,
      enabled: () => !!staffId.value,
    }
);

const { data: dataStaffRole, isLoading: isLoadingStaffRole, refetch: refetchStaffRole } = useGetStaffRole(
    staffId,
    {
      refetchOnWindowFocus: false,
      enabled: () => !!staffId.value,
    }
);

const handleRefechStaffRole = () => {
  refetchStaffRole();
}

const { data: dataStaffDepartmentMajor, isLoading: isLoadingStaffDepartmentMajor } = useGetStaffDepartmentMajor(
    staffId,
    {
      refetchOnWindowFocus: false,
      enabled: () => !!staffId.value,
    }
);

const staffRole = computed(() => dataStaffRole?.value?.data || []);
const staffDepartmentMajor = computed(() => dataStaffDepartmentMajor?.value?.data || []);
const staffDetail = computed(() => dataDetail?.value?.data || null);

const staffRef = reactive<StaffForm>({
  name: "",
  staffCode: "",
  emailFe: "",
  emailFpt: "",
});

const { mutate: updateStaff } = useUpdateStaff();

const handleUpdateStaff = (values: any) => {
  Modal.confirm({
    content: 'Bạn chắc chắn muốn cập nhật thông tin chứ?',
    icon: createVNode(ExclamationCircleOutlined),
    centered: true,
    async onOk() {
      try {
        updateStaff({ ...values, id: staffDetail.value?.id }, {
          onSuccess: () => {
            toast.success("Cập nhật nhân viên thành công");
          },
          onError: (error: any) => {
            toast.error(
              error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
            )
          },
        })

      } catch (error: any) {
        console.error("🚀 ~ handleUpdate ~ error:", error);
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

const rulesStaffRef = reactive({
  name: [
    { required: true, message: "Vui lòng nhập tên", trigger: "blur" },
  ],
  staffCode: [
    { required: true, message: "Vui lòng nhập mã nhân viên", trigger: "blur" },
    { message: "Vui lòng nhập lại, mã nhân không hợp lệ", trigger: "blur", pattern: '^[^\s]+$' },
  ],
  emailFe: [
    { required: true, message: "Vui lòng nhập email FE", trigger: "blur" },
    { message: "Vui lòng nhập lại, email FE không hợp lệ", trigger: "blur", pattern: '^[A-Za-z0-9._%+-]+@fe\.edu\.vn$' },
  ],
  emailFpt: [
    { required: true, message: "Vui lòng nhập email FPT", trigger: "blur" },
    { message: "Vui lòng nhập lại, email FPT không hợp lệ", trigger: "blur", pattern: '^[A-Za-z0-9._%+-]+@fpt\.edu\.vn$' },
  ],
});

watch(staffDetail, (newDetail) => {
  if (newDetail) {
    Object.assign(staffRef, {
      staffCode: newDetail.staffCode,
      name: newDetail.staffName,
      emailFe: newDetail.emailFe,
      emailFpt: newDetail.emailFpt
    })
  }
}, { immediate: true });
</script>
