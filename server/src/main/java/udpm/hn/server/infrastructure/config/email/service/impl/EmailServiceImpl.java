package udpm.hn.server.infrastructure.config.email.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import udpm.hn.server.infrastructure.config.email.service.EmailService;
import udpm.hn.server.infrastructure.constant.MailConstant;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    @Async
    public void sendEmailToHeadSubjectAboutPlan(String content, List<String> recipientEmails) {
        String header = MailConstant.HEADER
                .replace("${title}", "Thông báo trương môn kế hoạch tutor.");

        StringBuilder emailContent = new StringBuilder();
        emailContent.append("<p>").append(content).append("</p>");
        String footer = MailConstant.FOOTER;

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = null;

        try {
            // Tải logo từ classpath
            ClassPathResource resource = new ClassPathResource(MailConstant.LOGO_PATH);

            for (String toEmail : recipientEmails) {
                System.out.println(toEmail);
                System.out.println("xxxxxxxxxxxxxxxxxx");
                mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.toString());
                mimeMessageHelper.setFrom(fromEmail);
                mimeMessageHelper.setTo(toEmail);
                mimeMessageHelper.setBcc(toEmail); // Nếu cần BCC
                mimeMessageHelper.setText(header + emailContent + footer, true);
                mimeMessageHelper.setSubject(MailConstant.SUBJECT);
                mimeMessageHelper.addInline("logoImage", resource);
                // Gửi email
                mailSender.send(mimeMessage);
            }
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


}