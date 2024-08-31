import { PREFIX_API_SUBJECT_ADMIN } from "@/constants/url";
import request from "@/services/request";
import { DefaultResponse } from "@/types/api.common";
import { AxiosResponse } from "axios";

export type DepartmentOptions = {
  departmentId: string;
  departmentName: string;
};

export const getDepartmentOptions = async () => {
  const res = (await request({
    url: `${PREFIX_API_SUBJECT_ADMIN}/department`,
    method: "GET",
  })) as AxiosResponse<DefaultResponse<Array<DepartmentOptions>>>;

  return res.data;
};
