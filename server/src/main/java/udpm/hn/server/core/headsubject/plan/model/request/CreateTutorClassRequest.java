package udpm.hn.server.core.headsubject.plan.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTutorClassRequest {

    private String planeId;
    private String subjectId;
    private Integer numberOfClasses;

}
