import { PREFIX_API_TEACHER_STUDENT_ATTENDANCE } from "@/constants/url";
import request from "@/services/request";
import { DefaultResponse, ResponseList } from "@/types/api.common";
import { AxiosResponse } from "axios";

export type StudentAttendanceResponse = ResponseList & {
    room: string,
    studentCode: string,
    studentName: string,
    studentId: string,
    isAttendance: string,
    attendanceReason: string,
    attendanceTime: number,
    note: string
}

export interface CreateAttendanceRequest {
    studentId: string;
    lectureId: string;
    note: string;
    isPresent: boolean;
}

export const getStudentAttendance = async (
    lectureId: string | null
) => {
    const res = (await request({
        url: `${PREFIX_API_TEACHER_STUDENT_ATTENDANCE}/${lectureId}`,
        method: "GET",
    })) as AxiosResponse<
        DefaultResponse<Array<StudentAttendanceResponse>>
    >;

    return res.data;
};

export const createAttendance = async (params: Array<CreateAttendanceRequest>) => {
    const res = (await request({
        url: `${PREFIX_API_TEACHER_STUDENT_ATTENDANCE}/attendance`,
        method: "PUT",
        data: params,
    })) as AxiosResponse<
        DefaultResponse<DefaultResponse<null>>
    >;

    return res.data;
};