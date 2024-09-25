<template>
  <div class="shadow-md p-3 rounded-md">
    <h2 class="flex items-center text-primary text-3xl font-semibold p-2">
      <span class="text-xl">Tìm Kiếm Chuyên Ngành Theo Cơ Sở</span>
    </h2>
    <a-form layout="vertical" class="grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-2 p-2">
      <a-form-item class="col-span-1 md:col-span-2 lg:col-span-1 mb-0">
        <a-input
          :value="params.majorCodeOrName"
          @input="onChange('majorCodeOrName', $event)"
          placeholder="Tìm Kiếm Chuyên Ngành hoặc mã chuyên ngành"
          allowClear
        />
      </a-form-item>
      <a-form-item class="col-span-1 md:col-span-2 lg:col-span-1 mb-0">
        <a-input
          :value="params.staffCodeOrName"
          @input="onChange('staffCodeOrName', $event)"
          placeholder="Tìm kiếm chuyên ngành theo mã nhân viên hoặc tên nhân viên"
          allowClear
        />
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { debounce } from 'lodash';
import { defineEmits, ref, watch } from 'vue';

const emit = defineEmits(['filter']);

type ParamsFilterMajors = {
  majorCodeOrName: string | null;
  staffCodeOrName: string | null;
  departmentFacilityId: string | null;
};

const params = ref<ParamsFilterMajors>({
  majorCodeOrName: null,
  staffCodeOrName: null,
  departmentFacilityId: null,
});

const debouncedEmit = debounce(() => {
  emit('filter', params.value);
}, 2000);

function onChange(key: keyof ParamsFilterMajors, event: Event) {
  if (event && event.target && (event.target as HTMLInputElement).value !== undefined) {
    params.value[key] = (event.target as HTMLInputElement).value;
  } else if (event && typeof event === 'string') {
    params.value[key] = event;
  }
}

watch(params, () => {
  debouncedEmit();
}, { deep: true });
</script>
