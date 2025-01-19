package ro.chirila.ExpenseEase.service.implementation;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import ro.chirila.ExpenseEase.repository.dto.UserResponseDTO;
import ro.chirila.ExpenseEase.repository.entity.User;
import ro.chirila.ExpenseEase.service.SendEmailService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class SendEmailServiceImpl implements SendEmailService {

    private final Configuration configuration;

    public SendEmailServiceImpl(Configuration configuration) {
        this.configuration = configuration;
    }

    @Value("${spring.mail.username}")
    private String adminEmail;

    @Value("${spring.mail.password}")
    private String password;
    private String companyName = "Expense Ease";

    @Override
    public void sendPasswordEmail(String password, UserResponseDTO userResponseDTO) {
        MimeMessage message = new MimeMessage(getSession());
        try {
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            helper.setFrom(new InternetAddress(adminEmail));
            helper.setTo(userResponseDTO.getEmail());
            helper.setSubject("Welcome to " + companyName);

            Template template = configuration.getTemplate("send-password-email.html");
            Map<String, Object> templateMapper = new HashMap<>();
            templateMapper.put("username", userResponseDTO.getUsername());
            templateMapper.put("password", password);
            templateMapper.put("adminEmail", adminEmail);
            templateMapper.put("companyName", companyName);

            String htmlTemplate = FreeMarkerTemplateUtils.processTemplateIntoString(template, templateMapper);

            helper.setText(htmlTemplate, true);

            Transport.send(message);
        } catch (MessagingException | IOException | TemplateException e) {
            throw new RuntimeException("Failed to send password email", e);
        }
    }

    @Override
    public void sendSecurityCodeEmail(String email, String securityCode, String username) {
        MimeMessage message = new MimeMessage(getSession());
        try {
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            helper.setFrom(new InternetAddress(adminEmail));
            helper.setTo(email);
            helper.setSubject("Password Reset Request");
            Template template  = configuration.getTemplate("send-security-code.html");
            Map<String, Object> templateMapper = new HashMap<>();
            templateMapper.put("username", username);
            templateMapper.put("adminEmail", adminEmail);
            templateMapper.put("companyName", companyName);
            templateMapper.put("securityCode", securityCode);
            String htmlTemplate = FreeMarkerTemplateUtils.processTemplateIntoString(template, templateMapper);
            helper.setText(htmlTemplate, true);
            Transport.send(message);
        } catch (MessagingException | IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendPasswordResetEmail(String email, String password, String username) {
        MimeMessage message = new MimeMessage(getSession());
        try {
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            helper.setFrom(new InternetAddress(adminEmail));
            helper.setTo(email);
            helper.setSubject("Password Reset Notification");
            Template template = configuration.getTemplate("send-password.html");
            Map<String, Object> templateMapper = new HashMap<>();
            templateMapper.put("username", username);
            templateMapper.put("password", password);
            templateMapper.put("adminEmail", adminEmail);
            templateMapper.put("companyName", companyName);
            String htmlTemplate = FreeMarkerTemplateUtils.processTemplateIntoString(template, templateMapper);
            helper.setText(htmlTemplate, true);
            Transport.send(message);
        } catch (MessagingException | IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    private Properties getProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.trust", "*");
        return properties;
    }

    private Session getSession() {
        Properties properties = getProperties();
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(adminEmail, password);
            }
        });
        return session;
    }
}
