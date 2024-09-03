import { queryKey } from "@/constants/queryKey";
import { getDepartmentOptions } from "@/services/api/common.api";
import { UseQueryReturnType, useQuery } from "@tanstack/vue-query";

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
  Awaited<ReturnType<typeof getDepartmentOptions>>,
  Error
> => {
  return useQuery({
    queryKey: [queryKey.common.facilityOptions],
    queryFn: () => getDepartmentOptions(query),
    ...options,
  });
};
