import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";

export type AppRouteNames = "admin" | "subject";

export const routes: RouteRecordRaw[] = [
  {
    path: "/",
    redirect: "/login",
  },
  {
    path: "/:pathMatch(.*)*",
    name: "NotFound",
    component: () => import("@/pages/404/NotFound.vue"),
  },
  {
    path: "/login",
    component: () => import("@/pages/login/Login.vue"),
  },
  {
    path: "/admin",
    component: () => import("./layout/Admin.vue"),
    children: [
      {
        path: "subject",
        name: "subject",
        component: () => import("@/pages/admin/subject/Subject.vue"),
      },
    ],
  },
];

export const router = createRouter({
  history: createWebHistory(),
  routes,
});

export function routerPush(
  name: AppRouteNames,
  params?: Record<string, any>
): ReturnType<typeof router.push> {
  return params ? router.push({ name, params }) : router.push({ name });
}
