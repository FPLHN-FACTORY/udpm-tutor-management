import { queryKey } from "@/constants/queryKey";
import {
    getDetailOperationLog,
  getOperationLogs,
  getUserActivityLog,
  ParamsGetOperationLogs,
  ParamsGetUserActivityLog,
} from "@/services/api/superadmin/operationlog.api";
import { useQuery, UseQueryReturnType } from "@tanstack/vue-query";
import { Ref } from "vue";

export const useGetOperationLogs = (
  params: Ref<ParamsGetOperationLogs>,
  options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getOperationLogs>>, Error> => {
  return useQuery({
    queryKey: [queryKey.admin.operationLogs.operationLogList, params],
    queryFn: () => getOperationLogs(params),
    ...options,
  });
};

export const useGetUserActivityLog = (
  params: Ref<ParamsGetUserActivityLog>,
  options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getUserActivityLog>>, Error> => {
  return useQuery({
    queryKey: [queryKey.admin.userActivityLog.userActivityLogList, params],
    queryFn: () => getUserActivityLog(params),
    ...options,
  });
};

export const useDetailOperationLogs = (
  operationLogId: Ref<string | null>,
  options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getDetailOperationLog>>, Error> => {
  return useQuery({
    queryKey: [queryKey.admin.operationLogs.operationLogDetail, operationLogId],
    queryFn: () => getDetailOperationLog(operationLogId.value),
    ...options,
  });
};
