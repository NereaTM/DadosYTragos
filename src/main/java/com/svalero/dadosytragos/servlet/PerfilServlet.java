package com.svalero.dadosytragos.servlet;

import com.svalero.dadosytragos.dao.UserDao;
import com.svalero.dadosytragos.database.Database;
import com.svalero.dadosytragos.domain.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

@WebServlet("/perfil/*")
public class PerfilServlet extends HttpServlet {
    private static final String JSP_VIEW  = "/perfil.jsp";
    private static final String JSP_EDIT  = "/editarPerfil.jsp";
    private static final String JSP_ERROR = "/error.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getPathInfo();
        HttpSession session = req.getSession(false);
        Integer userId = session != null ? (Integer) session.getAttribute("userId") : null;
        // Si no estamos logeados redirigimos a login.jsp
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        Database db = new Database();
        try {
            db.connect();
            User user = new UserDao(db.getConnection()).findById(userId);
            req.setAttribute("usuario", user);

            if ("/editar".equals(action)) {
                req.getRequestDispatcher(JSP_EDIT).forward(req, resp);
            } else {
                req.getRequestDispatcher(JSP_VIEW).forward(req, resp);
            }
        } catch (Exception e) {
            req.setAttribute("error", "Error al cargar perfil: " + e.getMessage());
            req.getRequestDispatcher(JSP_ERROR).forward(req, resp);
        } finally {
            try { db.close(); } catch (SQLException ignore) {}
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getPathInfo();
        HttpSession session = req.getSession(false);
        Integer userId = session != null ? (Integer) session.getAttribute("userId") : null;
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        if ("/editar".equals(action)) {
            Database db = new Database();
            try {
                db.connect();
                UserDao dao = new UserDao(db.getConnection());
                User orig = dao.findById(userId);
                if (orig == null) {
                    session.setAttribute("errorMessage", "Usuario no encontrado");
                    resp.sendRedirect(req.getContextPath() + "/perfil");
                    return;
                }

                // Solo actualizamos los campos editables
                orig.setUsuario(req.getParameter("usuario"));

                String pass = req.getParameter("contrasena");
                if (pass != null && !pass.isBlank()) {
                    orig.setContrasena(pass);
                }

                orig.setApellido(req.getParameter("apellido"));

                String email = req.getParameter("email");
                if (email != null && !email.isBlank()) {
                    orig.setEmail(email);
                }

                String fn = req.getParameter("fechaNacimiento");
                if (fn != null && !fn.isEmpty()) {
                    orig.setFechaNacimiento(LocalDate.parse(fn));
                }

                String edadStr = req.getParameter("edad");
                if (edadStr != null && !edadStr.isEmpty()) {
                    orig.setEdad(Integer.parseInt(edadStr));
                }

                boolean ok = dao.update(orig);
                if (ok) {
                    session.setAttribute("successMessage", "Perfil actualizado correctamente");
                } else {
                    session.setAttribute("errorMessage", "No se pudo actualizar el perfil");
                }
                resp.sendRedirect(req.getContextPath() + "/perfil");
            } catch (Exception e) {
                session.setAttribute("errorMessage", "Error al actualizar perfil: " + e.getMessage());
                resp.sendRedirect(req.getContextPath() + "/perfil/editar");
            } finally {
                try { db.close(); } catch (SQLException ignore) {}
            }

        } else if ("/eliminar".equals(action)) {
            try {
                Database db = new Database();
                db.connect();
                boolean ok = new UserDao(db.getConnection()).delete(userId);
                db.close();

                if (ok) {
                    session.invalidate();
                    HttpSession newSession = req.getSession(true);
                    newSession.setAttribute("successMessage", "Tu cuenta se ha eliminado correctamente.");
                    resp.sendRedirect(req.getContextPath() + "/login.jsp");
                } else {
                    session.setAttribute("errorMessage", "No se pudo eliminar tu cuenta.");
                    resp.sendRedirect(req.getContextPath() + "/perfil");
                }
            } catch (Exception e) {
                session.setAttribute("errorMessage", "Error al eliminar cuenta: " + e.getMessage());
                resp.sendRedirect(req.getContextPath() + "/perfil");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}