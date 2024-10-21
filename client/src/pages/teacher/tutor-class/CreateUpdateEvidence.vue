<template>
  <div>
    <a-modal 
      :open="open" 
      :title="modalTitle" 
      @cancel="handleClose" 
      @ok="handleAddOrUpdate" 
      :ok-text="okText"
      cancel-text="Hủy"
      destroyOnClose 
      centered
    >
      <a-form layout="vertical">
        <template v-for="field in formFields" :key="field.name">
          <a-form-item :label="field.label" :name="field.name" v-bind="validateInfos[field.name]">
            <div style="display: flex; align-items: center;">
              <component
                :is="field.component" 
                v-bind="field.props" 
                v-model:value="modelRef[field.name]" 
                :disabled="disableField(field.name)"
              />
              <a-tooltip title="Tới liên kết tài liệu" color="#FFC26E">
                  <a-button 
                    v-if="isUpdateMode && modelRef[field.name]" 
                    size="large"
                    class="flex items-center justify-center"
                    type="link" 
                    :href="modelRef[field.name]" 
                    target="_blank" 
                    style="margin-left: 2px;"
                    :icon="h(LinkOutlined)"
                />
              </a-tooltip>
            </div>
          </a-form-item>
        </template>

        <div class="flex justify-start items-start">
          <a-upload
          name="file"
          :multiple="false"
          @change="handleFileChange"
          :disabled="!!modelRef.evidenceLink"
          :max-count="1"
        >
          <a-button class="flex justify-between items-center" :disabled="!!modelRef.evidenceLink">
            <UploadOutlined />
            Tải ảnh minh chứng
          </a-button>
        </a-upload> 
        <span style="line-height: 38px;" class="ms-2 me-2" v-if="isUpdateMode && modelRef.driveLink != null" >(1) file đã tải lên</span>
        <a-tooltip title="Tới liên kết tài liệu" color="#FFC26E">
          <a-button 
              size="large"
              class="flex items-center justify-center"
              v-if="isUpdateMode && modelRef.driveLink" 
              type="link" 
              :href="modelRef.driveLink" 
              target="_blank" 
              :icon="h(LinkOutlined)"
            />
        </a-tooltip>
        </div>
      </a-form>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import { ERROR_MESSAGE } from '@/constants/message.constant';
import { AddOrUpdateLectureEvidenceParams } from '@/services/api/teacher/tutor-class.api';
import { useAddOrUpdateLectureEvidence } from '@/services/service/teacher/tutor-class.action';
import { ExclamationCircleOutlined, LinkOutlined, UploadOutlined } from '@ant-design/icons-vue';
import { Form, Modal } from "ant-design-vue";
import { computed, createVNode, h, reactive, ref, watch } from "vue";
import { toast } from 'vue3-toastify';

const props = defineProps({
  open: Boolean,
  evidenceDetail: Object as () => any | null,
  lectureId: Object as () => any,
  isLoadingDetail: Boolean
});

const { mutate: addOrUpdateLectureEvidence } = useAddOrUpdateLectureEvidence();
const emit = defineEmits(["handleClose"]);

interface EvidenceForm {
  evidenceLink: string | null,
  exerciseLink: string | null,
  recordLink: string | null,
  lectureId: string | null,
  driveLink: string | null,
  file: File | null;
}

const modelRef = reactive<EvidenceForm>({
  evidenceLink: null,
  exerciseLink: null,
  recordLink: null,
  lectureId: null,
  file: null,
  driveLink: null
});

