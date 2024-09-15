package udpm.hn.server.infrastructure.connection.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInformationResponse {

    private String id;

    private String userCode;

    private String name;

    private String userName;

    private String emailFPT;

    private String emailFE;

    private String picture;

    private String userSubjectCode;

    private String userTrainingFacilityCode;

}
