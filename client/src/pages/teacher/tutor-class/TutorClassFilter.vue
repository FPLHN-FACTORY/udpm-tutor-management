<template>
    <div class="shadow-md p-3 rounded-md m-3">
        <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
            <v-icon name="co-filter" scale="2" />
            <span class="ml-2 text-2xl">Bộ lọc</span>
        </h2>
        <a-form layout="vertical" class="grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-2 p-5">
            <a-form-item label="Mã lớp" class="col-span-1">
                <a-input :value="params.classCode" @input="onChange('classCode', $event)" placeholder="Mã lớp:"
                    allowClear />
            </a-form-item>
            <a-form-item label="Môn học" class="col-span-1 md:col-span-2 lg:col-span-1">
                <a-select :value="params.subjectId" placeholder="Loại môn học" allowClear
                    @change="onSelectChange('subjectId', $event)">
                    <a-select-option v-for="option in subjectOptions" :key="option.value" :value="option.value">
                        {{ option.label }}
                    </a-select-option>
                </a-select>
            </a-form-item>
        </a-form>
    </div>
</template>

<script lang="ts" setup>
import { useGetSubjectOptions } from '@/services/service/common.action';
import { debounce } from 'lodash';
import { computed, ref, watch } from 'vue';

type ParamsFilterTutorClass = {
  classCode: string | null,
  subjectId: string | null
}

const { data: subjectOptionsData } = useGetSubjectOptions();

const subjectOptions = computed(() => {
  return subjectOptionsData?.value?.data?.map((item) => ({
    value: item.id,
    label: item.name,
  })) || [];
});

const params = ref<ParamsFilterTutorClass>({
  classCode: '',
  subjectId: ''
})


const emit = defineEmits(['filter'])

function onChange(key: keyof ParamsFilterTutorClass, event: Event) {
  if (
      event &&
      event.target &&
      (event.target as HTMLInputElement).value !== undefined
  ) {
    params.value[key] = (event.target as HTMLInputElement).value
  } else if (event && typeof event === 'string') {
    params.value[key] = event
  }
}


function onSelectChange(key: keyof ParamsFilterTutorClass, value: string) {
  params.value[key] = value;
  emit('filter', params.value)
}

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
</script>