import { Ref } from "vue";
import { useMutation, useQuery, useQueryClient, UseQueryReturnType } from "@tanstack/vue-query";
import { queryKey } from "@/constants/queryKey";
import { changeStatusFacility, createFacility, CreateUpdateFacilityParams, getDetailFacility, getFacilities, getFacilitySynchronize, ParamsGetFacility, updateFacility } from "@/services/api/admin/facility.api";

export const useGetFacility = (
    params: Ref<ParamsGetFacility>,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getFacilities>>, Error> => {
    return useQuery({
        queryKey: [queryKey.admin.facility.facilityList, params],
        queryFn: () => getFacilities(params),
        ...options
    })
}

export const useDetailFacility = (
    facilityId: Ref<string | null>,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getDetailFacility>>, Error> => {
    return useQuery({
        queryKey: [queryKey.admin.subject.subjectDetail, facilityId],
        queryFn: () => getDetailFacility(facilityId.value),
        ...options,
    });
};

export function useFacilitySynchronize() {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: () => getFacilitySynchronize(),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [queryKey.admin.facility.facilitySynchronize],
            });
        },
        onError: (error: any) => {
            // Handle error
            console.error('Error during synchronization:', error);
        }
    });
}


export const useCreateFacility = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (params: CreateUpdateFacilityParams) => createFacility(params),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [queryKey.admin.facility.facilityList],
            });
        },
        onError: (error: any) => {
            console.log("🚀 ~ useCreateFacility ~ error:", error);
        },
    });
};

export const useUpdateFacility = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: ({
            facilityId,
            params,
        }: {
            facilityId: string;
            params: CreateUpdateFacilityParams;
        }) => updateFacility(facilityId, params),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [queryKey.admin.facility.facilityList],
            });
        },
        onError: (error: any) => {
            console.log("🚀 ~ useUpdateFacility ~ error:", error);
        },
    });
};

export const useChangeStatusFacility = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (facilityId: string) => changeStatusFacility(facilityId),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [queryKey.admin.facility.facilityList],
            });
        },
        onError: (error: any) => {
            console.log("🚀 ~ useChangeStatusFacility ~ error:", error);
        },
    });
};