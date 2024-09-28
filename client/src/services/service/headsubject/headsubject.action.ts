import { queryKey } from "@/constants/queryKey";
import {
  createHeadSubjectTutorDetail,
  CreateHeadSubjectTutorDetailParams,
  createOrUpdateHeadSubject,
  CreateUpdateHeadSubjectParams,
  deleteTutorClassHeadSubject,
  getDetailTutorClass,
  getListTutorClassDetail,
  getTutorClass,
  ParamsGetTutorClass,
  ParamsGetTutorClassDetail,
  UpdateHeadSubjectTutorDetailParams,
  updateNumberTutorClassHeadSubject,
  updateStatusApproveTutorClassHeadSubject,
} from "@/services/api/headsubject/headsubject.api";
import {
  useMutation,
  useQuery,
  useQueryClient,
  UseQueryReturnType,
} from "@tanstack/vue-query";
import { computed, Ref } from "vue";

export const useCreateOrUpdateHeadSubject = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (params: CreateUpdateHeadSubjectParams) =>
      createOrUpdateHeadSubject(params),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.headSubject.tutor.tutorList],
      });
    },
    onError: (error) => {
      console.log("🚀 ~ useCreatePlan ~ error:", error);
    },
  });
};

export const useCreateHeadSubjectTutorDetail = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (params: CreateHeadSubjectTutorDetailParams) =>
      createHeadSubjectTutorDetail(params),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.headSubject.tutorDetail.tutorDetailList],
      });
    },
    onError: (error) => {
      console.log("🚀 ~ useCreatePlan ~ error:", error);
    },
  });
};

export const useUpdateHeadSubjectTutorDetail = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (params: UpdateHeadSubjectTutorDetailParams) =>
      updateNumberTutorClassHeadSubject(params),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.headSubject.tutorDetail.tutorDetailList], // Cập nhật key tương ứng nếu cần
      });
    },
    onError: (error) => {
      console.log("🚀 ~ useUpdateHeadSubjectTutorDetail ~ error:", error);
    },
  });
};

export const useDeleteHeadSubjectTutorDetail = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: string) => deleteTutorClassHeadSubject(id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.headSubject.tutorDetail.tutorDetailList], // Cập nhật key tương ứng nếu cần
      });
    },
    onError: (error) => {
      console.log("🚀 ~ useDeleteHeadSubjectTutorDetail ~ error:", error);
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
  tutorId: Ref<string | null>,
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
  queryKey: [queryKey.planner.plan.tutorClassList, params],
  queryFn: () => getListTutorClassDetail(params),
  ...options,
});
};