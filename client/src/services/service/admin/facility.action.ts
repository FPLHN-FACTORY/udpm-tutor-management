import { Ref } from "vue";
import { useMutation, useQuery, useQueryClient, UseQueryReturnType } from "@tanstack/vue-query";
import { queryKey } from "@/constants/queryKey";
import { getDetailFacility, getFacilities, getFacilitySynchronize, ParamsGetFacility } from "@/services/api/admin/facility.api";

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