import dayjs from "dayjs";
import { Modal } from "ant-design-vue";
import { ExclamationCircleOutlined } from "@ant-design/icons-vue";
import { createVNode } from "vue";

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

export const getTagColor = (status: string): string => {
  switch (status) {
    case "PLANNING":
      return "warning";
    case "PLANNER_APPROVED":
      return "grey";
    case "HEAD_DEPARTMENT_APPROVED":
      return "blue";
    case "IN_PROGRESS":
      return "processing";
    case "DONE":
      return "success";
    default:
      return "default";
  }
};

export const getTagColorLog = (status: number): string => {
  switch (status) {
    case 0:
      return "warning";
    case 1:
      return "grey";
    case 2:
      return "blue";
    case 3:
      return "processing";
    case 4:
      return "success";
    case 5:
      return "red";
    case 6:
      return "orange";
    case 7:
      return "success";
    case 8:
      return "grey";
    case 9:
      return "processing";
    case 10:
      return "blue";
    case 11:
      return "warning";
    case 12:
      return "grey";
    case 13:
      return "orange";
    case 14:
      return "blue";
    case 15:
      return "processing";
    case 16:
      return "red";
    default:
      return "default";
  }
};

export const getTagStatusLog = (status: number): string => {
  switch (status) {
    case 0:
      return "Tìm kiếm";
    case 1:
      return "Tạo mới";
    case 2:
      return "Xóa";
    case 3:
      return "Cập nhật";
    case 4:
      return "Đăng nhập";
    case 5:
      return "Đăng xuất";
    case 6:
      return "Đồng bộ";
    case 7:
      return "Phê duyệt";
    case 8:
      return "Từ chối";
    case 9:
      return "Thêm số lượng lớp tutor";
    case 10:
      return "Tạo kế hoạch";
    case 11:
      return "Cập nhật kế hoạch";
    case 12:
      return "Thêm giảng viên";
    case 13:
      return "Thêm lớp tutor";
    case 14:
      return "Xóa lớp tutor";
    case 15:
      return "Thêm sinh viên tutor và phòng";
    case 16:
      return "Tạo sinh viên tutor";
    default:
      return "default";
  }
};

export const getTagStatus = (status: string): string => {
  switch (status) {
    case "PLANNING":
      return "Đang lên kế hoạch";
    case "PLANNER_APPROVED":
      return "Người lập kế hoạch thông qua";
    case "HEAD_DEPARTMENT_APPROVED":
      return "Chủ nhiệm thông qua";
    case "IN_PROGRESS":
      return "Đang thực hiện";
    case "DONE":
      return "Đã xong";
    default:
      return "default";
  }
};

export const getTagFormat = (status: string): string => {
  switch (status) {
    case "ONLINE":
      return "success";
    case "OFFLINE":
      return "warning";
    default:
      return "default";
  }
};

export const getStatusLecture = (status: string): { label: string; color: string } => {
  switch (status) {
    case "NOT_STARTED":
      return { label: "Chưa bắt đầu", color: "gray" };
    case "IN_PROGRESS":
      return { label: "Đang bắt đầu", color: "yellow" };
    case "COMPLETED":
      return { label: "Đã hoàn thành", color: "green" };
    case "RESCHEDULED":
      return { label: "Dời lịch", color: "red" };
    default:
      return { label: "Không xác định", color: "default" };
  }
};

export const formatBlockName = (blockName: string): string => {
  return blockName.replace(/^BLOCK_/, "Block ");
};

export const filterOption = (input: string, option: any) => {
  return option.value.toLowerCase().includes(input.toLowerCase());
};

export const confirmModal = (message, onConfirm) => {
  Modal.confirm({
    content: message,
    icon: createVNode(ExclamationCircleOutlined),
    centered: true,
    onOk() {
      onConfirm();
    },
    cancelText: "Huỷ",
    onCancel() {
      Modal.destroyAll();
    },
  });
};
