package udpm.hn.server.core.teacher.tutorclass.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class TCTCUpdateLectureRequest {

    private String id;

    private String shift;

    private String format;

    private Long startTime;

}
