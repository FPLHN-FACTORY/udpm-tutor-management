package udpm.hn.server.infrastructure.connection.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentCampusResponse {

    private Long departmentCampusId;

    private Long departmentId;

    private Long campusId;

    private String emailHeadDepartmentFpt;

    private String emailHeadDepartmentFe;

}
