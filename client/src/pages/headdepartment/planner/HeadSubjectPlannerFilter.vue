<template>
  <div class="shadow-md p-3 rounded-md m-3">
    <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
      <v-icon name="co-filter" scale="2" />
      <span class="ml-2 text-2xl">Bộ lọc</span>
    </h2>
    <a-form layout="vertical" class="mb-4 grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-2">
      <a-form-item label="Mã nhân viên" class="col-span-1">
        <a-input :value="params.q" @input="onInputChange('q', $event)" placeholder="Nhập mã nhân viên" allowClear />
      </a-form-item>
      <a-form-item label="Tên nhân viên" class="col-span-1">
        <a-input :value="params.namePlanner" @input="onInputChange('namePlanner', $event)"
          placeholder="Nhập tên nhân viên" allowClear />
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from "vue";
import { debounce } from "lodash";
import { defineEmits } from "vue";

type ParamsGetListPlaner = {
  q?: string | null;
  namePlanner?: string | null;
};

// Khởi tạo tham số bộ lọc
const params = ref<ParamsGetListPlaner>({
  q: "",
  namePlanner: "",
});

// Khởi tạo emit sự kiện filter
const emit = defineEmits(["filter"]);

// Hàm debounce để phát sự kiện sau khi có thay đổi
const debouncedEmit = debounce(() => {
  params.value["page"] = 1;
  emit("filter", params.value);
}, 500);

// Xử lý thay đổi đầu vào
function onInputChange(key: keyof ParamsGetListPlaner, event: Event) {
  const target = event.target as HTMLInputElement;
  if (target.value !== undefined) {
    params.value[key] = target.value;
  } else if (typeof event === "string") {
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