const rulesRef = reactive({
  evidenceLink: [
    { 
      validator: (rule, value) => {
        if (!value && !modelRef.file) {
          return Promise.reject("Vui lòng nhập đường dẫn minh chứng hoặc tải ảnh minh chứng");
        } else {
          return Promise.resolve();
        }
      }, 
      trigger: "blur",
      required: true,
    },
    { pattern: /^(http|https):\/\/[^ "]+$/, message: "Đường dẫn chứng cớ không hợp lệ", trigger: "blur" }
  ],
  exerciseLink: [
    { required: true, message: "Vui lòng nhập đường dẫn tài liệu", trigger: "blur" },
    { pattern: /^(http|https):\/\/[^ "]+$/, message: "Đường dẫn tài liệu không hợp lệ", trigger: "blur" }
  ],
  recordLink: [
    { required: true, message: "Vui lòng nhập đường dẫn video buổi dạy", trigger: "blur" },
    { pattern: /^(http|https):\/\/[^ "]+$/, message: "Đường dẫn video không hợp lệ", trigger: "blur" }
  ],
});

const handleFileChange = (info: any) => {
  const file = info.file.originFileObj;
  modelRef.file = file;
  modelRef.evidenceLink = null;
};

const disableField = (fieldName: string) => {
  return fieldName === 'evidenceLink' && !!modelRef.file;
};

const { resetFields, validate, validateInfos } = Form.useForm(modelRef, rulesRef);

const modalTitle = computed(() => (props.evidenceDetail !=null && Object.keys(props.evidenceDetail).length > 0 ? "Cập nhật thông tin tài liệu buổi học" : "Thêm thông tin tài liệu buổi học"));
const okText = computed(() => (props.evidenceDetail !=null && Object.keys(props.evidenceDetail).length > 0 ? "Cập nhật thông tin buổi học" : "Thêm thông tin buổi học"));

const isUpdateMode = computed(() => props.evidenceDetail != null && Object.keys(props.evidenceDetail).length > 0);

watch(
  () => props.evidenceDetail,
  (newVal) => {
    if (newVal) {
      Object.assign(modelRef, {
        evidenceLink: newVal.evidenceLink,
        exerciseLink: newVal.exerciseLink,
        recordLink: newVal.recordLink,
        lectureId: newVal.lectureId,
        driveLink: newVal.driveLink
      });
    } else {
      Object.assign(modelRef, {
        evidenceLink: null,
        exerciseLink: null,
        recordLink: null,
        lectureId: null,
        file: null
      });
    }
  },
  { immediate: true }
);

watch(
  () => props.open,
  (openNew) => {
    if (openNew) {
      Object.assign(modelRef, {
        ...modelRef,
        lectureId: props.lectureId
      });
      resetFields();
    }
  }
);

const handleAddOrUpdate = async () => {
  try {
    await validate();

    Modal.confirm({
      content: 'Bạn chắc chắn muốn thêm tài liệu chứ?',
      icon: createVNode(ExclamationCircleOutlined),
      centered: true,
      async onOk() {
        try {
          const actionParams: AddOrUpdateLectureEvidenceParams = {
            lectureId:  props.lectureId,
            evidenceLink: modelRef.evidenceLink || null,
            exerciseLink: modelRef.exerciseLink || null,
            recordLink: modelRef.recordLink || null,
            file: modelRef.file,
            driveLink: modelRef.driveLink,
          };

          const action =  addOrUpdateLectureEvidence;
          const message = props.evidenceDetail != null && Object.keys(props.evidenceDetail).length > 0 ? "Cập nhật tài liệu thành công!" : "Thêm tài liệu thành công!";

          action(actionParams, {
            onSuccess: () => {
              toast.success(message);
              handleClose(); 
            },
            onError: (error: any) => {
              toast.error(
                error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
              );
            },
          });
        } catch (error: any) {
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
  } catch (error: any) {
    console.error("🚀 ~ handleAddOrUpdate ~ validation error:", error);
  }
};

const formFields = computed(() => [
  {
    label: "Đường dẫn video buổi dạy",
    name: "recordLink",
    component: "a-input",
    props: { placeholder: "Nhập đường dẫn video buổi dạy" },
  },
  {
    label: "Đường dẫn tài liệu",
    name: "exerciseLink",
    component: "a-input",
    props: { placeholder: "Nhập đường dẫn tài liệu" },
  },
  {
    label: "Tài liệu minh chứng",
    name: "evidenceLink",
    component: "a-input",
    props: { placeholder: "Nhập đường dẫn minh chứng" },
  },
]);

const handleClose = () => {
  emit("handleClose");
  resetFields();
};

</script>
