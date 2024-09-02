import { PREFIX_API_ROLE_ADMIN } from "@/constants/url";
import request from "@/services/request";
import {
  DefaultResponse,
  PaginationParams,
  PaginationResponse,
  ResponseList,
} from "@/types/api.common";
import { AxiosResponse } from "axios";
import { Ref } from "vue";

export interface ParamsGetRoles extends PaginationParams {
  roleName?: string;
  idFacility?: string;
}

export type RoleResponse = ResponseList & {
  roleCode: string;
  roleName: string;
  facilityName: string;
};

export const getRoles = async (paramsRef: Ref<ParamsGetRoles>) => {
  const res = (await request({
    url: `${PREFIX_API_ROLE_ADMIN}`,
    method: "GET",
    params: paramsRef.value,
  })) as AxiosResponse<
    DefaultResponse<PaginationResponse<Array<RoleResponse>>>
  >;

  return res.data;
};
