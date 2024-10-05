<template>
    <div class="shadow-md p-3 rounded-md m-3">
        <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
            <v-icon name="co-filter" scale="2" />
            <span class="ml-2 text-2xl">Bộ lọc</span>
        </h2>
        <a-form layout="vertical" class="mb-4 grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-3 p-5">
            <!-- Tên người dùng / Mã -->
            <a-form-item label="Tên người dùng / Mã" class="col-span-1">
                <a-input
                    placeholder="Tên người dùng / Mã"
                    allowClear
                    :value="params.nameOrCode"
                    @input="onChange('nameOrCode', $event)"
                />
            </a-form-item>

            <a-form-item label="Email" class="col-span-1">
                <a-input
                    placeholder="Email"
                    allowClear
                    :value="params.email"
                    @input="onChange('email', $event)"
                />
            </a-form-item>

            <a-form-item label="Chức năng" class="col-span-1">
                <a-select
                    :value="params.statusUserActivity"
                    @change="onSelectChange('statusUserActivity', $event)"
                    placeholder="Loại chức năng"
                    allowClear
                >
                    <a-select-option :value="null">Chọn tất cả</a-select-option>
                    <a-select-option :value="'LOGIN'">Đăng nhập</a-select-option>
                    <a-select-option :value="'LOGOUT'">Đăng xuất</a-select-option>
                    <a-select-option :value="'TOKEN_EXPIRED'">Hết hạn</a-select-option>
                </a-select>
            </a-form-item>

            <a-form-item label="Trạng thái" class="col-span-1">
                <a-select
                    :value="params.status"
                    @change="onSelectChange('status', $event)"
                    placeholder="Chọn trạng thái"
                    allowClear
                >
                    <a-select-option :value="null">Chọn tất cả</a-select-option>
                    <a-select-option :value="'ACTIVE'">Thành công</a-select-option>
                    <a-select-option :value="'INACTIVE'">Thất bại</a-select-option>
                </a-select>
            </a-form-item>

            <a-form-item label="Từ ngày" class="col-span-1">
                <a-date-picker
                    :value="params.fromDate ? dayjs(params.fromDate) : null"
                    @change="(date) => onChange('fromDate', date ? date.valueOf() : null)"
                    placeholder="Chọn ngày bắt đầu"
                    style="width: 100%"
                />
            </a-form-item>

            <a-form-item label="Đến ngày" class="col-span-1">
                <a-date-picker
                    :value="params.toDate ? dayjs(params.toDate) : null"
                    @change="(date) => onChange('toDate', date ? date.valueOf() : null)"
                    placeholder="Chọn ngày kết thúc"
                    style="width: 100%"
                />
            </a-form-item>
        </a-form>
    </div>
</template>

<script setup lang="ts">
import { ParamsGetUserActivityLog } from '@/services/api/operationlog/operationlog.api';
import dayjs from 'dayjs';
import { debounce } from 'lodash';
import { ref, watch } from 'vue';

const emit = defineEmits(["filter"]);

const debouncedEmit = debounce(() => {
    emit("filter", params.value);
}, 2000);

type ParamsFilterUserActivityLog = {
    nameOrCode: string | null;
    email: string | null;
    operation: string | null;
    fromDate: number | null;
    toDate: number | null;
    status: number | null;
    statusUserActivity: string | null;
};


function onChange(key: keyof ParamsGetUserActivityLog, event: any) {
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



const params = ref<ParamsFilterUserActivityLog>({
    nameOrCode: null,
    email: null,
    operation: null,
    fromDate: null,
    toDate: null,
    status: null,
    statusUserActivity: null
});

function onSelectChange(key: keyof ParamsFilterUserActivityLog, value: string | null) {
    params.value[key] = value;
    debouncedEmit(); 
}

watch(
    params,
    () => {
        debouncedEmit(); 
    },
    { deep: true }
);
</script>
