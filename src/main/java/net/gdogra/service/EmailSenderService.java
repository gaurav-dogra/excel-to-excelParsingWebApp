package net.gdogra.service;

import lombok.RequiredArgsConstructor;
import net.gdogra.AppConstants;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final JavaMailSender mailSender;

    public void sendEmailWithAttachment(File[] files) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper =
                new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(AppConstants.EMAIL_ADD);
        mimeMessageHelper.setTo(AppConstants.EMAIL_ADD);
        mimeMessageHelper.setText("Error reported by the user of Spring parsing app. ");
        mimeMessageHelper.setSubject("Error! Spring Parsing app");
        for (File file : files) {
            mimeMessageHelper.addAttachment(file.getName(), file);
        }
        mailSender.send(mimeMessage);
    }
}
