<template>
  <div>
    <a-modal
      :open="open"
      destroyOnClose
      centered
      :width="1000"
      @cancel="handleClose"
    >
      <div class="py-7">
        <a-table
          :columns="columns"
          :data-source="dataSource"
          :pagination="false"
        >
          <template #bodyCell="{ column }">
            <div
              v-if="column.key === '1'"
              class="space-x-2 flex items-center justify-center"
            >
              <a-checkbox
                :checked="checkedRoles.includes(column.dataIndex)"
                @change="handleCheckboxChange(column.dataIndex)"
              />
            </div>
          </template>
        </a-table>
      </div>
      <template #footer>
        <a-button key="back" @click="handleClose"> Đóng </a-button>
      </template>
      <template #title>
        <span class="text-xl">Phân chức vụ</span>
      </template>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import { ERROR_MESSAGE } from "@/constants/message.constant";
import { RoleResponse } from "@/services/api/admin/staff.api";
import { useUpdateStaffPermission } from "@/services/service/admin/staff.action";
import { useGetRoleOptions } from "@/services/service/common.action";
import { computed, ref, watch } from "vue";
import { useRoute } from "vue-router";
import { toast } from "vue3-toastify";

const route = useRoute();

const staffId = computed(() => {
  const id = route.params.staffId;
  return Array.isArray(id) ? id[0] : id || "";
});

const props = defineProps({
  open: Boolean,
  staffRole: Array<RoleResponse>,
});

const { data: roleOptionsData } = useGetRoleOptions({ facilityCode: "HA_NOI" });

const { mutate: updatePermission } = useUpdateStaffPermission();

const checkedRoles = ref<string[]>([]);

watch(
  () => props.staffRole,
  (newVal) => {
    checkedRoles.value = newVal?.map((role) => role.roleName) || [];
  }
);

const columns = computed(() =>
  roleOptionsData?.value?.data.map((role) => ({
    title: role.name,
    dataIndex: role.name,
    key: "1",
    align: "center",
  }))
);

const dataSource = [{ key: "1" }];

const handleCheckboxChange = (roleName: string) => {
  const role = roleOptionsData?.value?.data.find(
    (role) => role.name === roleName
  );

  const roleId = role?.id;

  if (!roleId) return;

  const index = checkedRoles.value.indexOf(roleName);
  if (index > -1) {
    checkedRoles.value.splice(index, 1);
  } else {
    checkedRoles.value.push(roleName);
  }

  try {
    updatePermission({ idRole: roleId, idStaff: staffId.value });
    toast.success("Cập nhật thành công");
  } catch (error: any) {
    console.error("🚀 ~ handleUpdate ~ error:", error);
    toast.error(
      error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
    );
  }
};

const emit = defineEmits(["handleCloseModal"]);

const handleClose = () => {
  emit("handleCloseModal");
};

watch(checkedRoles, (_) => {
  console.log(checkedRoles);
});
</script>
