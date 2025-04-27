<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.svalero.dadosytragos.domain.User" %>
<%@ include file="includes/header.jsp" %>
<%@ include file="includes/mensajes-alerta.jsp" %>
<%
    User u = (User) request.getAttribute("usuario");
    if (u == null) {
        response.sendRedirect(request.getContextPath() + "/usuarios");
        return;
    }
%>
<div class="container mt-4">
    <h1>Editar Usuario</h1>
    <form id="editarUsuarioForm" action="${pageContext.request.contextPath}/usuarios/editar" method="post">
        <input type="hidden" name="id" value="<%= u.getIdUsuario() %>">
        <div class="mb-3">
            <label for="usuario" class="form-label">Usuario*</label>
            <input type="text" id="usuario" name="usuario" class="form-control"
                   value="<%= u.getUsuario() %>" required>
        </div>
        <div class="mb-3">
            <label for="contrasena" class="form-label">Contraseña (dejar vacío para no cambiar)</label>
            <input type="password" id="contrasena" name="contrasena" class="form-control">
        </div>
        <div class="mb-3 form-check">
            <input type="checkbox" id="role" name="role" class="form-check-input"
                   <%= u.isRoleAdmin() ? "checked" : "" %>>
            <label for="role" class="form-check-label">Administrador</label>
        </div>
        <div class="mb-3">
            <label for="apellido" class="form-label">Apellido</label>
            <input type="text" id="apellido" name="apellido" class="form-control"
                   value="<%= u.getApellido() %>">
        </div>
        <div class="mb-3">
            <label for="fechaNacimiento" class="form-label">Fecha de Nacimiento</label>
            <input type="date" id="fechaNacimiento" name="fechaNacimiento" class="form-control"
                   value="<%= u.getFechaNacimiento() != null ? u.getFechaNacimiento() : "" %>">
        </div>
        <div class="mb-3">
            <label for="totalConsumo" class="form-label">Total Consumo (€)</label>
            <input type="number" id="totalConsumo" name="totalConsumo" class="form-control" step="0.01"
                   value="<%= u.getTotalConsumo() %>">
        </div>
        <div class="d-flex justify-content-between">
            <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#confirmarGuardarModal">
                <i class="bi bi-save"></i> Guardar cambios
            </button>
            <a href="${pageContext.request.contextPath}/usuarios" class="btn btn-secondary">Cancelar</a>
        </div>
        <!-- Modal editar -->
        <div class="modal fade" id="confirmarGuardarModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Confirmar cambios</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                    </div>
                    <div class="modal-body">
                        ¿Guardar los cambios en este usuario?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                        <button type="submit" class="btn btn-success">Confirmar</button>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<%@ include file="includes/footer.jsp" %>
