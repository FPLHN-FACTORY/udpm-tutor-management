import { queryKey } from "@/constants/queryKey";
import {
  getBlockOptions,
  getDepartmentOptions,
  getFacilityOptions,
  getSemesterOptions,
  getStaffByRole,
  ParamsStaffSearchByRole,
} from "@/services/api/common.api";
import { UseQueryReturnType, useQuery } from "@tanstack/vue-query";
import {Ref} from "vue";

export const useGetDepartmentOptions = (
  query?: string,
  options?: any
): UseQueryReturnType<
  Awaited<ReturnType<typeof getDepartmentOptions>>,
  Error
> => {
  return useQuery({
    queryKey: [queryKey.common.departmentOptions],
    queryFn: () => getDepartmentOptions(query),
    ...options,
  });
};

export const useGetFacilityOptions = (
  query?: string,
  options?: any
): UseQueryReturnType<
  Awaited<ReturnType<typeof getFacilityOptions>>,
  Error
> => {
  return useQuery({
    queryKey: [queryKey.common.facilityOptions],
    queryFn: () => getFacilityOptions(query),
    ...options,
  });
};

export const useGetSemesterOptions = (
    query?: string,
    options?: any
): UseQueryReturnType<
    Awaited<ReturnType<typeof getSemesterOptions>>,
    Error
> => {
  return useQuery({
    queryKey: [queryKey.common.semesterOptions],
    queryFn: () => getSemesterOptions(query),
    ...options,
  });
};

export const useGetBlockOptions = (
    semesterId: string,
    options?: any
): UseQueryReturnType<
    Awaited<ReturnType<typeof getBlockOptions>>,
    Error
> => {
  return useQuery({
    queryKey: [queryKey.common.blockOptions, semesterId],
    queryFn: () => getBlockOptions(semesterId),
    ...options,
  });
};

export const useGetStaffByRoleOptions = (
    params: Ref<ParamsStaffSearchByRole>,
    options?: any
): UseQueryReturnType<
    Awaited<ReturnType<typeof getStaffByRole>>,
    Error
> => {
  return useQuery({
    queryKey: [queryKey.common.staffOptions],
    queryFn: () => getStaffByRole(params.value),
    ...options,
  });
};