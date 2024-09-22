import {PREFIX_API_MAJOR_ADMIN, PREFIX_API_MAJOR_FACILITY_ADMIN } from "@/constants/url.ts";
import request from "@/services/request.ts";
import {
  DefaultResponse,
  PaginationParams,
  PaginationResponse,
  ResponseList,
} from "@/types/api.common.ts";
import { AxiosResponse } from "axios";
import { Ref } from "vue";

export interface ParamsGetMajor extends PaginationParams {
    majorName?: string | null;
    majorCode?: string | null;
  }

export interface ParamsGetMajorFacility extends PaginationParams {
  majorCodeOrName?: string | null;
  staffCodeOrName?: string | null;
  departmentFacilityId?: string | null;
}
  
  export type MajorResponse = ResponseList & {
    majorName: string;
    majorCode: string;
    majorStatus: string;
    createdDate: number;
  };

  export type MajorFacilityResponse = ResponseList & {
    majorName: string;
    headMajorCodeName: string;
    facilityDepartmentInfo: string;
    departmentFacilityId: String
    facilityCode: string;
    facilityName: string;
  };
  
  export type DetailMajorResponse = {
    id: string;
    majorName: string;
    majorCode: string;
  };

export const getMajors = async (departmentId: Ref<string | null>, params: Ref<ParamsGetMajor>) => {
    const res = (await request({
      url: `${PREFIX_API_MAJOR_ADMIN}/${departmentId.value}`,
      method: "GET",
      params: params.value,
    })) as AxiosResponse<
      DefaultResponse<PaginationResponse<Array<MajorResponse>>>
    >;
  
    return res.data;
  };


  export const getMajorsFacility = async (params: Ref<ParamsGetMajorFacility>) => {
    const res = (await request({
      url: `${PREFIX_API_MAJOR_FACILITY_ADMIN}`,
      method: "GET",
      params: params.value,
    })) as AxiosResponse<
      DefaultResponse<PaginationResponse<Array<MajorFacilityResponse>>>
    >;
  
    return res.data;
  };




export const getDetailMajor = async (id: string | null) => {
    const res = (await request({
      url: `${PREFIX_API_MAJOR_ADMIN}/${id}`,
      method: "GET",
    })) as AxiosResponse<DefaultResponse<DetailMajorResponse>>;
  
    return res.data;
  };
  