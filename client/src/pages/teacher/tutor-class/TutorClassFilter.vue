<template>
    <div class="shadow-md p-3 rounded-md m-3">
        <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
            <v-icon name="co-filter" scale="2" />
            <span class="ml-2 text-2xl">Bộ lọc</span>
        </h2>
        <a-form layout="vertical" class="grid grid-cols-1 gap-4 md:grid-cols-3 lg:grid-cols-3 p-5">
          <a-form-item label="Kế hoạch" class="col-span-1 md:col-span-2 lg:col-span-1">
            <a-select
                v-model:value="params.planId"
                allowClear>
              <a-select-option
                  v-for="option in planOptions"
                  :key="option.planId"
                  :value="option.value">
                {{ option.label.replace("BLOCK_","Block") }}
              </a-select-option>
            </a-select>
          </a-form-item>
            <a-form-item label="Mã lớp" class="col-span-1">
                <a-input
                    v-model:value="params.classCode"
                    placeholder="Mã lớp:"
                    allowClear />
            </a-form-item>
            <a-form-item label="Môn học" class="col-span-1 md:col-span-2 lg:col-span-1">
                <a-select
                    v-model:value="params.subjectId"
                    placeholder="Loại môn học"
                    allowClear>
                    <a-select-option
                        v-for="option in subjectOptions"
                        :key="option.value"
                        :value="option.value">
                        {{ option.label }}
                    </a-select-option>
                </a-select>
            </a-form-item>
        </a-form>
    </div>
</template>

<script lang="ts" setup>
import {useGetSubjectOptions} from '@/services/service/common.action';
import { debounce } from 'lodash';
import { computed, ref, watch } from 'vue';

const props = defineProps({
  planOptions: Object as () => any | null,
  planId: String
});

const emit = defineEmits(['filter'])

type ParamsFilterTutorClass = {
  classCode?: string | null,
  subjectId?: string | null,
  planId?: string | null
}

const params = ref<ParamsFilterTutorClass>({
  classCode: '',
  subjectId: '',
  planId: props.planId
})

const { data: subjectOptionsData } = useGetSubjectOptions();

const subjectOptions = computed(() => {
  return subjectOptionsData?.value?.data?.map((item) => ({
    value: item.id,
    label: item.name,
  })) || [];
});

const debouncedEmit = debounce(() => {
  emit('filter', params.value)
}, 2000)

watch(
  params,
  () => {
    debouncedEmit();
  },
  { deep: true }
);

watch(
    () => props.planId,
    (newPlanId) => {
      params.value.planId = newPlanId;
    }
);
</script>