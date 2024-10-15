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
  // ROLE SWITCH ROUTES
  {
    path: ROUTES_CONSTANTS.ROLE_SWITCH.path,
    name: ROUTES_CONSTANTS.ROLE_SWITCH.name,
    component: () => import("@/pages/roleswitch/index.vue"),
  },
  // ADMIN ROUTES
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
      {
        path: ROUTES_CONSTANTS.ADMIN.children.STAFF_DETAIL.path,
        name: ROUTES_CONSTANTS.ADMIN.children.STAFF_DETAIL.name,
        component: () => import("@/pages/admin/staff/StaffDetail.vue"),
        meta: {
          requiresRole: ROLES.ADMIN,
          requiresAuth: true,
        },
      },
    ],
  },
  // SYSTEM LOG ROUTES
  {
    path: ROUTES_CONSTANTS.SYSTEM_LOG.path,
    redirect: `${ROUTES_CONSTANTS.SYSTEM_LOG.path}/${ROUTES_CONSTANTS.SYSTEM_LOG.children.OPERATION_LOG.path}`,
    component: () => import("@/layout/LogSystemHistory.vue"),
    children: [
      {
        path: ROUTES_CONSTANTS.SYSTEM_LOG.children.OPERATION_LOG.path,
        name: ROUTES_CONSTANTS.SYSTEM_LOG.children.OPERATION_LOG.name,
        component: () => import("@/pages/operationlogs/OperationLog.vue"),
        meta: {
          requiresRole: ROLES.ADMIN,
          requiresAuth: true,
        },
      },
      {
        path: ROUTES_CONSTANTS.SYSTEM_LOG.children.USER_ACTIVITY_LOG.path,
        name: ROUTES_CONSTANTS.SYSTEM_LOG.children.USER_ACTIVITY_LOG.name,
        component: () => import("@/pages/operationlogs/UserActivityLog.vue"),
        meta: {
          requiresRole: ROLES.ADMIN,
          requiresAuth: true,
        },
      },
    ],
  },
  // HEAD DEPARTMENT ROUTES
  {
    path: ROUTES_CONSTANTS.HEAD_DEPARTMENT.path,
    redirect: `${ROUTES_CONSTANTS.HEAD_DEPARTMENT.path}/${ROUTES_CONSTANTS.HEAD_DEPARTMENT.children.HEAD_SUBJECT.path}`,
    component: () => import("@/layout/HeadDepartment.vue"),
    children: [
      {
        path: ROUTES_CONSTANTS.HEAD_DEPARTMENT.children.HEAD_SUBJECT.path,
        name: ROUTES_CONSTANTS.HEAD_DEPARTMENT.children.HEAD_SUBJECT.name,
        component: () =>
          import("@/pages/headdepartment/headsubject/HeadSubject.vue"),
        meta: {
          requiresRole: ROLES.HEAD_DEPARTMENT,
          requiresAuth: true,
        },
      },
      {
        path: ROUTES_CONSTANTS.HEAD_DEPARTMENT.children.PLAN.path,
        name: ROUTES_CONSTANTS.HEAD_DEPARTMENT.children.PLAN.name,
        component: () => import("@/pages/headdepartment/plan/Plan.vue"),
        meta: {
          requiresRole: ROLES.HEAD_DEPARTMENT,
          requiresAuth: true,
        },
      },
      {
        path: ROUTES_CONSTANTS.HEAD_DEPARTMENT.children.PLAN_DETAIL.path,
        name: ROUTES_CONSTANTS.HEAD_DEPARTMENT.children.PLAN_DETAIL.name,
        component: () => import("@/pages/headdepartment/plan/PlanDetail.vue"),
        meta: {
          requiresRole: ROLES.HEAD_DEPARTMENT,
          requiresAuth: true,
        },
      },
      {
        path: ROUTES_CONSTANTS.HEAD_DEPARTMENT.children.PLAN_LOG_HISTORY.path,
        name: ROUTES_CONSTANTS.HEAD_DEPARTMENT.children.PLAN_LOG_HISTORY.name,
        component: () =>
          import(
            "@/pages/headdepartment/planloghistory/HeadDepartmentPlanLogHistory.vue"
          ),
        meta: {
          requiresRole: ROLES.HEAD_DEPARTMENT,
          requiresAuth: true,
        },
      },
    ],
  },
  // HEAD SUBJECT ROUTES
  {
    path: ROUTES_CONSTANTS.HEAD_SUBJECT.path,
    redirect: `${ROUTES_CONSTANTS.HEAD_SUBJECT.path}/${ROUTES_CONSTANTS.HEAD_SUBJECT.children.PLAN.path}`,
    component: () => import("@/layout/LayoutHeadSubject.vue"),
    children: [
      {
        path: ROUTES_CONSTANTS.HEAD_SUBJECT.children.PLAN.path,
        name: ROUTES_CONSTANTS.HEAD_SUBJECT.children.PLAN.name,
        component: () => import("@/pages/headsubject/plan/Plan.vue"),
        meta: {
          requiresRole: ROLES.HEAD_SUBJECT,
          requiresAuth: true,
        },
      },
      {
        path: ROUTES_CONSTANTS.HEAD_SUBJECT.children.PLAN_DETAIL.path,
        name: ROUTES_CONSTANTS.HEAD_SUBJECT.children.PLAN_DETAIL.name,
        component: () => import("@/pages/headsubject/plan/PlanDetail.vue"),
        meta: {
          requiresRole: ROLES.HEAD_SUBJECT,
          requiresAuth: true,
        },
      },
      {
        path: ROUTES_CONSTANTS.HEAD_SUBJECT.children.PLAN_LOG.path,
        name: ROUTES_CONSTANTS.HEAD_SUBJECT.children.PLAN_LOG.name,
        component: () =>
          import(
            "@/pages/headsubject/planloghistory/HeadSubjectPlanLogHistory.vue"
          ),
        meta: {
          requiresRole: ROLES.HEAD_SUBJECT,
          requiresAuth: true,
        },
      },
      {
        path: ROUTES_CONSTANTS.HEAD_SUBJECT.children.PLANNER.path,
        name: ROUTES_CONSTANTS.HEAD_SUBJECT.children.PLANNER.name,
        component: () =>
          import("@/pages/headsubject/planner/HeadSubjectPlanner.vue"),
        meta: {
          requiresRole: ROLES.HEAD_SUBJECT,
          requiresAuth: true,
        },
      },
    ],
  },
  // PLANNER ROUTES
  {
    path: ROUTES_CONSTANTS.PLANNER.path,
    redirect: `${ROUTES_CONSTANTS.PLANNER.path}/${ROUTES_CONSTANTS.PLANNER.children.PLAN.path}`,
    component: () => import("@/layout/Planner.vue"),
    children: [
      {
        path: ROUTES_CONSTANTS.PLANNER.children.PLAN.path,
        name: ROUTES_CONSTANTS.PLANNER.children.PLAN.name,
        component: () => import("@/pages/planner/plan/Plan.vue"),
        meta: {
          requiresRole: ROLES.PLANER,
          requiresAuth: true,
        },
      },
      {
        path: ROUTES_CONSTANTS.PLANNER.children.PLAN_DETAIL.path,
        name: ROUTES_CONSTANTS.PLANNER.children.PLAN_DETAIL.name,
        component: () => import("@/pages/planner/plan/PlanDetail.vue"),
        meta: {
          requiresRole: ROLES.PLANER,
          requiresAuth: true,
        },
      },
      {
        path: ROUTES_CONSTANTS.PLANNER.children.PLAN_LOG_HISTORY.path,
        name: ROUTES_CONSTANTS.PLANNER.children.PLAN_LOG_HISTORY.name,
        component: () =>
          import("@/pages/planner/planloghistory/PlanLogHistory.vue"),
        meta: {
          requiresRole: ROLES.PLANER,
          requiresAuth: true,
        },
      },
    ],
  },
  // TEACHER ROUTES
  {
    path: ROUTES_CONSTANTS.TEACHER.path,
    redirect: `${ROUTES_CONSTANTS.TEACHER.path}/${ROUTES_CONSTANTS.TEACHER.children.TUTOR_CLASS.path}`,
    component: () => import("@/layout/Teacher.vue"),
    children: [
      {
        path: ROUTES_CONSTANTS.TEACHER.children.TUTOR_CLASS.path,
        name: ROUTES_CONSTANTS.TEACHER.children.TUTOR_CLASS.name,
        component: () => import("@/pages/teacher/tutor-class/TutorClass.vue"),
      },
    ],
  },
];

export const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, _from, next) => {
  const authStore = useAuthStore();
  const requiresAuth = to.matched.some((record) => record.meta.requiresAuth);
  const requiresRole = to.matched.some((record) => record.meta.requiresRole);
  const userRole = authStore?.user?.rolesCodes;

  if (userRole === null && requiresAuth) {
    next({ name: ROUTES_CONSTANTS.LOGIN.name });
  } else if (requiresAuth && !authStore.isAuthenticated) {
    next({ name: ROUTES_CONSTANTS.LOGIN.name });
  } else if (
    requiresRole &&
    (!userRole || !userRole.includes(to.meta.requiresRole as string))
  ) {
    next({ name: ROUTES_CONSTANTS.LOGIN.name });
  } else {
    next();
  }
});
