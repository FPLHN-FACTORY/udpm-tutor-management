<template>
    <div class="shadow-md p-3 rounded-md m-3">
        <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
            <v-icon name="co-filter" scale="2" />
            <span class="ml-2 text-2xl">Bộ lọc</span>
        </h2>
        <a-form layout="vertical" class="grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-2 p-5">
            <a-form-item label="Cơ sở" class="col-span-1 md:col-span-2 lg:col-span-1" @input="onChange('name', $event)"
                :value="params.name">
                <a-input placeholder="Tìm kiếm với từ khoá(Mã cơ sở / Tên sở sở)" allowClear />
            </a-form-item>
            <a-form-item label="Trạng thái" class="col-span-1 md:col-span-2 lg:col-span-1">
                <a-select :value="params.status" placeholder="Loại môn học" allowClear
                    @change="onSelectChange('status', $event)">
                    <a-select-option v-for="option in facilityStatusOptions" :key="option.value" :value="option.value">
                        {{ option.label }}
                    </a-select-option>
                </a-select>
            </a-form-item>
        </a-form>
    </div>

</template>

<script lang="ts" setup>
import { debounce } from 'lodash';
import { ref, watch } from 'vue';


type ParamsFilterFacilities = {
    name: string | null,
    status: string | null
}

const facilityStatusOptions = [
    { label: "Chọn trạng thái", value: "" },
    { label: "Đang hoạt động", value: '0' },
    { label: "Ngừng hoạt động", value: '1' }
]

const params = ref<ParamsFilterFacilities>({
    name: '',
    status: ''
})


const emit = defineEmits(['filter'])

function onChange(key: keyof ParamsFilterFacilities, event: Event) {
    if (
        event &&
        event.target &&
        (event.target as HTMLInputElement).value !== undefined
    ) {
        params.value[key] = (event.target as HTMLInputElement).value
    } else if (event && typeof event === 'string') {
        params.value[key] = event
    }
}


function onSelectChange(key: keyof ParamsFilterFacilities, value: string) {
    params.value[key] = value;
    emit('filter', params.value)
}

const debouncedEmit = debounce(() => {
    emit('filter', params.value)
}, 2000)

watch(
    params,
    () => {
        debouncedEmit();
    },
    { deep: true }
);
</script>