package com.svalero.dadosytragos.servlet;

import com.svalero.dadosytragos.dao.BebidaDao;
import com.svalero.dadosytragos.database.Database;
import com.svalero.dadosytragos.domain.Bebida;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/bebidas/*")
public class BebidaServlet extends HttpServlet {

    private static final String JSP_LISTA   = "/listaBebidas.jsp";
    private static final String JSP_NUEVO   = "/nuevaBebida.jsp";
    private static final String JSP_EDITAR  = "/editarBebida.jsp";
    private static final String JSP_ERROR   = "/error.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String accion = req.getPathInfo();
        if (accion == null || "/".equals(accion)) {
            listar(req, resp);
            return;
        }
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
        // Determinar si es admin y lo paso al JSP
        HttpSession session = req.getSession();
        Boolean roleAttr = (Boolean) session.getAttribute("role");
        boolean isAdmin = Boolean.TRUE.equals(roleAttr);
        req.setAttribute("isAdmin", isAdmin);

        // Obtener la lista de bebidas y enviarla
        Database db = new Database();
        try {
            db.connect();
            List<Bebida> bebidas = new BebidaDao(db.getConnection()).listarTodas();
            req.setAttribute("bebidas", bebidas);
            req.getRequestDispatcher(JSP_LISTA).forward(req, resp);
        } catch (Exception e) {
            error(req, resp, "Error al listar bebidas: " + e.getMessage());
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
            Bebida bebida = new BebidaDao(db.getConnection()).buscarPorId(id);
            if (bebida == null) {
                error(req, resp, "Bebida no encontrada");
            } else {
                req.setAttribute("bebida", bebida);
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
            Bebida bebida = extraerBebidaDeRequest(req);
            bebida.setFechaRegistro(LocalDate.now());

            Database db = new Database();
            try {
                db.connect();
                new BebidaDao(db.getConnection()).agregar(bebida);
                req.getSession().setAttribute("successMessage", "Bebida creada correctamente");
            } finally {
                closeDatabaseConnection(db);
            }
            resp.sendRedirect(req.getContextPath() + "/bebidas");
        } catch (Exception e) {
            req.getSession().setAttribute("errorMessage", "Error: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/bebidas/nuevo");
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
            BebidaDao dao = new BebidaDao(db.getConnection());
            // Cargo la bebida existente
            Bebida bebida = dao.buscarPorId(id);
            if (bebida == null) {
                error(req, resp, "Bebida no encontrada");
                return;
            }
            // Actualizo solo los campos
            bebida.setNombre(req.getParameter("nombre"));
            bebida.setTipo(req.getParameter("tipo"));
            bebida.setPrecio(parseDoubleOrError(req.getParameter("precio")));
            bebida.setStock(parseIntOrError(req.getParameter("stock")));
            bebida.setGraduacionAlcoholica(parseDoubleOrError(req.getParameter("graduacion")));
            bebida.setEsAlcoholica(req.getParameter("esAlcoholica") != null);

            boolean ok = dao.actualizar(bebida);
            req.getSession().setAttribute(ok ? "successMessage" : "errorMessage",
                    ok ? "Bebida actualizada correctamente" : "No se pudo actualizar");
            resp.sendRedirect(req.getContextPath() + "/bebidas");
        } catch (Exception e) {
            error(req, resp, "Error al editar bebida: " + e.getMessage());
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
            boolean ok = new BebidaDao(db.getConnection()).eliminar(id);
            req.getSession().setAttribute(ok ? "successMessage" : "errorMessage",
                    ok ? "Bebida eliminada correctamente" : "No se pudo eliminar");
        } catch (Exception e) {
            req.getSession().setAttribute("errorMessage", "Error al eliminar: " + e.getMessage());
        } finally {
            closeDatabaseConnection(db);
        }
        resp.sendRedirect(req.getContextPath() + "/bebidas");
    }

    private Bebida extraerBebidaDeRequest(HttpServletRequest req) throws Exception {
        Bebida b = new Bebida();
        String nombre = req.getParameter("nombre");
        if (nombre == null || nombre.isBlank()) throw new Exception("El nombre es obligatorio");
        b.setNombre(nombre);

        b.setTipo(req.getParameter("tipo"));
        b.setPrecio(parseDoubleOrError(req.getParameter("precio")));
        b.setStock(parseIntOrError(req.getParameter("stock")));
        b.setGraduacionAlcoholica(parseDoubleOrError(req.getParameter("graduacion")));
        b.setEsAlcoholica(req.getParameter("esAlcoholica") != null);
        return b;
    }

    private int parseIntOrDefault(String s, int defecto) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return defecto;
        }
    }

    private int parseIntOrError(String s) throws Exception {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new Exception("Valor entero invalido: " + s);
        }
    }

    private double parseDoubleOrError(String s) throws Exception {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            throw new Exception("Valor numerico inválido: " + s);
        }
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
        try {
            if (db != null) db.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}
