package com.svalero.dadosytragos.servlet;

import com.svalero.dadosytragos.dao.UserDao;
import com.svalero.dadosytragos.database.Database;
import com.svalero.dadosytragos.domain.User;
import com.svalero.dadosytragos.exception.UserNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(username + " " + password);

        try {
            Database database = new Database();
            database.connect();
            UserDao userDao = new UserDao(database.getConnection());
            User u = userDao.loginUser(username, password);

            HttpSession session = request.getSession();
            session.setAttribute("userId",  u.getIdUsuario());
            session.setAttribute("usuario", u.getUsuario());
            session.setAttribute("role",    u.isRoleAdmin());

            response.getWriter().print("ok");
        } catch (SQLException sqle) {
            try {
                response.getWriter().println("No se ha podido conectar con la base de datos");
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            sqle.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (UserNotFoundException unfe) {
            try {
                response.getWriter().println("Usuario/contraseña incorrectos");
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

    }
}
