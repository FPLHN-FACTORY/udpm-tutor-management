package udpm.hn.server.infrastructure.constant;

public final class MappingConstants {

    // Versioning
    public static final String VERSION = "/api/version";

    // Role Paths
    public static final String HEAD_SUBJECT = "/head-subject";
    public static final String ADMIN = "/admin";
    public static final String HEAD_DEPARTMENT = "/head-department";
    public static final String TEACHER = "/teacher";
    public static final String STUDENT = "/student";
    public static final String PLANNER = "/planner";
    public static final String SUPER_ADMIN = "/super-admin";

    // API Prefixes
    public static final String API_VERSION_PREFIX = "/api/v1";
    public static final String API_COMMON = API_VERSION_PREFIX + "/common";
    public static final String API_NOTIFICATION = API_VERSION_PREFIX + "/notification";

    // API By Role Prefixes
    public static final String API_HEAD_SUBJECT_PREFIX = API_VERSION_PREFIX + HEAD_SUBJECT;
    public static final String API_SUPER_ADMIN_PREFIX = API_VERSION_PREFIX + SUPER_ADMIN;
    public static final String API_ADMIN_PREFIX = API_VERSION_PREFIX + ADMIN;
    public static final String API_HEAD_DEPARTMENT_PREFIX = API_VERSION_PREFIX + HEAD_DEPARTMENT;
    public static final String API_PLANNER_PREFIX = API_VERSION_PREFIX + PLANNER;
    public static final String API_TEACHER_PREFIX = API_VERSION_PREFIX + TEACHER;
    public static final String API_STUDENT_PREFIX = API_VERSION_PREFIX + STUDENT;

    // Admin API Endpoints
    public static final String API_ADMIN_SUBJECT = API_ADMIN_PREFIX + "/subjects";
    public static final String API_ADMIN_DEPARTMENT = API_ADMIN_PREFIX + "/departments";
    public static final String API_ADMIN_MAJOR = API_ADMIN_PREFIX + "/majors";
    public static final String API_ADMIN_SEMESTER = API_ADMIN_PREFIX + "/semesters";
    public static final String API_ADMIN_BLOCK = API_ADMIN_PREFIX + "/blocks";
    public static final String API_ADMIN_MAJOR_FACILITY = API_ADMIN_PREFIX + "/majors-facility";
    public static final String API_ADMIN_DEPARTMENT_FACILITY = API_ADMIN_PREFIX + "/departments-facility";
    public static final String API_ADMIN_STAFF = API_ADMIN_PREFIX + "/staffs";
    public static final String API_ADMIN_ROLE = API_ADMIN_PREFIX + "/roles";
    public static final String API_ADMIN_PLAN_LOG_HISTORY = API_ADMIN_PREFIX + "/plan-log-history";


    // Super Admin API Endpoints
    public static final String API_SUPER_ADMIN_FACILITY = API_SUPER_ADMIN_PREFIX + "/facilities";
    public static final String API_SUPER_ADMIN_OPERATION_LOG = API_SUPER_ADMIN_PREFIX + "/operation-log";
    public static final String API_SUPER_ADMIN_USER_ACTIVITY = API_SUPER_ADMIN_PREFIX + "/user-log";

    // Head Department API Endpoints
    public static final String API_HEAD_DEPARTMENT_HEAD_SUBJECT = API_HEAD_DEPARTMENT_PREFIX + "/head-of-subjects";
    public static final String API_HEAD_DEPARTMENT_HEAD_PLAN = API_HEAD_DEPARTMENT_PREFIX + "/head-of-plans";

    // Planner API Endpoints
    public static final String API_PLANNER_PLAN = API_PLANNER_PREFIX + "/plans";

    // Head Subject API Endpoints
    public static final String API_HEAD_SUBJECT_PLAN = API_HEAD_SUBJECT_PREFIX + "/plans";
    public static final String API_HEAD_SUBJECT_TUTOR_DETAIL = API_HEAD_SUBJECT_PREFIX + "/tutor-detail";

    // Teacher API Endpoints
    public static final String API_TEACHER_TUTOR_CLASS = API_TEACHER_PREFIX + "/tutor-class";

    // Authentication
    public static final String API_AUTH_PREFIX = API_VERSION_PREFIX + "/auth";
    public static final String PATH_OAUTH2 = "/oauth2";

}