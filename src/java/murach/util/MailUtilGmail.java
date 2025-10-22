package murach.util;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class MailUtilGmail {

    public static void sendMail(String to, String from,
            String subject, String body, boolean bodyIsHTML)
            throws MessagingException {

        // 1. Cấu hình SMTP Relay
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.sendgrid.net");
        props.put("mail.smtp.port", "587"); 

        // 2. Thông tin đăng nhập SendGrid
        final String username = "apikey"; // cố định, không đổi
        final String password = "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"; 

        // 3. Tạo session
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        session.setDebug(true);

        // 4. Tạo message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);

        if (bodyIsHTML) {
            message.setContent(body, "text/html; charset=utf-8");
        } else {
            message.setText(body);
        }

        // 5. Gửi mail
        Transport.send(message);
        System.out.println("✅ Email sent successfully to " + to);
    }
}
