package com.lordbao.ch13ex1email.email;

import com.lordbao.ch13ex1email.business.User;
import com.lordbao.ch13ex1email.data.UserDB;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * @Author Lord_Bao
 * @Date 2024/6/6 17:11
 * @Version 1.0
 */
public class EmailListServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        String url = "/index.html";

        // get current action
        String action = request.getParameter("action");
        if (action == null) {
            action = "join";  // default action
        }

        // perform action and set URL to appropriate page
        if (action.equals("join")) {
            url = "/index.jsp";    // the "join" page
        }
        else if (action.equals("add")) {
            // get parameters from the request
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");

            // store data in User object
            User user = new User();
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);

            // validate the parameters
            String message;
            if (UserDB.emailExists(user.getEmail())) {
                message = "This email address already exists.<br>" +
                        "Please enter another email address.";
                url = "/index.jsp";
            }
            else {
                message = "";
                url = "/thanks.jsp";
                UserDB.insert(user);
            }
            request.setAttribute("user", user);
            request.setAttribute("message", message);
        }
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }
}
