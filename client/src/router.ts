import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";

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
    redirect: "/admin/semester",
    component: () => import("./layout/Admin.vue"),
    children: [
      {
        path: "subject",
        name: "subject",
        component: () => import("@/pages/admin/subject/Subject.vue"),
      },
      {
        path: "department",
        name: "department",
        component: () => import("@/pages/admin/department/Department.vue"),
      },
      {
        path: "facility",
        name: "facility",
        component: () => import("@/pages/admin/facility/Facility.vue"),
      },
      {
        path: "role",
        name: "role",
        component: () => import("@/pages/admin/role/Role.vue"),
      },
      {
        path: "semester",
        name: "semester",
        component: () => import("@/pages/admin/semester/Semester.vue"),
      },
      {
        path: "staff",
        name: "staff",
        component: () => import("@/pages/admin/staff/Staff.vue"),
      },
    ],
  },
  {
    path: "/head-department",
    redirect: "/head-department/manage-head-subject",
    component: () => import("./layout/HeadDepartment.vue"),
    children: [
      {
        path: "manage-head-subject",
        name: "manage-head-subject",
        component: () =>
          import(
            "@/pages/headdepartment/manageheadsubject/ManageHeadSubject.vue"
          ),
      },
    ],
  },
];

export const router = createRouter({
  history: createWebHistory(),
  routes,
});
