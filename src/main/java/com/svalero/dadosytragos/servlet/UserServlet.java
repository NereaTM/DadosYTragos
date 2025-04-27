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
import java.util.List;

@WebServlet("/usuarios/*")
public class UserServlet extends HttpServlet {

    private static final String JSP_LISTA  = "/listaUsuarios.jsp";
    private static final String JSP_NUEVO  = "/nuevoUsuario.jsp";
    private static final String JSP_EDITAR = "/editarUsuario.jsp";
    private static final String JSP_ERROR  = "/error.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getPathInfo();
        if (action == null || "/".equals(action)) {
            listar(req, resp);
            return;
        }
        switch (action) {
            case "/nuevo":
                mostrarFormularioNuevo(req, resp);
                break;
            case "/editar":
                mostrarFormularioEdicion(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getPathInfo();
        switch (action) {
            case "/crear":
                procesarCreacion(req, resp);
                break;
            case "/editar":
                procesarEdicion(req, resp);
                break;
            case "/eliminar":
                procesarEliminacion(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Database db = new Database();
        try {
            db.connect();
            List<User> usuarios = new UserDao(db.getConnection()).findAll();
            req.setAttribute("usuarios", usuarios);
            req.getRequestDispatcher(JSP_LISTA).forward(req, resp);
        } catch (Exception e) {
            error(req, resp, "Error al listar usuarios: " + e.getMessage());
        } finally {
            closeDatabaseConnection(db);
        }
    }

    private void mostrarFormularioNuevo(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(JSP_NUEVO).forward(req, resp);
    }

    private void mostrarFormularioEdicion(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!esAdmin(req.getSession())) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        int id = parseIntOrDefault(req.getParameter("id"), -1);
        if (id < 0) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Database db = new Database();
        try {
            db.connect();
            User user = new UserDao(db.getConnection()).findById(id);
            if (user == null) {
                error(req, resp, "Usuario no encontrado");
            } else {
                req.setAttribute("usuario", user);
                req.getRequestDispatcher(JSP_EDITAR).forward(req, resp);
            }
        } catch (Exception e) {
            error(req, resp, "Error al cargar formulario de edición: " + e.getMessage());
        } finally {
            closeDatabaseConnection(db);
        }
    }

    private void procesarCreacion(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User u = new User();
        u.setUsuario(req.getParameter("usuario"));
        u.setContrasena(req.getParameter("contrasena"));
        u.setRoleAdmin(req.getParameter("role") != null);
        u.setApellido(req.getParameter("apellido"));
        u.setEmail(req.getParameter("email"));
        String fn = req.getParameter("fechaNacimiento");
        if (fn != null && !fn.isEmpty()) u.setFechaNacimiento(LocalDate.parse(fn));
        String edad = req.getParameter("edad");
        if (edad != null && !edad.isEmpty()) u.setEdad(Integer.parseInt(edad));
        u.setTotalConsumo(0.0);
        u.setFechaRegistro(LocalDate.now());

        HttpSession session = req.getSession();
        boolean isAdmin = Boolean.TRUE.equals(session.getAttribute("role"));

        Database db = new Database();
        try{
            db.connect();
            UserDao dao = new UserDao(db.getConnection());

            if (dao.emailExists(u.getEmail())) {
                session.setAttribute("errorMessage", "El email ya está registrado");
                resp.sendRedirect(req.getContextPath() + "/usuarios/nuevo");
                return;
            }

            dao.add(u);

            if (isAdmin) {
                session.setAttribute("successMessage", "Usuario creado correctamente");
                resp.sendRedirect(req.getContextPath() + "/usuarios");
            } else {
                session.setAttribute("successMessage",
                        "Cuenta creada correctamente. Por favor, inicia sesión.");
                resp.sendRedirect(req.getContextPath() + "/login.jsp");
            }

        } catch (Exception e) {
            session.setAttribute("errorMessage", "Error al crear usuario: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/usuarios/nuevo");
        }
    }

    private void procesarEdicion(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!esAdmin(req.getSession())) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        int id = parseIntOrDefault(req.getParameter("id"), -1);
        if (id < 0) {
            error(req, resp, "ID inválido");
            return;
        }

        Database db = new Database();
        try {
            db.connect();
            UserDao dao = new UserDao(db.getConnection());
            User orig = dao.findById(id);

            User u = new User();
            u.setIdUsuario(id);
            u.setUsuario(req.getParameter("usuario"));
            String pass = req.getParameter("contrasena");
            if (pass != null && !pass.isBlank()) {
                u.setContrasena(pass);
            }
            u.setRoleAdmin(req.getParameter("role") != null);
            u.setApellido(req.getParameter("apellido"));
            u.setEmail(orig.getEmail());
            String fn = req.getParameter("fechaNacimiento");
            if (fn != null && !fn.isEmpty()) {
                u.setFechaNacimiento(LocalDate.parse(fn));
            }
            String edad = req.getParameter("edad");
            if (edad != null && !edad.isEmpty()) {
                u.setEdad(Integer.parseInt(edad));
            }
            u.setTotalConsumo(orig.getTotalConsumo());

            dao.update(u);
            req.getSession().setAttribute("successMessage", "Usuario editado correctamente");
        } catch (Exception e) {
            error(req, resp, "Error al editar usuario: " + e.getMessage());
        } finally {
            closeDatabaseConnection(db);
        }
        resp.sendRedirect(req.getContextPath() + "/usuarios");
    }

    private void procesarEliminacion(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!esAdmin(req.getSession())) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        int id = parseIntOrDefault(req.getParameter("id"), -1);
        if (id < 0) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Database db = new Database();
        try {
            db.connect();
            new UserDao(db.getConnection()).delete(id);
            req.getSession().setAttribute("successMessage", "Usuario eliminado correctamente");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabaseConnection(db);
        }
        resp.sendRedirect(req.getContextPath() + "/usuarios");
    }

    private boolean esAdmin(HttpSession session) {
        return Boolean.TRUE.equals(session.getAttribute("role"));
    }

    private int parseIntOrDefault(String s, int defecto) {
        try { return Integer.parseInt(s); } catch (Exception e) { return defecto; }
    }

    private void error(HttpServletRequest req, HttpServletResponse resp, String msg)
            throws ServletException, IOException {
        req.setAttribute("error", msg);
        req.getRequestDispatcher(JSP_ERROR).forward(req, resp);
    }

    private void closeDatabaseConnection(Database db) {
        try { if (db != null) db.close(); } catch (SQLException ignored) {}
    }
}
