<template>
  <div class="shadow-md p-3 rounded-md m-3">
    <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
      <v-icon name="co-filter" scale="2" />
      <span class="ml-2 text-2xl">Bộ lọc</span>
    </h2>
    <a-form
      layout="vertical"
      class="mb-4 grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-2"
    >
      <a-form-item label="Tên học kỳ" class="col-span-1">
        <a-input
            :value="params.semesterName"
            @input="onChange('semesterName', $event)"
            placeholder="Tên học kỳ"
            allowClear
        />
      </a-form-item>
      <a-form-item label="Năm học" class="col-span-1 w-full">
        <a-input
            :value="params.semesterYear"
            @input="onChange('semesterYear', $event)"
            placeholder="Năm học"
            type="number"
            min="2000"
            allowClear
        />
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { debounce } from "lodash";
import { defineEmits, ref, watch } from "vue";

type ParamsFilterSemesters = {
  semesterName?: string | null;
  semesterYear?: number | null;
};

const params = ref<ParamsFilterSemesters>({
  semesterName: "",
  semesterYear: null,
});

const emit = defineEmits(["filter"]);

const debouncedEmit = debounce(() => {
  params.value["page"] = 1;
  emit("filter", params.value);
}, 2000);

function onChange(key: keyof ParamsFilterSemesters, event: Event) {
  if (event && event.target) {
    const value = (event.target as HTMLInputElement).value;
    if (key === 'semesterYear') {
      const numberValue = value ? Number(value) : null;
      if (numberValue !== null && numberValue >= 1900) {
        params.value[key] = numberValue;
      } else {
        params.value[key] = null; // Hoặc xử lý theo yêu cầu
      }
    } else {
      params.value[key] = value;
    }
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
