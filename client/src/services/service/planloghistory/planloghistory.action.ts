import { queryKey } from "@/constants/queryKey";
import { getDetailPlanLogs, getPlanLogs, ParamsGetPlanLog } from "@/services/api/planloghistory/planloghistory.api";
import { useQuery, UseQueryReturnType } from "@tanstack/vue-query";
import { Ref } from "vue";

export const useGetPlanLogHistory = (
    params: Ref<ParamsGetPlanLog>,
    options?: any
  ): UseQueryReturnType<Awaited<ReturnType<typeof getPlanLogs>>, Error> => {
    return useQuery({
      queryKey: [queryKey.admin.planLogHistory.planLogHistoryList, params],
      queryFn: () => getPlanLogs(params),
      ...options,
    });
  };
  

  export const useDetailPlanLogHistory = (
    planLogHistoryId: Ref<string | null>,
    options?: any
  ): UseQueryReturnType<Awaited<ReturnType<typeof getDetailPlanLogs>>, Error> => {
    return useQuery({
      queryKey: [queryKey.admin.planLogHistory.planLogHistoryDetail, planLogHistoryId],
      queryFn: () => getDetailPlanLogs(planLogHistoryId.value),
      ...options,
    });
  };
  