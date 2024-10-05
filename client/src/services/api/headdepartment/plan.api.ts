import { PREFIX_API_HEAD_OF_PLAN_HEAD_DEPARTMENT } from "@/constants/url";
import request from "@/services/request";
import { DefaultResponse, PaginationParams, PaginationResponse, ResponseList } from "@/types/api.common";
import { AxiosResponse } from "axios";
import { Ref } from "vue";

export interface ParamsGetPlans extends PaginationParams {
    semesterId?: string | null;
    facilityCode?: string | null;
}

export type HeadPlanResponse = ResponseList & {
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
        url: `${PREFIX_API_HEAD_OF_PLAN_HEAD_DEPARTMENT}`,
        method: "GET",
        params: params.value,
    })) as AxiosResponse<
        DefaultResponse<PaginationResponse<Array<HeadPlanResponse>>>
    >;

    return res.data;
};

export const approvePlan = async (planId: string) => {
    const res = (await request({
        url: `${PREFIX_API_HEAD_OF_PLAN_HEAD_DEPARTMENT}/status/${planId}`,
        method: "PUT",
    })) as AxiosResponse<
        DefaultResponse<DefaultResponse<null>>
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
        url: `${PREFIX_API_HEAD_OF_PLAN_HEAD_DEPARTMENT}/semester`,
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
        url: `${PREFIX_API_HEAD_OF_PLAN_HEAD_DEPARTMENT}/info`,
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
        url: `${PREFIX_API_HEAD_OF_PLAN_HEAD_DEPARTMENT}/info/${planId}`,
        method: "GET",
    })) as AxiosResponse<
        DefaultResponse<DefaultResponse<PlanInfoDetailResponse>>
    >;

    return res.data;
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
        url: `${PREFIX_API_HEAD_OF_PLAN_HEAD_DEPARTMENT}/${planId}`,
        method: "GET",
    })) as AxiosResponse<DefaultResponse<DetailPlanResponse>>;

    return res.data;
};