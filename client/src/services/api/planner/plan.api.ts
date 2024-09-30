import {DefaultResponse, PaginationParams, PaginationResponse, ResponseList} from "@/types/api.common.ts";
import {Ref} from "vue";
import {AxiosResponse} from "axios";
import {PREFIX_API_PLANNER_PLAN} from "@/constants/url.ts";
import request from "@/services/request.ts";
import {toast} from "vue3-toastify";

export interface ParamsGetPlans extends PaginationParams {
  semesterId?: string | null;
  departmentCode?: string | null;
  facilityCode?: string | null;
}

export interface ParamsGetTutorClass extends PaginationParams {
  planId?: string | null;
}

export interface ParamsGetSemesterInfo {
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
  status: string;
};

export const getPlans = async (params: Ref<ParamsGetPlans>) => {
  const res = (await request({
    url: `${PREFIX_API_PLANNER_PLAN}`,
    method: "GET",
    params: params.value,
  })) as AxiosResponse<
      DefaultResponse<PaginationResponse<Array<PlanResponse>>>
  >;

  return res.data;
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
    url: `${PREFIX_API_PLANNER_PLAN}/semester`,
    method: "GET",
    params: params,
  })) as AxiosResponse<
      DefaultResponse<DefaultResponse<SemesterInfoResponse>>
  >;

  return res.data;
};

export type PlanInfoResponse = {
  name: string;
  status: string;
  facilityName: string;
  numberSubjects: number;
  numberClasses: number;
};

export const getPlanInfo = async (params: ParamsGetPlans) => {
  const res = (await request({
    url: `${PREFIX_API_PLANNER_PLAN}/info`,
    method: "GET",
    params: params,
  })) as AxiosResponse<
      DefaultResponse<DefaultResponse<PlanInfoResponse>>
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
};

export const getPlanInfoById = async (planId: string | null) => {
  const res = (await request({
    url: `${PREFIX_API_PLANNER_PLAN}/info/${planId}`,
    method: "GET",
  })) as AxiosResponse<
      DefaultResponse<DefaultResponse<PlanInfoDetailResponse>>
  >;

  return res.data;
};

export interface CreateUpdatePlanParams {
  blockId: string;
  departmentCode: string;
  facilityCode: string;
  userCode: string;
  description: string;
  semesterId: string,
}

export const createPlan = async (params: CreateUpdatePlanParams) => {

  try {
    const res = (await request({
      url: `${PREFIX_API_PLANNER_PLAN}`,
      method: "POST",
      data: params,
    })) as AxiosResponse<DefaultResponse<null>>;
    toast.success("Thêm kế hoạch thành công");
    return res.data;
  } catch (error) {
    toast.error(error?.response?.data?.message || "Có lỗi xảy ra trong quá trình đồng bộ");
    throw error;  // Ném lại lỗi để xử lý ở nơi gọi
  }
};

export const updatePlan = async (planId: string, params: CreateUpdatePlanParams) => {

  try {
    const res = (await request({
      url: `${PREFIX_API_PLANNER_PLAN}/${planId}`,
      method: "PUT",
      data: params,
    })) as AxiosResponse<DefaultResponse<null>>;
    toast.success("Sửa hoạch thành công");
    return res.data;
  } catch (error) {
    toast.error(error?.response?.data?.message || "Có lỗi xảy ra trong quá trình đồng bộ");
    throw error;  // Ném lại lỗi để xử lý ở nơi gọi
  }
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
    url: `${PREFIX_API_PLANNER_PLAN}/${planId}`,
    method: "GET",
  })) as AxiosResponse<DefaultResponse<DetailPlanResponse>>;

  return res.data;
};
