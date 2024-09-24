<template>
  <a-modal
      footer=""
      :open="open"
      title="Phân công môn học"
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
            <a-tag :color="subjectTypeColor(record.subjectType)">
              {{ record.subjectType }}
            </a-tag>
          </div>
          <!-- Hiển thị checkbox cho trạng thái phân công -->
          <div v-if="column.key === 'isAssigned'" class="space-x-2 text-center">
            <a-checkbox
                :key="record.isAssigned"
                :checked="record.isAssigned === 1"
                :disabled="isAssigning"
            style="line-height: 32px"
            @change="(e) => handleAssignSubject(record.id, e.target.checked)"
            ></a-checkbox>
          </div>
        </template>
      </tutor-table>
    </div>
  </a-modal>
</template>

<script lang="ts" setup>
import TutorTable from "@/components/ui/TutorTable/TutorTable.vue";
import { ColumnType } from "ant-design-vue/es/table";
import { computed, ref } from "vue";
import { useAssignSubjectForHeadSubject, useUnAssignSubjectForHeadSubject } from "@/services/service/headdepartment/head-subject.action.ts";
import { useAuthStore } from "@/stores/auth.ts";
import { toast } from "vue3-toastify";

// Nhận các props từ component cha
const props = defineProps({
  open: Boolean,
  semesterId: String,
  headSubjectId: String,
  subjects: { type: Array, default: () => [] },
  paginationParams: { type: Object, default: () => ({ page: 1, size: 10 }) },
  totalPages: { type: Number, default: 0 },
  isLoadingSubject: Boolean
});

// Khởi tạo emit để phát sự kiện
const emit = defineEmits(["handleClose", "update:paginationParams"]);

// Lấy thông tin user từ store auth
const auth = useAuthStore();
const userInfo = computed(() => auth.user);

// Params dùng cho việc assign/unassign môn học
const params = ref({
  currentFacilityId: userInfo.value?.facilityId,
  currentDepartmentCode: userInfo.value?.departmentCode,
  currentUserId: userInfo.value?.userId,
  currentSemesterId: props.semesterId,
});

// State loading cho việc assign/unassign
const isAssigning = ref(false);

// Đóng modal
const handleClose = () => emit("handleClose");

// Các hàm call API gán và bỏ gán môn học cho trưởng môn
const { mutate: assignSubjectForHeadSubject } = useAssignSubjectForHeadSubject();
const { mutate: unAssignSubjectForHeadSubject } = useUnAssignSubjectForHeadSubject();

// Xử lý gán hoặc bỏ gán môn học cho trưởng môn
const handleAssignSubject = async (subjectId: string, isChecked: boolean) => {
  const payload = {
    semesterId: props.semesterId,
    facilityId: userInfo.value?.facilityId,
    subjectId: subjectId,
  };

  // Bật trạng thái loading cho quá trình assign/unassign
  isAssigning.value = true;

  try {
    if (isChecked) {
      await assignSubjectForHeadSubject({
        params: payload,
        headSubjectId: props.headSubjectId,
      });
      toast.success("Gán môn học cho trưởng môn thành công");
    } else {
      await unAssignSubjectForHeadSubject({
        params: payload,
        headSubjectId: props.headSubjectId,
      });
      toast.success("Bỏ gán môn học cho trưởng môn thành công");
    }
  } catch (error) {
    toast.error(error?.response?.data?.message || "Có lỗi xảy ra trong quá trình gán môn học");
  } finally {
    // Tắt trạng thái loading sau khi hoàn tất
    isAssigning.value = false;
  }
};

// Hàm xác định màu cho loại môn học
const subjectTypeColor = (type: string) => {
  switch (type) {
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
  { title: "Phân công", dataIndex: "isAssigned", key: "isAssigned", ellipsis: true, width: "120px" },
];
</script>
