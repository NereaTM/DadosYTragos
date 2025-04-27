<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.svalero.dadosytragos.domain.User" %>
<%@ include file="includes/header.jsp" %>
<%@ include file="includes/mensajes-alerta.jsp" %>

<%
    User u = (User) request.getAttribute("usuario");
    if (u == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>

<div class="container mt-4">
    <h1>Mi Perfil</h1>
    <ul class="list-group mb-3">
        <li class="list-group-item"><strong>Usuario:</strong> <%= u.getUsuario() %></li>
        <li class="list-group-item"><strong>Apellido:</strong> <%= u.getApellido() %></li>
        <li class="list-group-item"><strong>Email:</strong> <%= u.getEmail() %></li>
        <% Integer edad = u.getEdad();
           if (edad != null) { %>
          <li class="list-group-item"><strong>Edad:</strong> <%= edad %> años</li>
        <% } %>
    </ul>

    <div class="d-flex">
        <a href="<%= request.getContextPath() %>/perfil/editar" class="btn btn-primary me-2">
            <i class="bi bi-pencil"></i> Editar Perfil
        </a>
        <a href="<%= request.getContextPath() %>/" class="btn btn-secondary me-2">
            <i class="bi bi-arrow-left"></i> Volver
        </a>
        <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#eliminarCuentaModal">
            <i class="bi bi-trash"></i> Eliminar mi cuenta
        </button>
    </div>

    <!-- Modal eliminar -->
    <div class="modal fade" id="eliminarCuentaModal" tabindex="-1" aria-labelledby="eliminarCuentaLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="eliminarCuentaLabel">Confirmar eliminación</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                </div>
                <div class="modal-body">
                    ¿Estás seguro de que deseas eliminar tu cuenta? Esta acción es irreversible.
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                    <form action="${pageContext.request.contextPath}/perfil/eliminar" method="post">
                        <button type="submit" class="btn btn-danger">Sí, eliminar mi cuenta</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="includes/footer.jsp" %>