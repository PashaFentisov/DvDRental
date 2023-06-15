package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.EmailDto;
import com.pashonokk.dvdrental.exception.EmailDtoValidationException;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class EmailService implements NotificationService {

    private final SendGrid sendGrid;
    private final Logger logger = LoggerFactory.getLogger(EmailService.class);

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
        validateEmailDto(emailDto);
        Email fromEmail = new Email(EmailDto.FROM);

        Email toEmail = new Email(emailDto.getTo());

        String subject = emailDto.getSubject();

        Content content = new Content();
        content.setType("text/plain");
        content.setValue("http://localhost:10000/confirmEmail/" + emailDto.getBody());

        return new Mail(fromEmail, subject, toEmail, content);
    }

    private void validateEmailDto(EmailDto emailDto){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<EmailDto>> constraintViolations = validator.validate(emailDto);
        factory.close();
        for(ConstraintViolation<EmailDto> constraintViolation: constraintViolations){
            logger.error(constraintViolation.getMessage());
        }
        if(!constraintViolations.isEmpty()){
            throw new EmailDtoValidationException("We cannot send you confirming letter");
        }
    }
}
