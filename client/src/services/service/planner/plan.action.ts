import {
  approvePlan, checkApprovePlan,
  createPlan,
  CreateUpdatePlanParams,
  getDetailPlan, getPlanInfo, getPlanInfoById,
  getPlans, getSemesterInfo,
  ParamsGetPlans, startPlan, updatePlan
} from "@/services/api/planner/plan.api.ts";
import {Ref} from "vue";
import {useMutation, useQuery, useQueryClient, UseQueryReturnType} from "@tanstack/vue-query";
import {queryKey} from "@/constants/queryKey.ts";

export const useGetPlans = (
    params: Ref<ParamsGetPlans>,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getPlans>>, Error> => {
  return useQuery({
    queryKey: [queryKey.planner.plan.planList, params],
    queryFn: () => getPlans(params),
    ...options,
  });
};

export const useGetSemesterInfo = (
    params: ParamsGetPlans,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getSemesterInfo>>, Error> => {
  return useQuery({
    queryKey: [queryKey.planner.plan.semesterInfo, params],
    queryFn: () => getSemesterInfo(params),
    ...options,
  });
};

export const useGetPlanInfo = (
    params: ParamsGetPlans,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getPlanInfo>>, Error> => {
  return useQuery({
    queryKey: [queryKey.planner.plan.planInfo, params],
    queryFn: () => getPlanInfo(params),
    ...options,
  });
};

export const useGetPlanInfoById = (
    planId: string | null,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getPlanInfoById>>, Error> => {
  return useQuery({
    queryKey: [queryKey.planner.plan.planInfo, planId],
    queryFn: () => getPlanInfoById(planId),
    ...options,
  });
};

export const useCreatePlan = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (params: CreateUpdatePlanParams) => createPlan(params),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.planner.plan.planList],
      });
    },
    onError: (error: any) => {
      console.log("🚀 ~ useCreatePlan ~ error:", error);
    },
  });
};

export const useUpdatePlan = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({
                   planId,
                   params,
                 }: {
      planId: string;
      params: CreateUpdatePlanParams;
    }) => updatePlan(planId, params),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.planner.plan.planList],
      });
    },
    onError: (error: any) => {
      console.log("🚀 ~ useUpdatePlan ~ error:", error);
    },
  });
};

export const useDetailPlan = (
    planId: Ref<string | null>,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getDetailPlan>>, Error> => {
  return useQuery({
    queryKey: [queryKey.planner.plan.planDetail, planId],
    queryFn: () => getDetailPlan(planId.value),
    ...options,
  });
};

export const useApprovePlan = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (planId: string) => approvePlan(planId),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.planner.plan.planList],
      });
    },
    onError: (error: any) => {
      console.log("🚀 ~ useApprovePlan ~ error:", error);
    },
  });
};


export const useStartPlan = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (planId: string) => startPlan(planId),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.planner.plan.planList],
      });
    },
    onError: (error: any) => {
      console.log("🚀 ~ useApprovePlan ~ error:", error);
    },
  });
};

export const useCheckApprovePlan = (
    planId: string,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof checkApprovePlan>>, Error> => {
  return {
    queryFn: () => checkApprovePlan(planId),
    ...options,
  };
};