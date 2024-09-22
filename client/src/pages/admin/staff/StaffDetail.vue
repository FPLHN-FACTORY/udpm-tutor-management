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
            layout="vertical"
            class="mb-4 grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-4 p-5"
        >
          <a-form-item
              label="Mã"
              class="col-span-1 md:col-span-2 lg:col-span-1"
          >
            <a-input
                :value="staffDetail?.staffCode"
                :readonly="true"
            />
          </a-form-item>
          <a-form-item
              label="Tên"
              class="col-span-1 md:col-span-2 lg:col-span-1"
          >
            <a-input
                :value="staffDetail?.staffName"
                :readonly="true"
            />
          </a-form-item>
          <a-form-item
              label="Email FPT"
              class="col-span-1 md:col-span-2 lg:col-span-1"
          >
            <a-input
                :value="staffDetail?.emailFpt"
                :readonly="true"
            />
          </a-form-item>
          <a-form-item
              label="Email FE"
              class="col-span-1 md:col-span-2 lg:col-span-1"
          >
            <a-input
                :value="staffDetail?.emailFe"
                :readonly="true"
            />
          </a-form-item>
        </a-form>
      </div>
    </div>

    <StaffRoleTable
        :data-source="staffRole"
        :loading="isLoadingStaffRole"
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
import {useDetailStaff, useGetStaffDepartmentMajor, useGetStaffRole} from "@/services/service/admin/staff.action.ts";
import {computed} from "vue";

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

const { data: dataStaffRole, isLoading: isLoadingStaffRole } = useGetStaffRole(
    staffId,
    {
      refetchOnWindowFocus: false,
      enabled: () => !!staffId.value,
    }
);

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
</script>
