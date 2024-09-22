import { Ref } from "vue";
import {
  getDetailSemester,
  getSemesters, getSemesterSynchronize, getSynchronize,
  ParamsGetSemester,
} from "../../api/admin/semester.api.ts";
import {useMutation, useQuery, useQueryClient, UseQueryReturnType} from "@tanstack/vue-query";
import { queryKey } from "@/constants/queryKey.ts";

export const useGetSemester = (
  params: Ref<ParamsGetSemester>,
  options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getSemesters>>, Error> => {
  return useQuery({
    queryKey: [queryKey.admin.semester.semesterList, params],
    queryFn: () => getSemesters(params),
    ...options,
  });
};

export const useDetailSemester = (
  semesterId: Ref<string | null>,
  options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getDetailSemester>>, Error> => {
  return useQuery({
    queryKey: [queryKey.admin.semester.semesterDetail, semesterId],
    queryFn: () => getDetailSemester(semesterId.value),
    ...options,
  });
};

export function useSemesterSynchronize() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: () => getSemesterSynchronize(),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.admin.semester.semesterList],
      });
    },
    onError: (error) => {
      // Handle error
      console.error('Error during synchronization:', error);
    }
  });
}
