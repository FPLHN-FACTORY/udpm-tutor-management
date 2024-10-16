<template>
    <div>
        <a-modal
            :open="open"
            title="Chỉnh sửa chức vụ"
            @cancel="handleClose"
            destroyOnClose
            centered
            ok-text="Lưu"
            @ok=""
            :width="1000"
        >
            <div class="py-7">
                <label class="mb-3">Chức vụ:</label>
                <a-table :columns="columns" :data-source="dataSource" :pagination="false">
                    <template #bodyCell="{ column }">
                        <div v-if="column.key === '1'" class="space-x-2 flex items-center justify-center">
                            <a-checkbox :checked="checkedRoles.includes(column.dataIndex)"
                                @change="handleCheckboxChange(column.dataIndex)" />
                        </div>
                    </template>
                </a-table>
            </div>
        </a-modal>
    </div>
</template>

<script lang="ts" setup>
import { ERROR_MESSAGE } from '@/constants/message.constant';
import { RoleResponse } from '@/services/api/admin/staff.api';
import { useUpdateStaffPermission } from '@/services/service/admin/staff.action';
import { useGetRoleOptions } from '@/services/service/common.action';
import { computed, ref, watch } from 'vue';
import { useRoute } from 'vue-router';
import { toast } from 'vue3-toastify';

const route = useRoute();
const staffId = computed(() => {
    const id = route.params.staffId;
    // Kiểm tra nếu id là mảng thì lấy phần tử đầu tiên, nếu là string thì trả về, nếu không thì null
    return Array.isArray(id) ? id[0] : id || '';
});

const props = defineProps({
    open: Boolean,
    staffRole: Array<RoleResponse>
});

const { data: roleOptionsData } = useGetRoleOptions({ facilityCode: 'HA_NOI' });

const { mutate: updatePermission } = useUpdateStaffPermission();

// Danh sách các roles được checked, khởi tạo từ props staffRole
const checkedRoles = ref<string[]>([]);

watch(() => props.staffRole, (newVal) => {
    checkedRoles.value = newVal?.map(role => role.roleName) || []
})

// Tạo cấu trúc cột với tiêu đề
const columns = computed(() =>
    roleOptionsData?.value?.data.map((role) => ({
        title: role.name,
        dataIndex: role.name,
        key: '1',
        align: 'center',
    }))
);

// Tạo dataSource chỉ với một dòng dữ liệu rỗng
const dataSource = [{ key: '1' }];

// Hàm xử lý khi checkbox thay đổi
const handleCheckboxChange = (roleName: string) => {
    // Tìm role tương ứng với roleName để lấy id
    const role = roleOptionsData?.value?.data.find(role => role.name === roleName);
    const roleId = role?.id; // Lấy id của role

    if (!roleId) return; // Nếu không tìm thấy roleId, không làm gì cả

    const index = checkedRoles.value.indexOf(roleName);
    if (index > -1) {
        // Nếu role đã checked, bỏ nó khỏi danh sách
        checkedRoles.value.splice(index, 1);

    } else {
        // Nếu role chưa checked, thêm nó vào danh sách
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

const emit = defineEmits(['handleCloseModal']);

const handleClose = () => {
    emit('handleCloseModal');
};

watch(checkedRoles, (newVal) => {
    console.log(checkedRoles);
    
})

</script>