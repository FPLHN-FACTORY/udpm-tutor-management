<template>
  <div v-if="plan">
    <a-descriptions title="" class="p-4">
      <a-descriptions-item label="Trạng thái">
        <a-tag :color="getTagColor(plan.status)">{{ getTagStatus(plan.status) }}</a-tag>
      </a-descriptions-item>
      <a-descriptions-item label="Số môn tutor">{{ plan.numberSubjects }}</a-descriptions-item>
      <a-descriptions-item label="Số lớp tutor">{{ plan.numberClasses }}</a-descriptions-item>
      <a-descriptions-item label="Số sinh viên tham gia">{{ plan.numberStudents }}</a-descriptions-item>
      <a-descriptions-item label="Số giảng viên tham gia">{{ plan.numberTeachers }}</a-descriptions-item>
      <a-descriptions-item label="Trưởng môn đã thêm môn">{{ plan.addedSubjects }}</a-descriptions-item>
      <a-descriptions-item label="Trưởng môn chưa thêm môn">{{ plan.notAddedSubjects }}</a-descriptions-item>
      <a-descriptions-item label="Ngày bắt đầu">{{ getDateFormat(plan.startTime, false) }}</a-descriptions-item>
      <a-descriptions-item label="Ngày kết thúc">{{ getDateFormat(plan.endTime, false) }}</a-descriptions-item>
      <a-descriptions-item label="Đánh giá">{{ plan.rating }}</a-descriptions-item>
    </a-descriptions>
  </div>
  <div v-else>
    <p>Không có thông tin kế hoạch.</p>
  </div>
</template>

<script lang="ts" setup>
import {getDateFormat, getTagColor, getTagStatus} from "@/utils/common.helper.ts";
import {PlanInfoResponse} from "@/services/api/planner/plan.api.ts";

defineProps({
  plan: Object as () => PlanInfoResponse,
});
</script>
