<template>
    <div class="shadow-xl p-3 m-3 rounded-md flex h-full flex-col overflow-auto">
        <div class="flex justify-between items-center min-h-36">
            <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
                <v-icon name="bi-list-ul" scale="2" />
                <span class="ml-2 text-2xl">Danh sách cở sở</span>
            </h2>
            <a-popconfirm placement="bottom" ok-text="Yes" cancel-text="No" @confirm="handleSync">
                <template #title>
                    <p>Bạn chắc chắn muốn đồng bộ chứ?</p>
                </template>
                <a-button
                    :loading="loadingSync"
                    type="primary" size="large"
                    class="m-4 flex justify-between items-center"
                    :disabled="isSyncing">
                    Đồng bộ
                </a-button>
            </a-popconfirm>
        </div>
        <div class="flex h-0 flex-1 flex-col">
            <tutor-table
                wrapperClassName="min-h-[410px]"
                :columns="columnsFacility"
                :data-source="dataSource"
                :pagination-params="paginationParams || {}"
                :total-pages="totalPages || 0"
                @update:pagination-params="$emit('update:paginationParams', $event)"
                :loading="loading"
            >
                <template #bodyCell="{ column, record }">
                    <div v-if="column.key === 'action'" class="space-x-2 flex items-center justify-center">
                        <a-button
                            type="primary"
                            size="large"
                            class="flex items-center justify-center"
                            :icon="h(EditOutlined)"
                            @click="$emit('handleOpenModalUpdate', record)"
                        />
                    </div>
                    <div v-else-if="column.key === 'facilityStatus'">
                        <a-tag :color="record.facilityStatus === 0 ? 'green' : 'red'">
                            {{ record.facilityStatus === 0 ? 'Đang hoạt động' : 'Ngừng hoạt động' }}
                        </a-tag>
                    </div>
                </template>
            </tutor-table>
        </div>
    </div>
</template>

<script lang="ts" setup>
import TutorTable from '@/components/ui/TutorTable/TutorTable.vue';
import { ERROR_MESSAGE } from '@/constants/message.constant';
import { queryKey } from '@/constants/queryKey';
import { FacilityResponse } from '@/services/api/admin/department.api';
import { useFacilitySynchronize } from '@/services/service/admin/facility.action';
import { EditOutlined } from '@ant-design/icons-vue';
import { useQueryClient } from '@tanstack/vue-query';
import { ColumnType } from 'ant-design-vue/es/table';
import { h } from 'vue';
import { toast } from 'vue3-toastify';

defineProps({
    dataSource: Array<FacilityResponse>,
    totalPages: Number,
    paginationParams: Object,
    loadingSync: Boolean,
    loading: Boolean
})

const emit = defineEmits(['handleOpenModalAdd', 'handleOpenModalUpdate', 'update:paginationParams', "syncSuccess"])

const { mutate: onSync, isLoading: isSyncing } = useFacilitySynchronize();

// Sử dụng useQueryClient để lấy queryClient
const queryClient = useQueryClient();

// Handle button click
const handleSync = async () => {
    try {
        await onSync(); // Chỉ gọi khi nhấn nút

        // Chờ invalidate hoàn tất trước khi thực hiện refetch
        // Invalidating query và refetch ngay lập tức
        await queryClient.invalidateQueries({ queryKey: [queryKey.admin.facility.facilityList] });
        await queryClient.refetchQueries({ queryKey: [queryKey.admin.facility.facilityList] });

        emit('syncSuccess');
    } catch (error: any) {
        console.error("🚀 ~ handleSync ~ error:", error); // Log lỗi để dễ dàng debug
        toast.error(
            error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
        );
    }
};

const columnsFacility: ColumnType[] = [
    {
        title: "STT",
        dataIndex: "orderNumber",
        key: "index",
        ellipsis: true,
    },
    {
        title: "Mã cở sở",
        dataIndex: "facilityCode",
        key: "facilityCode",
        ellipsis: true,
    },
    {
        title: "Tên cở sở",
        dataIndex: "facilityName",
        key: "facilityName",
        ellipsis: true,
    },
    {
        title: "Trạng thái",
        dataIndex: "facilityStatus",
        key: "facilityStatus",
        ellipsis: true,
        width: "300px",
    },
    {
        title: "Hành động",
        key: "action",
        align: "center",
        width: "200px",
    },
];
</script>