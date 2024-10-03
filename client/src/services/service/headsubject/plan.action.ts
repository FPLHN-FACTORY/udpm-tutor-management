import {
  getDetailPlan,
  getPlanInfo,
  getPlanInfoById,
  getPlans, getSemesterInfo,
  ParamsGetPlans
} from "@/services/api/headsubject/plan.api.ts";
import {useQuery, UseQueryReturnType} from "@tanstack/vue-query";
import {queryKey} from "@/constants/queryKey.ts";
import {Ref} from "vue";

export const useGetPlans = (
    params: Ref<ParamsGetPlans>,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getPlans>>, Error> => {
  return useQuery({
    queryKey: [queryKey.headSubject.plan.planList, params],
    queryFn: () => getPlans(params.value),
    ...options,
  });
};

export const useGetSemesterInfo = (
    params: ParamsGetPlans,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getSemesterInfo>>, Error> => {
  return useQuery({
    queryKey: [queryKey.headSubject.plan.semesterInfo, params],
    queryFn: () => getSemesterInfo(params),
    ...options,
  });
};

export const useGetPlanInfo = (
    params: ParamsGetPlans,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getPlanInfo>>, Error> => {
  return useQuery({
    queryKey: [queryKey.headSubject.plan.planInfo, params],
    queryFn: () => getPlanInfo(params),
    ...options,
  });
};

export const useGetPlanInfoById = (
    planId: string | null,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getPlanInfoById>>, Error> => {
  return useQuery({
    queryKey: [queryKey.headSubject.plan.planInfo, planId],
    queryFn: () => getPlanInfoById(planId),
    ...options,
  });
};

export const useDetailPlan = (
  planId: Ref<string | null>,
  options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getDetailPlan>>, Error> => {
  return useQuery({
    queryKey: [queryKey.headSubject.plan.planDetail, planId],
    queryFn: () => getDetailPlan(planId.value),
    ...options,
  });
};
