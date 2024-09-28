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
        url: `${PREFIX_API_HEAD_OF_PLAN_HEAD_DEPARTMENT}/${planId}`,
        method: "PUT",
    })) as AxiosResponse<
        DefaultResponse<DefaultResponse<null>>
    >;

    return res.data;
};
