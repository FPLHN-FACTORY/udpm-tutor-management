import { queryKey } from "@/constants/queryKey";
import { getDepartmentOptions } from "@/services/api/common.api";
import { UseQueryReturnType, useQuery } from "@tanstack/vue-query";

export const useGetDepartmentOptions = (
  options?: any
): UseQueryReturnType<
  Awaited<ReturnType<typeof getDepartmentOptions>>,
  Error
> => {
  return useQuery({
    queryKey: [queryKey.common.departmentOptions],
    queryFn: () => getDepartmentOptions(),
    ...options,
  });
};
