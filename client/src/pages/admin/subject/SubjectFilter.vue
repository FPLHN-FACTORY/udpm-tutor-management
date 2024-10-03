<template>
  <div class="shadow-md p-3 rounded-md m-3">
    <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
      <v-icon name="co-filter" scale="2" />
      <span class="ml-2 text-2xl">Bộ lọc</span>
    </h2>
    <a-form
        layout="vertical"
        class="mb-4 grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-3 p-5"
    >
      <a-form-item
          label="Mã môn học"
          class="col-span-1 md:col-span-2 lg:col-span-1"
      >
        <a-input
            :value="params.subjectCode"
            @input="onChange('subjectCode', $event)"
            placeholder="Mã môn học"
            allowClear
        />
      </a-form-item>
      <a-form-item
          label="Tên môn học"
          class="col-span-1 md:col-span-2 lg:col-span-1"
      >
        <a-input
            :value="params.subjectName"
            @input="onChange('subjectName', $event)"
            placeholder="Tên môn học"
            allowClear
        />
      </a-form-item>
      <a-form-item
          label="Loại môn học"
          class="col-span-1 md:col-span-2 lg:col-span-1"
      >
        <a-select
            :value="params.subjectType"
            @change="onSelectChange('subjectType', $event)"
            placeholder="Loại môn học"
            allowClear
        >
          <a-select-option
              v-for="option in subjectTypeOptions"
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

const subjectTypeOptions = [
  { label: "Truyền thống", value: "TRADITIONAL" },
  { label: "Online", value: "ONLINE" },
  { label: "Blend", value: "BLEND" },
];

type ParamsFilterSubjects = {
  subjectCode: string | null;
  subjectName: string | null;
  subjectType: string | null;
};

const params = ref<ParamsFilterSubjects>({
  subjectCode: null,
  subjectName: null,
  subjectType: null,
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
