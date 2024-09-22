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
      <a-form-item label="Tìm kiếm" class="col-span-1">
        <a-input
            :value="params.q"
            @input="onInputChange('q', $event)"
            placeholder="Nhập mã nhân viên hoặc tên nhân viên"
            allowClear
        />
      </a-form-item>
      <a-form-item label="Học kỳ" class="col-span-1 w-full">
        <a-select
            :value="params.currentSemesterId"
            @change="onSelectChange('currentSemesterId', $event)"
            :loading="semesterOptionsLoading"
        >
          <a-select-option
              v-for="item in semesterOptions"
              :key="item.value"
              :value="item.value"
          >
            {{ item.label }}
          </a-select-option>
        </a-select>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from "vue";
import { debounce } from "lodash";
import { useGetSemesterOptions } from "@/services/service/common.action.ts";
import { defineEmits } from "vue";

type ParamsFilterHeadSubjects = {
  q?: string | null;
  currentSemesterId?: string | null;
};

// Khởi tạo tham số bộ lọc
const params = ref<ParamsFilterHeadSubjects>({
  q: "",
  currentSemesterId: "",
});

// Khởi tạo emit sự kiện filter
const emit = defineEmits(["filter"]);

// Hàm debounce để phát sự kiện sau khi có thay đổi
const debouncedEmit = debounce(() => {
  params.value["page"] = 1;
  emit("filter", params.value);
}, 500);

// Xử lý thay đổi đầu vào
function onInputChange(key: keyof ParamsFilterHeadSubjects, event: Event) {
  const target = event.target as HTMLInputElement;
  if (target.value !== undefined) {
    params.value[key] = target.value;
  } else if (typeof event === "string") {
    params.value[key] = event;
  }
}

// Xử lý thay đổi select
function onSelectChange(key: keyof ParamsFilterHeadSubjects, value: string) {
  params.value[key] = value;
  emit("filter", params.value);
}

// Lấy dữ liệu tùy chọn học kỳ
const { data: semesterOptionsData, isLoading: semesterOptionsLoading } =
    useGetSemesterOptions();

// Dữ liệu tùy chọn học kỳ dưới dạng computed
const semesterOptions = computed(() => {
  return semesterOptionsData?.value?.data?.map((item) => ({
    value: item.id,
    label: item.name,
  })) || [];
});

// Theo dõi sự thay đổi của `semesterOptions` và đặt giá trị mặc định cho `currentSemesterId`
watch(
    semesterOptions,
    (newOptions) => {
      if (newOptions.length > 0 && !params.value.currentSemesterId) {
        params.value.currentSemesterId = newOptions[0].value;
        emit("filter", params.value); // Gọi filter sau khi thiết lập mặc định
      }
    },
    { immediate: true }
);

// Theo dõi sự thay đổi của `params` và phát sự kiện sau khi debounce
watch(
    params,
    () => {
      debouncedEmit();
    },
    { deep: true }
);
</script>
