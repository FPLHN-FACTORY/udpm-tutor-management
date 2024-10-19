import { ref, computed, nextTick } from "vue";
import { throttle } from "lodash";

export function useResizableSidebar() {
  const collapsed = ref<boolean>(false);

  const sidebarWidth = ref<number>(250);

  const resizing = ref<boolean>(false);

  const logoSize = computed(() => {
    const minSize = 40;
    const maxSize = 80;
    return Math.max(minSize, Math.min(maxSize, sidebarWidth.value * 0.3));
  });

  const startResizing = (event: MouseEvent) => {
    event.preventDefault();
    resizing.value = true;
    document.addEventListener("mousemove", throttledResizeSidebar);
    document.addEventListener("mouseup", stopResizing);
  };

  const throttledResizeSidebar = throttle((event: MouseEvent) => {
    if (!resizing.value) {
      return;
    }
    const newWidth = Math.min(Math.max(event.clientX, 200), 400);
    sidebarWidth.value = newWidth;
    nextTick(() => {
      console.log("Next tick: Sidebar width updated", sidebarWidth.value);
    });
  }, 50);

  const stopResizing = () => {
    if (!resizing.value) {
      return;
    }
    resizing.value = false;
    document.removeEventListener("mousemove", throttledResizeSidebar);
    document.removeEventListener("mouseup", stopResizing);
  };

  return {
    collapsed,
    sidebarWidth,
    logoSize,
    startResizing,
    stopResizing,
  };
}
