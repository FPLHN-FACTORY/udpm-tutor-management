import { Ref } from "vue";
import { getDetailFacilityChild, getFacilityChilds, ParamsGetFacilityChild } from "../api/facility-child.api";
import { useQuery, UseQueryReturnType } from "@tanstack/vue-query";
import { queryKey } from "@/constants/queryKey";

export const useGetFacilityChild = (
    params: Ref<ParamsGetFacilityChild>,
    facilityId: Ref<string | null>,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getFacilityChilds>>, Error> => {
    return useQuery({
        queryKey: [queryKey.admin.facilityChild.facilityChildList, params],
        queryFn: () => getFacilityChilds(params, facilityId.value),
        ...options
    })
}

export const useDetailFacilityChild = (
    facilityChildId: Ref<string | null>,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getDetailFacilityChild>>, Error> => {
    return useQuery({
        queryKey: [queryKey.admin.subject.subjectDetail, facilityChildId],
        queryFn: () => getDetailFacilityChild(facilityChildId.value),
        ...options,
    });
};