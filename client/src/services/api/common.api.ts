import { PREFIX_API_COMMON } from "@/constants/url";
import request from "@/services/request";
import { DefaultResponse } from "@/types/api.common";
import { AxiosResponse } from "axios";

export type CommonOptionsResponse = {
  id: string;
  name: string;
};

export const getDepartmentOptions = async (query?: string) => {
  const res = (await request({
    url: `${PREFIX_API_COMMON}/department`,
    params: {
      query,
    },
    method: "GET",
  })) as AxiosResponse<DefaultResponse<Array<CommonOptionsResponse>>>;

  return res.data;
};

export const getFacilityOptions = async (query?: string) => {
  const res = (await request({
    url: `${PREFIX_API_COMMON}/facility`,
    params: {
      query,
    },
    method: "GET",
  })) as AxiosResponse<DefaultResponse<Array<CommonOptionsResponse>>>;

  return res.data;
};

export const getSemesterOptions = async (query?: string) => {
  const res = (await request({
    url: `${PREFIX_API_COMMON}/semester`,
    params: {
      query,
    },
    method: "GET",
  })) as AxiosResponse<DefaultResponse<Array<CommonOptionsResponse>>>;

  return res.data;
};

export const getBlockOptions = async (semesterId?: string) => {
  const res = (await request({
    url: `${PREFIX_API_COMMON}/block`,
    params: {
      semesterId,
    },
    method: "GET",
  })) as AxiosResponse<DefaultResponse<Array<CommonOptionsResponse>>>;

  return res.data;
};
