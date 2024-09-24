package udpm.hn.server.infrastructure.connection.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StaffResponse {
    @JsonProperty("staff_detail")
    private StaffDetail staffDetail;

    @JsonProperty("major_department_campus")
    private MajorDepartmentCampus majorDepartmentCampus;

    @Getter
    @Setter
    public class StaffDetail {
        private Long id;
        @JsonProperty("full_name")
        private String fullName;
        @JsonProperty("staff_code")
        private String staffCode;
        @JsonProperty("email_fpt")
        private String emailFpt;
        @JsonProperty("phone_number")
        private String phoneNumber;
        @JsonProperty("email_fe")
        private String emailFe;
        private String avatar;
    }

    @Getter
    @Setter
    public class MajorDepartmentCampus {
        @JsonProperty("major_campus")
        private MajorCampus majorCampus;
        @JsonProperty("department_campus")
        private DepartmentCampus departmentCampus;
        private Campus campus;

        @Getter
        @Setter
        public static class MajorCampus {
            @JsonProperty("major_campus_id")
            private Long majorCampusId;
            @JsonProperty("major_id")
            private Long majorId;
            @JsonProperty("major_code")
            private String majorCode;
            @JsonProperty("major_name")
            private String majorName;
        }

        @Getter
        @Setter
        public static class DepartmentCampus {
            @JsonProperty("department_campus_id")
            private Long departmentCampusId;
            @JsonProperty("department_id")
            private Long departmentId;
            @JsonProperty("department_code")
            private String departmentCode;
            @JsonProperty("department_name")
            private String departmentName;
        }

        @Getter
        @Setter
        public static class Campus {
            @JsonProperty("campus_id")
            private Long campusId;
            @JsonProperty("campus_code")
            private String campusCode;
            @JsonProperty("campus_name")
            private String campusName;
        }
    }

}



