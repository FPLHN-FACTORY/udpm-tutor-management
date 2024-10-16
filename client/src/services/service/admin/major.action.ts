import { queryKey } from "@/constants/queryKey";
import {
    useMutation,
    useQuery,
    useQueryClient,
    UseQueryReturnType,
} from "@tanstack/vue-query";
import { Ref } from "vue";
import { createMajor, createMajorFacility, CreateUpdateMajorFacilityParams, CreateUpdateMajorParams, getDetailMajor, getDetailMajorFacility, getMajorCampusSynchronize, getMajors, getMajorsFacility, getMajorSynchronize, ParamsGetMajor, updateMajor, updateMajorFacility } from "../../api/admin/major.api";

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

export const useGetDetailMajor = (
  majorId: Ref<string | null>,
  options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getDetailMajor>>, Error> => {
  return useQuery({
    queryKey: [queryKey.admin.major.majorDetail],
    queryFn: () => getDetailMajor(majorId.value),
    ...options,
  });
};

export const useGetDetailMajorFacility = (
  majorFacilityId: Ref<string | null>,
  options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getDetailMajorFacility>>, Error> => {
  return useQuery({
    queryKey: [queryKey.admin.majorFacility.majorFacilityDetail],
    queryFn: () => getDetailMajorFacility(majorFacilityId.value),
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
    onError: (error: any) => {
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
    onError: (error: any) => {
      throw error;
    }
  });
}

export const useCreateMajor = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (params: CreateUpdateMajorParams) =>
      createMajor(params),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.admin.major.majorList],
      });
    },
    onError: (error: any) => {
      console.log("🚀 ~ useCreateMajor ~ error:", error);
    },
  });
};

export const useUpdateMajor = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({
      majorId,
      params,
    }: {
      majorId: string;
      params: CreateUpdateMajorParams;
    }) => updateMajor(majorId, params),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.admin.major.majorList],
      });
    },
    onError: (error: any) => {
      console.log("🚀 ~ useUpdateMajor ~ error:", error);
    },
  });
};

export const useCreateMajorFacility = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (params: CreateUpdateMajorFacilityParams) =>
      createMajorFacility(params),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.admin.majorFacility.majorFacilityList],
      });
    },
    onError: (error: any) => {
      console.log("🚀 ~ useCreateMajorFacility ~ error:", error);
    },
  });
};

export const useUpdateMajorFacility = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({
      majorFacilityId,
      params,
    }: {
      majorFacilityId: string;
      params: CreateUpdateMajorFacilityParams;
    }) => updateMajorFacility(majorFacilityId, params),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.admin.majorFacility.majorFacilityList],
      });
    },
    onError: (error: any) => {
      console.log("🚀 ~ useUpdateMajorFacility ~ error:", error);
    },
  });
};

