<template>
  <div>
    <a-modal
      :open="open"
      :title="modalTitle"
      @cancel="handleClose"
      destroyOnClose
      centered
      :footer="null"
    >
      <div v-if="props.isLoadingDetail" class="flex justify-center items-center">
        <a-spin />
      </div>
      <div v-else>
        <a-form layout="vertical">
          <template v-for="field in formFields" :key="field.name">
            <a-form-item :label="field.label" :name="field.name" v-bind="validateInfos[field.name]">
              <component :is="field.component" v-model:value="modelRef[field.name]" />
            </a-form-item>
          </template>
        </a-form>
      </div>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import { computed, reactive, watch } from 'vue';
import { Form } from 'ant-design-vue';

interface DepartmentForm {
  departmentCode: string;
  departmentName: string;
}

const props = defineProps({
  open: Boolean,
  departmentDetail: Object as () => any | null,
  isLoadingDetail: Boolean,
});

const emit = defineEmits(['handleClose']);

const modelRef = reactive<DepartmentForm>({
  departmentCode: '',
  departmentName: '',
});

const rulesRef = reactive({
  departmentCode: [
    { required: false, trigger: 'blur' },
  ],
  departmentName: [
    { required: false, trigger: 'blur' },
  ],
});

const { resetFields, validateInfos } = Form.useForm(
  modelRef,
  rulesRef
);

const modalTitle = computed(() => 'Chi tiết bộ môn');

const formFields = computed(() => [
  {
    label: 'Mã bộ môn',
    name: 'departmentCode',
    component: 'a-input',
  },
  {
    label: 'Tên bộ môn',
    name: 'departmentName',
    component: 'a-input',
  },
]);

watch(
  () => props.departmentDetail,
  (newVal) => {
    if (newVal) {
      Object.assign(modelRef, {
        departmentCode: newVal.departmentCode,
        departmentName: newVal.departmentName,
      });
    } else {
      resetFields();
    }
  },
  { immediate: true }
);

const handleClose = () => {
  emit('handleClose');
  resetFields();
};
</script>
