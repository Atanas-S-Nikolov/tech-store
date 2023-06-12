package com.techstore.service.mail;

import com.techstore.exception.mail.MailServiceException;
import com.techstore.model.dto.QuickOrderDto;
import com.techstore.model.entity.CartEntity;
import com.techstore.model.entity.OrderEntity;
import com.techstore.model.entity.UserEntity;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.io.IOException;
import java.util.Map;

import static com.techstore.constants.ApiConstants.FULL_CONFIRM_REGISTER_URL;
import static com.techstore.constants.ApiConstants.RESET_PASSWORD_URL;
import static com.techstore.constants.DateTimeConstants.LOCAL_DATE_TIME_PRECISION_FORMAT;
import static com.techstore.utils.DateTimeUtils.millisToDateString;
import static java.lang.System.currentTimeMillis;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.springframework.ui.freemarker.FreeMarkerTemplateUtils.processTemplateIntoString;

public class MailSenderService implements IMailSenderService {
    private final JavaMailSender javaMailSender;
    private final Configuration fmConfiguration;
    private final String senderEmail;

    public MailSenderService(JavaMailSender javaMailSender, Configuration fmConfiguration, String senderEmail) {
        this.javaMailSender = javaMailSender;
        this.fmConfiguration = fmConfiguration;
        this.senderEmail = senderEmail;
    }

    @Override
    public void sendRegistrationMailConfirmation(String email, String token, long limitTimeMs) {
        String limitTime = millisToDateString(limitTimeMs);
        String confirmationLink = getApplicationUrl() + FULL_CONFIRM_REGISTER_URL + "?token=" + token;
        Map<String, Object> model = Map.of(
                "time", limitTime,
                "link", confirmationLink
        );
        composeAndSendMail(email, "Registration confirmation", "registration-email.ftl", model);
    }

    @Override
    public void sendForgottenPasswordMail(String email, String token, long limitTimeMs) {
        String limitTime = millisToDateString(limitTimeMs);
        String link = "http://localhost:3000/" + "auth" + RESET_PASSWORD_URL;
        Map<String, Object> model = Map.of(
                "token", token,
                "time", limitTime,
                "link", link
        );
        composeAndSendMail(email, "Reset password", "forgot-password-email.ftl", model);
    }

    @Override
    public void sendQuickOrderMail(CartEntity cart, QuickOrderDto quickOrderDto) {
        String dateString = millisToDateString(currentTimeMillis());
        String fullName = quickOrderDto.getFirstName() + " " + quickOrderDto.getLastName();
        String email = quickOrderDto.getEmail();
        Map<String, Object> model = Map.of(
                "date", dateString,
                "orderId", cart.getCartKey(),
                "products", cart.getProductsToBuy(),
                "totalPrice", cart.getTotalPrice(),
                "fullName", fullName,
                "address", quickOrderDto.getAddress(),
                "phone", quickOrderDto.getPhone(),
                "email", email
        );
        composeAndSendMail(email, "Order successful", "order-received.ftl", model);
    }

    @Override
    public void sendOrderMail(OrderEntity orderEntity) {
        String dateString = orderEntity.getDate().format(ofPattern(LOCAL_DATE_TIME_PRECISION_FORMAT));
        UserEntity user = orderEntity.getUser();
        String fullName = user.getFirstName() + " " + user.getLastName();
        String email = user.getEmail();
        Map<String, Object> model = Map.of(
                "date", dateString,
                "orderId", orderEntity.getCartKey(),
                "products", orderEntity.getPurchasedProducts(),
                "totalPrice", orderEntity.getTotalPrice(),
                "fullName", fullName,
                "address", user.getAddress(),
                "phone", user.getPhone(),
                "email", email
        );
        composeAndSendMail(email, "Order successful", "order-received.ftl", model);
    }

    private void composeAndSendMail(String emailTo, String subject, String templateName, Map<String, Object> contentModel) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setFrom(senderEmail);
            messageHelper.setTo(emailTo);
            messageHelper.setSubject(subject);
            messageHelper.setText(geContentFromTemplate(templateName, contentModel), true);
            javaMailSender.send(message);
        } catch (MailAuthenticationException e) {
            throw new MailServiceException("Mail authentication failed", e);
        } catch (MailSendException e) {
            throw new MailServiceException("Failed sending mail", e);
        } catch(MessagingException e) {
            throw new MailServiceException("Failed to compose mail", e);
        }
    }

    private String getApplicationUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    }

    private String geContentFromTemplate(String templateName, Map<String, Object> model) {
        StringBuilder content = new StringBuilder();
        try {
            content.append(processTemplateIntoString(fmConfiguration.getTemplate(templateName), model));
        } catch (TemplateException | ParseException e) {
            throw new MailServiceException("Malformed template: " + templateName, e);
        } catch (TemplateNotFoundException e) {
            throw new MailServiceException("Template is not found template: " + templateName, e);
        } catch (MalformedTemplateNameException e) {
            throw new MailServiceException("Malformed template name: " + templateName, e);
        } catch (IOException e) {
            throw new MailServiceException("IO exception with template: " + templateName, e);
        }
        return content.toString();
    }
}
