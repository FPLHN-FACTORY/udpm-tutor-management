import { queryKey } from "@/constants/queryKey.ts";
import { getRoles, ParamsGetRoles } from "../../api/admin/role.api.ts";
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
