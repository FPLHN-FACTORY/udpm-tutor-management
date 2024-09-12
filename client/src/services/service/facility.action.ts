import { Ref } from "vue";
import { getDetailFacility, getFacilities, ParamsGetFacility } from "../api/facility.api";
import { useQuery, UseQueryReturnType } from "@tanstack/vue-query";
import { queryKey } from "@/constants/queryKey";

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