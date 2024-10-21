<template>
  <a-descriptions title="" class="p-4">
    <a-descriptions-item label="Mã lớp">{{ tutorClassDetailData.classCode }}</a-descriptions-item>
    <a-descriptions-item label="Môn học">{{ tutorClassDetailData.subjectName }}</a-descriptions-item>
    <a-descriptions-item label="Trợ giảng">{{ tutorClassDetailData.student }}</a-descriptions-item>
    <a-descriptions-item label="Hình thức">
      <a-tag :color="getTagFormat(tutorClassDetailData.format)"> {{ tutorClassDetailData.format }}</a-tag>
    </a-descriptions-item>
    <a-descriptions-item label="Sĩ số">{{ tutorClassDetailData.totalStudent? tutorClassDetailData.totalStudent : 0 }}</a-descriptions-item>
    <a-descriptions-item label="Số SV bỏ học"></a-descriptions-item>
    <a-descriptions-item label="Tiến trình"></a-descriptions-item>
  </a-descriptions>
</template>

<script lang="ts" setup>
import {getTagFormat} from "@/utils/common.helper.ts";

defineProps({
  tutorClassDetailData: Object as () => any | null,
});
</script>