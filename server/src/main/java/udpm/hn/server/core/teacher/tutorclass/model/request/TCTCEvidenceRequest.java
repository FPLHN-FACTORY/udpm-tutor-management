package udpm.hn.server.core.teacher.tutorclass.model.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class TCTCEvidenceRequest {
    private MultipartFile file;
    private String lectureId;
    private String evidenceLink;
    private String exerciseLink;
    private String recordLink;
    private String driveLink;
}
