import { queryKey } from "@/constants/queryKey";
import { approvePlan, getPlans, ParamsGetPlans } from "@/services/api/headdepartment/plan.api.ts";
import { useMutation, useQuery, useQueryClient, UseQueryReturnType } from "@tanstack/vue-query";
import { Ref } from "vue";

export const useGetPlans = (
  params: Ref<ParamsGetPlans>,
  options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getPlans>>, Error> => {
  return useQuery({
    queryKey: [queryKey.headOfDepartment.headOfPlan.headOfPlanList, params],
    queryFn: () => getPlans(params),
    ...options,
  });
};

export const useApprovePlan = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (planId: string) => approvePlan(planId),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.headOfDepartment.headOfPlan.headOfPlanList],
      });
    },
    onError: (error) => {
      console.log("🚀 ~ useApprovePlan ~ error:", error);
    },
  });
};