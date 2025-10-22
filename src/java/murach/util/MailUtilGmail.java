package murach.util;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class MailUtilGmail {

    public static void sendMail(String to, String from,
                                String subject, String body,
                                boolean bodyIsHTML) throws MessagingException {

        // --- 1. Cấu hình SMTP (dùng TLS, port 587) ---
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // --- 2. Lấy username/password từ biến môi trường ---
        // (Render khuyến khích không hardcode mật khẩu)
        final String username = System.getenv("GMAIL_USER");
        final String password = System.getenv("GMAIL_APP_PASSWORD");

        if (username == null || password == null) {
            throw new MessagingException("GMAIL_USER or GMAIL_APP_PASSWORD not set in environment");
        }

        // --- 3. Tạo session có xác thực ---
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        session.setDebug(true);

        // --- 4. Soạn email ---
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);

        if (bodyIsHTML) {
            message.setContent(body, "text/html; charset=UTF-8");
        } else {
            message.setText(body);
        }

        // --- 5. Gửi email ---
        Transport.send(message);
        System.out.println("✅ Email sent successfully to " + to);
    }
}
