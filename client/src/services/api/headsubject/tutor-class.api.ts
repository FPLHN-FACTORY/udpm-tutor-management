import {
  PREFIX_API_HEAD_SUBJECT_PLAN,
} from "@/constants/url";
import request from "@/services/request.ts";
import { DefaultResponse, PaginationParams, PaginationResponse, ResponseList } from "@/types/api.common";
import { AxiosResponse } from "axios";
import { Ref } from "vue";

export interface CreateTutorClassParams {
  planId?: string;
  subjectId?: string;
  numberOfClasses?: number;
  numberOfLectures?: number;
  format?: string;
}

export interface UpdateTutorClassParams {
  numberOfLectures?: number;
  format?: string;
}

export type DetailSubjectTutorResponse = {
  id: string;
  format: string;
  numberClasses: number;
  numberLectures: number;
  tutorClassId: string;
  subjectName: string;
};

export type TutorClassDetailResponse = ResponseList & {
  tutorClassCode: number;
  studentTutor: string;
  teacherTutor: string;
  shift: string;
  room: string;
  startTime: number;
  endTime: number;
  subjectId: string;
};


export const createTutorClass = async (
  params: CreateTutorClassParams
) => {
    const res = (await request({
      url: `${PREFIX_API_HEAD_SUBJECT_PLAN}/tutor`,
      method: "POST",
      data: params,
    })) as AxiosResponse<DefaultResponse<null>>;

    return res.data;
};

export const updateTutorClass = async (
    id: string,
    params: UpdateTutorClassParams) => {
  const res = (await request({
    url: `${PREFIX_API_HEAD_SUBJECT_PLAN}/tutor/${id}`,
    method: "PUT",
    data: params,
  })) as AxiosResponse<DefaultResponse<null>>;

  return res.data;
};

export const getDetailTutorClass = async (planId: string | null) => {
    const res = (await request({
      url: `${PREFIX_API_HEAD_SUBJECT_PLAN}/tutor/${planId}`,
      method: "GET",
    })) as AxiosResponse<DefaultResponse<DetailSubjectTutorResponse>>;

    return res.data; // Return the response data
};

export interface ParamsGetTutorClass extends PaginationParams {
  planId?: string | null;
  facilityId?: string | null;
  semesterId?: string | null;
  staffId?: string | null;
  userId?: string | null;
}

export type TutorClassResponse = ResponseList & {
  subjectName: string;
  numberClasses: string;
  format: string;
  headSubject: number;
};

export const getTutorClass = async (params: Ref<ParamsGetTutorClass>) => {
  const res = (await request({
    url: `${PREFIX_API_HEAD_SUBJECT_PLAN}/tutor`,
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
  userId?: string | null;
  semesterId?: string | null;
  teacherId?: string | null;
  query?: string | null;
}

export const getListTutorClassDetail = async (params: Ref<ParamsGetTutorClassDetail>) => {
  const res = (await request({
    url: `${PREFIX_API_HEAD_SUBJECT_PLAN}/tutor-detail`,
    method: "GET",
    params: params.value,
  })) as AxiosResponse<
      DefaultResponse<PaginationResponse<Array<TutorClassDetailResponse>>>
  >;

  return res.data;
};

export interface updateTutorClassDetailParams {
  staffId: string;
}

export const updateTutorClassDetail = async (
    id: string,
    params: updateTutorClassDetailParams) => {
  const res = (await request({
    url: `${PREFIX_API_HEAD_SUBJECT_PLAN}/tutor-detail/${id}`,
    method: "PUT",
    data: params,
  })) as AxiosResponse<DefaultResponse<null>>;

  return res.data;
};

export const deleteTutorClassDetail = async (
    id: string) => {
  const res = (await request({
    url: `${PREFIX_API_HEAD_SUBJECT_PLAN}/tutor-detail/${id}`,
    method: "DELETE",
  })) as AxiosResponse<DefaultResponse<null>>;

  return res.data;
};

export const addTutorClassDetail = async (
    id: string) => {
  const res = (await request({
    url: `${PREFIX_API_HEAD_SUBJECT_PLAN}/tutor-detail/${id}`,
    method: "POST",
  })) as AxiosResponse<DefaultResponse<null>>;

  return res.data;
};

