<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="includes/header.jsp"%>

<div class="container mt-4">
    <h1 class="mb-4">Registrar Nuevo Juego</h1>

    <form action="${pageContext.request.contextPath}/juegos/crear"
          method="post"
          enctype="multipart/form-data">

        <div class="row mb-3">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0">Información Básica</h5>
                    </div>
                    <div class="card-body">
                        <div class="mb-3">
                            <label for="nombre" class="form-label">Nombre*</label>
                            <input type="text" id="nombre" name="nombre"
                                   class="form-control" required>
                        </div>
                        <div class="mb-3">
                            <label for="descripcion" class="form-label">Descripción*</label>
                            <textarea id="descripcion" name="descripcion"
                                      class="form-control" rows="3" required></textarea>
                        </div>
                        <div class="mb-3">
                            <label for="categoria" class="form-label">Categoría*</label>
                            <input type="text" id="categoria" name="categoria"
                                   class="form-control" required>
                        </div>
                        <div class="mb-3">
                            <label for="imagen" class="form-label">Imagen del juego</label>
                            <input type="file" id="imagen" name="imagen"
                                   class="form-control" accept="image/*">
                        </div>
                    </div>
                </div>
            </div>

            <!-- detalles juego -->
            <div class="col-md-6">
                <div class="card h-100">
                    <div class="card-header bg-info text-white">
                        <h5 class="mb-0">Detalles del Juego</h5>
                    </div>
                    <div class="card-body">
                        <div class="row g-2 mb-3">
                            <div class="col">
                                <label for="minJugadores" class="form-label">Mínimo jugadores*</label>
                                <input type="number" id="minJugadores" name="minJugadores"
                                       class="form-control" min="1" required>
                            </div>
                            <div class="col">
                                <label for="maxJugadores" class="form-label">Máximo jugadores*</label>
                                <input type="number" id="maxJugadores" name="maxJugadores"
                                       class="form-control" min="1" required>
                            </div>
                        </div>
                        <div class="mb-3">
                            <label for="edadMinima" class="form-label">Edad mínima*</label>
                            <input type="number" id="edadMinima" name="edadMinima"
                                   class="form-control" min="0" required>
                        </div>
                        <div class="mb-3 form-check">
                            <input class="form-check-input" type="checkbox" id="disponible" name="disponible" checked>
                            <label class="form-check-label" for="disponible">Disponible</label>
                        </div>
                        <div class="mb-3">
                            <label for="valorAdquisicion" class="form-label">Valor adquisición (€)</label>
                            <input type="number" step="0.01" id="valorAdquisicion"
                                   name="valorAdquisicion" class="form-control">
                        </div>
                        <div class="mb-3">
                            <label for="fechaAdquisicion" class="form-label">Fecha adquisición</label>
                            <input type="date" id="fechaAdquisicion" name="fechaAdquisicion"
                                   class="form-control">
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- botones -->
        <div class="d-flex justify-content-between">
            <a href="${pageContext.request.contextPath}/juegos" class="btn btn-secondary">
                <i class="bi bi-arrow-left"></i> Cancelar
            </a>
            <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#confirmarGuardarModal">
                <i class="bi bi-save"></i> Guardar
            </button>
        </div>

        <!-- Modal creacion -->
        <div class="modal fade" id="confirmarGuardarModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Confirmar creación</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        ¿Seguro que quieres crear este juego?
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
