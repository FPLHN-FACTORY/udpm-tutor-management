package udpm.hn.server.core.headsubject.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateNumberOfClassesRequest {
    private String tutorClassId;
    private Integer numberOfClasses;
}
