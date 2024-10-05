<template>
  <a-descriptions title="" class="p-4">
    <a-descriptions-item label="Trạng thái"><a-tag :color="getTagColor(plan.status)">{{ getTagStatus(plan.status) }}</a-tag></a-descriptions-item>
    <a-descriptions-item label="Số môn tutor">{{ plan.numberSubjects }}</a-descriptions-item>
    <a-descriptions-item label="Số lớp tutor">{{ plan.numberClasses }}</a-descriptions-item>
    <a-descriptions-item label="Số sinh viên tham gia">0</a-descriptions-item>
    <a-descriptions-item label="Số giảng viên tham gia">0</a-descriptions-item>
    <a-descriptions-item label="Trưởng môn đã thêm môn">0</a-descriptions-item>
    <a-descriptions-item label="Trưởng môn chưa thêm môn">0</a-descriptions-item>
    <a-descriptions-item label="Ngày bắt đầu">{{ getDateFormat(plan?.startTime, false) }}</a-descriptions-item>
    <a-descriptions-item label="Ngày kết thúc">{{ getDateFormat(plan?.endTime, false) }}</a-descriptions-item>
    <a-descriptions-item label="Đánh giá">0</a-descriptions-item>
  </a-descriptions>
</template>

<script lang="ts" setup>
import {getDateFormat, getTagColor, getTagStatus} from "@/utils/common.helper.ts";

defineProps({
  plan: Object as () => {} | null,
});
</script>
