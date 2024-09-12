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
        name: "DetailStaff",
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
      MANAGE_HEAD_SUBJECT: {
        path: "manage-head-subject",
        name: "manage-head-subject",
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
