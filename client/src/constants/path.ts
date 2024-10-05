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
      FACILITY: {
        path: "facility",
        name: "facility",
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
        name: "tutorClass",
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
      }
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
