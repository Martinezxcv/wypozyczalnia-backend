package projekt.wypozyczalnia.services;

public interface MailService {

    void sendMail(String to, String subject, String text);
    void sendMail(String subject, String text);
    void sendMailWithAttachment(String subject, String text, String pathToAttachment);
}
