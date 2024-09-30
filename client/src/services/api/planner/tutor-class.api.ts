import {
  PREFIX_API_PLANNER_PLAN,
} from "@/constants/url";
import request from "@/services/request.ts";
import { DefaultResponse, PaginationParams, PaginationResponse, ResponseList } from "@/types/api.common";
import { AxiosResponse } from "axios";
import { Ref } from "vue";
import { toast } from "vue3-toastify";

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

export const getDetailTutorClass = async (planId: string | null) => {
  try {
    const res = (await request({
      url: `${PREFIX_API_PLANNER_PLAN}/tutor/${planId}`,
      method: "GET",
    })) as AxiosResponse<DefaultResponse<DetailSubjectTutorResponse>>;

    return res.data; // Return the response data
  } catch (error) {
    toast.error(
      error?.response?.data?.message ||
        "Có lỗi xảy ra trong quá trình lấy thông tin lớp tutor"
    );
    throw error; 
  }
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
    url: `${PREFIX_API_PLANNER_PLAN}/tutor`,
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
    url: `${PREFIX_API_PLANNER_PLAN}/tutor-detail`,
    method: "GET",
    params: params.value,
  })) as AxiosResponse<
      DefaultResponse<PaginationResponse<Array<TutorClassDetailResponse>>>
  >;

  return res.data;
};

