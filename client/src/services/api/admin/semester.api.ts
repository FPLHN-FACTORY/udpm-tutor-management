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
    const res: AxiosResponse<DefaultResponse<string>> = await request({
      url: `${PREFIX_API_SEMESTER_ADMIN}/synchronize`,
      method: 'GET',
    });

    return res.data;
};

export const getDetailSemester = async (id: string | null) => {
  const res = (await request({
    url: `${PREFIX_API_SEMESTER_ADMIN}/${id}`,
    method: "GET",
  })) as AxiosResponse<DefaultResponse<DetailSemesterResponse>>;

  return res.data;
};

export interface CreateUpdateSemesterParams {
  semesterName: string;
  startTime: number;
  endTime: number;
  endTimeBlock1: number;
}

export const createSemester = async (params: CreateUpdateSemesterParams) => {
  const res = (await request({
    url: `${PREFIX_API_SEMESTER_ADMIN}`,
    method: "POST",
    data: params,
  })) as AxiosResponse<DefaultResponse<null>>;

  return res.data;
};

export const updateSemester = async (
    semesterId: string,
    params: CreateUpdateSemesterParams
) => {
  const res = (await request({
    url: `${PREFIX_API_SEMESTER_ADMIN}/${semesterId}`,
    method: "PUT",
    data: params,
  })) as AxiosResponse<DefaultResponse<null>>;

  return res.data;
};
