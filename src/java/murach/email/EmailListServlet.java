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

        // Lấy action (mặc định là join)
        String action = request.getParameter("action");
        if (action == null) {
            action = "join";
        }

        String url = "/index.jsp";

        if (action.equals("join")) {
            url = "/index.jsp";
        } 
        else if (action.equals("add")) {
            // Lấy dữ liệu từ form
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");

            // Gửi mail chào mừng
            String to = email;
            String from = "hnam19567@gmail.com"; // ← địa chỉ đã verify trong SendGrid
            String subject = "Welcome to our email list";
            String body = "Dear " + firstName + ",\n\n"
                    + "Thanks for joining our email list. We'll make sure to send "
                    + "you announcements about new products and promotions.\n"
                    + "Have a great day and thanks again!\n";
            boolean isBodyHTML = false;

            try {
                // Gửi mail qua SendGrid API
                MailUtilGmail.sendMail(to, from, subject, body, isBodyHTML);
            } catch (Exception e) {
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
                        "BODY:\n" + body + "\n", e);
            }

            // Lưu lại thông tin để hiện ở thanks.jsp
            request.setAttribute("firstName", firstName);
            request.setAttribute("lastName", lastName);
            request.setAttribute("email", email);

            // Chuyển sang trang cảm ơn
            url = "/thanks.jsp";
        }

        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }   
}