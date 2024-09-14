import { queryKey } from "@/constants/queryKey";
import {
  useQuery,
  UseQueryReturnType,
} from "@tanstack/vue-query";
import { Ref } from "vue";
import { getMajors, getMajorsFacility, ParamsGetMajor } from "../api/major.api";

export const useGetMajors = (
    departmentId: Ref<string | null>,
    params: Ref<ParamsGetMajor>,
    options?: any
  ): UseQueryReturnType<Awaited<ReturnType<typeof getMajors>>, Error> => {
    return useQuery({
      queryKey: [queryKey.admin.major.majorList, departmentId, params],
      queryFn: () => getMajors(departmentId, params),
      ...options,
    });
  };

  export const useGetMajorsFacility = (
    params: Ref<ParamsGetMajor>,
    options?: any
  ): UseQueryReturnType<Awaited<ReturnType<typeof getMajorsFacility>>, Error> => {
    return useQuery({
      queryKey: [queryKey.admin.major.majorList, params],
      queryFn: () => getMajorsFacility(params),
      ...options,
    });
  };