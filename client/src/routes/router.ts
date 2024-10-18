import { ROUTES_CONSTANTS } from "@/constants/path";
import { ROLES } from "@/constants/roles";
import { useAuthStore } from "@/stores/auth";
import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";

export const routes: RouteRecordRaw[] = [
  // Root route
  {
    path: "/",
    redirect: ROUTES_CONSTANTS.LOGIN.path,
  },
  // Not Found route
  {
    path: ROUTES_CONSTANTS.NOT_FOUND.path,
    name: ROUTES_CONSTANTS.NOT_FOUND.name,
    component: () => import("@/pages/404/NotFound.vue"),
  },
  // Login route
  {
    path: ROUTES_CONSTANTS.LOGIN.path,
    name: ROUTES_CONSTANTS.LOGIN.name,
    component: () => import("@/pages/login/Login.vue"),
  },
  // Redirect route
  {
    path: ROUTES_CONSTANTS.REDIRECT.path,
    component: () => import("@/routes/guard/Redirect.vue"),
  },
  // Role Switch route
  {
    path: ROUTES_CONSTANTS.ROLE_SWITCH.path,
    name: ROUTES_CONSTANTS.ROLE_SWITCH.name,
    meta: {
      requiresAuth: true,
    },
    component: () => import("@/pages/roleswitch/index.vue"),
  },
  // Admin routes
  {
    path: ROUTES_CONSTANTS.ADMIN.path,
    redirect: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.SUBJECT.path}`,
    component: () => import("@/layout/Admin.vue"),
    children: [
      // Admin Subject route
      {
        path: ROUTES_CONSTANTS.ADMIN.children.SUBJECT.path,
        name: ROUTES_CONSTANTS.ADMIN.children.SUBJECT.name,
        component: () => import("@/pages/admin/subject/Subject.vue"),
        meta: {
          requiresRole: ROLES.ADMIN,
          requiresAuth: true,
        },
      },
      // Admin Department route
      {
        path: ROUTES_CONSTANTS.ADMIN.children.DEPARTMENT.path,
        name: ROUTES_CONSTANTS.ADMIN.children.DEPARTMENT.name,
        component: () => import("@/pages/admin/department/Department.vue"),
        meta: {
          requiresRole: ROLES.ADMIN,
          requiresAuth: true,
        },
      },
      // Admin Role route
      {
        path: ROUTES_CONSTANTS.ADMIN.children.ROLE.path,
        name: ROUTES_CONSTANTS.ADMIN.children.ROLE.name,
        component: () => import("@/pages/admin/role/Role.vue"),
        meta: {
          requiresRole: ROLES.ADMIN,
          requiresAuth: true,
        },
      },
      // Admin Semester route
      {
        path: ROUTES_CONSTANTS.ADMIN.children.SEMESTER.path,
        name: ROUTES_CONSTANTS.ADMIN.children.SEMESTER.name,
        component: () => import("@/pages/admin/semester/Semester.vue"),
        meta: {
          requiresRole: ROLES.ADMIN,
          requiresAuth: true,
        },
      },
      // Admin Staff route
      {
        path: ROUTES_CONSTANTS.ADMIN.children.STAFF.path,
        name: ROUTES_CONSTANTS.ADMIN.children.STAFF.name,
        component: () => import("@/pages/admin/staff/Staff.vue"),
        meta: {
          requiresRole: ROLES.ADMIN,
          requiresAuth: true,
        },
      },
      // Admin Staff Detail route
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
  // System Log routes
  {
    path: ROUTES_CONSTANTS.SUPER_ADMIN.path,
    redirect: `${ROUTES_CONSTANTS.SUPER_ADMIN.path}/${ROUTES_CONSTANTS.SUPER_ADMIN.children.OPERATION_LOG.path}`,
    component: () => import("@/layout/SuperAdmin.vue"),
    children: [
      // Operation Log route
      {
        path: ROUTES_CONSTANTS.SUPER_ADMIN.children.OPERATION_LOG.path,
        name: ROUTES_CONSTANTS.SUPER_ADMIN.children.OPERATION_LOG.name,
        component: () =>
          import("@/pages/superadmin/operation/OperationLog.vue"),
        meta: {
          requiresRole: ROLES.SUPER_ADMIN,
          requiresAuth: true,
        },
      },
      // User Activity Log route
      {
        path: ROUTES_CONSTANTS.SUPER_ADMIN.children.USER_ACTIVITY_LOG.path,
        name: ROUTES_CONSTANTS.SUPER_ADMIN.children.USER_ACTIVITY_LOG.name,
        component: () =>
          import("@/pages/superadmin/activity/UserActivityLog.vue"),
        meta: {
          requiresRole: ROLES.SUPER_ADMIN,
          requiresAuth: true,
        },
      },
      // Facility route
      {
        path: ROUTES_CONSTANTS.SUPER_ADMIN.children.FACILITY.path,
        name: ROUTES_CONSTANTS.SUPER_ADMIN.children.FACILITY.name,
        component: () => import("@/pages/superadmin/facility/Facility.vue"),
        meta: {
          requiresRole: ROLES.SUPER_ADMIN,
          requiresAuth: true,
        },
      },
    ],
  },
  // Head Department routes
  {
    path: ROUTES_CONSTANTS.HEAD_DEPARTMENT.path,
    redirect: `${ROUTES_CONSTANTS.HEAD_DEPARTMENT.path}/${ROUTES_CONSTANTS.HEAD_DEPARTMENT.children.HEAD_SUBJECT.path}`,
    component: () => import("@/layout/HeadDepartment.vue"),
    children: [
      // Head Subject route
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
      // Plan route
      {
        path: ROUTES_CONSTANTS.HEAD_DEPARTMENT.children.PLAN.path,
        name: ROUTES_CONSTANTS.HEAD_DEPARTMENT.children.PLAN.name,
        component: () => import("@/pages/headdepartment/plan/Plan.vue"),
        meta: {
          requiresRole: ROLES.HEAD_DEPARTMENT,
          requiresAuth: true,
        },
      },
      // Plan Detail route
      {
        path: ROUTES_CONSTANTS.HEAD_DEPARTMENT.children.PLAN_DETAIL.path,
        name: ROUTES_CONSTANTS.HEAD_DEPARTMENT.children.PLAN_DETAIL.name,
        component: () => import("@/pages/headdepartment/plan/PlanDetail.vue"),
        meta: {
          requiresRole: ROLES.HEAD_DEPARTMENT,
          requiresAuth: true,
        },
      },
      // Plan Log History route
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
  // Head Subject routes
  {
    path: ROUTES_CONSTANTS.HEAD_SUBJECT.path,
    redirect: `${ROUTES_CONSTANTS.HEAD_SUBJECT.path}/${ROUTES_CONSTANTS.HEAD_SUBJECT.children.PLAN.path}`,
    component: () => import("@/layout/LayoutHeadSubject.vue"),
    children: [
      // Plan route
      {
        path: ROUTES_CONSTANTS.HEAD_SUBJECT.children.PLAN.path,
        name: ROUTES_CONSTANTS.HEAD_SUBJECT.children.PLAN.name,
        component: () => import("@/pages/headsubject/plan/Plan.vue"),
        meta: {
          requiresRole: ROLES.HEAD_SUBJECT,
          requiresAuth: true,
        },
      },
      // Plan Detail route
      {
        path: ROUTES_CONSTANTS.HEAD_SUBJECT.children.PLAN_DETAIL.path,
        name: ROUTES_CONSTANTS.HEAD_SUBJECT.children.PLAN_DETAIL.name,
        component: () => import("@/pages/headsubject/plan/PlanDetail.vue"),
        meta: {
          requiresRole: ROLES.HEAD_SUBJECT,
          requiresAuth: true,
        },
      },
      // Plan Log route
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
      // Planner route
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
  // Planner routes
  {
    path: ROUTES_CONSTANTS.PLANNER.path,
    redirect: `${ROUTES_CONSTANTS.PLANNER.path}/${ROUTES_CONSTANTS.PLANNER.children.PLAN.path}`,
    component: () => import("@/layout/Planner.vue"),
    children: [
      // Plan route
      {
        path: ROUTES_CONSTANTS.PLANNER.children.PLAN.path,
        name: ROUTES_CONSTANTS.PLANNER.children.PLAN.name,
        component: () => import("@/pages/planner/plan/Plan.vue"),
        meta: {
          requiresRole: ROLES.PLANNER,
          requiresAuth: true,
        },
      },
      // Plan Detail route
      {
        path: ROUTES_CONSTANTS.PLANNER.children.PLAN_DETAIL.path,
        name: ROUTES_CONSTANTS.PLANNER.children.PLAN_DETAIL.name,
        component: () => import("@/pages/planner/plan/PlanDetail.vue"),
        meta: {
          requiresRole: ROLES.PLANNER,
          requiresAuth: true,
        },
      },
      // Plan Log History route
      {
        path: ROUTES_CONSTANTS.PLANNER.children.PLAN_LOG_HISTORY.path,
        name: ROUTES_CONSTANTS.PLANNER.children.PLAN_LOG_HISTORY.name,
        component: () =>
          import("@/pages/planner/planloghistory/PlanLogHistory.vue"),
        meta: {
          requiresRole: ROLES.PLANNER,
          requiresAuth: true,
        },
      },
    ],
  },
  // Teacher routes
  {
    path: ROUTES_CONSTANTS.TEACHER.path,
    redirect: `${ROUTES_CONSTANTS.TEACHER.path}/${ROUTES_CONSTANTS.TEACHER.children.TUTOR_CLASS.path}`,
    component: () => import("@/layout/Teacher.vue"),
    children: [
      // Tutor Class route
      {
        path: ROUTES_CONSTANTS.TEACHER.children.TUTOR_CLASS.path,
        name: ROUTES_CONSTANTS.TEACHER.children.TUTOR_CLASS.name,
        component: () => import("@/pages/teacher/tutor-class/TutorClass.vue"),
      },
      // Tutor Class Detail route
      {
        path: ROUTES_CONSTANTS.TEACHER.children.TUTOR_CLASS_DETAIL.path,
        name: ROUTES_CONSTANTS.TEACHER.children.TUTOR_CLASS_DETAIL.name,
        component: () =>
          import("@/pages/teacher/tutor-class/TutorClassDetail.vue"),
      },
      {
        path: ROUTES_CONSTANTS.TEACHER.children.STUDENT_ATTENDANCE.path,
        name: ROUTES_CONSTANTS.TEACHER.children.STUDENT_ATTENDANCE.name,
        component: () =>
            import(
                "@/pages/teacher/student-attendance/StudentAttendance.vue"
                ),
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
  const userRole = authStore?.user?.rolesCodes;

  if (userRole === null && requiresAuth) {
    next({ name: ROUTES_CONSTANTS.LOGIN.name });
  } else if (requiresAuth && !authStore.isAuthenticated) {
    next({ name: ROUTES_CONSTANTS.LOGIN.name });
  } else {
    next();
  }
});
