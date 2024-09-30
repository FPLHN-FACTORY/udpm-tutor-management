<template>
  <a-modal
      footer=""
      :open="open"
      :title="userDetail"
      @cancel="handleClose"
      @ok="handleClose"
      :width="1000"
      class="h-96"
  >
    <!-- Loading spinner khi đang tải dữ liệu chi tiết -->
    <div v-if="isLoadingSubject" class="flex justify-center items-center">
      <a-spin />
    </div>
    <!-- Hiển thị bảng phân công nếu không có loading -->
    <div v-else>
      <!-- Phần chọn trưởng môn mới và nút phân công lại -->
      <div v-if="subjects.length > 0" class="flex justify-between items-center min-h-16">
        <a-select
            v-model:value="newHeadSubject"
            :value="newHeadSubject"
            @change="onSelectChange"
            class="min-w-[250px]"
            :loading="staffSubjectOptionsLoading"
            placeholder="Chọn trưởng môn mới"
        >
          <a-select-option
              v-for="item in staffSubjectOptions"
              :key="item.value"
              :value="item.value"
          >
            {{ item.label }}
          </a-select-option>
        </a-select>
        <a-button
            type="primary"
            @click="handleReassignSubject"
            size="large"
            class="m-4"
            :loading="isReassigning"
        >
        Phân công
        </a-button>
      </div>
      <tutor-table
          wrapperClassName="min-h-[410px]"
          :columns="columnsSubject"
          :dataSource="subjects || []"
          :totalPages="totalPages || 0"
          :paginationParams="paginationParams || {}"
          class="w-full"
          @update:pagination-params="$emit('update:paginationParams', $event)"
      >
        <template #bodyCell="{ column, record }">
          <!-- Hiển thị loại môn học với màu sắc khác nhau -->
          <div v-if="column.key === 'subjectType'" class="space-x-2">
            <a-tag :color="getTagColor(record.subjectType)">
              {{ record.subjectType }}
            </a-tag>
          </div>
        </template>
      </tutor-table>
    </div>
  </a-modal>
</template>

<script lang="ts" setup>
import TutorTable from "@/components/ui/TutorTable/TutorTable.vue";
import { ColumnType } from "ant-design-vue/es/table";
import { computed, ref, watch } from "vue";
import { useGetStaffByHeadSubject, useReassignSubjectForAnotherHeadSubject } from "@/services/service/headdepartment/head-subject.action.ts";
import { useAuthStore } from "@/stores/auth.ts";
import { toast } from "vue3-toastify";

// Nhận các props từ component cha
const props = defineProps({
  open: Boolean,
  semesterId: String,
  headSubjectId: String,
  userDetail: String,
  subjects: { type: Array, default: () => [] },
  paginationParams: { type: Object, default: () => ({ page: 1, size: 10 }) },
  totalPages: { type: Number, default: 0 },
  isLoadingSubject: Boolean
});

// Khởi tạo emit để phát sự kiện
const emit = defineEmits(["handleClose", "update:paginationParams"]);
const newHeadSubject = ref<string | null>(null);
const auth = useAuthStore();
const userInfo = computed(() => auth.user);

// Params dùng cho việc lấy danh sách trưởng môn
const params = ref({
  currentFacilityId: userInfo.value?.facilityId,
  currentDepartmentCode: userInfo.value?.departmentCode,
  currentUserId: userInfo.value?.userId,
  currentSemesterId: "",
  headSubjectId: ""
});

// Trạng thái loading cho việc phân công lại môn học
const isReassigning = ref(false);

// Đóng modal
const handleClose = () => {
  newHeadSubject.value = null;
  emit("handleClose");
};

// Fetch dữ liệu trưởng môn
const { data: staffSubjectOptionsData, isLoading: staffSubjectOptionsLoading, refetch } = useGetStaffByHeadSubject(params, {
  refetchOnWindowFocus: false,
  enabled: () => !!props.headSubjectId,
});

// Mutation phân công môn học cho trưởng môn khác
const { mutate: reassignSubjectForAnotherHeadSubject } = useReassignSubjectForAnotherHeadSubject();

// Xử lý phân công môn học cho trưởng môn khác
const handleReassignSubject = async () => {
  try {
    if (!newHeadSubject.value) {
      toast.warning("Vui lòng chọn trưởng môn muốn phân công!");
      return;
    }
    isReassigning.value = true; // Bật trạng thái loading khi phân công
    reassignSubjectForAnotherHeadSubject({
      semesterId: props.semesterId,
      facilityId: userInfo.value?.facilityId,
      currentHeadSubjectId: props.headSubjectId,
      newHeadSubjectId: newHeadSubject.value,
    }, );
    handleClose();
  } catch (error) {
    toast.error(error?.response?.data?.message || "Có lỗi xảy ra");
  }
};

// Danh sách lựa chọn trưởng môn khác
const staffSubjectOptions = computed(() =>
    staffSubjectOptionsData?.value?.data?.map(item => ({
      value: item.code,
      label: item.staffInfo,
    })) || []
);

// Xác định màu sắc của thẻ tag theo loại môn học
const getTagColor = (subjectType: string) => {
  switch (subjectType) {
    case "BLEND":
      return "green";
    case "TRADITIONAL":
      return "red";
    default:
      return "yellow";
  }
};

// Cấu hình cột cho bảng hiển thị môn học
const columnsSubject: ColumnType[] = [
  { title: "STT", dataIndex: "orderNumber", key: "index", ellipsis: true },
  { title: "Mã môn học", dataIndex: "subjectCode", key: "facilityCode", ellipsis: true },
  { title: "Tên môn học", dataIndex: "subjectName", key: "facilityName", ellipsis: true },
  { title: "Loại môn học", dataIndex: "subjectType", key: "subjectType", ellipsis: true, width: "120px" },
];

// Theo dõi sự thay đổi của prop `open` để refetch dữ liệu khi mở modal
watch(
    () => props.open,
    (open) => {
      if (open) {
        params.value.headSubjectId = props.headSubjectId;
        params.value.currentSemesterId = props.semesterId;
        refetch(); // Fetch dữ liệu lại khi mở modal
      }
    },
    { immediate: true }
);
</script>
