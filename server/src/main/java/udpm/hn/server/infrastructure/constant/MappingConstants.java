package udpm.hn.server.infrastructure.constant;

public final class MappingConstants {

    // Constants representing the base paths for various resources
    public static final String HEAD_SUBJECT = "/head-subject";
    public static final String ADMIN = "/admin";
    public static final String HEAD_DEPARTMENT = "/head-department";
    public static final String TEACHER = "/teacher";
    public static final String STUDENT = "/student";

    // Constant representing the API version prefix
    public static final String API_VERSION_PREFIX = "/api/v1";

    // Constants representing the common API
    public static final String API_COMMON = API_VERSION_PREFIX + "/common";

    // Constants representing the full paths for various resources
    public static final String API_HEAD_SUBJECT_PREFIX = API_VERSION_PREFIX + HEAD_SUBJECT;
    public static final String API_ADMIN_PREFIX = API_VERSION_PREFIX + ADMIN;
    public static final String API_HEAD_DEPARTMENT_PREFIX = API_VERSION_PREFIX + HEAD_DEPARTMENT;
    public static final String API_TEACHER_PREFIX = API_VERSION_PREFIX + TEACHER;
    public static final String API_STUDENT_PREFIX = API_VERSION_PREFIX + STUDENT;

    // Constants representing the redirect paths for various resources under admin
    public static final String REDIRECT_ADMIN_SUBJECT = ADMIN + "/subjects";
    public static final String REDIRECT_ADMIN_SEMESTER = ADMIN + "/semesters";
    public static final String REDIRECT_ADMIN_FACILITY = ADMIN + "/facilities";
    public static final String REDIRECT_ADMIN_BLOCK = ADMIN + "/blocks";
    public static final String REDIRECT_ADMIN_STAFF = ADMIN + "/staffs";
    public static final String REDIRECT_ADMIN_ROLE = ADMIN + "/roles";
    public static final String REDIRECT_ADMIN_DEPARTMENT = ADMIN + "/departments";
    public static final String REDIRECT_ADMIN_MAJOR = ADMIN + "/majors";
    public static final String REDIRECT_ADMIN_DEPARTMENT_FACILITY = ADMIN + "/departments-facility";
    public static final String REDIRECT_ADMIN_MAJOR_FACILITY = ADMIN + "/majors-facility";

    // Constants representing the redirect paths for various resources under head-department
    public static final String REDIRECT_HEAD_DEPARTMENT_HEAD_SUBJECTS = HEAD_DEPARTMENT + "/head-of-subjects";

    // Constants representing the full paths for various resources under admin
    public static final String API_HEAD_OFFICE_SUBJECT = API_ADMIN_PREFIX + "/subjects";
    public static final String API_HEAD_OFFICE_SEMESTER = API_ADMIN_PREFIX + "/semesters";
    public static final String API_HEAD_OFFICE_BLOCK = API_ADMIN_PREFIX + "/blocks";
    public static final String API_HEAD_OFFICE_STAFF = API_ADMIN_PREFIX + "/staffs";
    public static final String API_HEAD_OFFICE_ROLE = API_ADMIN_PREFIX + "/roles";
    public static final String API_ADMIN_FACILITY = API_ADMIN_PREFIX + "/facilities";

    // Constants representing the full paths for various resources under head-department
    public static final String API_HEAD_DEPARTMENT_HEAD_SUBJECT = API_HEAD_DEPARTMENT_PREFIX + "/head-of-subjects";

}