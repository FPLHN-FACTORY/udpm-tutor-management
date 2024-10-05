package udpm.hn.server.core.planner.plan.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PLPLCreateStudentTutorRequest {

    private String code;
    private String name;
    private String email;

}
