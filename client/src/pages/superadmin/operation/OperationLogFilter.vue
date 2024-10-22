<template>
    <div class="shadow-md p-3 rounded-md m-3">
        <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
            <v-icon name="co-filter" scale="2" />
            <span class="ml-2 text-2xl">Bộ lọc</span>
        </h2>
        <a-form layout="vertical" class="mb-4 grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-3 p-5">
            <!-- Tên người dùng / Mã / email -->
            <a-form-item label="Tên người dùng / Mã / email" class="col-span-1 lg:col-span-2">
                <a-input placeholder="Tên người dùng / Mã / email" allowClear :value="params.emailOrNameOrCode"
                    @input="onChange('emailOrNameOrCode', $event)" />
            </a-form-item>

            <!-- Chức năng -->
            <a-form-item label="Chức năng" class="col-span-1">
                <a-select :value="params.typeFunction" @change="onSelectChange('typeFunction', $event)"
                    placeholder="Loại chức năng" allowClear>
                    <a-select-option :value="null">Chọn tất cả</a-select-option>
                    <a-select-option v-for="option in logFunctionTypeOptions" :key="option.value" :value="option.value">
                        {{ option.label }}
                    </a-select-option>
                </a-select>
            </a-form-item>

            <a-form-item label="Trạng thái" class="col-span-1">
                <a-select :value="params.status" @change="onSelectChange('status', $event)" placeholder="Chọn trạng thái" allowClear>
                    <a-select-option :value="null">Chọn tất cả</a-select-option>
                    <a-select-option :value="'ACTIVE'">Thành công</a-select-option>
                    <a-select-option :value="'INACTIVE'">Thất bại</a-select-option>
                </a-select>
            </a-form-item>

            <a-form-item label="Từ ngày" class="col-span-1">
                <a-date-picker :value="params.fromDate ? dayjs(params.fromDate) : null"
                    @change="(date) => onChange('fromDate', date ? date.valueOf() : null)"
                    placeholder="Chọn ngày bắt đầu" style="width: 100%" />
            </a-form-item>

            <!-- Đến ngày -->
            <a-form-item label="Đến ngày" class="col-span-1">
                <a-date-picker :value="params.toDate ? dayjs(params.toDate) : null"
                    @change="(date) => onChange('toDate', date ? date.valueOf() : null)"
                    placeholder="Chọn ngày kết thúc" style="width: 100%" />
            </a-form-item>
        </a-form>
    </div>
</template>

<script setup lang="ts">
import { ParamsGetOperationLogs } from '@/services/api/superadmin/operationlog.api';
import dayjs from 'dayjs';
import { debounce } from 'lodash';
import { ref, watch } from 'vue';

const emit = defineEmits(["filter"]);

const debouncedEmit = debounce(() => {
    emit("filter", params.value);
}, 2000);


const logFunctionTypeOptions = [
    { label: "Tìm kiếm", value: 0 },
    { label: "Tạo mới", value: 1 },
    { label: "Cập nhật", value: 3 },
    { label: "Xóa", value: 2 },
    { label: "Đồng bộ", value: 6 },
];

type ParamsFilterSubjects = {
    emailOrNameOrCode: string | null;
    typeFunction: number | null;
    fromDate: number | null;
    toDate: number | null;
    status: string | null;
};

function onChange(key: keyof ParamsGetOperationLogs, event: any) {
    if (key === "fromDate") {
        // Đặt thời gian của fromDate là đầu ngày (00:00:00)
        const selectedDate = event ? dayjs(event).startOf('day').valueOf() : null;
        params.value[key] = selectedDate;
    } else if (key === "toDate") {
        // Đặt thời gian của toDate là cuối ngày (23:59:59)
        const selectedDate = event ? dayjs(event).endOf('day').valueOf() : null;
        params.value[key] = selectedDate;
    } else {
        params.value[key] = event;
    }
}

const params = ref<ParamsFilterSubjects>({
    emailOrNameOrCode: null,
    typeFunction: null,
    fromDate: null,
    toDate: null,
    status: null,
});

function onSelectChange(key: keyof ParamsFilterSubjects, value: string) {
    params.value[key] = value;
    emit("filter", params.value);
}

watch(
    params,
    () => {
        debouncedEmit();
    },
    { deep: true }
);

</script>