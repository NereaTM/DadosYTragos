<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%-- boton lupa --%>
<button class="btn btn-outline-secondary"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#collapseBuscarComidas"
        aria-expanded="false"
        aria-controls="collapseBuscarComidas">
  <i class="bi bi-search"></i>
</button>

<%-- formulario de busqueda --%>
<div class="collapse mt-3" id="collapseBuscarComidas">
  <div class="card mb-4">
    <div class="card-header bg-primary text-white">
      <i class="bi bi-search"></i> Buscar Comidas
    </div>
    <div class="card-body">
      <form action="${pageContext.request.contextPath}/buscar-comidas" method="get" class="row g-3">
        <div class="col-md-6">
          <label for="nombre" class="form-label">Nombre</label>
          <input type="text" class="form-control" id="nombre" name="nombre"
                 value="${requestScope.nombreBusqueda != null ? requestScope.nombreBusqueda : ''}">
        </div>
        <div class="col-md-6">
          <label for="tipo" class="form-label">Tipo</label>
          <input type="text" class="form-control" id="tipo" name="tipo"
                 value="${param.tipo != null ? param.tipo : ''}" placeholder="Buscar por tipo">
        </div>

        <div class="col-md-4">
          <label for="precioMin" class="form-label">Precio mínimo</label>
          <input type="number" class="form-control" id="precioMin" name="precioMin" min="0" step="0.01"
                 value="${requestScope.precioMinBusqueda != null ? requestScope.precioMinBusqueda : ''}">
        </div>
        <div class="col-md-4">
          <label for="precioMax" class="form-label">Precio máximo</label>
          <input type="number" class="form-control" id="precioMax" name="precioMax" min="0" step="0.01"
                 value="${requestScope.precioMaxBusqueda != null ? requestScope.precioMaxBusqueda : ''}">
        </div>
        <div class="col-md-4">
          <label for="esVegetariana" class="form-label">Vegetariana</label>
          <select class="form-select" id="esVegetariana" name="esVegetariana">
            <option value="">- Cualquiera -</option>
            <option value="true"  ${requestScope.esVegetarianaBusqueda==true  ? 'selected' : ''}>Sí</option>
            <option value="false" ${requestScope.esVegetarianaBusqueda==false ? 'selected' : ''}>No</option>
          </select>
        </div>

        <div class="col-12 text-center mt-3">
          <button type="submit" class="btn btn-primary">
            <i class="bi bi-search"></i> Buscar
          </button>
          <a href="comidas" class="btn btn-secondary">
            <i class="bi bi-x-circle"></i> Limpiar
          </a>
        </div>
      </form>
    </div>
  </div>
</div>

<%@ include file="mensajes-busqueda.jsp" %>
