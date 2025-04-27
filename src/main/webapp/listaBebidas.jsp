<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.svalero.dadosytragos.domain.Bebida" %>
<%@ page import="java.util.List" %>
<%@ include file="includes/header.jsp" %>

<div class="container mt-4">
  <h1 class="text-center mb-4">Listado de Bebidas</h1>

  <% if (request.getAttribute("isAdmin") != null && (Boolean) request.getAttribute("isAdmin")) { %>
  <div class="text-end mb-3">
    <a href="${pageContext.request.contextPath}/bebidas/nuevo" class="btn btn-success">
      <i class="bi bi-plus-lg"></i> Añadir Bebida
    </a>
  </div>
  <% } %>

  <%@ include file="includes/formularioBuscarBebidas.jsp" %>

  <%
    List<Bebida> bebidas = (List<Bebida>) request.getAttribute("bebidas");
    if (bebidas != null && !bebidas.isEmpty()) {
  %>
  <table class="table table-striped table-hover">
    <thead class="table-dark">
      <tr>
        <th>Nombre</th>
        <th>Tipo</th>
        <th>Precio</th>
        <th>Alcohol</th>
        <% if (request.getAttribute("isAdmin") != null && (Boolean) request.getAttribute("isAdmin")) { %>
          <th>Acciones</th>
        <% } %>
      </tr>
    </thead>
    <tbody>
    <%
      for (Bebida bebida : bebidas) {
    %>
      <tr>
        <!-- abre modal detalle -->
        <td>
          <a href="#"
             class="text-decoration-none"
             data-bs-toggle="modal"
             data-bs-target="#detalleBebidaModal<%= bebida.getIdBebida() %>">
            <%= bebida.getNombre() %>
          </a>
        </td>
        <td><%= bebida.getTipo() %></td>
        <td><%= String.format("%.2f", bebida.getPrecio()) %> €</td>
        <td><%= bebida.isEsAlcoholica() ? "Sí" : "No" %></td>
        <% if (request.getAttribute("isAdmin") != null && (Boolean) request.getAttribute("isAdmin")) { %>
        <td>
          <div class="d-flex justify-content-end align-items-center">
            <!-- Editar -->
            <a href="${pageContext.request.contextPath}/bebidas/editar?id=<%= bebida.getIdBebida() %>"
               class="btn btn-sm btn-warning me-2">
              <i class="bi bi-pencil"></i>
            </a>
            <!-- Eliminar -->
            <button type="button"
                    class="btn btn-sm btn-danger"
                    data-bs-toggle="modal"
                    data-bs-target="#eliminarBebidaModal<%= bebida.getIdBebida() %>">
              <i class="bi bi-trash"></i>
            </button>
          </div>
        </td>
        <% } %>
      </tr>

      <!-- Modal detalle -->
      <div class="modal fade" id="detalleBebidaModal<%= bebida.getIdBebida() %>" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content shadow rounded-3">
            <div class="modal-header bg-primary text-white">
              <h5 class="modal-title">Detalle: <%= bebida.getNombre() %></h5>
              <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Cerrar"></button>
            </div>
            <div class="modal-body">
              <p><strong>Graduación:</strong> <%= bebida.getGraduacionAlcoholica() %> %</p>
              <p><strong>Stock:</strong>
                <% if (request.getAttribute("isAdmin") != null && (Boolean) request.getAttribute("isAdmin")) { %>
                  <%= bebida.getStock() %>
                <% } else { %>
                  <% if (bebida.getStock() > 0) { %>
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
      <div class="modal fade" id="eliminarBebidaModal<%= bebida.getIdBebida() %>" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">Confirmar eliminación</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
              ¿Eliminar <strong><%= bebida.getNombre() %></strong>? Esta acción no se puede deshacer.
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
              <form action="${pageContext.request.contextPath}/bebidas/eliminar" method="post">
                <input type="hidden" name="id" value="<%= bebida.getIdBebida() %>">
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
  <div class="alert alert-warning">No hay bebidas disponibles</div>
  <% } %>

  <div class="text-center mt-4">
    <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Volver al Inicio</a>
  </div>
</div>

<%@ include file="includes/footer.jsp" %>