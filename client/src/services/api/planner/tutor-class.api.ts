import {
  PREFIX_API_PLANNER_PLAN,
} from "@/constants/url";
import request from "@/services/request.ts";
import { DefaultResponse, PaginationParams, PaginationResponse, ResponseList } from "@/types/api.common";
import { AxiosResponse } from "axios";
import { Ref } from "vue";

export type DetailSubjectTutorResponse = {
  numberOfClasses: number;
  tutorClassId: string;
  subjectName: string;
};

export type TutorClassDetailResponse = ResponseList & {
  id: string;
  tutorClassCode: number;
  studentTutor: string;
  teacherTutor: string;
  shift: string;
  room: string;
  name: string;
  link: string;
  startTime: number;
  endTime: number;
  selected: boolean;
};

export const getDetailTutorClass = async (planId: string | null) => {
    const res = (await request({
      url: `${PREFIX_API_PLANNER_PLAN}/tutor/${planId}`,
      method: "GET",
    })) as AxiosResponse<DefaultResponse<DetailSubjectTutorResponse>>;

    return res.data; // Return the response data
};

export interface ParamsGetTutorClass extends PaginationParams {
  planId?: string | null;
  facilityId?: string | null;
  semesterId?: string | null;
}

export type TutorClassResponse = ResponseList & {
  subjectName: string;
  numberClasses: string;
  format: string;
  headSubject: number;
};

export const getTutorClass = async (params: Ref<ParamsGetTutorClass>) => {
  const res = (await request({
    url: `${PREFIX_API_PLANNER_PLAN}/tutor`,
    method: "GET",
    params: params.value,
  })) as AxiosResponse<
      DefaultResponse<PaginationResponse<Array<TutorClassResponse>>>
  >;

  return res.data;
};

export interface ParamsGetTutorClassDetail extends PaginationParams {
  planId?: string | null;
  facilityId?: string | null;
  semesterId?: string | null;
  teacherId?: string | null;
  query?: string | null;
}

export const getListTutorClassDetail = async (params: Ref<ParamsGetTutorClassDetail>) => {
  const res = (await request({
    url: `${PREFIX_API_PLANNER_PLAN}/tutor-detail`,
    method: "GET",
    params: params.value,
  })) as AxiosResponse<
      DefaultResponse<PaginationResponse<Array<TutorClassDetailResponse>>>
  >;

  return res.data;
};

export interface UpdateTutorClassDetailParams {
  id: string;
  studentId: string;
  room: string;
}

export const updateStudentPlan = async (params: Array<UpdateTutorClassDetailParams>) => {
  const res = (await request({
    url: `${PREFIX_API_PLANNER_PLAN}/tutor-detail`,
    method: "PUT",
    data: params,
  })) as AxiosResponse<
      DefaultResponse<DefaultResponse<null>>
  >;

  return res.data;
};

export interface CreateStudentTutorParams {
  code: string;
  name: string;
  email: string;
}

export const createStudentTutor = async (params: CreateStudentTutorParams) => {
  const res = (await request({
    url: `${PREFIX_API_PLANNER_PLAN}/student`,
    method: "POST",
    data: params,
  })) as AxiosResponse<
      DefaultResponse<DefaultResponse<null>>
  >;

  return res.data;
};


