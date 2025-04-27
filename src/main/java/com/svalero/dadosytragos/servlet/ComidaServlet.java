package com.svalero.dadosytragos.servlet;

import com.svalero.dadosytragos.dao.ComidaDao;
import com.svalero.dadosytragos.database.Database;
import com.svalero.dadosytragos.domain.Comida;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/comidas/*")
public class ComidaServlet extends HttpServlet {

    private static final String JSP_LISTA  = "/listaComidas.jsp";
    private static final String JSP_NUEVO  = "/nuevaComida.jsp";
    private static final String JSP_EDITAR = "/editarComida.jsp";
    private static final String JSP_ERROR  = "/error.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String accion = req.getPathInfo();
        if (accion == null || "/".equals(accion)) {
            listar(req, resp);
        } else {
            switch (accion) {
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
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String accion = req.getPathInfo();
        switch (accion) {
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
        HttpSession session = req.getSession();
        Boolean roleAttr = (Boolean) session.getAttribute("role");
        boolean isAdmin = Boolean.TRUE.equals(roleAttr);
        req.setAttribute("isAdmin", isAdmin);

        Database db = new Database();
        try {
            db.connect();
            List<Comida> comidas = new ComidaDao(db.getConnection()).listarTodas();
            req.setAttribute("comidas", comidas);
            req.getRequestDispatcher(JSP_LISTA).forward(req, resp);
        } catch (Exception e) {
            error(req, resp, "Error al listar comidas: " + e.getMessage());
        } finally {
            closeDatabaseConnection(db);
        }
    }

    private void mostrarFormularioNuevo(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!esAdmin(req.getSession())) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
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
            Comida comida = new ComidaDao(db.getConnection()).buscarPorId(id);
            if (comida == null) {
                error(req, resp, "Comida no encontrada");
            } else {
                req.setAttribute("comida", comida);
                req.getRequestDispatcher(JSP_EDITAR).forward(req, resp);
            }
        } catch (Exception e) {
            error(req, resp, "Error al cargar edición: " + e.getMessage());
        } finally {
            closeDatabaseConnection(db);
        }
    }

    private void procesarCreacion(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!esAdmin(req.getSession())) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        try {
            Comida comida = extraerComidaDeRequest(req);
            comida.setFechaRegistro(LocalDate.now());

            Database db = new Database();
            try {
                db.connect();
                new ComidaDao(db.getConnection()).agregar(comida);
                req.getSession().setAttribute("successMessage", "Comida creada correctamente");
            } finally {
                closeDatabaseConnection(db);
            }
            resp.sendRedirect(req.getContextPath() + "/comidas");
        } catch (Exception e) {
            req.getSession().setAttribute("errorMessage", "Error: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/comidas/nuevo");
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
            ComidaDao dao = new ComidaDao(db.getConnection());
            // Cargo la comida existente
            Comida comida = dao.buscarPorId(id);
            if (comida == null) {
                error(req, resp, "Comida no encontrada");
                return;
            }
            // Actualizo solo los campos
            comida.setNombre(req.getParameter("nombre"));
            comida.setTipo(req.getParameter("tipo"));
            comida.setPrecio(parseDoubleOrError(req.getParameter("precio")));
            comida.setStock(parseIntOrError(req.getParameter("stock")));
            comida.setDescripcion(req.getParameter("descripcion"));
            comida.setEsVegetariana(req.getParameter("esVegetariana") != null);

            boolean ok = dao.actualizar(comida);
            req.getSession().setAttribute(ok ? "successMessage" : "errorMessage",
                    ok ? "Comida editada correctamente" : "No se pudo editar");
            resp.sendRedirect(req.getContextPath() + "/comidas");
        } catch (Exception e) {
            error(req, resp, "Error al editar comida: " + e.getMessage());
        } finally {
            closeDatabaseConnection(db);
        }
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
            boolean ok = new ComidaDao(db.getConnection()).eliminar(id);
            req.getSession().setAttribute(ok ? "successMessage" : "errorMessage",
                    ok ? "Comida eliminada correctamente" : "No se pudo eliminar");
        } catch (Exception e) {
            req.getSession().setAttribute("errorMessage", "Error al eliminar: " + e.getMessage());
        } finally {
            closeDatabaseConnection(db);
        }
        resp.sendRedirect(req.getContextPath() + "/comidas");
    }

    private Comida extraerComidaDeRequest(HttpServletRequest req) throws Exception {
        Comida c = new Comida();
        String nombre = req.getParameter("nombre");
        if (nombre == null || nombre.isBlank()) throw new Exception("El nombre es obligatorio");
        c.setNombre(nombre);

        c.setTipo(req.getParameter("tipo"));
        c.setPrecio(parseDoubleOrError(req.getParameter("precio")));
        c.setStock(parseIntOrError(req.getParameter("stock")));
        c.setDescripcion(req.getParameter("descripcion"));
        c.setEsVegetariana(req.getParameter("esVegetariana") != null);
        return c;
    }

    private int parseIntOrDefault(String s, int defecto) {
        try {
            return Integer.parseInt(s); }
        catch (Exception e) {
            return defecto;
        }
    }
    private int parseIntOrError(String s) throws Exception {
        try {
            return Integer.parseInt(s);
        }
        catch (NumberFormatException e) {
            throw new Exception("Valor entero inválido: " + s);
        }
    }
    private double parseDoubleOrError(String s) throws Exception {
        try { return Double.parseDouble(s); }
        catch (NumberFormatException e) { throw new Exception("Valor numérico inválido: " + s); }
    }

    private boolean esAdmin(HttpSession session) {
        Boolean roleAttr = (Boolean) session.getAttribute("role");
        return Boolean.TRUE.equals(roleAttr);
    }

    private void error(HttpServletRequest req, HttpServletResponse resp, String mensaje)
            throws ServletException, IOException {
        req.setAttribute("error", mensaje);
        req.getRequestDispatcher(JSP_ERROR).forward(req, resp);
    }

    private void closeDatabaseConnection(Database db) {
        try { if (db != null) db.close(); }
        catch (SQLException se) { se.printStackTrace(); }
    }
}
