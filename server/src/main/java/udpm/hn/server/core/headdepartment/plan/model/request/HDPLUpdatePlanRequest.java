package udpm.hn.server.core.headdepartment.plan.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class HDPLUpdatePlanRequest {

    private String blockId;

    private String description;

}
