package com.svalero.dadosytragos.servlet;

import com.svalero.dadosytragos.dao.JuegoDao;
import com.svalero.dadosytragos.database.Database;
import com.svalero.dadosytragos.domain.Juego;

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

@WebServlet("/buscar-juegos")
public class BuscarJuegosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recuperar parametros de busqueda
        String nombre = request.getParameter("nombre");
        String categoria = request.getParameter("categoria");

        // Comprobamos que los parametros no sean null o vacios y los convertimos al tipo que tienen que ser
        Integer minJugadores = null;
        if (request.getParameter("minJugadores") != null && !request.getParameter("minJugadores").isEmpty()) {
            try {
                minJugadores = Integer.parseInt(request.getParameter("minJugadores"));
            } catch (NumberFormatException ignored) {}
        }

        Integer maxJugadores = null;
        if (request.getParameter("maxJugadores") != null && !request.getParameter("maxJugadores").isEmpty()) {
            try {
                maxJugadores = Integer.parseInt(request.getParameter("maxJugadores"));
            } catch (NumberFormatException ignored) {}
        }

        Integer edadMinima = null;
        if (request.getParameter("edadMinima") != null && !request.getParameter("edadMinima").isEmpty()) {
            try {
                edadMinima = Integer.parseInt(request.getParameter("edadMinima"));
            } catch (NumberFormatException ignored) {}
        }

        Boolean disponible = null;
        if (request.getParameter("disponible") != null && !request.getParameter("disponible").isEmpty()) {
            disponible = Boolean.valueOf(request.getParameter("disponible"));
        }

        // Verificar si hay al menos hay 1 filtro
        boolean busquedaRealizada = (nombre != null && !nombre.isEmpty()) ||
                (categoria != null && !categoria.isEmpty()) ||
                minJugadores != null || maxJugadores != null ||
                edadMinima != null || disponible != null;

        List<Juego> juegos = new ArrayList<>();
        String mensajeBusqueda = "";
        Connection connection = null;

        try {
            Database database = new Database();
            database.connect();
            connection = database.getConnection();

            // Verificar que hay conexión
            if (connection == null) {
                mensajeBusqueda = "Error: No se pudo conectar a la base de datos";
                request.setAttribute("mensajeBusqueda", mensajeBusqueda);
                request.setAttribute("busquedaRealizada", true);
                request.setAttribute("paginaOrigen", "juegos");
                request.setAttribute("paginaActual", 1);

                // Mantenemos los filtros
                guardarParametrosBusqueda(request, nombre, categoria, minJugadores, maxJugadores, edadMinima, disponible);

                request.getRequestDispatcher("listaJuegos.jsp").forward(request, response);
                return;
            }

            JuegoDao juegoDao = new JuegoDao(connection);

            if (busquedaRealizada) {
                juegos = juegoDao.buscarPorCriterios(nombre, categoria, minJugadores,
                        maxJugadores, edadMinima, disponible);

                if (juegos.isEmpty()) {
                    mensajeBusqueda = "No se encontraron juegos";
                } else {
                    mensajeBusqueda = "Se encontraron " + juegos.size() + " juegos";
                }
            } else {
                // Mostrar todos los juegos si no hay filtros
                int paginaActual = 1;
                int elementosPorPagina = 9; // Mismo número que en ListJuegosServlet

                int offset = (paginaActual - 1) * elementosPorPagina;
                juegos = juegoDao.listar(elementosPorPagina, offset);

                int totalJuegos = juegoDao.contarTodos();
                int totalPaginas = (int) Math.ceil((double) totalJuegos / elementosPorPagina);

                request.setAttribute("paginaActual", paginaActual);
                request.setAttribute("totalPaginas", totalPaginas);
            }
        } catch (SQLException sqle) {
            mensajeBusqueda = "Error al buscar juegos: " + sqle.getMessage();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            // Cerrar conexión
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ignored) {}
            }
        }

        // Almacenar atributos para la vista
        request.setAttribute("juegos", juegos);
        request.setAttribute("mensajeBusqueda", mensajeBusqueda);
        request.setAttribute("busquedaRealizada", busquedaRealizada);
        request.setAttribute("paginaOrigen", "juegos");
        request.setAttribute("paginaActual", 1);

        // Recuperar si el usuario es admin (para mostrar/ocultar funcionalidades)
        Boolean roleAttr = (Boolean) request.getSession().getAttribute("role");
        boolean isAdmin = Boolean.TRUE.equals(roleAttr);
        request.setAttribute("isAdmin", isAdmin);


        // Almacenar los criterios para mantener el formulario con los valores introducidos
        guardarParametrosBusqueda(request, nombre, categoria, minJugadores, maxJugadores, edadMinima, disponible);

        // Redirigir a la página de resultados
        request.getRequestDispatcher("listaJuegos.jsp").forward(request, response);
    }

    private void guardarParametrosBusqueda(HttpServletRequest request, String nombre, String categoria,
                                           Integer minJugadores, Integer maxJugadores, Integer edadMinima,
                                           Boolean disponible) {
        request.setAttribute("nombreBusqueda", nombre);
        request.setAttribute("categoriaBusqueda", categoria);
        request.setAttribute("minJugadoresBusqueda", minJugadores);
        request.setAttribute("maxJugadoresBusqueda", maxJugadores);
        request.setAttribute("edadMinimaBusqueda", edadMinima);
        request.setAttribute("disponibleBusqueda", disponible);
    }
}