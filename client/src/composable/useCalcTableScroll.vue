<script setup>
import { debounce } from "lodash";
import { onBeforeUnmount, onMounted, ref } from "vue";

function useCalcTableScroll({ className }) {
  const scroll = ref({ y: "100%", x: "100%" });

  const calcTableScroll = (targetTableEle) => {
    return () => {
      const tableContentHeight = targetTableEle.offsetHeight;
      const tableContentWidth = targetTableEle.offsetWidth;
      const tableHeader =
        targetTableEle.querySelector < HTMLElement > ".ant-table-thead";
      const tableFooter =
        targetTableEle.querySelector < HTMLElement > ".ant-table-footer";

      scroll.value = {
        y:
          tableContentHeight -
          (tableHeader?.offsetHeight || 0) -
          (tableFooter?.offsetHeight || 0),
        x: tableContentWidth,
      };
    };
  };

  onMounted(() => {
    const targetTableEle =
      document.querySelector < HTMLElement > stringToSelector(className);

    if (!targetTableEle) return;

    const resizeObserver = new ResizeObserver(
      debounce(calcTableScroll(targetTableEle), 100)
    );

    resizeObserver.observe(targetTableEle);

    onBeforeUnmount(() => {
      resizeObserver.unobserve(targetTableEle);
    });
  });

  return { scroll };
}

function stringToSelector(str) {
  if (!str) return "";

  str = str.replace(/ /g, ".");

  ["[", "]", "(", ")"].forEach((it) => {
    str = str.replace(new RegExp(`\\${it}`, "g"), `\\${it}`);
  });

  return `.${str}`;
}
</script>
