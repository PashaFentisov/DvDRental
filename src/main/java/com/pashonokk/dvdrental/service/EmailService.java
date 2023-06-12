package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.UserTokenDto;
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
    public void send(UserTokenDto userTokenDto) {
        Mail mail = prepareMail(userTokenDto.getUserEmail(), userTokenDto.getTokenValue());
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        sendGrid.api(request);
    }

    private Mail prepareMail(String email, String tokenValue) {
        Email fromEmail = new Email("pasha16.ua@gmail.com");

        Email toEmail = new Email(email);

        String subject = "Follow this link to confirm your email";

        Content content = new Content();
        content.setType("text/plain");
        content.setValue("http://localhost:10000/confirmEmail/" + tokenValue);

        return new Mail(fromEmail, subject, toEmail, content);
    }
}
