import { queryKey } from "@/constants/queryKey"
import { createAttendance, CreateAttendanceRequest, getStudentAttendance } from "@/services/api/teacher/student-attendance.api"
import { useMutation, useQuery, useQueryClient, UseQueryReturnType } from "@tanstack/vue-query"
import { Ref } from "vue"

export const useGetStudentAttendance = (
    lectureId: Ref<string | null>,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getStudentAttendance>>, Error> => {
    return useQuery({
        queryKey: [queryKey.teacher.studentAttendance.studentAttendanceList, lectureId],
        queryFn: () => getStudentAttendance(lectureId.value),
        ...options
    })
}


export const useCreateAttendance = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (params: Array<CreateAttendanceRequest>) => createAttendance(params),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [queryKey.teacher.studentAttendance.studentAttendanceList],
            });
        },
        onError: (error: any) => {
            console.log("🚀 ~ useCreatePlan ~ error:", error);
        },
    });
};
