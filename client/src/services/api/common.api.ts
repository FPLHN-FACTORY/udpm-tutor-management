import { PREFIX_API_COMMON } from "@/constants/url";
import request from "@/services/request";
import { DefaultResponse } from "@/types/api.common";
import { AxiosResponse } from "axios";

export type CommonOptionsResponse = {
  id: string;
  name: string;
};

export type FormatCommonOptionsResponse = {
  label: string;
  value: string;
};

export const getDepartmentOptions = async (query?: string) => {
  const res = (await request({
    url: `${PREFIX_API_COMMON}/department`,
    params: {
      query,
    },
    method: "GET",
  })) as AxiosResponse<DefaultResponse<Array<CommonOptionsResponse>>>;

  return res.data;
};

export const getFacilityOptions = async (query?: string) => {
  const res = (await request({
    url: `${PREFIX_API_COMMON}/facility`,
    params: {
      query,
    },
    method: "GET",
  })) as AxiosResponse<DefaultResponse<Array<CommonOptionsResponse>>>;

  return res.data;
};

export const getSemesterOptions = async (query?: string) => {
  const res = (await request({
    url: `${PREFIX_API_COMMON}/semester`,
    params: {
      query,
    },
    method: "GET",
  })) as AxiosResponse<DefaultResponse<Array<CommonOptionsResponse>>>;

  return res.data;
};

export const getBlockOptions = async (semesterId?: string | null) => {
  const res = (await request({
    url: `${PREFIX_API_COMMON}/block`,
    params: {
      semesterId,
    },
    method: "GET",
  })) as AxiosResponse<DefaultResponse<Array<CommonOptionsResponse>>>;

  return res.data;
};

export const getSubjectOptions = async (query?: string) => {
  const res = (await request({
    url: `${PREFIX_API_COMMON}/subject`,
    params: {
      query,
    },
    method: "GET",
  })) as AxiosResponse<DefaultResponse<Array<CommonOptionsResponse>>>;

  return res.data;
};

export type ParamsStaffSearchByRole = {
  role?: string[];
  departmentCode?: string;
  facilityCode?: string;
};

export type ParamsStaffOptions = {
  roleCode?: string;
  departmentCode?: string;
  facilityCode?: string;
};

export const getStaffByRole = async (
    params: ParamsStaffSearchByRole,
) => {
  const res = (await request({
    url: `${PREFIX_API_COMMON}/staff/role`,  // Thay đổi URL nếu cần
    params: {
      departmentCode: params.departmentCode, // Đúng tên trường
      facilityCode: params.facilityCode, // Đúng tên trường
    },
    method: 'GET',
  })) as AxiosResponse<DefaultResponse<Array<CommonOptionsResponse>>>;

  return res.data;
};

export const getStaffOptions = async (
  params: ParamsStaffOptions,
) => {
const res = (await request({
  url: `${PREFIX_API_COMMON}/staff`,  // Thay đổi URL nếu cần
  params: params,
  method: 'GET',
})) as AxiosResponse<DefaultResponse<Array<CommonOptionsResponse>>>;

return res.data;
};

export type ParamsStudentTutorSearch = {
  departmentCode?: string;
  facilityCode?: string;
};

export const getStudentTutor = async (
) => {
  const res = (await request({
    url: `${PREFIX_API_COMMON}/student/tutor`,  // Thay đổi URL nếu cần
    method: 'GET',
  })) as AxiosResponse<DefaultResponse<Array<CommonOptionsResponse>>>;

  return res.data;
};

export type ParamsRoleSearch = {
  facilityCode: string;
};

export const getRoleOptions = async (params: ParamsRoleSearch) => {
  const res = (await request({
    url: `${PREFIX_API_COMMON}/role`,
    method: "GET",
    params: params,
  })) as AxiosResponse<DefaultResponse<Array<CommonOptionsResponse>>>;

  return res.data;
};

export interface ParamsMajorOption {
  departmentId: string | null;
  facilityId: string | null
}

export const getMajorOptions = async (
  params: ParamsMajorOption,
) => {
const res = (await request({
  url: `${PREFIX_API_COMMON}/major`,  // Thay đổi URL nếu cần
  method: 'GET',
  params: params,
})) as AxiosResponse<DefaultResponse<Array<CommonOptionsResponse>>>;

return res.data;
};