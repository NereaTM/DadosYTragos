<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.svalero.dadosytragos.domain.User" %>
<%@ page import="java.util.List" %>
<%@ include file="includes/header.jsp" %>
<%@ include file="includes/mensajes-alerta.jsp" %>

<div class="container mt-4">
    <h1>Gestión de Usuarios</h1>
    <a href="${pageContext.request.contextPath}/usuarios/nuevo" class="btn btn-success mb-3">
        <i class="bi bi-plus-circle"></i> Nuevo Usuario
    </a>
    <table class="table table-striped">
        <thead>
            <tr>
                <th>ID</th><th>Usuario</th><th>Email</th><th>Rol</th><th>Acciones</th>
            </tr>
        </thead>
        <tbody>
        <%
            List<User> usuarios = (List<User>) request.getAttribute("usuarios");
            for (User u : usuarios) {
        %>
            <tr>
                <td><%= u.getIdUsuario() %></td>
                <td><%= u.getUsuario() %></td>
                <td><%= u.getEmail() %></td>
                <td><%= u.isRoleAdmin() ? "Admin" : "User" %></td>
                <td>
                    <a href="${pageContext.request.contextPath}/usuarios/editar?id=<%= u.getIdUsuario() %>"
                       class="btn btn-sm btn-warning">
                        <i class="bi bi-pencil"></i>
                    </a>
                    <button type="button"
                            class="btn btn-sm btn-danger"
                            data-bs-toggle="modal"
                            data-bs-target="#eliminarUsuarioModal<%= u.getIdUsuario() %>">
                        <i class="bi bi-trash"></i>
                    </button>
                </td>
            </tr>
            <!-- Modal eliminar -->
            <div class="modal fade" id="eliminarUsuarioModal<%= u.getIdUsuario() %>" tabindex="-1" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Confirmar eliminación</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                        </div>
                        <div class="modal-body">
                            ¿Eliminar usuario <strong><%= u.getUsuario() %></strong>? Esta acción no se puede deshacer.
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                            <form action="${pageContext.request.contextPath}/usuarios/eliminar" method="post" style="display:inline;">
                                <input type="hidden" name="id" value="<%= u.getIdUsuario() %>">
                                <button type="submit" class="btn btn-danger">Confirmar</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        <% } %>
        </tbody>
    </table>
</div>

<%@ include file="includes/footer.jsp" %>
