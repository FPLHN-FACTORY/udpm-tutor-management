package udpm.hn.server.core.teacher.tutorclass.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class TCTCCurrentPlanRequest {

    private String blockId;
    private String departmentCode;
    private String facilityCode;

}
