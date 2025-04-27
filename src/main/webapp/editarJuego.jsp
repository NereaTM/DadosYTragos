<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.svalero.dadosytragos.domain.Juego" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<%@include file="includes/header.jsp"%>

<%
    // recuperamos el juego a editar
    Juego juego = (Juego) request.getAttribute("juego");
    if (juego == null) {
        // si no hay juego redirigimos a la lista
        response.sendRedirect(request.getContextPath() + "/juegos");
        return;
    }
%>

<div class="container mt-4">
    <h1>Editar Juego</h1>

    <form action="${pageContext.request.contextPath}/juegos/editar" method="post" enctype="multipart/form-data">
    <input type="hidden" name="id" value="<%= juego.getIdJuego() %>">

        <div class="row mb-3">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0">Información Básica</h5>
                    </div>
                    <div class="card-body">
                        <div class="mb-3">
                            <label for="nombre" class="form-label">Nombre*</label>
                            <input type="text" class="form-control" id="nombre" name="nombre"
                                   value="<%= juego.getNombre() %>" required>
                        </div>

                        <div class="mb-3">
                            <label for="descripcion" class="form-label">Descripción*</label>
                            <textarea class="form-control" id="descripcion" name="descripcion"
                                      rows="4" required><%= juego.getDescripcion() %></textarea>
                        </div>

                        <div class="mb-3">
                            <label for="categoria" class="form-label">Categoría*</label>
                            <input type="text" class="form-control" id="categoria" name="categoria"
                                   value="<%= juego.getCategoria() %>" required>
                        </div>

                        <div class="mb-3">
                            <label for="imagen" class="form-label">Imagen del juego</label>
                            <div class="row">
                                <div class="col-md-8">
                                    <input type="file" class="form-control" id="imagen" name="imagen" accept="image/*">
                                    <div class="form-text">Subir una nueva imagen para reemplazar la actual</div>
                                    <input type="hidden" name="rutaImagenActual" value="<%= juego.getRutaImagen() %>"/>
                                </div>
                                <div class="col-md-4">
                                    <% if (juego.getRutaImagen() != null && !juego.getRutaImagen().isEmpty()) { %>
                                        <img src="images/juegos/<%= juego.getRutaImagen() %>" alt="Imagen actual" class="img-thumbnail" style="max-height: 100px;">
                                    <% } else { %>
                                        <div class="text-muted">Sin imagen</div>
                                    <% } %>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-md-6">
                <div class="card h-100">
                    <div class="card-header bg-info text-white">
                        <h5 class="mb-0">Detalles del Juego</h5>
                    </div>
                    <div class="card-body">
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="minJugadores" class="form-label">Mínimo de jugadores*</label>
                                <input type="number" class="form-control" id="minJugadores" name="minJugadores"
                                       value="<%= juego.getMinJugadores() %>" min="1" required>
                            </div>

                            <div class="col-md-6">
                                <label for="maxJugadores" class="form-label">Máximo de jugadores*</label>
                                <input type="number" class="form-control" id="maxJugadores" name="maxJugadores"
                                       value="<%= juego.getMaxJugadores() %>" min="1" required>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="edadMinima" class="form-label">Edad mínima*</label>
                            <input type="number" class="form-control" id="edadMinima" name="edadMinima"
                                   value="<%= juego.getEdadMinima() %>" min="0" required>
                        </div>

                        <div class="mb-3 form-check">
                            <input type="checkbox" class="form-check-input" id="disponible" name="disponible"
                                   <%= juego.isDisponible() ? "checked" : "" %>>
                            <label class="form-check-label" for="disponible">Disponible</label>
                        </div>

                        <div class="mb-3">
                            <label for="valorAdquisicion" class="form-label">Valor de adquisición (€)</label>
                            <input type="number" class="form-control" id="valorAdquisicion" name="valorAdquisicion"
                                   value="<%= juego.getValorAdquisicion() %>" step="0.01" min="0">
                        </div>

                        <div class="mb-3">
                            <label for="fechaAdquisicion" class="form-label">Fecha de adquisición</label>
                            <input type="date" class="form-control" id="fechaAdquisicion" name="fechaAdquisicion"
                                   value="<%= juego.getFechaAdquisicion() != null ? juego.getFechaAdquisicion() : "" %>">
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="mb-3">
            <p class="text-muted">* Campos obligatorios</p>
        </div>

        <div class="d-flex justify-content-between">
            <a href="${pageContext.request.contextPath}/juegos/detalle?id=${juego.idJuego}" class="btn btn-secondary">
            <i class="bi bi-arrow-left"></i> Cancelar </a>

            <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#guardarCambiosModal">
                <i class="bi bi-save"></i> Guardar Cambios
            </button>
        </div>

        <!-- Modal editar -->
        <div class="modal fade" id="guardarCambiosModal" tabindex="-1" aria-labelledby="guardarCambiosModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="guardarCambiosModalLabel">Confirmar cambios</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        ¿Estás seguro de guardar los cambios para el juego <strong><%= juego.getNombre() %></strong>?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                        <button type="submit" class="btn btn-success">
                            <i class="bi bi-save"></i> Confirmar
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>

<%@include file="includes/footer.jsp"%>