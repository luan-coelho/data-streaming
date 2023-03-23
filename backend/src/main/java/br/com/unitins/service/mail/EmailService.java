package br.com.unitins.service.mail;

import io.quarkus.arc.profile.IfBuildProfile;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

@Slf4j
@ApplicationScoped
public class EmailService {

    @ConfigProperty(name = "quarkus.mailer.host")
    String host;
    @ConfigProperty(name = "quarkus.mailer.port")
    String port;
    @ConfigProperty(name = "quarkus.mailer.username")
    String username;
    @ConfigProperty(name = "quarkus.mailer.password")
    String password;

    public void buildAndSend(Exception exception) {
        try {
            log.info("Building email to notify {}", username);
            String htmlWithOriginalContent = readHtmlFile();
            String htmlWithNewContent = injectContentInHtml(htmlWithOriginalContent, exception);

            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", port);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });


            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("luancoelho.dev@gmail.com"));
            message.setSubject("Erro");
            message.setContent(htmlWithNewContent, "text/html");

            Transport.send(message);
            log.info("Email successfully sent to {}", username);
        } catch (Exception ignored) {
            log.info("Error sending email to {}", username);
        }
    }

    private String readHtmlFile() throws IOException, URISyntaxException {
        URL resourceUrl = getClass().getClassLoader().getResource("META-INF/resources/notify-error.html");

        if (resourceUrl == null) {
            throw new FileNotFoundException("File not found: META-INF/resources/notify-error.html");
        }

        Path resourcePath = Paths.get(resourceUrl.toURI());
        byte[] fileBytes = Files.readAllBytes(resourcePath);

        return new String(fileBytes, StandardCharsets.UTF_8);
    }

    private String injectContentInHtml(String html, Exception e) {
        String stackTrace = getStackTraceAsString(e);
        return html.replace("***message***", e.getMessage()).replace("***stacktrace***", stackTrace);
    }

    private String getStackTraceAsString(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
