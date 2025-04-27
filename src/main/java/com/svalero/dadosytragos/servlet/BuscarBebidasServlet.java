package com.svalero.dadosytragos.servlet;

import com.svalero.dadosytragos.dao.BebidaDao;
import com.svalero.dadosytragos.database.Database;
import com.svalero.dadosytragos.domain.Bebida;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/buscar-bebidas")
public class BuscarBebidasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recuperar parametros de busqueda
        String nombre = request.getParameter("nombre");
        String tipo = request.getParameter("tipo");

        // Comprobamos que los parametros no sean null o vacios y los convertimos al tipo que tienen que ser
        Double precioMin = null;
        if (request.getParameter("precioMin") != null && !request.getParameter("precioMin").isEmpty()) {
            try {
                precioMin = Double.parseDouble(request.getParameter("precioMin"));
            } catch (NumberFormatException ignored) {}
        }

        Double precioMax = null;
        if (request.getParameter("precioMax") != null && !request.getParameter("precioMax").isEmpty()) {
            try {
                precioMax = Double.parseDouble(request.getParameter("precioMax"));
            } catch (NumberFormatException ignored) {}
        }

        Boolean esAlcoholica = null;
        if (request.getParameter("esAlcoholica") != null && !request.getParameter("esAlcoholica").isEmpty()) {
            esAlcoholica = Boolean.valueOf(request.getParameter("esAlcoholica"));
        }

        Double graduacionMin = null;
        if (request.getParameter("graduacionMin") != null && !request.getParameter("graduacionMin").isEmpty()) {
            try {
                graduacionMin = Double.parseDouble(request.getParameter("graduacionMin"));
            } catch (NumberFormatException ignored) {}
        }

        Double graduacionMax = null;
        if (request.getParameter("graduacionMax") != null && !request.getParameter("graduacionMax").isEmpty()) {
            try {
                graduacionMax = Double.parseDouble(request.getParameter("graduacionMax"));
            } catch (NumberFormatException ignored) {}
        }

        // Verificar si hay al menos hay 1 filtro
        boolean busquedaRealizada = (nombre != null && !nombre.isEmpty()) ||
                (tipo != null && !tipo.isEmpty()) ||
                precioMin != null || precioMax != null ||
                esAlcoholica != null ||
                graduacionMin != null || graduacionMax != null;

        List<Bebida> bebidas = new ArrayList<>();
        String mensajeBusqueda = "";

        Connection connection = null;
        Database db = new Database();
        try {
            db.connect();
            connection = db.getConnection();

            // Verificar que hay conexión
            if (connection == null) {
                mensajeBusqueda = "Error: No se pudo conectar a la base de datos";
                request.setAttribute("mensajeBusqueda", mensajeBusqueda);
                request.setAttribute("busquedaRealizada", true);
                request.setAttribute("paginaActual", "bebidas");

                // Mantenemos los filtros
                guardarParametrosBusqueda(request, nombre, tipo, precioMin, precioMax,
                        esAlcoholica, graduacionMin, graduacionMax);

                request.getRequestDispatcher("listaBebidas.jsp").forward(request, response);
                return;
            }

            BebidaDao bebidaDao = new BebidaDao(connection);

            if (busquedaRealizada) {
                bebidas = bebidaDao.buscarPorCriterios(nombre, tipo, precioMin,
                        precioMax, esAlcoholica,
                        graduacionMin, graduacionMax);

                if (bebidas.isEmpty()) {
                    mensajeBusqueda = "No se encontraron bebidas";
                } else {
                    mensajeBusqueda = "Se encontraron " + bebidas.size() + " bebidas";
                }
            } else {
                // Mostrar todas las bebidas si no hay filtros
                bebidas = bebidaDao.listarTodas();
            }
        } catch (Exception e) {
            mensajeBusqueda = "Error al buscar bebidas: " + e.getMessage();
        } finally {
            // Cerrar conexión
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ignored) {}
            }
        }

        // Almacenar atributos para la vista
        request.setAttribute("bebidas", bebidas);
        request.setAttribute("mensajeBusqueda", mensajeBusqueda);
        request.setAttribute("busquedaRealizada", busquedaRealizada);
        request.setAttribute("paginaActual", "bebidas");

        // Recuperar si el usuario es admin
        Boolean roleAttr = (Boolean) request.getSession().getAttribute("role");
        boolean isAdmin = Boolean.TRUE.equals(roleAttr);
        request.setAttribute("isAdmin", isAdmin);

        // Almacenar los criterios para mantener el formulario
        guardarParametrosBusqueda(request, nombre, tipo, precioMin, precioMax,
                esAlcoholica, graduacionMin, graduacionMax);

        // Redirigir a la página de resultados
        request.getRequestDispatcher("listaBebidas.jsp").forward(request, response);
    }

    private void guardarParametrosBusqueda(HttpServletRequest request, String nombre, String tipo,
                                           Double precioMin, Double precioMax, Boolean esAlcoholica,
                                           Double graduacionMin, Double graduacionMax) {
        request.setAttribute("nombreBusqueda", nombre);
        request.setAttribute("tipoBusqueda", tipo);
        request.setAttribute("precioMinBusqueda", precioMin);
        request.setAttribute("precioMaxBusqueda", precioMax);
        request.setAttribute("esAlcoholicaBusqueda", esAlcoholica);
        request.setAttribute("graduacionMinBusqueda", graduacionMin);
        request.setAttribute("graduacionMaxBusqueda", graduacionMax);
    }
}