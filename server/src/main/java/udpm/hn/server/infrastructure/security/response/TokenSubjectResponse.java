package udpm.hn.server.infrastructure.security.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class TokenSubjectResponse {

    private String userId;

    private String userCode;

    private String fullName;

    private String pictureUrl;

    private String host;

    private List<String> rolesCode;

    private List<String> rolesName;

    private String emailFpt;

    private String emailFe;

    private String departmentCode;

    private String departmentName;

    private String facilityCode;

    private String facilityId;

    private String facilityName;

}
