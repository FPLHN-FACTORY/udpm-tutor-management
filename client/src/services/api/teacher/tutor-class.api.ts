import { PREFIX_API_TEACHER_TUTOR_CLASS } from "@/constants/url";
import request from "@/services/request";
import { DefaultResponse, PaginationResponse, ResponseList } from "@/types/api.common";
import { AxiosResponse } from "axios";
import { Ref } from "vue";
import { PaginationParams } from "@/types/api.common";

export interface ParamsGetTutorClass extends PaginationParams {
    subjectId?: string | null,
    classCode?: string | null
}

export type TutorClassResponse = ResponseList & {
    totalStudent: number,
    studentName: string,
    classCode: string,
    subjectName: string,
    shift: string,
    startTime: number,
    endTime: number,
}

export const getTutorClasses = async (
    teacherId: string | null,
    params: Ref<ParamsGetTutorClass>
) => {
    const res = (await request({
        url: `${PREFIX_API_TEACHER_TUTOR_CLASS}/${teacherId}`,
        method: "GET",
        params: params.value,
    })) as AxiosResponse<
        DefaultResponse<PaginationResponse<Array<TutorClassResponse>>>
    >;

    return res.data;
};

export interface UpdateTutorClassDetailParams {
    id: string;
    shift: string;
    startTime: number;
    endTime: number;
}

export const updateTutorClassDetail = async (params: Array<UpdateTutorClassDetailParams>) => {
    const res = (await request({
        url: `${PREFIX_API_TEACHER_TUTOR_CLASS}`,
        method: "PUT",
        data: params,
    })) as AxiosResponse<
        DefaultResponse<DefaultResponse<null>>
    >;

    return res.data;
};