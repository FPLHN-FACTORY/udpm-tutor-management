package udpm.hn.server.core.teacher.tutorclass.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class TCTCUpdateTutorClassDetailRequest {

    private String id;
    private String shift;
    private Long endTime;
    private Long startTime;

}
