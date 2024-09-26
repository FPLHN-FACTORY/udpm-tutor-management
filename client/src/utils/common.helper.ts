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

export const getDateFormat = (unix: number, showTime: boolean = false) => {
  return dayjs(unix).format(showTime ? "DD/MM/YYYY HH:mm:ss" : "DD/MM/YYYY");
};

export const getFormatted = (status: number): string => {
  return status === 0 ? 'ONLINE' : 'OFFLINE';
};

export const getTagColor = (status: string): string => {
  switch (status) {
    case 'PENDING':
      return 'warning';
    case 'APPROVED':
      return 'success';
    case 'REJECTED':
      return 'error';
    case 'CANCELED':
      return 'default';
    case 'COMPLETED':
      return 'success';
    default:
      return 'default';
  }
};

export const getTagStatus = (status: string): string => {
  switch (status) {
    case 'PENDING':
      return 'Chờ phê duyệt';
    case 'APPROVED':
      return 'Đã phê duyệt';
    case 'REJECTED':
      return 'Đã từ chối';
    case 'CANCELED':
      return 'Đã hủy';
    case 'COMPLETED':
      return 'Đã hoàn thành';
    default:
      return 'default';
  }
};

export const formatBlockName = (blockName: string): string => {
  return blockName.replace(/^BLOCK_/, 'Block ');
};


export const filterOption = (input: string, option: any) => {
  return option.value.toLowerCase().includes(input.toLowerCase());
};
