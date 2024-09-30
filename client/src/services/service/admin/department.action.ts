import { queryKey } from "@/constants/queryKey.ts";
import {
  createDepartment,
  CreateUpdateDepartmentParams,
  getDepartmentCampusSynchronize,
  getDepartmentFacility,
  getDepartments,
  getDepartmentSynchronize,
  getDetailDepartment,
  ParamsGetDepartment,
  ParamsGetDepartmentFacility,
  updateDepartment,
} from "../../api/admin/department.api.ts";
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
    onError: (error) => {
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
    onError: (error) => {
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

export function useDepartmentSynchronize() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: () => getDepartmentSynchronize(),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.admin.department.departmentSynchronize],
      });
    },
    onError: (error) => {
      // Handle error
      console.error("Error during synchronization:", error);
    },
  });
}

export function useDepartmentCampusSynchronize() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: () => getDepartmentCampusSynchronize(),
    onSuccess: (data) => {
      queryClient.invalidateQueries({
        queryKey: [
          queryKey.admin.departmentFacility.departmentFacilitySynchronize,
        ],
      });
      return data;
    },
    onError: (error) => {
      console.error("Error during synchronization:", error);
    },
  });
}
