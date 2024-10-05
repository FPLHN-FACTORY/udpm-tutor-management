import {
    getDetailStaff, getStaffDepartmentMajor,
    getStaffRoles,
    getStaffs, getStaffSynchronize,
    ParamsGetStaff
} from "../../api/admin/staff.api.ts";
import {ComputedRef, Ref} from "vue";
import {useMutation, useQuery, useQueryClient, UseQueryReturnType} from "@tanstack/vue-query";
import {queryKey} from "@/constants/queryKey.ts";

export const useGetStaff = (
    params: Ref<ParamsGetStaff>,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getStaffs>>, Error> => {
    return useQuery({
        queryKey: [queryKey.admin.staff.staffList, params],
        queryFn: () => getStaffs(params),
        ...options,
    });
};

export const useDetailStaff = (
    staffId: ComputedRef<string | null>,
    options?: any
): UseQueryReturnType<
    Awaited<ReturnType<typeof getDetailStaff>>,
    Error
> => {
    return useQuery({
        queryKey: [queryKey.admin.staff.staffDetail, staffId],
        queryFn: () => getDetailStaff(staffId.value),
        ...options,
    });
};

export const useGetStaffRole = (
    staffId: ComputedRef<string | null>,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getStaffRoles>>, Error> => {
    return useQuery({
        queryKey: [queryKey.admin.staff.staffRole, staffId],
        queryFn: () => getStaffRoles(staffId.value),
        ...options,
    });
};

export const useGetStaffDepartmentMajor = (
    staffId: ComputedRef<string | null>,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getStaffDepartmentMajor>>, Error> => {
    return useQuery({
        queryKey: [queryKey.admin.staff.staffDepartmentMajor, staffId],
        queryFn: () => getStaffDepartmentMajor(staffId.value),
        ...options,
    });
};

export function useStaffSynchronize() {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (campusCode: string | null) => getStaffSynchronize(campusCode),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [queryKey.admin.staff.staffList],
            });
        },
        onError: (error: any) => {
            // Handle error
            console.error('Error during synchronization:', error?.response?.data?.message);
        }
    });
}


