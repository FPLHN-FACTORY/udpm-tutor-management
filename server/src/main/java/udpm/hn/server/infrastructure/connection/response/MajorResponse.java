package udpm.hn.server.infrastructure.connection.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MajorResponse {

    private Long majorId;

    private Long departmentId;

    private String majorCode;

    private String majorName;

}
