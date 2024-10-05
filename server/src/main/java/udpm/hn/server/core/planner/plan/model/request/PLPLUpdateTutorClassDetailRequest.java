package udpm.hn.server.core.planner.plan.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import udpm.hn.server.core.common.base.PageableRequest;

@Setter
@Getter
@ToString
public class PLPLUpdateTutorClassDetailRequest{

    private String id;
    private String studentId;
    private String room;

}
