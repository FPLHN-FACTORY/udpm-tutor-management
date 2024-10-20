import { queryKey } from "@/constants/queryKey"
import {
    addOrUpdateLectureEvidence,
    AddOrUpdateLectureEvidenceParams,
    getEvidenceLectureDetail,
    getLectures,
    getTutorClasses,
    ParamsGetTutorClass, updateLecture, UpdateLectureParams, updateTutorClassDetail,
    UpdateTutorClassDetailParams
} from "@/services/api/teacher/tutor-class.api"
import {useMutation, useQuery, useQueryClient, UseQueryReturnType} from "@tanstack/vue-query"
import { Ref } from "vue"

export const useGetTutorClassByTeacher = (
    params: Ref<ParamsGetTutorClass>,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getTutorClasses>>, Error> => {
    return useQuery({
        queryKey: [queryKey.teacher.tutorClass.tutorClassDetailList, params],
        queryFn: () => getTutorClasses(params),
        ...options
    })
}

export const useGetLectureByTutorClassDetail = (
    tutorClassDetailId: Ref<string | null>,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getLectures>>, Error> => {
    return useQuery({
        queryKey: [queryKey.teacher.tutorClass.lectureList, tutorClassDetailId],
        queryFn: () => getLectures(tutorClassDetailId.value),
        ...options
    })
}

export const useGetEvidenceLectureDetail = (
    lectureId: Ref<string | null>,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getEvidenceLectureDetail>>, Error> => {
    return useQuery({
        queryKey: [queryKey.teacher.tutorClass.lectureList, lectureId],
        queryFn: () => getEvidenceLectureDetail(lectureId.value),
        ...options
    })
}

export const useUpdateTutorClassDetail = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (params: Array<UpdateTutorClassDetailParams>) => updateTutorClassDetail(params),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [queryKey.teacher.tutorClass.tutorClassDetailList],
            });
        },
        onError: (error: any) => {
            console.log("🚀 ~ useCreatePlan ~ error:", error);
        },
    });
};

export const useUpdateLecture = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (params: Array<UpdateLectureParams>) => updateLecture(params),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [queryKey.teacher.tutorClass.lectureList],
            });
        },
        onError: (error: any) => {
            console.log("🚀 ~ useCreatePlan ~ error:", error);
        },
    });
};


export const useAddOrUpdateLectureEvidence = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (params: AddOrUpdateLectureEvidenceParams) => addOrUpdateLectureEvidence(params),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [queryKey.teacher.tutorClass.tutorClassDetailList],
            });
        },
        onError: (error: any) => {
            console.log("🚀 ~ useCreatePlan ~ error:", error);
        },
    });
};
