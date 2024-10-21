import { PREFIX_API_HEAD_DEPARTMENT_PLANNER } from "@/constants/url";
import request from "@/services/request";
import {
  DefaultResponse,
  PaginationParams,
  PaginationResponse,
  ResponseList,
} from "@/types/api.common";
import { AxiosResponse } from "axios";
import { Ref } from "vue";

export type HeadDepartmentPlannerResponse = ResponseList & {
  staffCode: string;
  staffName: string;
  emailFPT: string;
  emailFE: string;
  isAssigned: number;
};

export interface ParamsGetListPlaner extends PaginationParams {
  currentSemesterId?: string | null;
  currentBlockId?: string | null;
  currentFacilityCode?: string | null;
  currentDepartmentCode?: string | null;
  currentUserId?: string | null;
}

export interface AssignedPlannerRequest {
  currentSemesterId: string | null;
  currentBlockId: string | null;
  currentFacilityCode: string | null;
  currentDepartmentCode: string | null;
  currentUserId: string | null;
}

export type AssignedPlannerResponse = {
  id: string;
};

export const getHeadDepartmentPlanner = async (
  params: Ref<ParamsGetListPlaner>
) => {
  const res = (await request({
    url: `${PREFIX_API_HEAD_DEPARTMENT_PLANNER}`,
    method: "GET",
    params: params.value,
  })) as AxiosResponse<
    DefaultResponse<PaginationResponse<Array<HeadDepartmentPlannerResponse>>>
  >;

  return res.data;
};

export const assignedPlanner = async (
  id: string,
  params: AssignedPlannerRequest
) => {
  const res = (await request({
    url: `${PREFIX_API_HEAD_DEPARTMENT_PLANNER}/${id}`,
    method: "PUT",
    data: params,
  })) as AxiosResponse<DefaultResponse<null>>;

  return res.data;
};

export const unassignedPlanner = async (
    id: string,
  ) => {
    const res = (await request({
      url: `${PREFIX_API_HEAD_DEPARTMENT_PLANNER}/unassigned/${id}`,
      method: "PUT"
    })) as AxiosResponse<DefaultResponse<null>>;
  
    return res.data;
};
