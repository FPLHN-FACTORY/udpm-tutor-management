import { queryKey } from "@/constants/queryKey";
import {useMutation, useQuery, useQueryClient, UseQueryReturnType} from "@tanstack/vue-query";
import { ComputedRef, Ref } from "vue";
import {
  createStudentTutor, CreateStudentTutorParams,
  getDetailTutorClass, getListTutorClassDetail,
  getTutorClass,
  ParamsGetTutorClass,
  ParamsGetTutorClassDetail, updateStudentPlan, UpdateTutorClassDetailParams
} from "@/services/api/planner/tutor-class.api.ts";

export const useGetDetailTutorClass = (
  tutorId: ComputedRef<string | null>,
  options?: any
): UseQueryReturnType<
  Awaited<ReturnType<typeof getDetailTutorClass>>,
  Error
> => {
  return useQuery({
    queryKey: [queryKey.planner.plan.tutorDetail, tutorId],
    queryFn: () => getDetailTutorClass(tutorId.value),
    ...options,
  });
};

export const useGetTutorClass = (
  params: Ref<ParamsGetTutorClass>,
  options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getTutorClass>>, Error> => {
  return useQuery({
    queryKey: [queryKey.planner.plan.tutorClassList, params],
    queryFn: () => getTutorClass(params),
    ...options,
  });
};

export const useListTutorClassDetail = (
    params: Ref<ParamsGetTutorClassDetail>,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getListTutorClassDetail>>, Error> => {
  return useQuery({
    queryKey: [queryKey.planner.plan.tutorClassDetailList, params],
    queryFn: () => getListTutorClassDetail(params),
    ...options,
  });
};

export const useUpdateStudentPlan = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (params: Array<UpdateTutorClassDetailParams>) => updateStudentPlan(params),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.planner.plan.tutorClassDetailList],
      });
    },
    onError: (error: any) => {
      console.log("🚀 ~ useCreatePlan ~ error:", error);
    },
  });
};

export const useCreateStudentTutor = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (params: CreateStudentTutorParams) => createStudentTutor(params),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.common.studentOptions],
      });
    },
    onError: (error: any) => {
      console.log("🚀 ~ useCreatePlan ~ error:", error);
    },
  });
};

