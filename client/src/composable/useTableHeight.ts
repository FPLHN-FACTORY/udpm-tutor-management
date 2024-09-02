import { debounce } from 'lodash';
import { ref, onMounted, onUnmounted, watch, Ref } from 'vue';

export function useTableHeight(
  elementRef: Ref<HTMLElement | null>, 
  minHeight: number = 0 // Default to 0 if minHeight is not provided
) {
  const tableHeight = ref<number | undefined>(undefined);

  const resizeTable = debounce(() => {
    const node = elementRef.value;
    if (!node) return;
  
    const { top } = node.getBoundingClientRect();  // Get the top position of the element
    const windowHeight = window.innerHeight;

    // Calculate height so that the element extends to the bottom of the viewport
    const calculatedHeight = windowHeight - top - 48;
    tableHeight.value = Math.max(calculatedHeight, minHeight);
  }, 100, {
    trailing: true
  });

  onMounted(() => {
    resizeTable();
    window.addEventListener('resize', resizeTable);
  });

  onUnmounted(() => {
    window.removeEventListener('resize', resizeTable);
  });

  watch(elementRef, (newVal) => {
    if (newVal) {
      resizeTable();
    }
  });

  return tableHeight;
}
