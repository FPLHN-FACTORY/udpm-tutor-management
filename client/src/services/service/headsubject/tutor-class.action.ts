import { queryKey } from "@/constants/queryKey";
import {
  getDetailTutorClass,
  getListTutorClassDetail,
  getTutorClass,
  ParamsGetTutorClass,
  ParamsGetTutorClassDetail,
  UpdateHeadSubjectTutorDetailParams,
  updateNumberTutorClassHeadSubject,
  updateStatusApproveTutorClassHeadSubject,
} from "@/services/api/headsubject/tutor-class.api.ts";
import {
  useMutation,
  useQuery,
  useQueryClient,
  UseQueryReturnType,
} from "@tanstack/vue-query";
import { ComputedRef, Ref} from "vue";

export const useUpdateHeadSubjectTutorDetail = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (params: UpdateHeadSubjectTutorDetailParams) =>
      updateNumberTutorClassHeadSubject(params),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.headSubject.tutor.tutorList], // Cập nhật key tương ứng nếu cần
      });
    },
    onError: (error) => {
      console.log("🚀 ~ useUpdateHeadSubjectTutorDetail ~ error:", error);
    },
  });
};

export const useUpdateStatusApproveTutorClassHeadSubject = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: string) => updateStatusApproveTutorClassHeadSubject(id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.headSubject.tutor.tutorList],
      });
    },
    onError: (error) => {
      console.log("🚀 ~ useDeleteHeadSubjectTutorDetail ~ error:", error);
    },
  });
};

export const useGetDetailTutorClass = (
  tutorId: ComputedRef<string | null>,
  options?: any
): UseQueryReturnType<
  Awaited<ReturnType<typeof getDetailTutorClass>>,
  Error
> => {
  return useQuery({
    queryKey: [queryKey.headSubject.tutorDetail, tutorId],
    queryFn: () => getDetailTutorClass(tutorId.value),
    ...options,
  });
};

export const useGetTutorClass = (
  params: Ref<ParamsGetTutorClass>,
  options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getTutorClass>>, Error> => {
  return useQuery({
    queryKey: [queryKey.headSubject.tutor.tutorList, params],
    queryFn: () => getTutorClass(params),
    ...options,
  });
};

export const useListTutorClassDetail = (
  params: Ref<ParamsGetTutorClassDetail>,
  options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getListTutorClassDetail>>, Error> => {
return useQuery({
  queryKey: [queryKey.headSubject.plan.tutorClassList, params],
  queryFn: () => getListTutorClassDetail(params),
  ...options,
});
};
