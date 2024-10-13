export const { VITE_BASE_URL_SERVER } = import.meta.env || {};

export const { VITE_BASE_URL_CLIENT } = import.meta.env || {};

// API URL
export const API_URL = `${VITE_BASE_URL_SERVER}/api/v1` as string;

// DOMAIN
export const DOMAIN_BACKEND = `${VITE_BASE_URL_SERVER}` as string;

export const DOMAIN_FRONTEND = `${VITE_BASE_URL_CLIENT}` as string;

export const URL_OAUTH2_GOOGLE =
  `${DOMAIN_BACKEND}/oauth2/authorize/google?redirect_uri=` as string;

export const URL_FRONTEND = `${DOMAIN_FRONTEND}/redirect`;

// COMMON API
export const PREFIX_API_COMMON = `${API_URL}/common` as string;

// NOTIFICATION API
export const PREFIX_API_NOTIFICATION = `${API_URL}/notification` as string;

// AUTH API
export const PREFIX_API_AUTH = `${API_URL}/auth` as string;

// ADMIN API
export const PREFIX_API_SUBJECT_ADMIN = `${API_URL}/admin/subjects` as string;

export const PREFIX_API_FACILITY_ADMIN =
  `${API_URL}/admin/facilities` as string;

export const PREFIX_API_FACILITY_CHILD_ADMIN =
  `${API_URL}/admin/facility-child` as string;

export const PREFIX_API_DEPARTMENT_ADMIN =
  `${API_URL}/admin/departments` as string;

export const PREFIX_API_ADMIN_OPERATION_LOG =
  `${API_URL}/admin/operation-log` as string;

export const PREFIX_API_ADMIN_PLAN_HISTORY_LOG =
  `${API_URL}/admin/plan-log-history` as string;

export const PREFIX_API_ADMIN_USER_ACTIVITY =
  `${API_URL}/admin/user-log` as string;

export const PREFIX_API_DEPARTMENT_FACILITY_ADMIN =
  `${API_URL}/admin/departments-facility` as string;

export const PREFIX_API_MAJOR_FACILITY_ADMIN =
  `${API_URL}/admin/majors-facility` as string;

export const PREFIX_API_MAJOR_ADMIN = `${API_URL}/admin/majors` as string;

export const PREFIX_API_ROLE_ADMIN = `${API_URL}/admin/roles` as string;

export const PREFIX_API_SEMESTER_ADMIN = `${API_URL}/admin/semesters` as string;

export const PREFIX_API_STAFF_ADMIN = `${API_URL}/admin/staffs` as string;

// HEAD DEPARTMENT API
export const PREFIX_API_HEAD_DEPARTMENT_HEAD_SUBJECT =
  `${API_URL}/head-department/head-of-subjects` as string;

export const PREFIX_API_HEAD_DEPARTMENT_PLAN =
  `${API_URL}/head-department/head-of-plans` as string;

// PLANNER API
export const PREFIX_API_PLANNER_PLAN = `${API_URL}/planner/plans` as string;

//TEACHER API
export const PREFIX_API_TEACHER_TUTOR_CLASS =
  `${API_URL}/teacher/tutor-class` as string;

export const PREFIX_API_HEAD_SUBJECT_PLAN =
  `${API_URL}/head-subject/plans` as string;
