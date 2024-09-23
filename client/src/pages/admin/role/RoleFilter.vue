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
      <a-form-item label="Mã chức vụ" class="col-span-1">
        <a-input
          :value="params.roleName"
          @input="onChange('roleName', $event)"
          placeholder="Mã chức vụ"
          allowClear
        />
      </a-form-item>
      <a-form-item label="Cơ sở" class="col-span-1">
        <a-select
          :value="params.idFacility"
          @change="onSelectChange('idFacility', $event)"
          placeholder="Cơ sở"
          allowClear
        >
          <a-select-option
            v-for="item in facilityOptions"
            :key="item.value"
            :value="item.value"
            :loading="facilityOptionsLoading"
          >
            {{ item.label }}
          </a-select-option>
        </a-select>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { ParamsGetRoles } from "@/services/api/admin/role.api";
import { useGetFacilityOptions } from "@/services/service/common.action";
import { debounce } from "lodash";
import { computed, defineEmits, ref, watch } from "vue";

type ParamsFilterRoles = {
  roleName: string;
  idFacility: string | null;
};

const params = ref<ParamsFilterRoles>({
  roleName: "",
  idFacility: null,
});

const emit = defineEmits(["filter"]);

const debouncedEmit = debounce(() => {
  emit("filter", params.value);
}, 2000);

function onChange(key: keyof ParamsGetRoles, event: Event) {
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

function onSelectChange(key: keyof ParamsGetRoles, value: string) {
  params.value[key] = value;
  emit("filter", params.value);
}

const { data: facilityOptionsData, isLoading: facilityOptionsLoading } =
  useGetFacilityOptions();

const facilityOptions = computed(() => {
  return facilityOptionsData?.value?.data?.map((item) => ({
    value: item.id,
    label: item.name,
  }));
});

watch(
  params,
  () => {
    debouncedEmit();
  },
  { deep: true }
);
</script>
