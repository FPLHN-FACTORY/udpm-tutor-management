import { queryKey } from "@/constants/queryKey"
import {
    getTutorClasses,
    ParamsGetTutorClass, updateTutorClassDetail,
    UpdateTutorClassDetailParams
} from "@/services/api/teacher/tutor-class.api"
import {useMutation, useQuery, useQueryClient, UseQueryReturnType} from "@tanstack/vue-query"
import { Ref } from "vue"

export const useGetTutorClassByTeacher = (
    teacherId: string | null,
    params: Ref<ParamsGetTutorClass>,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getTutorClasses>>, Error> => {
    return useQuery({
        queryKey: [queryKey.teacher.tutorClass.tutorClassDetailList, params],
        queryFn: () => getTutorClasses(teacherId, params),
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
