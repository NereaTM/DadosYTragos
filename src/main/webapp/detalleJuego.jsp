<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.svalero.dadosytragos.domain.Juego" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<%@ include file="includes/header.jsp" %>
<%@ include file="includes/mensajes-alerta.jsp" %>

<div class="container mt-4">
<%
    Juego juego = (Juego) request.getAttribute("juego");

    if (juego != null) {
%>
    <div class="row">
        <div class="col-md-4">
            <% if (juego.getRutaImagen() != null && !juego.getRutaImagen().isEmpty()) { %>
                <img src="${pageContext.request.contextPath}/images/juegos/<%= juego.getRutaImagen() %>"
                     class="img-fluid rounded" alt="<%= juego.getNombre() %>">
            <% } else { %>
                <div class="bg-secondary text-white text-center p-5 rounded">Sin imagen</div>
            <% } %>
        </div>

        <div class="col-md-8">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h1><%= juego.getNombre() %></h1>
                <% if (Boolean.TRUE.equals(isAdmin)) { %>
                    <div>
                        <a href="${pageContext.request.contextPath}/juegos/editar?id=<%= juego.getIdJuego() %>"
                           class="btn btn-warning me-2">
                            <i class="bi bi-pencil"></i> Editar
                        </a>
                        <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#eliminarModal">
                            <i class="bi bi-trash"></i> Eliminar
                        </button>
                    </div>
                <% } %>
            </div>

            <div class="card mb-4">
                <div class="card-header"><h5 class="mb-0">Descripción</h5></div>
                <div class="card-body"><p><%= juego.getDescripcion() %></p></div>
            </div>

            <div class="row">
                <div class="col-md-6">
                    <div class="card mb-3">
                        <div class="card-header bg-primary"><h5 class="mb-0">Detalles del Juego</h5></div>
                        <ul class="list-group list-group-flush">
                            <li class="list-group-item"><strong>Categoría:</strong> <%= juego.getCategoria() %></li>
                            <li class="list-group-item"><strong>Jugadores:</strong> <%= juego.getMinJugadores() %>-<%= juego.getMaxJugadores() %></li>
                            <li class="list-group-item"><strong>Edad mínima:</strong> <%= juego.getEdadMinima() %>+</li>
                            <li class="list-group-item">
                                <strong>Disponible:</strong>
                                <span class="badge bg-<%= juego.isDisponible() ? "success" : "danger" %>">
                                    <%= juego.isDisponible() ? "Sí" : "No" %>
                                </span>
                            </li>
                        </ul>
                    </div>
                </div>

                <% if (Boolean.TRUE.equals(isAdmin)) { %>
                    <div class="col-md-6">
                        <div class="card mb-3">
                            <div class="card-header bg-info text-white"><h5 class="mb-0">Información Privada</h5></div>
                            <ul class="list-group list-group-flush">
                                <% if (juego.getFechaAdquisicion() != null) { %>
                                    <li class="list-group-item">
                                        <strong>Fecha:</strong>
                                        <%= juego.getFechaAdquisicion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) %>
                                    </li>
                                <% } %>
                                <li class="list-group-item"><strong>Valor:</strong> <%= String.format("%.2f", juego.getValorAdquisicion()) %> €</li>
                                <li class="list-group-item"><strong>ID:</strong> <%= juego.getIdJuego() %></li>
                            </ul>
                        </div>
                    </div>
                <% } %>
            </div>

            <div class="mt-3">
                <a href="${pageContext.request.contextPath}/juegos" class="btn btn-secondary">
                    <i class="bi bi-arrow-left"></i> Volver
                </a>
            </div>

            <% if (Boolean.TRUE.equals(isAdmin)) { %>
                <!-- Modal eliminar -->
                <div class="modal fade" id="eliminarModal" tabindex="-1" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">Confirmar eliminación</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                ¿Eliminar el juego <strong><%= juego.getNombre() %></strong>? Esta acción no se puede deshacer.
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                                <form action="${pageContext.request.contextPath}/juegos/eliminar" method="post" style="display:inline;">
                                    <input type="hidden" name="id" value="<%= juego.getIdJuego() %>">
                                    <button type="submit" class="btn btn-danger"><i class="bi bi-trash"></i> Eliminar</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            <% } %>

<% } else { %>
    <div class="alert alert-danger">
        <h4>Error</h4>
        <p>No se ha encontrado el juego solicitado</p>
        <a href="${pageContext.request.contextPath}/juegos" class="btn btn-secondary">
            <i class="bi bi-arrow-left"></i> Volver
        </a>
    </div>
<% } %>

</div>

<%@ include file="includes/footer.jsp" %>
