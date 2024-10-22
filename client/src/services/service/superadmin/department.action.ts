import { queryKey } from "@/constants/queryKey.ts";
import {
  createDepartment,
  createDepartmentFacility,
  CreateUpdateDepartmentFacilityParams,
  CreateUpdateDepartmentParams,
  getDepartmentFacility,
  getDepartments,
  getDetailDepartment,
  ParamsGetDepartment,
  ParamsGetDepartmentFacility,
  updateDepartment,
  updateDepartmentFacility,
} from "@/services/api/superadmin/department.api";
import {
  useMutation,
  useQuery,
  useQueryClient,
  UseQueryReturnType,
} from "@tanstack/vue-query";
import { Ref } from "vue";

export const useGetDepartment = (
  params: Ref<ParamsGetDepartment>,
  options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getDepartments>>, Error> => {
  return useQuery({
    queryKey: [queryKey.admin.department.departmentList, params],
    queryFn: () => getDepartments(params),
    ...options,
  });
};

export const useGetDepartmentFacility = (
  departmentId: Ref<string | null>,
  params: Ref<ParamsGetDepartmentFacility>,
  options?: any
): UseQueryReturnType<
  Awaited<ReturnType<typeof getDepartmentFacility>>,
  Error
> => {
  return useQuery({
    queryKey: [
      queryKey.admin.departmentFacility.departmentFacilityList,
      departmentId,
      params,
    ],
    queryFn: () => getDepartmentFacility(departmentId, params),
    ...options,
  });
};

export const useCreateDepartment = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (params: CreateUpdateDepartmentParams) =>
      createDepartment(params),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.admin.department.departmentList],
      });
    },
    onError: (error: any) => {
      console.log("🚀 ~ useCreateDepartment ~ error:", error);
    },
  });
};

export const useUpdateDepartment = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({
      departmentId,
      params,
    }: {
      departmentId: string;
      params: CreateUpdateDepartmentParams;
    }) => updateDepartment(departmentId, params),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.admin.department.departmentList],
      });
    },
    onError: (error: any) => {
      console.log("🚀 ~ useUpdateDepartment ~ error:", error);
    },
  });
};

export const useDetailDepartment = (
  departmentId: Ref<string | null>,
  options?: any
): UseQueryReturnType<
  Awaited<ReturnType<typeof getDetailDepartment>>,
  Error
> => {
  return useQuery({
    queryKey: [queryKey.admin.department.departmentDetail, departmentId],
    queryFn: () => getDetailDepartment(departmentId.value),
    ...options,
  });
};

export const useCreateDepartmentFacility = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (params: CreateUpdateDepartmentFacilityParams) =>
      createDepartmentFacility(params),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.admin.departmentFacility.departmentFacilityList],
      });
    },
    onError: (error: any) => {
      console.log("🚀 ~ useCreateDepartmentFacility ~ error:", error);
    },
  });
};

export const useUpdateDepartmentFacility = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({
      id,
      params,
    }: {
      id: string;
      params: CreateUpdateDepartmentFacilityParams;
    }) => updateDepartmentFacility(id, params),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.admin.departmentFacility.departmentFacilityList],
      });
    },
    onError: (error: any) => {
      console.log("🚀 ~ useUpdateDepartmentFacility ~ error:", error);
    },
  });
};
