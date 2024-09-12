<template>
    <a-modal :open="open" :title="modalTitle" @cancel="handleClose" @ok="handleClose" :width="1000" class="h-96" aria-hidden="false">
        <div>
            <a-input :value="facilityDetail.facilityName" readonly aria-readonly="false" :placeholder="'Tên cở sở'" />
        </div>
        <div class="mt-5">
            <h2 class="text-lg font-semibold">Danh sách cơ sở con</h2>
            <tutor-table
                wrapperClassName="min-h-[410px]"
                :columns="columnsFacilityChild"
                :dataSource="facilityChilds || []"
                :totalPages="totalPages || 0"
                :paginationParams="paginationParams || {}"
                :className="'w-full'"
                @update:pagination-params="$emit('update:paginationParams', $event)"
            >
                <template #bodyCell="{ column, record }">
                    <div v-if="column.key === 'action'" class="space-x-2 text-center">
                        <a-tooltip title="Chỉnh sửa cơ sở con" color="#FFC26E">
                            <a-button type="primary" size="large" :icon="h(EyeOutlined)"
                                @click="handleOpenModalChild(record)" />
                        </a-tooltip>
                    </div>
                    <div v-else-if="column.key === 'facilityChildStatus'">
                        <a-tag :color="record.facilityChildStatus === 0 ? 'green' : 'red'">
                            {{ record.facilityChildStatus === 0 ? 'Đang hoạt động' : 'Ngừng hoạt động' }}
                        </a-tag>
                    </div>
                </template>
            </tutor-table>
            <detail-facility-child-modal :open="openModalChild" @handle-close="handleCloseModalChild"
                :data-detail="facilityChildDetail" />
        </div>
    </a-modal>
</template>

<script lang="ts" setup>
import { computed, h, ref } from 'vue';
import TutorTable from '@/components/ui/TutorTable/TutorTable.vue';
import { ColumnType } from 'ant-design-vue/es/table';
import DetailFacilityChildModal from './facility-child/DetailFacilityChildModal.vue';
import { FacilityChildResponse } from '@/services/api/facility-child.api';
import { EyeOutlined } from '@ant-design/icons-vue';


defineProps({
    open: Boolean,
    facilityDetail: Object as () => any | {},
    facilityChilds: Array<FacilityChildResponse>,
    paginationParams: Object,
    totalPages: Number
});
const emit = defineEmits(['handleClose', 'update:paginationParams'])

const openModalChild = ref(false);
const facilityChildDetail = ref<FacilityChildResponse | Object>({});

const handleOpenModalChild = (record: FacilityChildResponse) => {
    openModalChild.value = true;
    facilityChildDetail.value = record;
}

const handleCloseModalChild = () => {
    openModalChild.value = false;
    facilityChildDetail.value = {}
}

const handleClose = () => {
    emit('handleClose')
}

const modalTitle = computed(() => "Chi tiết cơ sở");

const columnsFacilityChild: ColumnType[] = [
    {
        title: "STT",
        dataIndex: "orderNumber",
        key: "index",
        ellipsis: true,
    },
    {
        title: "Mã cở sở",
        dataIndex: "facilityChildCode",
        key: "facilityCode",
        ellipsis: true,
    },
    {
        title: "Tên cở sở",
        dataIndex: "facilityChildName",
        key: "facilityName",
        ellipsis: true,
    },
    {
        title: "Trạng thái",
        dataIndex: "facilityChildStatus",
        key: "facilityChildStatus",
        ellipsis: true,
        width: "120px",
    },
    {
        title: "Hành động",
        key: "action",
        align: "center",
        width: "150px",
    },
];

</script>