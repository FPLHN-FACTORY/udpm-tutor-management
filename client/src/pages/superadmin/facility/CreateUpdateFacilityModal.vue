<template>
  <a-modal
    :open="open"
    :title="modalTitle" 
    @cancel="handleClose"
    @ok="handleAddOrUpdate"
    destroyOnClose
    centered
    :ok-text="okText"
  >
    <div v-if="props.isLoadingDetail" class="flex justify-center items-center">
      <a-spin />
    </div>
    <div v-else>
      <a-form layout="vertical">
        <template v-for="field in formFields">
          <a-form-item
            v-if="!field.isHide"
            :label="field.label"
            :name="field.name"
            v-bind="validateInfos[field.name]"
          >
            <component
              :is="field.component"
              v-model:value="modelRef[field.name]"
            />
          </a-form-item>
        </template>
      </a-form>
    </div>
  </a-modal>
</template>

<script lang="ts" setup>
import { ERROR_MESSAGE } from '@/constants/message.constant';
import { useCreateFacility, useUpdateFacility } from '@/services/service/superadmin/facility.action';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import { Form, Modal } from 'ant-design-vue';
import { computed, createVNode, reactive, watch } from 'vue';
import { toast } from 'vue3-toastify';

interface FacilityForm {
  facilityName: string
  facilityCode: string
}

const props = defineProps({
  open: Boolean,
  facilityDetail: Object as () => any | {},
  totalPages: Number,
  isLoadingDetail: Boolean
});
const emit = defineEmits(['handleClose'])

const modelRef = reactive<FacilityForm>({
  facilityName: props.facilityDetail?.facilityName || '',
  facilityCode: props.facilityDetail?.facilityCode || ''
});

const rulesRef = reactive({
  facilityName: [
    { required: true, message: "Vui lòng nhập tên cơ sở", trigger: "blur" },
  ],
});

const { resetFields, validate, validateInfos } = Form.useForm(
  modelRef,
  rulesRef
);

const { mutate: createFacility } = useCreateFacility();
const { mutate: updateFacility } = useUpdateFacility();

const formFields = computed(() => [
  {
    label: "Tên cở sở",
    name: "facilityName",
    component: "a-input",
    isHide: false
  },
  {
    label: "Mã cơ sở",
    name: "facilityCode",
    component: "a-input",
    isHide: props.facilityDetail === null //Nếu k có facility detail thì sẽ ẩn đi
  }
]);

const handleClose = () => {
  emit('handleClose')
}

const modalTitle = computed(() =>
  props.facilityDetail ? "Cập nhật cơ sở" : "Thêm cơ sở"
);

const okText = computed(() =>
  props.facilityDetail ? "Cập nhật cơ sở" : "Thêm cơ sở"
);

watch(
  () => props.facilityDetail,
  (newVal) => {
    if (newVal) {
      Object.assign(modelRef, {
        facilityName: newVal.facilityName,
        facilityCode: newVal.facilityCode,
      });
    } else {
      resetFields();
    }
  },
  { immediate: true }
);

const handleAddOrUpdate = () => {
  Modal.confirm({
    content: 'Bạn chắc chắn muốn thêm chứ',
    icon: createVNode(ExclamationCircleOutlined),
    centered: true,
    async onOk() {
      try {
        await validate(); // Kiểm tra tính hợp lệ

        const payload = {
          ...modelRef
        };

        // Tạo biến để giữ thông tin về hành động (cập nhật hay tạo mới)
        const actionParams = props.facilityDetail
          ? {
            facilityId: props.facilityDetail.id,
            params: payload,
          }
          : payload;

        // Gọi hàm phù hợp dựa vào facilityDetail
        const action = props.facilityDetail ? updateFacility : createFacility;
        const message = props.facilityDetail ? "Cập nhật cơ sở thành công!" : "Tạo cơ sở thành công!";

        action(actionParams, {
          onSuccess: () => {
            toast.success(message);
            handleClose();
          },
          onError: (error: any) => {
            toast.error(
              error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
            )
          },
        })
      } catch (error: any) {
        console.error("🚀 ~ handleAddOrUpdate ~ error:", error);
        toast.error(
          error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
        );
      }
    },
    cancelText: 'Huỷ',
    onCancel() {
      Modal.destroyAll();
    },
  });
}

</script>