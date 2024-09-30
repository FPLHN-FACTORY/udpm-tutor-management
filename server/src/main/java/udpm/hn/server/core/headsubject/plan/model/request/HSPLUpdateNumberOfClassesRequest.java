package udpm.hn.server.core.headsubject.plan.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HSPLUpdateNumberOfClassesRequest {

    private String tutorClassId;
    private Integer numberOfClasses;

}
