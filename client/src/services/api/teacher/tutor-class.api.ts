import { PREFIX_API_TEACHER_TUTOR_CLASS } from "@/constants/url";
import request from "@/services/request";
import { DefaultResponse, PaginationResponse, ResponseList } from "@/types/api.common";
import { AxiosResponse } from "axios";
import { Ref } from "vue";
import { PaginationParams } from "@/types/api.common";

export interface ParamsGetTutorClass extends PaginationParams {
    subjectId?: string | null,
    classCode?: string | null,
    teacherId?: string | null
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

export type EvidenceLectureDetail = ResponseList & {
    lectureId: number,
    evidenceLink: string,
    exerciseLink: string,
    rerecordLink: string,
    driveLink: string
}

export type LectureResponse = ResponseList & {
    lectureContent: string,
    shift: string,
    name: string,
    recordLink: string,
    lectureType: string,
    exerciseLink: string,
    evidenceLink: string,
    startTime: number,
    lectureStatus: string,
    format: string,
    status: string,
}

export const getTutorClasses = async (
    params: Ref<ParamsGetTutorClass>
) => {
    const res = (await request({
        url: `${PREFIX_API_TEACHER_TUTOR_CLASS}`,
        method: "GET",
        params: params.value,
    })) as AxiosResponse<
        DefaultResponse<PaginationResponse<Array<TutorClassResponse>>>
    >;

    return res.data;
};

export const getLectures = async (
    tutorClassDetailId: string | null
) => {
    const res = (await request({
        url: `${PREFIX_API_TEACHER_TUTOR_CLASS}/${tutorClassDetailId}`,
        method: "GET",
    })) as AxiosResponse<
        DefaultResponse<Array<LectureResponse>>
    >;

    return res.data;
};

export interface UpdateTutorClassDetailParams {
    id: string;
    shift: string;
}

export interface UpdateLectureParams {
    id: string;
    shift: string;
    startTime: number;
    format: string;
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

export const getEvidenceLectureDetail = async (
    lectureId: string | null
) => {
    const res = (await request({
        url: `${PREFIX_API_TEACHER_TUTOR_CLASS}/lecture/${lectureId}`,
        method: "GET",
    })) as AxiosResponse<
        DefaultResponse<Array<EvidenceLectureDetail>>
    >;

    return res.data;
};


export const updateLecture = async (params: Array<UpdateLectureParams>) => {
    const res = (await request({
        url: `${PREFIX_API_TEACHER_TUTOR_CLASS}/lecture`,
        method: "PUT",
        data: params,
    })) as AxiosResponse<
        DefaultResponse<DefaultResponse<null>>
    >;

    return res.data;
};

export interface AddOrUpdateLectureEvidenceParams {
    file: File | null;
    lectureId: String | null;
    evidenceLink: String | null;
    exerciseLink: String | null ;
    recordLink: String | null;
    driveLink: String | null;
}

export const addOrUpdateLectureEvidence = async (params: AddOrUpdateLectureEvidenceParams) => {
    const formData = new FormData();
 
    if (params.file) {
       formData.append('file', params.file);
    }
    if (params.lectureId) formData.append('lectureId', params.lectureId as string);
    if (params.evidenceLink) formData.append('evidenceLink', params.evidenceLink as string);
    if (params.exerciseLink) formData.append('exerciseLink', params.exerciseLink as string);
    if (params.recordLink) formData.append('recordLink', params.recordLink as string);
 
    const res = await request({
       url: `${PREFIX_API_TEACHER_TUTOR_CLASS}/lecture-evidence`,
       method: 'PUT',
       data: formData,
       headers: {
          'Content-Type': 'multipart/form-data',
       },
    }) as AxiosResponse<DefaultResponse<null>>;
    return res.data;
 };