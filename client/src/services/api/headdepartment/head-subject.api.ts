import {PREFIX_API_HEAD_OF_SUBJECT_HEAD_DEPARTMENT} from "@/constants/url.ts";
import request from "@/services/request.ts";
import {
    DefaultResponse,
    PaginationParams,
    PaginationResponse,
    ResponseList,
} from "@/types/api.common.ts";
import { AxiosResponse } from "axios";
import {Ref} from "vue";

export interface ParamsGetHeadOfSubjects extends PaginationParams {
    currentSemesterId: string | null,
    currentFacilityCode: string | null,
    currentDepartmentCode: string | null,
    currentUserId: string | null,
}

export interface ParamsGetSubjectsByHeadSubject extends PaginationParams {
    currentSemesterId: string | null,
    currentFacilityCode: string | null,
    currentDepartmentCode: string | null,
};

export interface ParamsGetSubjectsWithAssign extends PaginationParams {
    currentSemesterId: string | null;
    departmentCode: string | null;
    facilityCode: string | null;
    headSubjectId: string | null;
};

export interface ParamsGetStaffSubject {
    currentSemesterId: string | null,
    currentFacilityId: string | null,
    currentDepartmentCode: string | null,
    currentUserId: string | null,
    headSubjectId: string | null;
    q: string | null;
}

export interface ParamsReassignSubjectForAnotherHeadSubject {
    semesterId: string | null,
    facilityId: string | null,
    currentHeadSubjectId: string | null,
    newHeadSubjectId: string | null,
}

export interface ParamsAssignOrUnAssignSubject {
    semesterId: string | null,
    facilityId: string | null,
    subjectId: string | null,
}

export type HeadOfSubjectResponse = ResponseList & {
    staffCode: string;
    staffName: string;
    emailFPT: string;
    emailFE: string;
    isAssigned: number;
    assignedCount: number;
};

export type SubjectsByHeadSubjectResponse = ResponseList & {
    subjectCode: string;
    subjectName: string;
    departmentName: string;
    subjectType: string;
    subjectStatus: string;
};

export type SubjectsWithAssignResponse = ResponseList & {
    subjectCode: string;
    subjectName: string;
    subjectType: string;
    isAssigned: string;
};

export type StaffSubjectResponse = {
    staffInfo: string;
    code: string;
};

export const getHeadOfSubjects = async (params: Ref<ParamsGetHeadOfSubjects>) => {
    const res = (await request({
        url: `${PREFIX_API_HEAD_OF_SUBJECT_HEAD_DEPARTMENT}`,
        method: "GET",
        params: params.value,
    })) as AxiosResponse<
        DefaultResponse<PaginationResponse<Array<HeadOfSubjectResponse>>>
    >;

    return res.data;
};

export const getSubjectsByHeadSubject = async (
    params: Ref<ParamsGetSubjectsByHeadSubject>,
    headSubjectId: string | null) => {
    const res = (await request({
        url: `${PREFIX_API_HEAD_OF_SUBJECT_HEAD_DEPARTMENT}/${headSubjectId}/subjects`,
        method: "GET",
        params: params.value,
    })) as AxiosResponse<
        DefaultResponse<PaginationResponse<Array<SubjectsByHeadSubjectResponse>>>
    >;

    return res.data;
};

export const getSubjectsWithAssign = async (
    params: Ref<ParamsGetSubjectsWithAssign>,
    headSubjectId: string | null) => {
    const res = (await request({
        url: `${PREFIX_API_HEAD_OF_SUBJECT_HEAD_DEPARTMENT}/${headSubjectId}/subjects/assign`,
        method: "GET",
        params: params.value,
    })) as AxiosResponse<
        DefaultResponse<PaginationResponse<Array<SubjectsByHeadSubjectResponse>>>
    >;

    return res.data;
};

export const getStaffByHeadSubject = async (
    params: Ref<ParamsGetStaffSubject>) => {
    const res = (await request({
        url: `${PREFIX_API_HEAD_OF_SUBJECT_HEAD_DEPARTMENT}/staff/search`,
        method: "GET",
        params: params.value,
    })) as AxiosResponse<
        DefaultResponse<Array<StaffSubjectResponse>>
    >;
    return res.data;
};

export const reassignSubjectForAnotherHeadSubject = async (
    params: ParamsReassignSubjectForAnotherHeadSubject
) => {
     const res = await request({
        url: `${PREFIX_API_HEAD_OF_SUBJECT_HEAD_DEPARTMENT}/subjects/reassign`,
        method: "PUT",
        data: params,
     }) as AxiosResponse<{ status: string; data: string; message: string; timestamp: string; success: boolean }>;

     return res.data; // Bây giờ res.data sẽ là "OK"
};

export const assignSubjectForHeadSubject = async (
    params: ParamsAssignOrUnAssignSubject,
    headSubjectId: string | null
) => {
    const res = await request({
        url: `${PREFIX_API_HEAD_OF_SUBJECT_HEAD_DEPARTMENT}/${headSubjectId}/subjects/assign`,
        method: "PUT",
        data: params,
    }) as AxiosResponse<{ status: string; data: string; message: string; timestamp: string; success: boolean }>;

    return res.data; // Bây giờ res.data sẽ là "OK"
};

export const unAssignSubjectForHeadSubject = async (
    params: ParamsAssignOrUnAssignSubject,
    headSubjectId: string | null
) => {
   const res = await request({
        url: `${PREFIX_API_HEAD_OF_SUBJECT_HEAD_DEPARTMENT}/${headSubjectId}/subjects/assign`,
        method: "DELETE",
        data: params,
   }) as AxiosResponse<{ status: string; data: string; message: string; timestamp: string; success: boolean }>;

   return res.data; // Bây giờ res.data sẽ là "OK"
};

export const checkCurrentSemesterHasHeadSubject = async (
    semesterId: string | null
) => {
    try {
        const res = await request({
            url: `${PREFIX_API_HEAD_OF_SUBJECT_HEAD_DEPARTMENT}/subjects/can-sync`,
            method: "GET",
            params: { semesterId },
        }) as AxiosResponse< DefaultResponse<Object>>;

        return res.data; // Bây giờ res.data sẽ là "OK"
    } catch (error) {
        console.error("Error in reassignSubjectForAnotherHeadSubject:", error);
        throw error;  // Ném lại lỗi để xử lý ở nơi gọi
    }
};

export const syncHeadSubjectAttachWithSubjectFromPreviousSemesterToCurrentSemester = async (
    semesterId: string
) => {
    const res = await request({
        url: `${PREFIX_API_HEAD_OF_SUBJECT_HEAD_DEPARTMENT}/subjects/sync`,
        method: "GET",
        params: { semesterId },
    }) as AxiosResponse<DefaultResponse<Object>>;

    return res.data; // res.data sẽ là phản hồi từ API (VD: "OK")
};

