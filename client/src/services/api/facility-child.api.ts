import { DefaultResponse, PaginationParams, PaginationResponse, ResponseList } from "@/types/api.common";
import request from "../request";
import { Ref } from "vue";
import { PREFIX_API_FACILITY_ADMIN, PREFIX_API_FACILITY_CHILD_ADMIN } from "@/constants/url";
import { AxiosResponse } from "axios";
import { FacilityDetailResponse, FacilityResponse } from "./facility.api";

export interface ParamsGetFacilityChild extends PaginationParams {

}

export type FacilityChildResponse = ResponseList & {
    facilityChildName: string,
    facilityChildCode: string,
    facilityChildStatus: number,
    createdDate: number
}

export type FacilityChildDetailResponse = {
    id: string,
    facilityChildName: string,
    facilityChildCode: string,
    facilityChildStatus: number,
    createdDate: number
}

export const getFacilityChilds = async (parmas: Ref<ParamsGetFacilityChild>, facilityId: string | null) => {

    const res = (await request({
        url: `${PREFIX_API_FACILITY_ADMIN}/${facilityId}/facility-child`,
        method: 'GET',
        params: parmas.value
    })) as AxiosResponse<
        DefaultResponse<PaginationResponse<Array<FacilityChildResponse>>>
    >

    return res.data;
}

export const getDetailFacilityChild = async (facilityChildId: string | null) => {
    const res = (await request({
        url: `${PREFIX_API_FACILITY_CHILD_ADMIN}/${facilityChildId}`,
        method: "GET",
    })) as AxiosResponse<DefaultResponse<FacilityDetailResponse>>;

    return res.data;
};