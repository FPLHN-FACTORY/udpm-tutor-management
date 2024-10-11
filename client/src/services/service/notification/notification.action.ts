import { queryKey } from "@/constants/queryKey";
import { getCountNotifications, getNotifications, ParamsGetNotification, ParamsReadNotification, readNotification } from "@/services/api/notification/notification.api";
import { useMutation, useQuery, useQueryClient, UseQueryReturnType } from "@tanstack/vue-query";
import { Ref } from "vue";

export const useGetNotifications = (
    params: Ref<ParamsGetNotification>,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getNotifications>>, Error> => {
  return useQuery({
    queryKey: [queryKey.notification.notificationList, params],
    queryFn: () => getNotifications(params),
    ...options,
  });
};

export const useGetCountNotifications = (
  params: Ref<ParamsGetNotification>,
  options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getCountNotifications>>, Error> => {
return useQuery({
  queryKey: [queryKey.notification.notificationCount, params],
  queryFn: () => getCountNotifications(params),
  ...options,
});
};

export const useReadNotification = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, params }: {id: string, params: Ref<ParamsReadNotification> }) => readNotification(id, params),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.notification.notificationList],
      });
      queryClient.invalidateQueries({
        queryKey: [queryKey.notification.notificationCount],
      });
    },
    onError: (error: any) => {
      console.log("🚀 ~ useReadNotification ~ error:", error);
    },
  });
};
