import {
  DefaultResponse,
  PaginationParams,
  PaginationResponse,
  ResponseList,
} from "@/types/api.common.ts";
import { Ref } from "vue";
import {PREFIX_API_SEMESTER_ADMIN, PREFIX_API_STAFF_ADMIN} from "@/constants/url.ts";
import { AxiosResponse } from "axios";
import request from "@/services/request.ts";
import {toast} from "vue3-toastify";
import {ERROR_MESSAGE} from "@/constants/message.constant.ts";

export interface ParamsGetSemester extends PaginationParams {
  semesterName?: string | null;
  semesterYear?: number | null;
}

export type SemesterResponse = ResponseList & {
  semesterName: string;
  semesterYear: number;
  startTime: number;
  endTime: number;
  startTimeFirstBlock: number;
  endTimeFirstBlock: number;
  startTimeSecondBlock: number;
  endTimeSecondBlock: number;
};

export type DetailSemesterResponse = {
  id: string;
  semesterName: string;
  semesterYear: number;
  startTime: number;
  endTime: number;
  startTimeFirstBlock: number;
  endTimeFirstBlock: number;
  startTimeSecondBlock: number;
  endTimeSecondBlock: number;
};

export const getSemesters = async (params: Ref<ParamsGetSemester>) => {
  const res = (await request({
    url: `${PREFIX_API_SEMESTER_ADMIN}`,
    method: "GET",
    params: params.value,
  })) as AxiosResponse<
      DefaultResponse<PaginationResponse<Array<SemesterResponse>>>
  >;

  return res.data;
};

export const getSemesterSynchronize = async () => {
  try {
    const res: AxiosResponse<DefaultResponse<string>> = await request({
      url: `${PREFIX_API_SEMESTER_ADMIN}/synchronize`,
      method: 'GET',
    });

    // Hiển thị thông báo thành công sau khi nhận được phản hồi
    toast.success("Đồng bộ học kỳ thành công");
    return res.data;
  } catch (error) {
    // Hiển thị thông báo lỗi và ném lỗi để có thể được bắt trong handleSync
    toast.error(
        error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
    );
    throw error; // Ném lỗi để catch ở hàm gọi
  }
};

export const getDetailSemester = async (id: string | null) => {
  const res = (await request({
    url: `${PREFIX_API_SEMESTER_ADMIN}/${id}`,
    method: "GET",
  })) as AxiosResponse<DefaultResponse<DetailSemesterResponse>>;

  return res.data;
};
