<template>
  <div :class="['flex-1', wrapperClassName]">
    <a-table
      :class="className"
      :title="title"
      :columns="columns"
      :data-source="dataSource || []"
      :loading="loading"
      :table-layout="tableLayout || 'auto'"
      :scroll="scroll"
      :size="size || 'small'"
      :sticky="true"
      :show-sorter-tooltip="false"
      @change="handleTableChange"
      :pagination="false"
      v-bind="$attrs"
    >
      <template
        v-for="(column, index) in columns"
        :key="index"
        #bodyCell="{ text, record, column }"
      >
        <slot name="bodyCell" :column="column" :record="record">
          {{ text }}
        </slot>
      </template>
    </a-table>
  </div>
  <div
    v-if="isPagination && dataSource?.length > 0"
    class="mt-3 flex w-full justify-end"
  >
    <a-pagination
      :current="paginationParams.page"
      :total="
        isNaN(totalPages * paginationParams.size)
          ? 0
          : totalPages * paginationParams.size
      "
      :show-size-changer="!isNaN(totalPages * paginationParams.size)"
      :page-size-options="
        isNaN(totalPages * paginationParams.size) ? [] : ['5', '10', '15', '20']
      "
      :default-page-size="paginationParams.size"
      :show-quick-jumper="showSizeChanger"
      :locale="{
        jump_to: 'Đến',
        page: 'Trang',
        prev_page: 'Trang trước',
        next_page: 'Trang sau',
        items_per_page: ' / trang',
      }"
      :show-total="showTotal ? totalFormatter : undefined"
      responsive
      @change="(page, pageSize) => onPaginationChange(page, pageSize)"
    />
  </div>
</template>

<script>
import { Empty, Pagination, Table } from "ant-design-vue";
import { defineComponent, h } from "vue";

export default defineComponent({
  name: "TutorTable",
  components: {
    ATable: Table,
    APagination: Pagination,
  },
  props: {
    title: [String, Function],
    columns: {
      type: Array,
      required: true,
    },
    dataSource: {
      type: Array,
      default: () => [],
    },
    paginationParams: {
      type: Object,
      required: true,
    },
    totalPages: [Number, String],
    tableLayout: {
      type: String,
      default: "auto",
    },
    className: String,
    size: {
      type: String,
      default: "small",
    },
    loading: Boolean,
    showSizeChanger: {
      type: Boolean,
      default: true,
    },
    showTotal: {
      type: Boolean,
      default: false,
    },
    isPagination: {
      type: Boolean,
      default: true,
    },
    scroll: {
      type: Object,
      default: () => ({
        x: "max-content",
      }),
    },
    wrapperClassName: {
      type: String,
      default: "min-h-[310px]",
    },
  },
  emits: ["update:paginationParams", "tableChange"],
  setup(props, { emit }) {
    const handleTableChange = (pagination, filters, sorter) => {
      emit("tableChange", pagination, filters, sorter);
    };

    const onPaginationChange = (page, pageSize) => {
      emit("update:paginationParams", {
        ...props.paginationParams,
        page,
        size: pageSize,
      });
    };

    const totalFormatter = (total, range) => {
      return props.showTotal
        ? `Hiển thị ${range[0]}-${range[1]} trong tổng số ${total} bản ghi`
        : "";
    };

    return {
      handleTableChange,
      onPaginationChange,
      totalFormatter,
    };
  },
});
</script>
