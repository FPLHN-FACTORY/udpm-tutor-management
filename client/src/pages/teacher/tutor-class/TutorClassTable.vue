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
                :columns="columnsTutorClass"
                :data-source="props.dataSource || []"
                :pagination-params="props.paginationParams || {}"
                :total-pages="props.totalPages || 1"
                @update:pagination-params="$emit('update:paginationParams', $event)"
                :loading="props.loading"
            >
                <template #bodyCell="{ column, record }">
                    <div v-if="column.key === 'action'" class="space-x-2 flex items-center justify-center">
                        <a-button type="primary" size="large" class="flex items-center justify-center"
                            :icon="h(EditOutlined)" />
                    </div>
                </template>
            </tutor-table>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { EditOutlined } from '@ant-design/icons-vue';
import { ColumnType } from 'ant-design-vue/es/table';
import { h } from 'vue';
import TutorTable from '@/components/ui/TutorTable/TutorTable.vue';

const props = defineProps({
    dataSource: Array<any>,
    totalPages: Number,
    paginationParams: Object,
    loading: Boolean
})

const emit = defineEmits(['update:paginationParams'])

const columnsTutorClass: ColumnType[] = [
    {
        title: "STT",
        dataIndex: "orderNumber",
        key: "index",
        ellipsis: true,
        width: "100px"
    },
    {
        title: "Mã lớp",
        dataIndex: "classCode",
        key: "classCode",
        ellipsis: true,
    },
    {
        title: "Tên môn học",
        dataIndex: "subjectName",
        key: "subjectName",
        ellipsis: true,
    },
    {
        title: "Sinh viên",
        dataIndex: "studentName",
        key: "studentName",
        ellipsis: true,
    },
    {
        title: "Sĩ số lớp",
        dataIndex: "totalStudent",
        key: "totalStudent",
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