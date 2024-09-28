import { PREFIX_API_DEPARTMENT_ADMIN, PREFIX_API_DEPARTMENT_FACILITY_ADMIN } from "@/constants/url.ts";
import request from "@/services/request.ts";
import {
  DefaultResponse,
  PaginationParams,
  PaginationResponse,
  ResponseList,
} from "@/types/api.common.ts";
import { AxiosResponse } from "axios";
import { Ref } from "vue";
import { MajorFacilityResponse } from "./major.api";
import { toast } from "vue3-toastify";
import { ERROR_MESSAGE } from "@/constants/message.constant";

export interface ParamsGetDepartment extends PaginationParams {
  departmentCode?: string | null;
  departmentName?: string | null;
}

export interface ParamsGetDepartmentFacility extends PaginationParams {
  facilityName?: string | null;
  staffCodeOrEmail?: string | null;
}

export type DepartmentResponse = ResponseList & {
  departmentName: string;
  departmentCode: string;
  departmentStatus: string;
  createdDate: number;
};

export type DepartmentFacilityResponse = ResponseList & {
  departmentFacilityId: string;
  facilityId: string;
  headOfDepartmentId: string;
  facilityName: string;
  headOfDepartmentName: string;
  headOfDepartmentCode: string;
  departmentFacilityStatus: string;
  createdDate: string;
  profileStaff: string,
  departmentName: string; 
  departmentCode: string;
};

export type DetailDepartmentResponse = {
  id: string;
  departmentName: string;
  departmentCode: string;
};

export type FacilityResponse = DepartmentFacilityResponse | MajorFacilityResponse;

export const getDepartments = async (params: Ref<ParamsGetDepartment>) => {
  const res = (await request({
    url: `${PREFIX_API_DEPARTMENT_ADMIN}`,
    method: "GET",
    params: params.value,
  })) as AxiosResponse<
      DefaultResponse<PaginationResponse<Array<DepartmentResponse>>>
  >;

  return res.data;
};

export const getDepartmentsManagedByStaff = async (staffId: string) => {
  try {
    const res = (await request({
      url: `${PREFIX_API_DEPARTMENT_ADMIN}/staff/${staffId}`,
      method: "GET",
    })) as AxiosResponse<DefaultResponse<Array<DetailDepartmentResponse>>>;

    return res.data;
  } catch (error) {
    toast.error(
      error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
    );
    throw error;
  }
};

export const getDepartmentFacility = async (departmentId: Ref<string | null>, params: Ref<ParamsGetDepartmentFacility>) => {
  const res = (await request({
    url: `${PREFIX_API_DEPARTMENT_FACILITY_ADMIN}/${departmentId.value}`,
    method: "GET",
    params: params.value,
  })) as AxiosResponse<
    DefaultResponse<PaginationResponse<Array<DepartmentFacilityResponse>>>
  >;

  return res.data;
};

export const getDetailDepartment = async (id: String | null) => {
  const res = (await request({
    url: `${PREFIX_API_DEPARTMENT_ADMIN}/${id}`,
    method: "GET",
  })) as AxiosResponse<DefaultResponse<DetailDepartmentResponse>>;

  return res.data;
};


export interface CreateUpdateDepartmentParams {
  departmentName: string;
  departmentCode: string;
}

export const createDepartment = async (
    params: CreateUpdateDepartmentParams
) => {
  const res = (await request({
    url: `${PREFIX_API_DEPARTMENT_ADMIN}`,
    method: "POST",
    data: params,
  })) as AxiosResponse<DefaultResponse<null>>;

  return res.data;
};

export const updateDepartment = async (
    id: string,
    params: CreateUpdateDepartmentParams
) => {
  const res = (await request({
    url: `${PREFIX_API_DEPARTMENT_ADMIN}/${id}`,
    method: "PUT",
    data: params,
  })) as AxiosResponse<DefaultResponse<null>>;

  return res.data;
};

export const getDepartmentSynchronize = async () => {
  try {
    const res: AxiosResponse<DefaultResponse<string>> = await request({
      url: `${PREFIX_API_DEPARTMENT_ADMIN}/synchronize`,
      method: 'GET',
    });
    // toast.success("Đồng bộ bộ môn thành công");
    return res.data;
  } catch (error) {
    toast.error(
        error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
    );
    throw error; 
  }
};

export const getDepartmentCampusSynchronize = async () => {
  try {
    const res: AxiosResponse<DefaultResponse<string>> = await request({
      url: `${PREFIX_API_DEPARTMENT_FACILITY_ADMIN}/synchronize`,
      method: 'GET',
    });
    // toast.success("Đồng bộ bộ môn theo cơ sở thành công");
    return res.data;
  } catch (error) {
    toast.error(
        error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
    );
    throw error; 
  }
};
