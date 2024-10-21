import {
    createStaff,
    createStaffDepartmentMajor,
    CreateUpdateStaffDepartmentMajorParams,
    CreateUpdateStaffParams,
    getDetailStaff, getDetailStaffDepartmentMajor, getStaffDepartmentMajor,
    getStaffRoles,
    getStaffs, getStaffSynchronize,
    ParamsGetStaff,
    updateStaff,
    updateStaffDepartmentMajor,
    updateStaffPermission,
    UpdateStaffPermissionParams, uploadFileStaff
} from "../../api/admin/staff.api.ts";
import { ComputedRef, Ref } from "vue";
import { useMutation, useQuery, useQueryClient, UseQueryReturnType } from "@tanstack/vue-query";
import { queryKey } from "@/constants/queryKey.ts";

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

export const useCreateStaff = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (params: CreateUpdateStaffParams) => createStaff(params),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [queryKey.admin.staff.staffList],
            });
        },
        onError: (error: any) => {
            console.log("🚀 ~ useCreateStaff ~ error:", error);
        },
    });
};

export const useUpdateStaff = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (params: CreateUpdateStaffParams) => updateStaff(params),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [queryKey.admin.staff.staffList],
            });
        },
        onError: (error: any) => {
            console.log("🚀 ~ useUpdateStaff ~ error:", error);
        },
    });
};

export const useGetRoleByStaff = (
    staffId: string,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getStaffRoles>>, Error> => {
    return useQuery({
        queryKey: [queryKey.admin.staff.staffRole, staffId],
        queryFn: () => getStaffRoles(staffId),
        ...options,
    });
};

export const useUpdateStaffPermission = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (params: UpdateStaffPermissionParams) => updateStaffPermission(params),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [queryKey.admin.staff.staffRole],
            });
        },
        onError: (error: any) => {
            console.log("🚀 ~ useUpdateStaffPermission ~ error:", error);
        },
    });
};


export const useCreateStaffDeparmentMajor = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (params: CreateUpdateStaffDepartmentMajorParams) => createStaffDepartmentMajor(params),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [queryKey.admin.staff.staffDepartmentMajor],
            });
        },
        onError: (error: any) => {
            console.log("🚀 ~ useCreateStaffDeparmentMajor ~ error:", error);
        },
    });
};

export const useUpdateStaffDeparmentMajor = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (params: CreateUpdateStaffDepartmentMajorParams) => updateStaffDepartmentMajor(params),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [queryKey.admin.staff.staffDepartmentMajor],
            });
        },
        onError: (error: any) => {
            console.log("🚀 ~ useUpdateStaffDepartmentMajor ~ error:", error);
        },
    });
};


export const useGetDetailStaffDeparmentMajor = (
    id: Ref<string | null>,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getDetailStaffDepartmentMajor>>, Error> => {
    return useQuery({
        queryKey: [queryKey.admin.staff.staffDepartmentMajorDetail, id],
        queryFn: () => getDetailStaffDepartmentMajor(id.value),
        ...options,
    });
};

export const useUploadFileStaff = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (file: File) => uploadFileStaff(file),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [queryKey.admin.staff.staffList], // Cập nhật query key nếu cần
            });
        },
        onError: (error: any) => {
            console.log("🚀 ~ useUploadFile ~ error:", error);
        },
    });
};

// export const useGetDownloadTemplate = (
//     facilityId: string | null,
//     options?: any
// ): UseQueryReturnType<Awaited<ReturnType<typeof downloadTemplateStaff>>, Error> => {
//     return useQuery({
//         queryKey: [queryKey.admin.staff.staffDownloadTemplate],
//         queryFn: () => downloadTemplateStaff(facilityId),
//         ...options,
//     });
// };