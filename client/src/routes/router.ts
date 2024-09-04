import { ROUTES_CONSTANTS } from "@/constants/path";
import { ROLES } from "@/constants/roles";
import { useAuthStore } from "@/stores/auth";
import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";

export const routes: RouteRecordRaw[] = [
  {
    path: "/",
    redirect: ROUTES_CONSTANTS.LOGIN.path,
  },
  {
    path: ROUTES_CONSTANTS.NOT_FOUND.path,
    name: ROUTES_CONSTANTS.NOT_FOUND.name,
    component: () => import("@/pages/404/NotFound.vue"),
  },
  {
    path: ROUTES_CONSTANTS.LOGIN.path,
    name: ROUTES_CONSTANTS.LOGIN.name,
    component: () => import("@/pages/login/Login.vue"),
  },
  {
    path: ROUTES_CONSTANTS.REDIRECT.path,
    component: () => import("@/routes/guard/Redirect.vue"),
  },
  {
    path: ROUTES_CONSTANTS.ROLE_SWITCH.path,
    name: ROUTES_CONSTANTS.ROLE_SWITCH.name,
    component: () => import("@/pages/roleswitch/index.vue"),
  },
  {
    path: ROUTES_CONSTANTS.ADMIN.path,
    redirect: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.SUBJECT.path}`,
    component: () => import("@/layout/Admin.vue"),
    children: [
      {
        path: ROUTES_CONSTANTS.ADMIN.children.SUBJECT.path,
        name: ROUTES_CONSTANTS.ADMIN.children.SUBJECT.name,
        component: () => import("@/pages/admin/subject/Subject.vue"),
        meta: {
          requiresRole: ROLES.ADMIN,
          requiresAuth: true,
        },
      },
      {
        path: ROUTES_CONSTANTS.ADMIN.children.DEPARTMENT.path,
        name: ROUTES_CONSTANTS.ADMIN.children.DEPARTMENT.name,
        component: () => import("@/pages/admin/department/Department.vue"),
        meta: {
          requiresRole: ROLES.ADMIN,
          requiresAuth: true,
        },
      },
      {
        path: ROUTES_CONSTANTS.ADMIN.children.FACILITY.path,
        name: ROUTES_CONSTANTS.ADMIN.children.FACILITY.name,
        component: () => import("@/pages/admin/facility/Facility.vue"),
        meta: {
          requiresRole: ROLES.ADMIN,
          requiresAuth: true,
        },
      },
      {
        path: ROUTES_CONSTANTS.ADMIN.children.ROLE.path,
        name: ROUTES_CONSTANTS.ADMIN.children.ROLE.name,
        component: () => import("@/pages/admin/role/Role.vue"),
        meta: {
          requiresRole: ROLES.ADMIN,
          requiresAuth: true,
        },
      },
      {
        path: ROUTES_CONSTANTS.ADMIN.children.SEMESTER.path,
        name: ROUTES_CONSTANTS.ADMIN.children.SEMESTER.name,
        component: () => import("@/pages/admin/semester/Semester.vue"),
        meta: {
          requiresRole: ROLES.ADMIN,
          requiresAuth: true,
        },
      },
      {
        path: ROUTES_CONSTANTS.ADMIN.children.STAFF.path,
        name: ROUTES_CONSTANTS.ADMIN.children.STAFF.name,
        component: () => import("@/pages/admin/staff/Staff.vue"),
        meta: {
          requiresRole: ROLES.ADMIN,
          requiresAuth: true,
        },
      },
    ],
  },
  {
    path: ROUTES_CONSTANTS.HEAD_DEPARTMENT.path,
    redirect:
      ROUTES_CONSTANTS.HEAD_DEPARTMENT.children.MANAGE_HEAD_SUBJECT.path,
    component: () => import("@/layout/HeadDepartment.vue"),
    children: [
      {
        path: ROUTES_CONSTANTS.HEAD_DEPARTMENT.children.MANAGE_HEAD_SUBJECT
          .path,
        name: ROUTES_CONSTANTS.HEAD_DEPARTMENT.children.MANAGE_HEAD_SUBJECT
          .name,
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

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();
  const requiresAuth = to.matched.some((record) => record.meta.requiresAuth);
  const requiresRole = to.matched.some((record) => record.meta.requiresRole);
  const userRole = authStore?.user?.rolesCodes;

  if (userRole === null && requiresAuth) {
    next({ name: ROUTES_CONSTANTS.LOGIN.name });
  } else if (requiresAuth && !authStore.isAuthenticated) {
    next({ name: ROUTES_CONSTANTS.LOGIN.name });
  } else {
    next();
  }
});
