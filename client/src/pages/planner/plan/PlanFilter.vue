<template>
  <div class="shadow-md p-3 rounded-md m-3 mb-10">
    <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
      <v-icon name="co-filter" scale="2" />
      <span class="ml-2 text-2xl">Bộ lọc</span>
    </h2>
    <a-form
        layout="vertical"
        class="mb-4 grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-2 p-5"
    >
      <a-form-item
          label="Học kỳ"
          class="col-span-1 md:col-span-1 lg:col-span-1"
      >
        <a-select
            :value="params.semesterId"
            @change="onSelectChange('semesterId', $event)"
            placeholder="Chọn học kỳ"
            allowClear
        >
          <a-select-option
              v-for="option in props.semesterOptions"
              :key="option.value"
              :value="option.value"
          >
            {{ option.label }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item
          label="Trạng thái"
          class="col-span-1 md:col-span-1 lg:col-span-1"
      >
        <a-select
            :value="params.planStatus"
            @change="onSelectChange('planStatus', $event)"
            placeholder="Chọn trạng thái"
            allowClear
        >
          <a-select-option
              v-for="option in PlanStatusOptions"
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

const props = defineProps({
  semesterOptions: Array as () => any,
  currentSemester: String
});

const PlanStatusOptions = [
  { label: "Đang lên kế hoạch", value: "PLANNING" },
  { label: "Người lập kế hoạch thông qua", value: "PLANNER_APPROVED" },
  { label: "Chủ nhiệm thông qua", value: "HEAD_DEPARTMENT_APPROVED" },
  { label: "Đang thực hiện", value: "IN_PROGRESS" },
  { label: "Đã xong", value: "DONE" },
];

type ParamsFilterSubjects = {
  semesterId?: string | null;
  planStatus?: string | null;
};

const params = ref<ParamsFilterSubjects>({
  semesterId: props.currentSemester,
  planStatus: null,
});

const emit = defineEmits(["filter"]);

const debouncedEmit = debounce(() => {
  emit("filter", params.value);
}, 2000);

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
