package udpm.hn.server.core.headdepartment.plan.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class HDPLCreatePlanRequest {

    private String blockId;

    private String semesterId;

    private String departmentCode;

    private String facilityCode;

    private String userCode;

    private String description;

}