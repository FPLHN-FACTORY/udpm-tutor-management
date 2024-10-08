import { PREFIX_API_NOTIFICATION } from "@/constants/url";
import request from "@/services/request";
import { DefaultResponse, PaginationParams, PaginationResponse } from "@/types/api.common";
import { AxiosResponse } from "axios";
import { Ref } from "vue";

export interface ParamsGetNotification extends PaginationParams {
    keyWord: string
    departmentCode: string
    facilityCode: string
    userId: string
}

export type NotificationResponse = {
    id: string;
    content: string;
    status: boolean;
    createdDate: number
};

export interface ParamsReadNotification {
    userId: string
}

export const getNotifications = async (parmas: Ref<ParamsGetNotification>) => {
    const res = (await request({
        url: `${PREFIX_API_NOTIFICATION}`,
        method: 'GET',
        params: parmas.value
    })) as AxiosResponse<
        DefaultResponse<PaginationResponse<Array<NotificationResponse>>>
    >

    return res.data;
}

export const getCountNotifications = async (parmas: Ref<ParamsGetNotification>) => {
    const res = (await request({
        url: `${PREFIX_API_NOTIFICATION}/count`,
        method: 'GET',
        params: parmas.value
    })) as AxiosResponse<
        DefaultResponse<Number>
    >

    return res.data;
}

export const readNotification = async (
    id: string,
    params: Ref<ParamsReadNotification>
) => {
  const res = (await request({
    url: `${PREFIX_API_NOTIFICATION}/${id}`,
    method: "PUT",
    data: params.value
  })) as AxiosResponse<DefaultResponse<null>>;

  return res.data;
};