package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.repository.TokenRepository;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailService implements NotificationService {

    private final SendGrid sendGrid;

    private final TokenRepository tokenRepository;

    @Transactional(readOnly = true)
    @SneakyThrows
    public void send(String email) {
        Mail mail = prepareMail(email);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        sendGrid.api(request);
    }

    private Mail prepareMail(String email) {
        String tokenValue = tokenRepository.findTokenByUserEmail(email);

        Email fromEmail = new Email("pasha16.ua@gmail.com");

        Email toEmail = new Email(email);

        String subject = "Follow this link to confirm your email";

        Content content = new Content();
        content.setType("text/plain");
        content.setValue("http://localhost:10000/confirmEmail/" + tokenValue);

        return new Mail(fromEmail, subject, toEmail, content);
    }
}
