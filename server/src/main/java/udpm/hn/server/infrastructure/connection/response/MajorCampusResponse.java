package udpm.hn.server.infrastructure.connection.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MajorCampusResponse {

    private Long majorCampusId;

    private Long departmentCampusId;

    private Long majorId;

    private String emailHeadDepartmentFpt;

    private String emailHeadDepartmentFe;

}
