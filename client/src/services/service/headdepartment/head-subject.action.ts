import {
    assignSubjectForHeadSubject,
    checkCurrentSemesterHasHeadSubject,
    getHeadOfSubjects,
    getStaffByHeadSubject,
    getSubjectsByHeadSubject,
    getSubjectsWithAssign,
    ParamsAssignOrUnAssignSubject,
    ParamsGetHeadOfSubjects,
    ParamsGetStaffSubject,
    ParamsGetSubjectsByHeadSubject,
    ParamsGetSubjectsWithAssign,
    ParamsReassignSubjectForAnotherHeadSubject,
    reassignSubjectForAnotherHeadSubject,
    syncHeadSubjectAttachWithSubjectFromPreviousSemesterToCurrentSemester,
    unAssignSubjectForHeadSubject
} from "@/services/api/headdepartment/head-subject.api.ts";
import {queryKey} from "@/constants/queryKey.ts";
import {useMutation, useQuery, useQueryClient, UseQueryReturnType} from "@tanstack/vue-query";
import {Ref} from "vue";

export const useGetHeadOfSubject = (
    params: Ref<ParamsGetHeadOfSubjects>,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getHeadOfSubjects>>, Error> => {
    return useQuery({
        queryKey: [queryKey.headOfDepartment.headOfSubject.headOfSubjectList, params],
        queryFn: () => getHeadOfSubjects(params),
        ...options,
    });
};

export const useGetSubjectByHeadOfSubject = (
    params: Ref<ParamsGetSubjectsByHeadSubject>,
    headSubjectId: Ref<string | null>,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getSubjectsByHeadSubject>>, Error> => {
    return useQuery({
        queryKey: [queryKey.headOfDepartment.headOfSubject.subjectByHeadOfSubject, params],
        queryFn: () => getSubjectsByHeadSubject(params,headSubjectId.value),
        ...options,
    });
};

export const useGetSubjectsWithAssignByHeadOfSubject = (
    params: Ref<ParamsGetSubjectsWithAssign>,
    headSubjectId: Ref<string | null>,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getSubjectsWithAssign>>, Error> => {
    return useQuery({
        queryKey: [queryKey.headOfDepartment.headOfSubject.subjectByHeadOfSubject, params],
        queryFn: () => getSubjectsWithAssign(params,headSubjectId.value),
        ...options,
    });
};

export const useGetStaffByHeadSubject = (
    params: Ref<ParamsGetStaffSubject>,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getStaffByHeadSubject>>, Error> => {
    return useQuery({
        queryKey: [queryKey.headOfDepartment.headOfSubject.staffByHeadOfSubject, params],
        queryFn: () => getStaffByHeadSubject(params),
        ...options,
    });
};

export const useReassignSubjectForAnotherHeadSubject = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (params: ParamsReassignSubjectForAnotherHeadSubject) => reassignSubjectForAnotherHeadSubject(params),
        onSuccess: (data) => {
            if (data.success) {
                queryClient.invalidateQueries({
                    queryKey: [queryKey.headOfDepartment.headOfSubject.headOfSubjectList],
                });
            }
        },
        onError: (error: any) => {
            console.error("🚀 ~ useReassignSubjectForAnotherHeadSubject ~ error:", error);
            // Không hiển thị toast.error ở đây
        },
    });
};

export const useAssignSubjectForHeadSubject = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: ({
                         params,
                         headSubjectId,
                     }: {
            params: ParamsAssignOrUnAssignSubject;
            headSubjectId: string | null;
        })  => assignSubjectForHeadSubject(params, headSubjectId),
        onSuccess: (data) => {
            if (data.success) {
                queryClient.invalidateQueries({
                    queryKey: [queryKey.headOfDepartment.headOfSubject.subjectByHeadOfSubject],
                });
            }
        },
        onError: (error: any) => {
            console.error("🚀 ~ useReassignSubjectForAnotherHeadSubject ~ error:", error);
            // Không hiển thị toast.error ở đây
        },
    });
};

export const useUnAssignSubjectForHeadSubject = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: ({
                         params,
                         headSubjectId,
                     }: {
            params: ParamsAssignOrUnAssignSubject;
            headSubjectId: string | null;
        }) => unAssignSubjectForHeadSubject(params, headSubjectId),
        onSuccess: (data) => {
            if (data.success) {
                queryClient.invalidateQueries({
                    queryKey: [queryKey.headOfDepartment.headOfSubject.subjectByHeadOfSubject],
                });
            }
        },
        onError: (error: any) => {
            console.error("🚀 ~ useReassignSubjectForAnotherHeadSubject ~ error:", error);
            // Không hiển thị toast.error ở đây
        },
    });
};

export const useCheckCurrentSemesterHasHeadSubject = (
    semesterId: Ref<string | null>,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof checkCurrentSemesterHasHeadSubject>>, Error> => {
    return useQuery({
        queryKey: [queryKey.headOfDepartment.headOfSubject.headOfSubjectList],
        queryFn: () => checkCurrentSemesterHasHeadSubject(semesterId.value),
        ...options,
    });
};

export function useSyncHeadSubjectAttach() {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (semesterId: string) => syncHeadSubjectAttachWithSubjectFromPreviousSemesterToCurrentSemester(semesterId),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [queryKey.headOfDepartment.headOfSubject.headOfSubjectList],
            });
        },
        onError: (error: any) => {
            // Handle error
            console.error('Error during synchronization:', error);
        }
    });
}
