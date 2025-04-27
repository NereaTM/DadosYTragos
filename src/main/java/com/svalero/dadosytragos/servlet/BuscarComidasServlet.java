package com.svalero.dadosytragos.servlet;

import com.svalero.dadosytragos.dao.ComidaDao;
import com.svalero.dadosytragos.database.Database;
import com.svalero.dadosytragos.domain.Comida;

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

@WebServlet("/buscar-comidas")
public class BuscarComidasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recuperar parámetros de búsqueda
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

        Boolean esVegetariana = null;
        if (request.getParameter("esVegetariana") != null && !request.getParameter("esVegetariana").isEmpty()) {
            esVegetariana = Boolean.valueOf(request.getParameter("esVegetariana"));
        }

        // Verificar si hay al menos hay 1 filtro
        boolean busquedaRealizada = (nombre != null && !nombre.isEmpty()) ||
                (tipo != null && !tipo.isEmpty()) ||
                precioMin != null || precioMax != null ||
                esVegetariana != null;

        List<Comida> comidas = new ArrayList<>();
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
                request.setAttribute("paginaActual", "comidas");

                // Mantenemos los filtros
                guardarParametrosBusqueda(request, nombre, tipo, precioMin, precioMax, esVegetariana);

                request.getRequestDispatcher("listaComidas.jsp").forward(request, response);
                return;
            }

            ComidaDao comidaDao = new ComidaDao(connection);

            if (busquedaRealizada) {
                comidas = comidaDao.buscarPorCriterios(nombre, tipo, precioMin,
                        precioMax, esVegetariana);

                if (comidas.isEmpty()) {
                    mensajeBusqueda = "No se encontraron comidas";
                } else {
                    mensajeBusqueda = "Se encontraron " + comidas.size() + " comidas";
                }
            } else {
                // Mostrar todas las comidas si no hay filtros
                comidas = comidaDao.listarTodas();
            }
        } catch (Exception e) {
            mensajeBusqueda = "Error al buscar comidas: " + e.getMessage();
        } finally {
            // Cerrar conexión
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ignored) {}
            }
        }

        // Almacenar atributos para la vista
        request.setAttribute("comidas", comidas);
        request.setAttribute("mensajeBusqueda", mensajeBusqueda);
        request.setAttribute("busquedaRealizada", busquedaRealizada);
        request.setAttribute("paginaActual", "comidas");

        // Recuperar si el usuario es admin
        Boolean roleAttr = (Boolean) request.getSession().getAttribute("role");
        boolean isAdmin = Boolean.TRUE.equals(roleAttr);
        request.setAttribute("isAdmin", isAdmin);


        // Almacenar los criterios para mantener el formulario
        guardarParametrosBusqueda(request, nombre, tipo, precioMin, precioMax, esVegetariana);

        // Redirigir a la página de resultados
        request.getRequestDispatcher("listaComidas.jsp").forward(request, response);
    }

    private void guardarParametrosBusqueda(HttpServletRequest request, String nombre, String tipo,
                                           Double precioMin, Double precioMax, Boolean esVegetariana) {
        request.setAttribute("nombreBusqueda", nombre);
        request.setAttribute("tipoBusqueda", tipo);
        request.setAttribute("precioMinBusqueda", precioMin);
        request.setAttribute("precioMaxBusqueda", precioMax);
        request.setAttribute("esVegetarianaBusqueda", esVegetariana);
    }
}