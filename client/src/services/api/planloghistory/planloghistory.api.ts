import { PREFIX_API_ADMIN_PLAN_HISTORY_LOG } from "@/constants/url";
import { DefaultResponse, PaginationParams, PaginationResponse, ResponseList } from "@/types/api.common";
import { AxiosResponse } from "axios";
import request from "@/services/request.ts";
import { Ref } from "vue";


export interface ParamsGetPlanLog extends PaginationParams {
    staffId?: string | null;
    blockId?: string | null;
    planId?: string | null;
    semesterId?: string | null;
    roleStaff?: string | null;
    facilityId?: string | null;
    logCodeRole?: string | null;
    toDate?: string | null;
    fromDate?: string | null;
    typeFunction?: number | null;
    status?: number | null;
}

export type GetPlanLogResponse = ResponseList & {
    createDate: string;
    status: string;
    actionName: string;
    codeStaff: string;
    email: string;
    staff: string;
    timeStampDate: number;
    typeFunction: string;
    nameBlock: string;
    namePlan: string;
};

export type GetPlanLogResponseDetail = {
    createDate: string;
    status: string;
    actionName: string;
    codeStaff: string;
    email: string;
    staff: string;
    timeStampDate: number;
    typeFunction: string;
    nameBlock: string;
    namePlan: string;
    endDate:number;
    startDate:number;
    description: string;
    numberOfClass: number;
    numberOfLecture: number;
    formality: number;
    staffInfo: string;
    codeTutorClassDetail: string;
    rejectNote: string;
    studentTutor: string;
    roomPlan: string;
};
  
export const getPlanLogs = async (params: Ref<ParamsGetPlanLog>) => {
    const res = (await request({
      url: `${PREFIX_API_ADMIN_PLAN_HISTORY_LOG}`,
      method: "GET",
      params: params.value,
    })) as AxiosResponse<
        DefaultResponse<PaginationResponse<Array<GetPlanLogResponse>>>
    >;
  
    return res.data;
};


export const getDetailPlanLogs = async (planLogsId: string | null) => {
    const res = (await request({
      url: `${PREFIX_API_ADMIN_PLAN_HISTORY_LOG}/${planLogsId}`,
      method: "GET",
    })) as AxiosResponse<DefaultResponse<GetPlanLogResponseDetail>>;
  
    return res.data;
  };
