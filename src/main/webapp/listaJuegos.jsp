<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.svalero.dadosytragos.domain.Juego" %>
<%@ page import="java.util.List" %>

<%@include file="includes/header.jsp"%>

<div class="container mt-4">
    <h1 class="text-center mb-4">Nuestra Colección de Juegos</h1>

    <%-- Formulario de busqueda --%>
    <%@include file="includes/formularioBuscarJuegos.jsp"%>

    <%
        List<Juego> juegos = (List<Juego>) request.getAttribute("juegos");
        Integer paginaActual = (Integer) request.getAttribute("paginaActual");
        Integer totalPaginas = (Integer) request.getAttribute("totalPaginas");

        if (juegos != null && !juegos.isEmpty()) {
    %>
    <div class="row row-cols-1 row-cols-md-3 g-4">
        <% for (Juego juego : juegos) { %>
        <div class="col">
            <div class="card h-100">
                <% if (juego.getRutaImagen() != null && !juego.getRutaImagen().isEmpty()) { %>
                <img src="images/juegos/<%= juego.getRutaImagen() %>"
                     class="card-img-top"
                     alt="<%= juego.getNombre() %>">
                <% } else { %>
                <div class="card-img-top bg-secondary text-white text-center p-5">
                    <i class="bi bi-dice-5" style="font-size: 2rem;"></i>
                </div>
                <% } %>

                <div class="card-body">
                    <h5 class="card-title"><%= juego.getNombre() %></h5>
                    <p class="card-text text-muted"><%= juego.getCategoria() %></p>
                    <p class="card-text">
                        <small class="text-muted">
                            <%= juego.getMinJugadores() %>-<%= juego.getMaxJugadores() %> jugadores |
                            Edad <%= juego.getEdadMinima() %>+
                        </small>
                    </p>
                </div>

                <div class="card-footer bg-transparent">
                    <a href="<%= request.getContextPath() %>/juegos/detalle?id=<%= juego.getIdJuego() %>"
                       class="btn btn-primary btn-sm">
                        Ver detalles
                    </a>
                </div>
            </div>
        </div>
        <% } %>
    </div>


    <!-- Paginación - Solo mostrar si no es busqueda -->
    <% if (totalPaginas != null && totalPaginas > 1 &&
            (request.getAttribute("busquedaRealizada") == null ||
                    !(Boolean)request.getAttribute("busquedaRealizada"))) { %>
    <nav aria-label="Paginación de juegos" class="mt-4">
        <ul class="pagination justify-content-center">
            <% if (paginaActual > 1) { %>
            <li class="page-item">
                <a class="page-link" href="juegos?pagina=<%= paginaActual - 1 %>" aria-label="Anterior">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>
            <% } %>

            <% for (int i = 1; i <= totalPaginas; i++) { %>
            <li class="page-item <%= (i == paginaActual) ? "active" : "" %>">
                <a class="page-link" href="juegos?pagina=<%= i %>"><%= i %></a>
            </li>
            <% } %>

            <% if (paginaActual < totalPaginas) { %>
            <li class="page-item">
                <a class="page-link" href="juegos?pagina=<%= paginaActual + 1 %>" aria-label="Siguiente">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>
            <% } %>
        </ul>
    </nav>
    <% } %>

    <% } else { %>
    <div class="alert alert-info text-center">
        <i class="bi bi-info-circle"></i> Actualmente no hay juegos disponibles
    </div>
    <% } %>
</div>

<%@include file="includes/footer.jsp"%>