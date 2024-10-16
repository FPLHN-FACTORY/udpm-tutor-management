<template>
    <div class="shadow-xl p-3 m-3 rounded-md flex h-full flex-col overflow-auto">
        <div class="flex justify-between items-center min-h-36">
            <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
                <v-icon name="bi-list-ul" scale="2" />
                <span class="ml-2 text-2xl">Danh sách cở sở</span>
            </h2>
            <a-button
                type="primary" size="large"
                class="m-4 flex justify-between items-center"
                @click="$emit('handleOpenModalAdd')"
            >
                Thêm cơ sở
            </a-button>
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
                        <a-button
                            type="primary"
                            size="large"
                            class="flex items-center justify-center"
                            :icon="h(SwapOutlined)"
                            @click="handleChangeStatusFacility(record.id)"
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
import { FacilityResponse } from '@/services/api/superadmin/facility.api';
import { useChangeStatusFacility } from '@/services/service/superadmin/facility.action';
import { EditOutlined, ExclamationCircleOutlined, SwapOutlined } from '@ant-design/icons-vue';
import { Modal } from 'ant-design-vue';
import { ColumnType } from 'ant-design-vue/es/table';
import { createVNode, h } from 'vue';
import { toast } from 'vue3-toastify';

defineProps({
    dataSource: Array<FacilityResponse>,
    totalPages: Number,
    paginationParams: Object,
    loading: Boolean
})

const emit = defineEmits(['handleOpenModalAdd', 'handleOpenModalUpdate', 'update:paginationParams', "syncSuccess"])

const { mutate: update } = useChangeStatusFacility();

const handleChangeStatusFacility = (id: string) => {
    Modal.confirm({
        content: 'Bạn chắc chắn muốn đổi trạng thái chứ?',
        icon: createVNode(ExclamationCircleOutlined),
        centered: true,
        onOk() {
            update(id, {
            onSuccess: () => {
                toast.success("Cập nhật trạng thái thành công!");
            },
            onError: (error: any) => {
                toast.error(
                error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
                )
            },
            })
        },
        cancelText: 'Huỷ',
        onCancel() {
        Modal.destroyAll();
        },
    });
}


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