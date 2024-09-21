package udpm.hn.server.infrastructure.connection.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SemesterResponse {

    private Long id;

    private Long startTime;

    private Long endTime;

    private Long startTimeFirstBlock;

    private Long endTimeFirstBlock;

    private Long startTimeSecondBlock;

    private Long endTimeSecondBlock;

    private String semesterName;

}
