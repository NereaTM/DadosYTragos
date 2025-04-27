<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.svalero.dadosytragos.domain.Comida" %>
<%@ include file="includes/header.jsp" %>

<div class="container mt-4">
  <h1 class="text-center mb-4">Editar Comida</h1>

  <%
    Comida comida = (Comida) request.getAttribute("comida");
    if (comida != null) {
  %>
  <form action="${pageContext.request.contextPath}/comidas/editar" method="post">
    <input type="hidden" name="id" value="<%= comida.getIdComida() %>">

    <div class="mb-3">
      <label for="nombre" class="form-label">Nombre*</label>
      <input type="text" id="nombre" name="nombre"
             class="form-control" value="<%= comida.getNombre() %>" required>
    </div>
    <div class="mb-3">
      <label for="tipo" class="form-label">Tipo*</label>
      <input type="text" id="tipo" name="tipo"
             class="form-control" value="<%= comida.getTipo() %>" required>
    </div>
    <div class="mb-3">
      <label for="precio" class="form-label">Precio (€)*</label>
      <input type="number" step="0.01" id="precio" name="precio"
             class="form-control" value="<%= comida.getPrecio() %>" required>
    </div>
    <div class="mb-3">
      <label for="stock" class="form-label">Stock*</label>
      <input type="number" id="stock" name="stock"
             class="form-control" value="<%= comida.getStock() %>" min="0" required>
    </div>
    <div class="mb-3">
      <label for="descripcion" class="form-label">Descripción</label>
      <textarea id="descripcion" name="descripcion"
                class="form-control" rows="2"><%= comida.getDescripcion() %></textarea>
    </div>
    <div class="form-check mb-3">
      <input class="form-check-input" type="checkbox" id="esVegetariana" name="esVegetariana"
        <%= comida.isEsVegetariana() ? "checked" : "" %>>
      <label class="form-check-label" for="esVegetariana">Es vegetariana</label>
    </div>

    <div class="text-center">
      <button type="button" class="btn btn-primary"
              data-bs-toggle="modal" data-bs-target="#confirmarGuardarModal">
        Guardar cambios
      </button>
      <a href="${pageContext.request.contextPath}/comidas" class="btn btn-secondary">Cancelar</a>
    </div>

    <!-- Modal editar -->
    <div class="modal fade" id="confirmarGuardarModal" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Confirmar cambios</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            ¿Guardar los cambios en esta comida?
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
            <button type="submit" class="btn btn-success">Confirmar</button>
          </div>
        </div>
      </div>
    </div>
  </form>
  <% } else { %>
  <div class="alert alert-danger">No se encontró la comida.</div>
  <a href="${pageContext.request.contextPath}/comidas" class="btn btn-primary">Volver</a>
  <% } %>
</div>

<%@ include file="includes/footer.jsp" %>
