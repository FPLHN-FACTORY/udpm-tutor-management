<template>
  <div class="shadow-md p-3 rounded-md m-3">
    <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
      <v-icon name="co-filter" scale="2" />
      <span class="ml-2 text-2xl">Bộ lọc</span>
    </h2>
    <a-form
      layout="vertical"
      class="mb-4 grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-2 mt-3"
    >
      <a-form-item label="Mã chức vụ" class="col-span-2">
        <a-input
          :value="params.roleName"
          @input="onChange('roleName', $event)"
          placeholder="Mã chức vụ"
          allowClear
        />
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { ParamsGetRoles } from "@/services/api/admin/role.api";
import { debounce } from "lodash";
import { defineEmits, ref, watch } from "vue";

type ParamsFilterRoles = {
  roleName: string;
};

const params = ref<ParamsFilterRoles>({
  roleName: "",
});

const emit = defineEmits(["filter"]);

const debouncedEmit = debounce(() => {
  emit("filter", params.value);
}, 500);

function onChange(key: keyof ParamsGetRoles, event: Event) {
  if (event?.target && (event.target as HTMLInputElement).value !== undefined) {
    params.value[key] = (event.target as HTMLInputElement).value;
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
