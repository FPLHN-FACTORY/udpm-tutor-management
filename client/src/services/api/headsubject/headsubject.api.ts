import { PREFIX_API_HEAD_SUBJECT_TUTOR, PREFIX_API_HEAD_SUBJECT_TUTOR_DETAIL } from "@/constants/url";
import request from "@/services/request.ts";
import { DefaultResponse, PaginationParams, PaginationResponse, ResponseList } from "@/types/api.common";
import { AxiosResponse } from "axios";
import { Ref } from "vue";
import { toast } from "vue3-toastify";

export interface CreateUpdateHeadSubjectParams {
  planeId: string;
  subjectId: string;
  numberOfClasses: number;
  planFormat: number;
}

export interface CreateHeadSubjectTutorDetailParams {
  numberOfClasses: number;
  tutorClassId: string;
  numberOfLectures: number;
}

export interface UpdateHeadSubjectTutorDetailParams {
  numberOfClasses: number;
  tutorClassId: string;
}

export type DetailSubjectTutorResponse = {
  numberOfClasses: number;
  tutorClassId: string;
};

export type TutorClassDetailResponse = {
  nameTutorClass: number;
  headSubject: string;
  numberOfLectures:  number;
  startTime: number;
  endTime: number;
};

export const createOrUpdateHeadSubject = async (
  params: CreateUpdateHeadSubjectParams
) => {
  try {
    const res = (await request({
      url: `${PREFIX_API_HEAD_SUBJECT_TUTOR}`,
      method: "POST",
      data: params,
    })) as AxiosResponse<DefaultResponse<null>>;
    toast.success("Thêm số lượng lớp tutor thành công");
    return res.data;
  } catch (error) {
    toast.error(
      error?.response?.data?.message ||
        "Có lỗi xảy ra trong quá thêm số lượng lớp tutor"
    );
    throw error; // Ném lại lỗi để xử lý ở nơi gọi
  }
};

export const updateNumberTutorClassHeadSubject = async (
  params: UpdateHeadSubjectTutorDetailParams
) => {
  try {
    const res = (await request({
      url: `${PREFIX_API_HEAD_SUBJECT_TUTOR}`,
      method: "PUT",
      data: params,
    })) as AxiosResponse<DefaultResponse<null>>;
    toast.success("Cập nhật thành công");
    return res.data;
  } catch (error) {
    toast.error(
      error?.response?.data?.message ||
        "Có lỗi xảy ra trong quá trình cập nhật tutor"
    );
    throw error; // Ném lại lỗi để xử lý ở nơi gọi
  }
};

export const updateStatusApproveTutorClassHeadSubject = async (id: string) => {
  try {
    const res = (await request({
      url: `${PREFIX_API_HEAD_SUBJECT_TUTOR}/${id}`,
      method: "PUT",
    })) as AxiosResponse<DefaultResponse<null>>;
    toast.success("Duyêt thành công");
    return res.data;
  } catch (error) {
    toast.error(
      error?.response?.data?.message ||
        "Có lỗi xảy ra trong quá duyêt lớp học"
    );
    throw error; 
  }
};

export const deleteTutorClassHeadSubject = async (id: string) => {
  try {
    const res = (await request({
      url: `${PREFIX_API_HEAD_SUBJECT_TUTOR}/${id}`, // Thêm id vào URL
      method: "delete",
    })) as AxiosResponse<DefaultResponse<null>>;

    toast.success("Xóa lớp học thành công"); // Thay đổi thông báo cho chính xác
    return res.data;
  } catch (error) {
    toast.error(
      error?.response?.data?.message ||
        "Có lỗi xảy ra trong quá trình xóa lớp học"
    );
    throw error; // Ném lại lỗi để xử lý ở nơi gọi
  }
};

export const createHeadSubjectTutorDetail = async (
  params: CreateHeadSubjectTutorDetailParams
) => {
  try {
    const res = (await request({
      url: `${PREFIX_API_HEAD_SUBJECT_TUTOR}`,
      method: "POST",
      data: params,
    })) as AxiosResponse<DefaultResponse<null>>;
    toast.success("Thêm số lượng lớp tutor thành công");
    return res.data;
  } catch (error) {
    toast.error(
      error?.response?.data?.message ||
        "Có lỗi xảy ra trong quá thêm số lượng lớp tutor"
    );
    throw error; // Ném lại lỗi để xử lý ở nơi gọi
  }
};

export const getDetailTutorClass = async (planId: string | null) => {
  try {
    const res = (await request({
      url: `${PREFIX_API_HEAD_SUBJECT_TUTOR}/tutor-detail/${planId}`, 
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
  facilityCode?: string | null;
  departmentCode?: string | null;
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
    url: `${PREFIX_API_HEAD_SUBJECT_TUTOR}/tutor`,
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
    url: `${PREFIX_API_HEAD_SUBJECT_TUTOR_DETAIL}`,
    method: "GET",
    params: params.value,
  })) as AxiosResponse<
      DefaultResponse<PaginationResponse<Array<TutorClassDetailResponse>>>
  >;

  return res.data;
};


