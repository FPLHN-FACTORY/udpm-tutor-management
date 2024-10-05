import { queryKey } from "@/constants/queryKey"
import { getTutorClasses, ParamsGetTutorClass } from "@/services/api/teacher/tutor-class.api"
import { useQuery, UseQueryReturnType } from "@tanstack/vue-query"
import { Ref } from "vue"

export const useGetTutorClassByTeacher = (
    teacherId: string | null,
    params: Ref<ParamsGetTutorClass>,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getTutorClasses>>, Error> => {
    return useQuery({
        queryKey: [queryKey.teacher.tutorClass.tutorClassList, params],
        queryFn: () => getTutorClasses(teacherId, params),
        ...options
    })
}