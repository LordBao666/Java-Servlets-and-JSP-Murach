package com.lordbao.ch13ex2useradmin.admin;

import com.lordbao.ch13ex2useradmin.business.User;
import com.lordbao.ch13ex2useradmin.data.UserDB;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

public class UsersServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();

        String url = "/index.jsp";
        
        // get current action
        String action = request.getParameter("action");
        if (action == null) {
            action = "display_users";  // default action
        }
        
        // perform action and set URL to appropriate page
        if (action.equals("display_users")) {
            // get list of users
            List<User> users = UserDB.selectUsers();

            // set list as a request attribute
            request.setAttribute("users",users);
            // forward to index.jsp
            url = "/index.jsp";
        } 
        else if (action.equals("display_user")) {
            // get specified email
            String email = request.getParameter("email");
            // get user for email
            User user = UserDB.selectUser(email);
            // set as request attribute
            request.setAttribute("user",user);
            // forward to user.jsp
            url = "/user.jsp";
        }
        else if (action.equals("update_user")) {

            // get new data from request
            // note that the email is always unchanged
            String email = request.getParameter("email");
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            User user=null;

            if((user = UserDB.selectUser(email))!=null){

                user.setFirstName(firstName);
                user.setLastName(lastName);
                // update user
                // update user in database
                UserDB.update(user);
            }


            // get current list of users
            List<User> users = UserDB.selectUsers();
            // set as request attribute
            request.setAttribute("users",users);
            // forward to index.jsp
            url = "/index.jsp";
        }
        else if (action.equals("delete_user")) {
            // get the user for the specified email
            String email = request.getParameter("email");
            User user = UserDB.selectUser(email);
            if(user!=null){
                // delete the user
                UserDB.delete(user);
            }

            // get current list of users
            List<User> users = UserDB.selectUsers();
            // set as request attribute
            request.setAttribute("users",users);
            // forward to index.jsp
            url = "/index.jsp";
        }
        
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }    
    
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }    
}