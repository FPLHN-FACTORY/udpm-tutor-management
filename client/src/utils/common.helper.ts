import dayjs from "dayjs";

export function sortObjectKeys(obj: Record<string, any>) {
  if (!obj) return obj;

  return sortAlphaText(Object.keys(obj)).reduce((acc, key) => {
    // @ts-ignore
    acc[key] = obj[key];

    return acc;
  }, {});
}

export function sortAlphaText(arr: string[], type?: "asc" | "desc") {
  if (!arr) return arr;

  return arr.sort((a, b) => {
    return a.localeCompare(b) * (type === "asc" ? 1 : -1);
  });
}

export const getDateFormat = (unix: number, showTime: boolean) => {
  return dayjs(unix).format(showTime ? "DD/MM/YYYY HH:mm:ss" : "DD/MM/YYYY");
};

export const filterOption = (input: string, option: any) => {
  return option.value.toLowerCase().includes(input.toLowerCase());
};
