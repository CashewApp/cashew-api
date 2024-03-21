package br.app.cashew.feature02.user.service.email;

import br.app.cashew.feature01.authentication.util.email.EmailConfirmationEmailProperties;
import br.app.cashew.feature01.authentication.util.email.ForgotPasswordEmailProperties;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailSenderService(JavaMailSender javaMailSender) {

        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendForgotPasswordEmail(String recipient, String token) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            mimeMessageHelper.setFrom(ForgotPasswordEmailProperties.getSender());
            mimeMessageHelper.setTo(recipient);
            mimeMessageHelper.setSubject(ForgotPasswordEmailProperties.getSubject());
            String text = String.format(ForgotPasswordEmailProperties.getText(), token);
            mimeMessageHelper.setText(text, true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        javaMailSender.send(mimeMessage);
    }

    @Async
    public void sendEmailConfirmationEmail(String recipient, String pin) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            mimeMessageHelper.setFrom(EmailConfirmationEmailProperties.getSender());
            mimeMessageHelper.setTo(recipient);
            mimeMessageHelper.setSubject(EmailConfirmationEmailProperties.getSubject());

            String text = String.format(EmailConfirmationEmailProperties.getText(), pin);
            mimeMessageHelper.setText(text, true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
            javaMailSender.send(mimeMessage);

    }
}
