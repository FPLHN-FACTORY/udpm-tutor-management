import { debounce } from "lodash";
import { onBeforeUnmount, onMounted, ref, watch } from "vue";

interface IProps {
  className: string;
}

type IScroll = {
  y: number | string;
  x: number | string;
};

export function useCalcTableScroll({ className }: IProps) {
  const scroll = ref<IScroll>({ y: "100%", x: "100%" });

  const calcTableScroll = (targetTableEle: HTMLElement) => {
    const tableContentHeight = targetTableEle.offsetHeight;
    const tableContentWidth = targetTableEle.offsetWidth;
    const tableHeader =
      targetTableEle.querySelector<HTMLElement>(".ant-table-thead");
    const tableFooter =
      targetTableEle.querySelector<HTMLElement>(".ant-table-footer");

    scroll.value = {
      y:
        tableContentHeight -
        (tableHeader?.offsetHeight || 0) -
        (tableFooter?.offsetHeight || 0),
      x: tableContentWidth,
    };
  };

  onMounted(() => {
    if (!className) return;

    const targetTableEle = document.querySelector<HTMLElement>(
      stringToSelector(className)
    );

    if (!targetTableEle) return;

    const debouncedCalcScroll = debounce(
      () => calcTableScroll(targetTableEle),
      100
    );

    const resizeObserver = new ResizeObserver(debouncedCalcScroll);

    resizeObserver.observe(targetTableEle);

    onBeforeUnmount(() => {
      resizeObserver.unobserve(targetTableEle);
    });
  });

  watch(
    () => className,
    () => {
      if (!className) return;

      const targetTableEle = document.querySelector<HTMLElement>(
        stringToSelector(className)
      );
      if (targetTableEle) {
        calcTableScroll(targetTableEle);
      }
    }
  );

  return { scroll };
}

function stringToSelector(str: string) {
  if (!str) return "";

  str = str.replace(/ /g, ".");

  ["[", "]", "(", ")"].forEach((it) => {
    str = str.replace(new RegExp(`\\${it}`, "g"), `\\${it}`);
  });

  return `.${str}`;
}
