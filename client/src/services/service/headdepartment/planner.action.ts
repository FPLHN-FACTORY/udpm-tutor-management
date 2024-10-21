import { queryKey } from "@/constants/queryKey";
import {
  assignedPlanner,
  AssignedPlannerRequest,
  getHeadDepartmentPlanner,
  ParamsGetListPlaner,
  unassignedPlanner,
} from "@/services/api/headdepartment/planner.api";
import {
  useMutation,
  useQuery,
  useQueryClient,
  UseQueryReturnType,
} from "@tanstack/vue-query";
import { Ref } from "vue";

export const useGetDepartmentPlanner = (
  params: Ref<ParamsGetListPlaner>,
  options?: any
): UseQueryReturnType<
  Awaited<ReturnType<typeof getHeadDepartmentPlanner>>,
  Error
> => {
  return useQuery({
    queryKey: [queryKey.headOfDepartment.planner.plannerList, params],
    queryFn: () => getHeadDepartmentPlanner(params),
    ...options,
  });
};

export const useAssignedPlanner = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({
      id,
      params,
    }: {
      id: string;
      params: AssignedPlannerRequest;
    }) => assignedPlanner(id, params),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.headOfDepartment.planner.plannerList],
      });
    },
    onError: (error: any) => {
      console.log("🚀 ~ useDeleteHeadSubjectTutorDetail ~ error:", error);
    },
  });
};

export const useUnassignedPlanner = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ id }: { id: string }) => unassignedPlanner(id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.headOfDepartment.planner.plannerList],
      });
    },
    onError: (error: any) => {
      console.log("🚀 ~ useDeleteHeadSubjectTutorDetail ~ error:", error);
    },
  });
};
