package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.EmailDto;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService implements NotificationService {

    private final SendGrid sendGrid;

    @SneakyThrows
    public void send(EmailDto emailDto) {
        Mail mail = prepareMail(emailDto);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        sendGrid.api(request);
    }

    private Mail prepareMail(EmailDto emailDto) {
        Email fromEmail = new Email(EmailDto.from);

        Email toEmail = new Email(emailDto.getTo());

        String subject = emailDto.getSubject();

        Content content = new Content();
        content.setType("text/plain");
        content.setValue("http://localhost:10000/confirmEmail/" + emailDto.getBody());

        return new Mail(fromEmail, subject, toEmail, content);
    }
}
