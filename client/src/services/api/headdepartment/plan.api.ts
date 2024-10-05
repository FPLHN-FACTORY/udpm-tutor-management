import { PREFIX_API_HEAD_DEPARTMENT_PLAN } from "@/constants/url";
import request from "@/services/request";
import { DefaultResponse, PaginationParams, PaginationResponse, ResponseList } from "@/types/api.common";
import { AxiosResponse } from "axios";
import { Ref } from "vue";

export interface ParamsGetPlans extends PaginationParams {
    semesterId?: string | null;
    facilityCode?: string | null;
    departmentCode?: string | null;
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
        url: `${PREFIX_API_HEAD_DEPARTMENT_PLAN}`,
        method: "GET",
        params: params.value,
    })) as AxiosResponse<
        DefaultResponse<PaginationResponse<Array<PlanResponse>>>
    >;

    return res.data;
};

export const approvePlan = async (planId: string) => {
    const res = (await request({
        url: `${PREFIX_API_HEAD_DEPARTMENT_PLAN}/status/${planId}`,
        method: "PUT",
    })) as AxiosResponse<
        DefaultResponse<DefaultResponse<null>>
    >;

    return res.data;
};

export interface ParamsRejectPlan {
    planId?: string | null;
    reason?: string | null;
}

export const rejectPlan = async (params: ParamsRejectPlan) => {
    const res = (await request({
        url: `${PREFIX_API_HEAD_DEPARTMENT_PLAN}/reject`,
        method: "PUT",
        data: params,
    })) as AxiosResponse<
        DefaultResponse<null>
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
        url: `${PREFIX_API_HEAD_DEPARTMENT_PLAN}/semester`,
        method: "GET",
        params: params,
    })) as AxiosResponse<
        DefaultResponse<SemesterInfoResponse>
    >;

    return res.data;
};

export type PlanInfoResponse = {
    name: string;
    status: string;
    facilityName: string;
    numberSubjects: number;
    numberClasses: number;
    blockName: string;
    startTime: number;
    endTime: number;
    numberStudents: number;
    numberTeachers: number;
    addedSubjects: string;
    notAddedSubjects: string;
    rating: number;
};

export type TutorClassResponse = ResponseList & {
    subjectCode: string;
    subjectId: string;
    status: number;
    subjectName: string;
    numberClasses: number;
};

export const getPlanInfo = async (params: ParamsGetPlans) => {
    const res = (await request({
        url: `${PREFIX_API_HEAD_DEPARTMENT_PLAN}/info`,
        method: "GET",
        params: params,
    })) as AxiosResponse<
        DefaultResponse<Array<PlanInfoResponse>>
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
    startTime: number;
    endTime: number;
};

export const getPlanInfoById = async (planId: string | null) => {
    const res = (await request({
        url: `${PREFIX_API_HEAD_DEPARTMENT_PLAN}/info/${planId}`,
        method: "GET",
    })) as AxiosResponse<
        DefaultResponse<PlanInfoDetailResponse>
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
        url: `${PREFIX_API_HEAD_DEPARTMENT_PLAN}/${planId}`,
        method: "GET",
    })) as AxiosResponse<DefaultResponse<DetailPlanResponse>>;

    return res.data;
};