<template>
    <a-modal
        :open="open"
        :title="modalTitle"
        @cancel="handleClose"
        @ok="handleClose"
        class="h-96"
        aria-hidden="false"
    >
        <div v-if="isLoadingDetail" class="flex justify-center items-center">
            <a-spin />
        </div>
        <div v-else>
            <div class="flex items-center">
                <span class="text-nowrap mr-2">Tên cở sở:</span>
                <a-input :value="facilityDetail.facilityName" readonly aria-readonly="false"
                    :placeholder="'Tên cở sở'" />
            </div>
            <div class="flex items-center mt-4">
                <span class="text-nowrap mr-2">Mã cơ sở:</span>
                <a-input :value="facilityDetail.facilityCode" readonly aria-readonly="false"
                    :placeholder="'Tên cở sở'" />
            </div>
        </div>
    </a-modal>
</template>

<script lang="ts" setup>
import { computed } from 'vue';


defineProps({
    open: Boolean,
    facilityDetail: Object as () => any | {},
    totalPages: Number,
    isLoadingDetail: Boolean
});
const emit = defineEmits(['handleClose'])

const handleClose = () => {
    emit('handleClose')
}

const modalTitle = computed(() => "Chi tiết cơ sở");

</script>