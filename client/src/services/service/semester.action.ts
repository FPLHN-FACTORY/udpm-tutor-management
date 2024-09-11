import { Ref } from "vue";
import { getDetailSemester, getSemesters, ParamsGetSemester } from "../api/semester.api";
import { useQuery, UseQueryReturnType } from "@tanstack/vue-query";
import { queryKey } from "@/constants/queryKey";


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
): UseQueryReturnType<
  Awaited<ReturnType<typeof getDetailSemester>>,
  Error
> => {
  return useQuery({
    queryKey: [queryKey.admin.semester.semesterDetail, semesterId],
    queryFn: () => getDetailSemester(semesterId.value),
    ...options,
  });
};
