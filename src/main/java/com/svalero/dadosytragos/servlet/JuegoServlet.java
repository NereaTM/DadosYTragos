package com.svalero.dadosytragos.servlet;

import com.svalero.dadosytragos.dao.JuegoDao;
import com.svalero.dadosytragos.database.Database;
import com.svalero.dadosytragos.domain.Juego;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@WebServlet("/juegos/*")
@MultipartConfig
public class JuegoServlet extends HttpServlet {

    private static final int POR_PAGINA       = 6;
    private static final String DEFAULT_IMAGE = "NoImagen.jpg";

    private static final String JSP_LISTA   = "/listaJuegos.jsp";
    private static final String JSP_DETALLE = "/detalleJuego.jsp";
    private static final String JSP_NUEVO   = "/nuevoJuego.jsp";
    private static final String JSP_EDITAR  = "/editarJuego.jsp";
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
            case "/detalle":
                mostrarDetalle(req, resp);
                break;
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
        int pagina = parseIntOrDefault(req.getParameter("pagina"), 1);
        int offset = (pagina - 1) * POR_PAGINA;

        Database db = new Database();
        try {
            db.connect();
            JuegoDao dao = new JuegoDao(db.getConnection());
            List<Juego> juegos = dao.listar(POR_PAGINA, offset);
            int total     = dao.contarTodos();
            int paginas   = (int) Math.ceil((double) total / POR_PAGINA);

            req.setAttribute("juegos", juegos);
            req.setAttribute("paginaActual", pagina);
            req.setAttribute("totalPaginas", paginas);
            req.getRequestDispatcher(JSP_LISTA).forward(req, resp);

        } catch (Exception e) {
            error(req, resp, "Error al listar juegos: " + e.getMessage());
        } finally {
            closeDatabaseConnection(db);
        }
    }

    private void mostrarDetalle(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idParam = req.getParameter("id");
        int id = parseIntOrDefault(idParam, -1);

        Juego juego = null;
        if (id >= 0) {
            Database db = new Database();
            try {
                db.connect();
                juego = new JuegoDao(db.getConnection()).buscarPorId(id);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeDatabaseConnection(db);
            }
        }

        req.setAttribute("juego", juego);
        req.getRequestDispatcher(JSP_DETALLE).forward(req, resp);
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
            Juego juego = new JuegoDao(db.getConnection()).buscarPorId(id);
            if (juego == null) {
                error(req, resp, "Juego no encontrado");
            } else {
                req.setAttribute("juego", juego);
                req.getRequestDispatcher(JSP_EDITAR).forward(req, resp);
            }
        } catch (Exception e) {
            error(req, resp, "Error al cargar edición: " + e.getMessage());
        } finally {
            closeDatabaseConnection(db);
        }
    }

    private void procesarCreacion(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        if (!esAdmin(req.getSession())) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        try {
            Juego juego = extraerJuegoDeRequest(req);
            Database db = new Database();
            int nuevoId;
            try {
                db.connect();
                nuevoId = new JuegoDao(db.getConnection()).agregar(juego);
                req.getSession().setAttribute("successMessage", "Juego creado correctamente");
            } finally {
                closeDatabaseConnection(db);
            }

            if (nuevoId > 0) {
                resp.sendRedirect(req.getContextPath() + "/juegos/detalle?id=" + nuevoId);
            } else {
                req.getSession().setAttribute("errorMessage", "No se ha podido crear el juego.");
                resp.sendRedirect(req.getContextPath() + "/juegos/nuevo");
            }

        } catch (Exception e) {
            req.getSession().setAttribute("errorMessage", "Error: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/juegos/nuevo");
        }
    }

    private void procesarEdicion(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        if (!esAdmin(req.getSession())) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        resp.setContentType("text/plain;charset=UTF-8");

        try {
            int id = parseIntOrDefault(req.getParameter("id"), -1);
            if (id < 0) throw new Exception("ID inválido");

            Juego juego = extraerJuegoDeRequest(req);
            juego.setIdJuego(id);

            Database db = new Database();
            try {
                db.connect();
                new JuegoDao(db.getConnection()).actualizar(juego);
                req.getSession().setAttribute("successMessage", "Juego editado correctamente");
                resp.sendRedirect(req.getContextPath() + "/juegos/detalle?id=" + id);
            } finally {
                closeDatabaseConnection(db);
            }
        } catch (Exception e) {
            resp.getWriter().print("error: " + e.getMessage());
        }
    }

    private void procesarEliminacion(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        if (!esAdmin(req.getSession())) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        int id = parseIntOrDefault(req.getParameter("id"), -1);
        if (id < 0) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        HttpSession session = req.getSession();
        Database db = new Database();
        try {
            db.connect();
            new JuegoDao(db.getConnection()).eliminar(id);
            req.getSession().setAttribute("successMessage", "Juego eliminado correctamente");
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Error al eliminar: " + e.getMessage());
        } finally {
            closeDatabaseConnection(db);
        }
        resp.sendRedirect(req.getContextPath() + "/juegos");
    }

    private Juego extraerJuegoDeRequest(HttpServletRequest req) throws Exception {
        Juego j = new Juego();

        String nombre = req.getParameter("nombre");
        if (nombre == null || nombre.isBlank()) throw new Exception("El nombre es obligatorio");
        j.setNombre(nombre);

        String descripcion = req.getParameter("descripcion");
        if (descripcion == null || descripcion.isBlank()) throw new Exception("La descripción es obligatoria");
        j.setDescripcion(descripcion);

        j.setCategoria(req.getParameter("categoria"));
        j.setMinJugadores(parseIntOrError(req.getParameter("minJugadores")));
        j.setMaxJugadores(parseIntOrError(req.getParameter("maxJugadores")));
        j.setEdadMinima(parseIntOrError(req.getParameter("edadMinima")));
        j.setDisponible(req.getParameter("disponible") != null);

        String fecha = req.getParameter("fechaAdquisicion");
        if (fecha != null && !fecha.isEmpty()) {
            j.setFechaAdquisicion(LocalDate.parse(fecha));
        }

        String valor = req.getParameter("valorAdquisicion");
        if (valor != null && !valor.isEmpty()) {
            j.setValorAdquisicion(Double.parseDouble(valor));
        }

        String rutaActual = req.getParameter("rutaImagenActual");
        Part imagen      = req.getPart("imagen");
        if (imagen != null && imagen.getSize() > 0) {
            j.setRutaImagen(procesarImagen(imagen));
        } else {
            j.setRutaImagen(rutaActual);
        }
        return j;
    }

    private String procesarImagen(Part imagen) throws IOException {
        if (imagen == null || imagen.getSize() == 0) {
            return DEFAULT_IMAGE;
        }
        String nombreArchivo = UUID.randomUUID() + ".jpg";
        String uploadDir   = getServletContext().getRealPath("/images/juegos");
        Path   ruta        = Paths.get(uploadDir);
        Files.createDirectories(ruta);

        try (InputStream input = imagen.getInputStream()) {
            Files.copy(input, ruta.resolve(nombreArchivo), StandardCopyOption.REPLACE_EXISTING);
        }
        return nombreArchivo;
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
        } catch (Exception e) {
            throw new Exception("Valor numérico inválido: " + s);
        }
    }

    private boolean esAdmin(HttpSession sesion) {
        Boolean rol = (Boolean) sesion.getAttribute("role");
        return Boolean.TRUE.equals(rol);
    }

    private void error(HttpServletRequest req, HttpServletResponse resp, String mensaje)
            throws ServletException, IOException {
        req.setAttribute("error", mensaje);
        req.getRequestDispatcher(JSP_ERROR).forward(req, resp);
    }

    private void closeDatabaseConnection(Database db) {
        try {
            if (db != null) {
                db.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}


