package udpm.hn.server.infrastructure.connection.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentResponse {

    private Long departmentId;

    private String departmentCode;

    private String departmentName;

    private String emailHeadDepartmentFe;

    private Long campusId;

    private String campusCode;

}
