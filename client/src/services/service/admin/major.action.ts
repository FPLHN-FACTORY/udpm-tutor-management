import { queryKey } from "@/constants/queryKey";
import {
    useMutation,
    useQuery,
    useQueryClient,
    UseQueryReturnType,
} from "@tanstack/vue-query";
import { Ref } from "vue";
import { getMajorCampusSynchronize, getMajors, getMajorsFacility, getMajorSynchronize, ParamsGetMajor } from "../../api/admin/major.api";

export const useGetMajors = (
    departmentId: Ref<string | null>,
    params: Ref<ParamsGetMajor>,
    options?: any
  ): UseQueryReturnType<Awaited<ReturnType<typeof getMajors>>, Error> => {
    return useQuery({
      queryKey: [queryKey.admin.major.majorList, departmentId, params],
      queryFn: () => getMajors(departmentId, params),
      ...options,
    });
};

export const useGetMajorsFacility = (
    params: Ref<ParamsGetMajor>,
    options?: any
  ): UseQueryReturnType<Awaited<ReturnType<typeof getMajorsFacility>>, Error> => {
    return useQuery({
      queryKey: [queryKey.admin.majorFacility.majorFacilityList, params],
      queryFn: () => getMajorsFacility(params),
      ...options,
    });
};

export function useMajorSynchronize() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: () => getMajorSynchronize(),
    onSuccess: (data) => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.admin.major.majorSynchronize],
      });
      return data;
    },
    onError: (error) => {
      // Handle error
      console.error('Error during synchronization:', error);
    }
  });
}

export function useMajorCampusSynchronize() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: () => getMajorCampusSynchronize(),
    onSuccess: (data) => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.admin.majorFacility.majorFacilitySynchronize],
      });
      return data;
    },
    onError: (error) => {
      // Handle error
      console.error('Error during synchronization:', error);
      throw error;
    }
  });
}


