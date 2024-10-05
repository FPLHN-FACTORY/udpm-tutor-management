import { PREFIX_API_SUBJECT_ADMIN } from "@/constants/url.ts";
import request from "@/services/request.ts";
import {
  DefaultResponse,
  PaginationParams,
  PaginationResponse,
  ResponseList,
} from "@/types/api.common.ts";
import { AxiosResponse } from "axios";
import { Ref } from "vue";

export interface ParamsGetSubjects extends PaginationParams {
  subjectCode?: string | null;
  subjectName?: string | null;
  departmentId?: string | null;
  subjectType?: string | null;
  startDate?: number | null;
}

export type SubjectResponse = ResponseList & {
  subjectCode: string;
  subjectName: string;
  departmentName: string;
  subjectType: string;
  createDate: number;
};

export type DetailSubjectResponse = {
  id: string;
  subjectCode: string;
  subjectName: string;
  departmentId: string;
  subjectType: string;
  subjectStatus: string;
  createdDate: number;
};

export const getSubjects = async (params: Ref<ParamsGetSubjects>) => {
  const res = (await request({
    url: `${PREFIX_API_SUBJECT_ADMIN}`,
    method: "GET",
    params: params.value,
  })) as AxiosResponse<
      DefaultResponse<PaginationResponse<Array<SubjectResponse>>>
  >;

  return res.data;
};

export const getAllSubjectByStaffId = async (staffId: string) => {
  try {
    const res = (await request({
      url: `${PREFIX_API_SUBJECT_ADMIN}/staff/${staffId}`,
      method: "GET",
    })) as AxiosResponse<DefaultResponse<Array<SubjectResponse>>>;
    return res.data;
  } catch (error) {
   
    throw error;
  }
};

export interface CreateUpdateSubjectParams {
  subjectCode: string;
  subjectName: string;
  departmentId: string;
  subjectType: string;
  startDate: number;
}

export const createSubject = async (params: CreateUpdateSubjectParams) => {
  const res = (await request({
    url: `${PREFIX_API_SUBJECT_ADMIN}`,
    method: "POST",
    data: params,
  })) as AxiosResponse<DefaultResponse<null>>;

  return res.data;
};

export const updateSubject = async (
    subjectId: string,
    params: CreateUpdateSubjectParams
) => {
  const res = (await request({
    url: `${PREFIX_API_SUBJECT_ADMIN}/${subjectId}`,
    method: "PUT",
    data: params,
  })) as AxiosResponse<DefaultResponse<null>>;

  return res.data;
};

export const getDetailSubject = async (subjectId: string | null) => {
  const res = (await request({
    url: `${PREFIX_API_SUBJECT_ADMIN}/${subjectId}`,
    method: "GET",
  })) as AxiosResponse<DefaultResponse<DetailSubjectResponse>>;

  return res.data;
};
