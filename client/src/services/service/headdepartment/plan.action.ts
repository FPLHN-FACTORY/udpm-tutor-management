import { queryKey } from "@/constants/queryKey";
import { useMutation, useQuery, useQueryClient, UseQueryReturnType } from "@tanstack/vue-query";
import { Ref } from "vue";
import {
  getDetailPlan,
  getPlanInfo,
  getPlanInfoById,
  getSemesterInfo,
  approvePlan, getPlans, ParamsGetPlans
} from "@/services/api/headdepartment/plan.api.ts";

export const useGetPlans = (
  params: Ref<ParamsGetPlans>,
  options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getPlans>>, Error> => {
  return useQuery({
    queryKey: [queryKey.headOfDepartment.plan.planList, params],
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
        queryKey: [queryKey.headOfDepartment.plan.planList],
      });
    },
    onError: (error) => {
      console.log("🚀 ~ useApprovePlan ~ error:", error);
    },
  });
};

export const useGetSemesterInfo = (
    params: ParamsGetPlans,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getSemesterInfo>>, Error> => {
  return useQuery({
    queryKey: [queryKey.headOfDepartment.plan.semesterInfo, params],
    queryFn: () => getSemesterInfo(params),
    ...options,
  });
};

export const useGetPlanInfo = (
    params: ParamsGetPlans,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getPlanInfo>>, Error> => {
  return useQuery({
    queryKey: [queryKey.headOfDepartment.plan.planInfo, params],
    queryFn: () => getPlanInfo(params),
    ...options,
  });
};

export const useGetPlanInfoById = (
    planId: string | null,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getPlanInfoById>>, Error> => {
  return useQuery({
    queryKey: [queryKey.headOfDepartment.plan.planInfo, planId],
    queryFn: () => getPlanInfoById(planId),
    ...options,
  });
};

export const useDetailPlan = (
    planId: Ref<string | null>,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getDetailPlan>>, Error> => {
  return useQuery({
    queryKey: [queryKey.headOfDepartment.plan.planDetail, planId],
    queryFn: () => getDetailPlan(planId.value),
    ...options,
  });
};