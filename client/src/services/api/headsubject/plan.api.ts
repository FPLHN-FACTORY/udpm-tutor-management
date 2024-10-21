import {DefaultResponse, PaginationParams, PaginationResponse, ResponseList} from "@/types/api.common.ts";
import {AxiosResponse} from "axios";
import {PREFIX_API_HEAD_SUBJECT_PLAN} from "@/constants/url.ts";
import request from "@/services/request.ts";
export interface ParamsGetPlans extends PaginationParams {
  semesterId?: string | null;
  departmentCode?: string | null;
  facilityCode?: string | null;
}

export type PlanResponse = ResponseList & {
  planName: string;
  blockName: string;
  departmentName: string;
  facilityName: string;
  planType: string;
  numberSubjects: number;
  endTime: number;
  startTime: number;
  status: string;
};

export const getPlans = async (params: ParamsGetPlans) => {
  const res = (await request({
    url: `${PREFIX_API_HEAD_SUBJECT_PLAN}`,
    method: "GET",
    params: params,
  })) as AxiosResponse<
      DefaultResponse<PaginationResponse<Array<PlanResponse>>>
  >;

  return res.data;
};

export type TutorClassResponse = ResponseList & {
  subjectCode: string;
  subjectId: string;
  status: number;
  subjectName: string;
  numberClasses: number;
};

export type SemesterInfoResponse = {
  planName: string;
  departmentName: string;
  facilityName: string;
  startTime: number;
  endTime: number;
};

export const getSemesterInfo = async (params: ParamsGetPlans) => {
  const res = (await request({
    url: `${PREFIX_API_HEAD_SUBJECT_PLAN}/semester`,
    method: "GET",
    params: params,
  })) as AxiosResponse<
      DefaultResponse<SemesterInfoResponse>
  >;

  return res.data;
};

export type PlanInfoResponse = {
  name: string;
  status: string;
  facilityName: string;
  numberSubjects: number;
  numberClasses: number;
  blockName: string;
  startTime: number;
  endTime: number;
  numberStudents: number;
  numberTeachers: number;
  addedSubjects: string;
  notAddedSubjects: string;
  rating: number;
};

export const getPlanInfo = async (params: ParamsGetPlans) => {
  const res = (await request({
    url: `${PREFIX_API_HEAD_SUBJECT_PLAN}/info`,
    method: "GET",
    params: params,
  })) as AxiosResponse<
      DefaultResponse<Array<PlanInfoResponse>>
  >;

  return res.data;
};

export type PlanInfoDetailResponse = {
  planName: string;
  blockName: string;
  status: string;
  facilityName: string;
  numberSubjects: number;
  numberClasses: number;
  startTime: number;
  endTime: number;
};

export const getPlanInfoById = async (planId: string | null) => {
  const res = (await request({
    url: `${PREFIX_API_HEAD_SUBJECT_PLAN}/info/${planId}`,
    method: "GET",
  })) as AxiosResponse<
      DefaultResponse<PlanInfoDetailResponse>
  >;

  return res.data;
};

export type DetailPlanResponse = {
  id: string;
  planCode: string;
  planName: string;
  departmentId: string;
  planType: string;
  planStatus: string;
  createdDate: number;
};

export const getDetailPlan = async (planId: string | null) => {
  const res = (await request({
    url: `${PREFIX_API_HEAD_SUBJECT_PLAN}/${planId}`,
    method: "GET",
  })) as AxiosResponse<DefaultResponse<DetailPlanResponse>>;

  return res.data;
};


