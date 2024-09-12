<template>
    <div>
        <a-modal :open="open" :title="modalTitle" @cancel="handleClose"  @ok="handleClose" class="mt-5" aria-hidden="false">
            <div>
                <a-form>
                    <a-form-item label="Mã cở sở con" class="mt-6">
                        <a-input :value="dataDetail.facilityChildCode" readonly placeholder="" allowClear />
                    </a-form-item>
                    <a-form-item label="Tên cở sở con" class="mt-6">
                        <a-input :value="dataDetail.facilityChildName" readonly placeholder="" allowClear />
                    </a-form-item>
                </a-form>

            </div>
        </a-modal>
    </div>
</template>

<script lang="ts" setup>
import { computed } from 'vue';


defineProps({
    open: Boolean,
    dataDetail: Object as () => any || null
});


const emit = defineEmits(['handleClose'])


const handleClose = () => {
    emit('handleClose')
}

const modalTitle = computed(() => "Chi tiết cở sở con"
);

</script>