export const ROUTES_CONSTANTS = {
  LOGIN: {
    path: "/login",
    name: "login",
  },
  ADMIN: {
    path: "/admin",
    name: "admin",
    children: {
      SUBJECT: {
        path: "subject",
        name: "subject",
      },
      DEPARTMENT: {
        path: "department",
        name: "department",
      },
      ROLE: {
        path: "role",
        name: "role",
      },
      SEMESTER: {
        path: "semester",
        name: "semester",
      },
      STAFF: {
        path: "staff",
        name: "staff",
      },
      STAFF_DETAIL: {
        path: "staff/:staffId",
        name: "detailStaff",
      },
    },
  },
  ROLE_SWITCH: {
    path: "/role-switch",
    name: "role-switch",
  },
  HEAD_DEPARTMENT: {
    path: "/head-department",
    name: "head-department",
    children: {
      HEAD_SUBJECT: {
        path: "head-subject",
        name: "head-subject",
      },
      PLAN: {
        path: "plan",
        name: "hDPlPlan",
      },
      PLAN_DETAIL: {
        path: "plan/:planId",
        name: "hDPlDetailPlan",
      },
      PLAN_LOG_HISTORY: {
        path: "plan-log",
        name: "hDPplanLogHistory",
      },
      PLANNER: {
        path: "planner",
        name: "hDPlPlaner",
      },
    },
  },
  PLANNER: {
    path: "/planner",
    name: "planner",
    children: {
      PLAN: {
        path: "plan",
        name: "hSPlan",
      },
      PLAN_LOG_HISTORY: {
        path: "plan-log",
        name: "pLPlanLogHistory",
      },
      PLAN_DETAIL: {
        path: "plan/:planId",
        name: "pLPlDetailPlan",
      },
    },
  },
  TEACHER: {
    path: "/teacher",
    name: "teacher",
    children: {
      TUTOR_CLASS: {
        path: "tutor-class",
        name: "tcTutorClass",
      },
      TUTOR_CLASS_DETAIL: {
        path: "tutor-class-detail/:tutorClassDetailId",
        name: "tcTutorClassDetail",
      },
      STUDENT_ATTENDANCE: {
        path: "student-attendance/:lectureId",
        name: "tcStudentAttendance",
      },
    },
  },
  SUPER_ADMIN: {
    path: "/super-admin",
    name: "logSystem",
    children: {
      FACILITY: {
        path: "facility",
        name: "facility",
      },
      DEPARTMENT: {
        path: "admin-department",
        name: "admin-department",
      },
      OPERATION_LOG: {
        path: "operation-log",
        name: "operationLog",
      },
      USER_ACTIVITY_LOG: {
        path: "user-log",
        name: "userActivityLog",
      },
    },
  },
  HEAD_SUBJECT: {
    path: "/head-subject",
    name: "head-subject",
    children: {
      PLAN: {
        path: "subject-plan",
        name: "subject-plan",
      },
      PLAN_DETAIL: {
        path: "plan/:planId",
        name: "hSPlDetailPlan",
      },
      PLAN_LOG: {
        path: "plan-log",
        name: "subjectPlanLog",
      },
    },
  },
  NOT_FOUND: {
    path: "/:pathMatch(.*)*",
    name: "NotFound",
  },
  REDIRECT: {
    path: "/redirect",
    name: "redirect",
  },
};
