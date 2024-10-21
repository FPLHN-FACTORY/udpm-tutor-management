import {
  DefaultResponse,
  PaginationParams,
  PaginationResponse,
  ResponseList,
} from "@/types/api.common.ts";
import { Ref } from "vue";
import request from "../../request.ts";
import { PREFIX_API_FACILITY_SUPER_ADMIN } from "@/constants/url.ts";
import { AxiosResponse } from "axios";

export interface ParamsGetFacility extends PaginationParams {
  name?: string;
  status?: string;
}

export type FacilityResponse = ResponseList & {
  facilityName: string;
  facilityCode: string;
  facilityStatus: number;
  createdDate: number;
};

export type FacilityDetailResponse = {
  id: string;
  facilityName: string;
  facilityCode: string;
  facilityStatus: number;
  createdDate: number;
};

export const getFacilities = async (params: Ref<ParamsGetFacility>) => {
  const res = (await request({
    url: `${PREFIX_API_FACILITY_SUPER_ADMIN}`,
    method: "GET",
    params: params.value,
  })) as AxiosResponse<
    DefaultResponse<PaginationResponse<Array<FacilityResponse>>>
  >;

  return res.data;
};

export const getDetailFacility = async (facilityId: string | null) => {
  const res = (await request({
    url: `${PREFIX_API_FACILITY_SUPER_ADMIN}/${facilityId}`,
    method: "GET",
  })) as AxiosResponse<DefaultResponse<FacilityDetailResponse>>;

  return res.data;
};

export interface CreateUpdateFacilityParams {
  facilityName: string;
}

export const createFacility = async (params: CreateUpdateFacilityParams) => {
  const res = (await request({
    url: `${PREFIX_API_FACILITY_SUPER_ADMIN}`,
    method: "POST",
    data: params,
  })) as AxiosResponse<DefaultResponse<null>>;

  return res.data;
};

export const updateFacility = async (
  facilityId: string,
  params: CreateUpdateFacilityParams
) => {
  const res = (await request({
    url: `${PREFIX_API_FACILITY_SUPER_ADMIN}/${facilityId}`,
    method: "PUT",
    data: params,
  })) as AxiosResponse<DefaultResponse<null>>;

  return res.data;
};

export const changeStatusFacility = async (facilityId: string) => {
  const res = (await request({
    url: `${PREFIX_API_FACILITY_SUPER_ADMIN}/${facilityId}/change-status`,
    method: "PUT",
  })) as AxiosResponse<DefaultResponse<null>>;

  return res.data;
};
