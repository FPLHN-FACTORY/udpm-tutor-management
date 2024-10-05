package udpm.hn.server.core.headsubject.plan.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HSPLCreateTutorClassRequest {

    private String planId;
    private String subjectId;
    private Integer numberOfClasses;
    private Integer numberOfLectures;
    private String format;

}
