package projekt.wypozyczalnia.servicesImpl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import projekt.wypozyczalnia.jwt.JwtUtils;
import projekt.wypozyczalnia.services.MailService;

import java.io.File;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MailServiceImpl.class);
    private final JavaMailSender mailSender;
    private final JwtUtils jwtUtils;

    @Override
    public void sendMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("j48468566@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        try {
            mailSender.send(message);
            logger.info("Notification sent successfully to: {}", to);
        } catch (Exception e) {
            logger.error("Error occurred while sending email to {}: {}", to, e.getMessage(), e);
        }
    }

    @Override
    public void sendMail(String subject, String text) {
        String toEmail = jwtUtils.getCurrentUserEmail();
        logger.info("Preparing to send email to: {}", toEmail);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("j48468566@gmail.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(text);

        try {
            mailSender.send(message);
            logger.info("Email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            logger.error("Error occurred while sending email to {}: {}", toEmail, e.getMessage(), e);
        }
    }


    @Override
    public void sendMailWithAttachment(String subject, String text, String attachmentPath) {
        String to = jwtUtils.getCurrentUserEmail();
        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("j48468566@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            File attachmentFile = new File(attachmentPath);
            if (attachmentFile.exists() && attachmentFile.isFile()) {
                FileSystemResource file = new FileSystemResource(attachmentFile);
                helper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
                logger.info("Attachment {} added successfully.", file.getFilename());
            } else {
                logger.warn("Attachment not found or invalid: {}", attachmentPath);
            }

            mailSender.send(message);
            logger.info("Email sent successfully to {}", to);
        } catch (MessagingException e) {
            logger.error("Error while sending email to {}: {}", to, e.getMessage());
            logger.debug("Stack Trace:", e);
        }
    }

}

