package udpm.hn.server.infrastructure.config.email.service;

import java.util.List;

public interface EmailService {

    void sendEmailToHeadSubjectAboutPlan(String content, List<String> response);

}
