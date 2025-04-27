<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.svalero.dadosytragos.domain.Comida" %>
<%@ page import="java.util.List" %>
<%@ include file="includes/header.jsp" %>

<div class="container mt-4">
  <h1 class="text-center mb-4">Listado de Comidas</h1>

  <% if (request.getAttribute("isAdmin") != null && (Boolean) request.getAttribute("isAdmin")) { %>
  <div class="text-end mb-3">
    <a href="${pageContext.request.contextPath}/comidas/nuevo" class="btn btn-success">
      <i class="bi bi-plus-lg"></i> Añadir Comida
    </a>
  </div>
  <% } %>

  <%@ include file="includes/formularioBuscarComidas.jsp" %>

  <%
    List<Comida> comidas = (List<Comida>) request.getAttribute("comidas");
    if (comidas != null && !comidas.isEmpty()) {
  %>
  <table class="table table-striped table-hover">
    <thead class="table-dark">
    <tr>
      <th>Nombre</th>
      <th>Tipo</th>
      <th>Precio</th>
      <th>Vegetariana</th>
      <% if (request.getAttribute("isAdmin") != null && (Boolean) request.getAttribute("isAdmin")) { %>
        <th>Acciones</th>
      <% } %>
    </tr>
    </thead>
    <tbody>
    <%
      for (Comida comida : comidas) {
    %>
    <tr>
      <!--  abre modal detalle -->
      <td>
        <a href="#"
           class="text-decoration-none"
           data-bs-toggle="modal"
           data-bs-target="#detalleComidaModal<%= comida.getIdComida() %>">
          <%= comida.getNombre() %>
        </a>
      </td>
      <td><%= comida.getTipo() %></td>
      <td><%= String.format("%.2f", comida.getPrecio()) %> €</td>
      <td><%= comida.isEsVegetariana() ? "Sí" : "No" %></td>
      <% if (request.getAttribute("isAdmin") != null && (Boolean) request.getAttribute("isAdmin")) { %>
      <td>
        <div class="d-flex justify-content-end align-items-center">
          <!-- Editar -->
          <a href="${pageContext.request.contextPath}/comidas/editar?id=<%= comida.getIdComida() %>"
             class="btn btn-sm btn-warning me-2">
            <i class="bi bi-pencil"></i>
          </a>
          <!-- Eliminar -->
          <button type="button"
                  class="btn btn-sm btn-danger"
                  data-bs-toggle="modal"
                  data-bs-target="#eliminarComidaModal<%= comida.getIdComida() %>">
            <i class="bi bi-trash"></i>
          </button>
        </div>
      </td>
      <% } %>
    </tr>

    <!-- Modal detalle -->
    <div class="modal fade" id="detalleComidaModal<%= comida.getIdComida() %>" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content shadow rounded-3">
          <div class="modal-header bg-primary text-white">
            <h5 class="modal-title">Detalle: <%= comida.getNombre() %></h5>
            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Cerrar"></button>
          </div>
          <div class="modal-body">
            <p><strong>Descripción:</strong><br><%= comida.getDescripcion() %></p>
            <p><strong>Stock:</strong>
              <% if (request.getAttribute("isAdmin") != null && (Boolean) request.getAttribute("isAdmin")) { %>
                <%= comida.getStock() %>
              <% } else { %>
                <% if (comida.getStock() > 0) { %>
                  <span class="badge bg-success">Disponible</span>
                <% } else { %>
                  <span class="badge bg-danger">No disponible</span>
                <% } %>
              <% } %>
            </p>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
          </div>
        </div>
      </div>
    </div>

    <% if (request.getAttribute("isAdmin") != null && (Boolean) request.getAttribute("isAdmin")) { %>
    <!-- Modal eliminar -->
    <div class="modal fade" id="eliminarComidaModal<%= comida.getIdComida() %>" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Confirmar eliminación</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            ¿Eliminar <strong><%= comida.getNombre() %></strong>? Esta acción no se puede deshacer.
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
            <form action="${pageContext.request.contextPath}/comidas/eliminar" method="post">
              <input type="hidden" name="id" value="<%= comida.getIdComida() %>">
              <button type="submit" class="btn btn-danger">Confirmar</button>
            </form>
          </div>
        </div>
      </div>
    </div>
    <% } %>

    <% } %>
    </tbody>
  </table>
  <% } else { %>
  <div class="alert alert-warning">No hay comidas disponibles</div>
  <% } %>

  <div class="text-center mt-4">
    <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Volver al Inicio</a>
  </div>
</div>

<%@ include file="includes/footer.jsp" %>