package murach.email;

import java.io.*;
import javax.mail.MessagingException;
import javax.servlet.*;
import javax.servlet.http.*;

import murach.business.User;
import murach.data.UserDB;
import murach.util.*;

public class EmailListServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy action (tùy form có gửi không)
        String action = request.getParameter("action");
        if (action == null) {
            action = "join";  // mặc định
        }

        String url = "/index.jsp"; // trang mặc định

        if (action.equals("join")) {
            url = "/index.jsp";
        } 
        else if (action.equals("add")) {
            // Lấy dữ liệu từ form
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");

            // Lưu thông tin vào request để hiển thị lại nếu cần
            request.setAttribute("firstName", firstName);
            request.setAttribute("lastName", lastName);
            request.setAttribute("email", email);

            // --- Gửi mail bằng Gmail ---
            String to = email;
            String from = "hnam19567@gmail.com"; // <-- thay bằng Gmail của bạn
            String subject = "Welcome to our email list";
            String body = "Dear " + firstName + ",\n\n" +
                "Thanks for joining our email list. We'll make sure to send " +
                "you announcements about new products and promotions.\n" +
                "Have a great day and thanks again!\n\n" +
                "Kelly Slivkoff\n" +
                "Mike Murach & Associates";
            boolean isBodyHTML = false;

            try {
                // Gửi email qua Gmail (MailUtilGmail đã chứa logic SMTP)
                MailUtilGmail.sendMail(to, from, subject, body, isBodyHTML);
            } catch (MessagingException e) {
                String errorMessage = 
                    "ERROR: Unable to send email. " +
                    "Check server logs for details.<br>" +
                    "ERROR MESSAGE: " + e.getMessage();
                request.setAttribute("errorMessage", errorMessage);
                this.log(
                    "Unable to send email.\n" +
                    "TO: " + email + "\n" +
                    "FROM: " + from + "\n" +
                    "SUBJECT: " + subject + "\n" +
                    "BODY:\n" + body + "\n");
            }

            // Sau khi gửi xong → đến trang cảm ơn
            url = "/thanks.jsp";
        }

        // Chuyển hướng đến trang kết quả
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }   
}