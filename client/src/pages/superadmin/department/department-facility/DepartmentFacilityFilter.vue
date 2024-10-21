<template>
    <div class="shadow-md p-3 rounded-md">
      <h2 class="flex items-center text-primary text-3xl font-semibold p-2">
        <span class="text-xl">Tìm Kiếm</span>
      </h2>
      <a-form layout="vertical" class="grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-2 p-2">
        <a-form-item class="col-span-1 md:col-span-2 lg:col-span-1 mb-0">
          <a-input
            :value="params.facilityName"
            @input="onChange('facilityName', $event)"
            placeholder="Tìm Kiếm theo tên cơ sở"
            allowClear
          />
        </a-form-item>
        <a-form-item class="col-span-1 md:col-span-2 lg:col-span-1 mb-0">
          <a-input
            :value="params.staffCodeOrEmail"
            @input="onChange('staffCodeOrEmail', $event)"
            placeholder="Tìm Kiếm theo mã hoặc email chủ bộ môn"
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
  
  type ParamsFilterDepartmentFacility = {
    facilityName: string | null;
    staffCodeOrEmail?: string | null;
  };
  
  const params = ref<ParamsFilterDepartmentFacility>({
    facilityName: null,
    staffCodeOrEmail: null,
  });
  
  const debouncedEmit = debounce(() => {
    emit('filter', params.value);
  }, 2000);
  
  function onChange(key: keyof ParamsFilterDepartmentFacility, event: Event) {
    if (
      event &&
      event.target &&
      (event.target as HTMLInputElement).value !== undefined
    ) {
      params.value[key] = (event.target as HTMLInputElement).value;
    } else if (event && typeof event === 'string') {
      params.value[key] = event;
    }
  }
  
  watch(
    params,
    () => {
      debouncedEmit();
    },
    { deep: true }
  );
  </script>
  