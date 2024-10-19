package udpm.hn.server.infrastructure.config.job.student.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentExcelRequest {

    private int orderNumber;

    private String name;

    private String code;

    private String email;

    private String tutorClassDetail;

    private String planId;

}
