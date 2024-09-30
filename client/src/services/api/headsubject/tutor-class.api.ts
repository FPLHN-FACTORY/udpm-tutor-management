import {
  PREFIX_API_HEAD_SUBJECT_PLAN,
} from "@/constants/url";
import request from "@/services/request.ts";
import { DefaultResponse, PaginationParams, PaginationResponse, ResponseList } from "@/types/api.common";
import { AxiosResponse } from "axios";
import { Ref } from "vue";

export interface UpdateHeadSubjectTutorDetailParams {
  planeId: string;
  subjectId: string;
  numberOfClasses: number;
}

export type DetailSubjectTutorResponse = {
  numberOfClasses: number;
  tutorClassId: string;
  subjectName: string;
};

export type TutorClassDetailResponse = {
  nameTutorClass: number;
  headSubject: string;
  numberOfLectures:  number;
  startTime: number;
  endTime: number;
};

export const updateNumberTutorClassHeadSubject = async (
  params: UpdateHeadSubjectTutorDetailParams
) => {
    const res = (await request({
      url: `${PREFIX_API_HEAD_SUBJECT_PLAN}/tutor`,
      method: "PUT",
      data: params,
    })) as AxiosResponse<DefaultResponse<null>>;

    return res.data;
};

export const updateStatusApproveTutorClassHeadSubject = async (id: string) => {
    const res = (await request({
      url: `${PREFIX_API_HEAD_SUBJECT_PLAN}/tutor/${id}`,
      method: "PUT",
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
  tutorClassId?: string | null;
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

