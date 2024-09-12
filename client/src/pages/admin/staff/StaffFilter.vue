<template>
  <div class="shadow-md p-3 rounded-md m-3">
    <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
      <v-icon name="co-filter" scale="2" />
      <span class="ml-2 text-2xl">Bộ lọc</span>
    </h2>
    <a-form
        layout="vertical"
        class="mb-4 grid grid-cols-1 gap-4 md:grid-cols-1 lg:grid-cols-1 p-5"
    >
      <a-form-item
          label="Tìm kiếm"
          class="col-span-1 md:col-span-1 lg:col-span-1"
      >
        <a-input
            :value="params.searchQuery"
            @input="onChange('searchQuery', $event)"
            placeholder="Tìm kiếm nhân viên (Mã, Tên, Mail Fpt, Mail Fe)"
            allowClear
        />
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">

import {ref, watch} from "vue";
import {debounce} from "lodash";

type ParamsFilterStaffs = {
  searchQuery: string | null;
};

const params = ref<ParamsFilterStaffs>({
  searchQuery: null,
});

const emit = defineEmits(["filter"]);

const debouncedEmit = debounce(() => {
  emit("filter", params.value);
}, 2000);

function onChange(key: keyof ParamsFilterStaffs, event: Event) {
  if (
      event &&
      event.target &&
      (event.target as HTMLInputElement).value !== undefined
  ) {
    params.value[key] = (event.target as HTMLInputElement).value.trim();
  } else if (event && typeof event === "string") {
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
