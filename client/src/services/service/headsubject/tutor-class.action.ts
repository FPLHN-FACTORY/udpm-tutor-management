import { queryKey } from "@/constants/queryKey";
import {
  addTutorClassDetail,
  createTutorClass, CreateTutorClassParams,
  deleteTutorClassDetail,
  getDetailTutorClass,
  getListTutorClassDetail,
  getTutorClass,
  ParamsGetTutorClass,
  ParamsGetTutorClassDetail,
  updateTutorClass,
  updateTutorClassDetail,
  updateTutorClassDetailParams,
  UpdateTutorClassParams,
} from "@/services/api/headsubject/tutor-class.api.ts";
import { useMutation, useQuery, useQueryClient, UseQueryReturnType } from "@tanstack/vue-query";
import { Ref} from "vue";

export const useCreateTutorClass = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (params: CreateTutorClassParams) => createTutorClass(params),
    onSuccess: () => {
      queryClient.invalidateQueries({
        predicate: (query) => {
          return (
              query.queryKey[0] === queryKey.headSubject.plan.tutorClassList ||
              query.queryKey[0] === queryKey.headSubject.plan.tutorClassDetailList
          );
        },
      });
    },
    onError: (error: any) => {
      console.log("🚀 ~ useUpdateHeadSubjectTutorDetail ~ error:", error);
    },
  });
};


export const useUpdateTutorClass = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({id, params}: {
      id: string;
      params: UpdateTutorClassParams;
    }) => updateTutorClass(id, params),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.headSubject.plan.tutorClassList],
      });
    },
    onError: (error: any) => {
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
    queryKey: [queryKey.headSubject.plan.tutorDetail, tutorId],
    queryFn: () => getDetailTutorClass(tutorId.value),
    ...options,
  });
};

export const useGetTutorClass = (
    params: Ref<ParamsGetTutorClass>,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getTutorClass>>, Error> => {
  return useQuery({
    queryKey: [queryKey.headSubject.plan.tutorClassList, params],
    queryFn: () => getTutorClass(params),
    ...options,
  });
};

export const useListTutorClassDetail = (
    params: Ref<ParamsGetTutorClassDetail>,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getListTutorClassDetail>>, Error> => {
  return useQuery({
    queryKey: [queryKey.headSubject.plan.tutorClassDetailList, params],
    queryFn: () => getListTutorClassDetail(params),
    ...options,
  });
};

export const useUpdateTutorClassDetail = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ id, params }: {
      id: string;
      params: updateTutorClassDetailParams;
    }) => updateTutorClassDetail(id, params),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.headSubject.plan.tutorClassDetailList],
      });
    },
    onError: (error: any) => {
      console.log("🚀 ~ useDeleteHeadSubjectTutorDetail ~ error:", error);
    },
  });
};

export const useDeleteTutorClassDetail = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: string) => deleteTutorClassDetail(id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        predicate: (query) => {
          return (
              query.queryKey[0] === queryKey.headSubject.plan.tutorClassList ||
              query.queryKey[0] === queryKey.headSubject.plan.tutorClassDetailList
          );
        },
      });
    },
    onError: (error: any) => {
      console.log("🚀 ~ useDeleteHeadSubjectTutorDetail ~ error:", error);
    },
  });
};

export const useAddTutorClassDetail = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: string) => addTutorClassDetail(id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        predicate: (query) => {
          return (
              query.queryKey[0] === queryKey.headSubject.plan.tutorClassList ||
              query.queryKey[0] === queryKey.headSubject.plan.tutorClassDetailList
          );
        },
      });
    },
    onError: (error: any) => {
      console.log("🚀 ~ useDeleteHeadSubjectTutorDetail ~ error:", error);
    },
  });
};
