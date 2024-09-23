<template>
    <div class="shadow-xl p-3 m-3 rounded-md flex h-full flex-col overflow-auto">
        <div class="flex justify-between items-center min-h-36">
            <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
                <v-icon name="bi-list-ul" scale="2" />
                <span class="ml-2 text-2xl">Danh sách cở sở</span>
            </h2>
        </div>
        <div class="flex h-0 flex-1 flex-col">
            <tutor-table
                wrapperClassName="min-h-[410px]"
                :columns="columnsFacility" :data-source="dataSource"
                :pagination-params="paginationParams || {}"
                :total-pages="totalPages || 0"
                @update:pagination-params="$emit('update:paginationParams', $event)"
            >
                <template #bodyCell="{ column, record }">
                    <div v-if="column.key === 'action'" class="space-x-2 flex items-center justify-center">
                        <a-tooltip title="Chỉnh sửa cơ sở" color="#FFC26E">
                            <a-button type="primary" size="large" class="flex items-center justify-center" :icon="h(EditOutlined)"
                                @click="$emit('handleOpenModalUpdate', record)" />
                        </a-tooltip>
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
import { FacilityResponse } from '@/services/api/admin/facility.api';
import { EditOutlined } from '@ant-design/icons-vue';
import { ColumnType } from 'ant-design-vue/es/table';
import { h } from 'vue';

defineProps({
    dataSource: Array<FacilityResponse>,
    totalPages: Number,
    paginationParams: Object,
})

defineEmits(['handleOpenModalAdd', 'handleOpenModalUpdate', 'update:paginationParams'])



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