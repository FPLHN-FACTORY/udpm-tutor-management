import { DefaultResponse, PaginationParams, PaginationResponse, ResponseList } from "@/types/api.common";
import request from "@/services/request.ts";
import { Ref } from "vue";
import { PREFIX_API_ADMIN_OPERATION_LOG, PREFIX_API_ADMIN_USER_ACTIVITY } from "@/constants/url";
import { AxiosResponse } from "axios";

export interface ParamsGetOperationLogs extends PaginationParams {
    emailOrNameOrCode?: string | null;
    typeFunction?: string | null;
    fromDate?: string | null;
    toDate?: string | null;
    status?: number | null;
}

export interface ParamsGetUserActivityLog extends PaginationParams {
  nameOrCode?: string | null;
  email?: string | null;
  fromDate?: string | null;
  toDate?: string | null;
  status?: number | null;
  statusUserActivity?: string | null;
}


export type GetOperationLogsResponse = ResponseList & {
    workstation: string;
    userName: string;
    api: string;
    email: string;
    code: string;
    createdDate: string;
    typeFunction: number;
    status: number;
};

export type GetUserActivityLogResponse = ResponseList & {
  workstation: string;
  name: string;
  email: string;
  code: string;
  loginTime: string;
  logoutTime: string;
  status: number;
  operation: string;
};


export type OperationLogResponse = {
    id: string;
    workstation: string;
    userName: string;
    api: string;
    email: string;
    code: string;
    createdDate: string;
    typeFunction: number;
    status: number;
  };

export const getOperationLogs = async (params: Ref<ParamsGetOperationLogs>) => {
    const res = (await request({
      url: `${PREFIX_API_ADMIN_OPERATION_LOG}`,
      method: "GET",
      params: params.value,
    })) as AxiosResponse<
        DefaultResponse<PaginationResponse<Array<GetOperationLogsResponse>>>
    >;
  
    return res.data;
};

export const getUserActivityLog = async (params: Ref<ParamsGetUserActivityLog>) => {
  const res = (await request({
    url: `${PREFIX_API_ADMIN_USER_ACTIVITY}`,
    method: "GET",
    params: params.value,
  })) as AxiosResponse<
      DefaultResponse<PaginationResponse<Array<GetUserActivityLogResponse>>>
  >;

  return res.data;
};

export const getDetailOperationLog = async (operationLogId: string | null) => {
    const res = (await request({
      url: `${PREFIX_API_ADMIN_OPERATION_LOG}/${operationLogId}`,
      method: "GET",
    })) as AxiosResponse<DefaultResponse<OperationLogResponse>>;
  
    return res.data;
  };

