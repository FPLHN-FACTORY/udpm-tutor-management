package udpm.hn.server.infrastructure.connection.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StaffResponse {
    private Long id;
    private String name;
    private String userCode;
    private String emailFE;
    private String emailFPT;
    private String picture;
    private String userSubjectCode;
    private String userTrainingFacilityCode;
}
