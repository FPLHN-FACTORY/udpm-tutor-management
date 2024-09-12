import { PREFIX_API_DEPARTMENT_ADMIN } from "@/constants/url";
import request from "@/services/request";
import {
  DefaultResponse,
  PaginationParams,
  PaginationResponse,
  ResponseList,
} from "@/types/api.common";
import { AxiosResponse } from "axios";
import { Ref } from "vue";

export interface ParamsGetDepartment extends PaginationParams {
  departmentName?: string | null;
}

export type DepartmentResponse = ResponseList & {
  departmentName: string;
  departmentCode: string;
  departmentStatus: string;
  createdDate: number;
};

export type DetailDepartmentResponse = {
  id: string;
  departmentName: string;
  departmentCode: string;
};

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

export const getDetailDepartment = async (id: string | null) => {
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
