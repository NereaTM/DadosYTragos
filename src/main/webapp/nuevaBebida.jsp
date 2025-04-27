<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="includes/header.jsp" %>

<div class="container mt-4">
    <h1 class="mb-4">Registrar Nueva Bebida</h1>

    <form action="${pageContext.request.contextPath}/bebidas/crear" method="post">
        <div class="card mb-3">
            <div class="card-header bg-primary text-white">
                <h5 class="mb-0">Datos de la Bebida</h5>
            </div>
            <div class="card-body">
                <div class="mb-3">
                    <label for="nombre" class="form-label">Nombre*</label>
                    <input type="text" id="nombre" name="nombre"
                           class="form-control" required>
                </div>
                <div class="mb-3">
                    <label for="tipo" class="form-label">Tipo*</label>
                    <input type="text" id="tipo" name="tipo"
                           class="form-control" required>
                </div>
                <div class="mb-3">
                    <label for="precio" class="form-label">Precio (€)*</label>
                    <input type="number" step="0.01" id="precio" name="precio"
                           class="form-control" required>
                </div>
                <div class="mb-3">
                    <label for="stock" class="form-label">Stock*</label>
                    <input type="number" id="stock" name="stock"
                           class="form-control" min="0" required>
                </div>
                <div class="mb-3">
                    <label for="graduacion" class="form-label">Graduación alcohólica (%)*</label>
                    <input type="number" step="0.1" id="graduacion" name="graduacion"
                           class="form-control" required>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" id="esAlcoholica" name="esAlcoholica">
                    <label class="form-check-label" for="esAlcoholica">Es alcohólica</label>
                </div>
            </div>
        </div>

        <div class="d-flex justify-content-between">
            <a href="${pageContext.request.contextPath}/bebidas" class="btn btn-secondary">
                <i class="bi bi-arrow-left"></i> Cancelar
            </a>
            <button type="button" class="btn btn-success"
                    data-bs-toggle="modal" data-bs-target="#confirmarGuardarModal">
                <i class="bi bi-save"></i> Guardar
            </button>
        </div>

        <!-- Modal crear -->
        <div class="modal fade" id="confirmarGuardarModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Confirmar creación</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        ¿Seguro que quieres crear esta bebida?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary"
                                data-bs-dismiss="modal">Cancelar</button>
                        <button type="submit" class="btn btn-success">
                            <i class="bi bi-save"></i> Confirmar
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>

<%@ include file="includes/footer.jsp" %>
