package com.pashenko.Board.services;

import com.pashenko.Board.entities.EmailMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;

@Service
public class SendMailService {
    private final JavaMailSender mailSender;

    @Autowired
    public SendMailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMessage(EmailMessage message) throws MessagingException {
        MimeMessage mimeMessage = getMimeBase(message);
        Multipart multipart = new MimeMultipart();
        attachBodyPart(multipart, message);
        attachFiles(multipart, message);
        mimeMessage.setContent(multipart);

        mailSender.send(mimeMessage);
    }

    private MimeMessage getMimeBase(EmailMessage message) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        mimeMessage.setFrom(message.getFrom());
        mimeMessage.setSubject(message.getSubject());
        mimeMessage.setRecipients(Message.RecipientType.TO, message.getTo());
        return mimeMessage;
    }

    private void attachBodyPart(Multipart multipart, EmailMessage message) throws MessagingException {
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(message.getBody(), "text/html; charset=utf-8");
        multipart.addBodyPart(mimeBodyPart);
    }

    private void attachFiles(Multipart multipart, EmailMessage message){
        message.getAttached().forEach(file -> {
            try {
                MimeBodyPart part = new MimeBodyPart();
                part.attachFile(file);
                multipart.addBodyPart(part);
            } catch (IOException | MessagingException e) {
                e.printStackTrace();
            }
        });
    }
}
