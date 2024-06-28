package com.project.emailnotificationservice.config;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.emailnotificationservice.dtos.SendEmailBodyDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Service
public class SendEmailConsumer {
    private ObjectMapper objectMapper;
    private EmailUtil emailUtil;

    public SendEmailConsumer(ObjectMapper objectMapper,
                             EmailUtil emailUtil) {
        this.objectMapper = objectMapper;
        this.emailUtil = emailUtil;
    }

    @KafkaListener(topics = "sendEmail", groupId = "emailService")
    public void handleSendEmailMessage(String message) {
        //Code to send an Email to the user.
        SendEmailBodyDTO sendEmailDto = null;
        try {
            sendEmailDto = objectMapper.readValue(message, SendEmailBodyDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        //Send an Email.
        //SMTP -> Simple Mail Transfer Protocol.
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("dhananjaybhadke10@gmail.com", "vhmbvmsnllbnnfzh");
            }
        };
        Session session = Session.getInstance(props, auth);

        emailUtil.sendEmail(
                session,
                sendEmailDto.getTo(),
                sendEmailDto.getSubject(),
                sendEmailDto.getBody()
        );
    }
}
