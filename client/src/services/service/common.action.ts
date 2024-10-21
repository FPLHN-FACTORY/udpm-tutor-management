import { queryKey } from "@/constants/queryKey";
import {
  getBlockOptions,
  getDepartmentOptions,
  getFacilityOptions,
  getSemesterOptions,
  getStaffByRole,
  ParamsStaffSearchByRole,
  getSubjectOptions, getStudentTutor,
  getRoleOptions,
  ParamsRoleSearch,
  ParamsMajorOption,
  getMajorOptions,
  ParamsStaffOptions,
  getStaffOptions
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
  semesterId: string | null,
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

export const useGetSubjectOptions = (
    query?: string,
    options?: any
): UseQueryReturnType<
    Awaited<ReturnType<typeof getSubjectOptions>>,
    Error
> => {
    return useQuery({
        queryKey: [queryKey.common.subjectOptions],
        queryFn: () => getSubjectOptions(query),
        ...options,
    });
};

export const useGetStudentTutorOptions = (
    options?: any
): UseQueryReturnType<
    Awaited<ReturnType<typeof getStudentTutor>>,
    Error
> => {
  return useQuery({
    queryKey: [queryKey.common.studentOptions],
    queryFn: () => getStudentTutor(),
    ...options,
  });
};

export const useGetRoleOptions = (
  params: ParamsRoleSearch,
  options?: any
): UseQueryReturnType<
  Awaited<ReturnType<typeof getRoleOptions>>,
  Error
> => {
  return useQuery({
      queryKey: [queryKey.common.roleOptions],
      queryFn: () => getRoleOptions(params),
      ...options,
  });
};

export const useGetMajorOptions = (
  params: Ref<ParamsMajorOption>,
  options?: any
): UseQueryReturnType<
  Awaited<ReturnType<typeof getStaffByRole>>,
  Error
> => {
return useQuery({
  queryKey: [queryKey.common.majorOptions],
  queryFn: () => getMajorOptions(params.value),
  ...options,
});
};

export const useGetStaffOptions = (
  params: Ref<ParamsStaffOptions>,
  options?: any
): UseQueryReturnType<
  Awaited<ReturnType<typeof getStaffOptions>>,
  Error
> => {
return useQuery({
  queryKey: [queryKey.common.staffOptions],
  queryFn: () => getStaffOptions(params.value),
  ...options,
});
};