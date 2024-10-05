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
      TUTOR_CLASS_DETAIL: {
        path: "plan/tutor-class-detail/:tutorClassId",
        name: "hDPlDetailTutorClassDetail",
      },
    },
  },
  PLANNER: {
    path: "/planner",
    name: "planner",
    children: {
      PLAN: {
        path: "plan",
        name: "plan",
      },
      PLAN_DETAIL: {
        path: "plan/:planId",
        name: "detailPlan",
      },
      TUTOR_CLASS_DETAIL: {
        path: "plan/tutor-class-detail/:tutorClassId",
        name: "detailTutorClassDetail",
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
      HEAD_SUBJECT_DETAIL: {
        path: "subject-plan/:planId",
        name: "detailSubjectPlan",
      },
      HEAD_TUTOR_CLASS_DETAIL: {
        path: "subject-tutor-detail/:tutorClassId",
        name: "detailTutorClassDetail",
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
