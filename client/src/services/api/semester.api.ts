import {
  DefaultResponse,
  PaginationParams,
  PaginationResponse,
  ResponseList,
} from "@/types/api.common";
import { Ref } from "vue";
import { PREFIX_API_SEMESTER_ADMIN } from "@/constants/url";
import { AxiosResponse } from "axios";
import request from "@/services/request";

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

export const getDetailSemester = async (id: string | null) => {
  const res = (await request({
    url: `${PREFIX_API_SEMESTER_ADMIN}/${id}`,
    method: "GET",
  })) as AxiosResponse<DefaultResponse<DetailSemesterResponse>>;

  return res.data;
};
