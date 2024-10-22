import { queryKey } from "@/constants/queryKey";
import {
  createMajor,
  createMajorFacility,
  CreateUpdateMajorFacilityParams,
  CreateUpdateMajorParams,
  getDetailMajor,
  getDetailMajorFacility,
  getMajors,
  getMajorsFacility,
  ParamsGetMajor,
  updateMajor,
  updateMajorFacility,
} from "@/services/api/superadmin/major.api";
import {
  useMutation,
  useQuery,
  useQueryClient,
  UseQueryReturnType,
} from "@tanstack/vue-query";
import { Ref } from "vue";

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
): UseQueryReturnType<
  Awaited<ReturnType<typeof getDetailMajorFacility>>,
  Error
> => {
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

export const useCreateMajor = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (params: CreateUpdateMajorParams) => createMajor(params),
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
