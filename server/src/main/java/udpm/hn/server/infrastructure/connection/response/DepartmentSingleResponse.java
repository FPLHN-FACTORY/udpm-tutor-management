package udpm.hn.server.infrastructure.connection.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentSingleResponse {

    private Long departmentId;

    private String departmentCode;

    private String departmentName;

}
