import { queryKey } from "@/constants/queryKey";
import { getRoles, ParamsGetRoles } from "@/services/api/role.api";
import { useQuery, UseQueryReturnType } from "@tanstack/vue-query";
import { Ref } from "vue";

export const useGetRoles = (
  params: Ref<ParamsGetRoles>,
  options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getRoles>>, Error> => {
  return useQuery({
    queryKey: [queryKey.admin.role.roleList, params],
    queryFn: () => getRoles(params),
    ...options,
  });
};
