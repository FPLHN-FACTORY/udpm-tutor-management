import { DefaultResponse, PaginationParams, PaginationResponse, ResponseList } from "@/types/api.common";
import { Ref } from "vue";
import request from "../request";
import { PREFIX_API_FACILITY_ADMIN } from "@/constants/url";
import { AxiosResponse } from "axios";

export interface ParamsGetFacility extends PaginationParams {
    name?: string,
    status?: string
}

export type FacilityResponse = ResponseList & {
    facilityName: string,
    facilityCode: string,
    facilityStatus: number,
    createdDate: number
}

export type FacilityDetailResponse = {
    id: string,
    facilityName: string,
    facilityCode: string,
    facilityStatus: number,
    createdDate: number
}

export interface CreateUpdateFacilityParams {
    facilityName: string
}

export const getFacilities = async (parmas: Ref<ParamsGetFacility>) => {
    const res = (await request({
        url: `${PREFIX_API_FACILITY_ADMIN}`,
        method: 'GET',
        params: parmas.value
    })) as AxiosResponse<
        DefaultResponse<PaginationResponse<Array<FacilityResponse>>>
    >

    return res.data;
}

export const getDetailFacility = async (facilityId: string | null) => {
    const res = (await request({
        url: `${PREFIX_API_FACILITY_ADMIN}/${facilityId}`,
        method: "GET",
    })) as AxiosResponse<DefaultResponse<FacilityDetailResponse>>;

    return res.data;
};