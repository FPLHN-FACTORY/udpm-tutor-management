<template>
  <div class="rounded-md m-3">
    <a-form
        layout="vertical"
        class="mb-4 grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-2"
    >
      <a-form-item
          label="Mã lớp"
          class="col-span-1 md:col-span-2 lg:col-span-1"
      >
        <a-input
            :value="params.query"
            @input="onChange('query', $event)"
            placeholder="Mã lớp tutor"
            allowClear
        />
      </a-form-item>
      <a-form-item
          label="Giảng viên"
          class="col-span-1 md:col-span-2 lg:col-span-1"
      >
        <a-select
            :value="params.teacherId"
            @change="onSelectChange('teacherId', $event)"
            placeholder="Loại môn học"
            allowClear
        >
          <a-select-option
              v-for="option in teacherOption"
              :key="option.value"
              :value="option.value"
          >
            {{ option.label }}
          </a-select-option>
        </a-select>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { debounce } from "lodash";
import { defineEmits, ref, watch } from "vue";
import {FormatCommonOptionsResponse} from "@/services/api/common.api.ts";

type ParamsFilterSubjects = {
  teacherId: string | null;
  query: string | null;
};

const params = ref<ParamsFilterSubjects>({
  teacherId: null,
  query: null,
});

defineProps({
  teacherOption: Array as () => FormatCommonOptionsResponse[],
});

const emit = defineEmits(["filter"]);

const debouncedEmit = debounce(() => {
  emit("filter", params.value);
}, 2000);

function onChange(key: keyof ParamsFilterSubjects, event: Event) {
  if (
      event &&
      event.target &&
      (event.target as HTMLInputElement).value !== undefined
  ) {
    params.value[key] = (event.target as HTMLInputElement).value;
  } else if (event && typeof event === "string") {
    params.value[key] = event;
  }
}

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
