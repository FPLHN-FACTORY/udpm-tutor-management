<template>
  <div class="shadow-md p-3 rounded-md m-3">
    <h2 className="p-4 flex items-center text-primary text-3xl font-semibold">
      <v-icon name="co-filter" scale="2" />
      <span className="ml-2 text-2xl">Bộ lọc</span>
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
          @change="onChange('subjectCode', $event)"
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
          @change="onChange('subjectName', $event)"
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
          @change="onChange('subjectType', $event)"
          placeholder="Loại môn học"
          allowClear
        >
          <a-select-option
            v-for="option in subjectTypeOptions"
            :key="option.value"
            :value="option.value"
            :label="option.label"
          />
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
  const target = event.target as HTMLInputElement;
  params.value[key] = target.value;
}

watch(
  params,
  () => {
    debouncedEmit();
  },
  { deep: true }
);
</script>
