import { queryKey } from "@/constants/queryKey";
import { useQuery, UseQueryReturnType } from "@tanstack/vue-query";
import { ComputedRef, Ref} from "vue";
import {
  getDetailTutorClass, getListTutorClassDetail,
  getTutorClass,
  ParamsGetTutorClass,
  ParamsGetTutorClassDetail
} from "@/services/api/headdepartment/tutor-class.api.ts";

export const useGetDetailTutorClass = (
  tutorId: ComputedRef<string | null>,
  options?: any
): UseQueryReturnType<
  Awaited<ReturnType<typeof getDetailTutorClass>>,
  Error
> => {
  return useQuery({
    queryKey: [queryKey.headOfDepartment.plan.tutorDetail, tutorId],
    queryFn: () => getDetailTutorClass(tutorId.value),
    ...options,
  });
};

export const useGetTutorClass = (
  params: Ref<ParamsGetTutorClass>,
  options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getTutorClass>>, Error> => {
  return useQuery({
    queryKey: [queryKey.headOfDepartment.plan.tutorList, params],
    queryFn: () => getTutorClass(params),
    ...options,
  });
};

export const useListTutorClassDetail = (
  params: Ref<ParamsGetTutorClassDetail>,
  options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getListTutorClassDetail>>, Error> => {
return useQuery({
  queryKey: [queryKey.headOfDepartment.plan.tutorClassList, params],
  queryFn: () => getListTutorClassDetail(params),
  ...options,
});

};
