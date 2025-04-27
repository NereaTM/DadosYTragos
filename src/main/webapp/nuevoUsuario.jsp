<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="includes/header.jsp" %>
<%
    // obtener rol de la sesión sin redeclarar 'session'
    javax.servlet.http.HttpSession _sess = request.getSession(false);
    boolean admin = _sess != null && Boolean.TRUE.equals(_sess.getAttribute("role"));
    String cancelUrl = admin
        ? request.getContextPath() + "/usuarios"
        : request.getContextPath() + "/login.jsp";
%>
<div class="container mt-4">
    <h1>Nuevo Usuario</h1>
    <form action="<%= request.getContextPath() %>/usuarios/crear" method="post">
        <div class="mb-3">
            <label for="usuario" class="form-label">Usuario*</label>
            <input type="text" id="usuario" name="usuario" class="form-control" required>
        </div>
        <div class="mb-3">
            <label for="contrasena" class="form-label">Contraseña*</label>
            <input type="password" id="contrasena" name="contrasena" class="form-control" required>
        </div>
        <% if (admin) { %>
        <div class="mb-3 form-check">
            <input type="checkbox" id="role" name="role" class="form-check-input">
            <label for="role" class="form-check-label">Administrador</label>
        </div>
        <% } %>
        <div class="mb-3">
            <label for="email" class="form-label">Email</label>
            <input type="email" id="email" name="email" class="form-control">
        </div>
        <div class="mb-3">
            <label for="apellido" class="form-label">Apellido</label>
            <input type="text" id="apellido" name="apellido" class="form-control">
        </div>
        <div class="mb-3">
            <label for="fechaNacimiento" class="form-label">Fecha de Nacimiento</label>
            <input type="date" id="fechaNacimiento" name="fechaNacimiento" class="form-control">
        </div>
        <button type="submit" class="btn btn-primary">Crear</button>
        <a href="<%= cancelUrl %>" class="btn btn-secondary">Cancelar</a>
    </form>
</div>
<%@ include file="includes/footer.jsp" %>