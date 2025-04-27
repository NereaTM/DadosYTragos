<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%-- boton lupa --%>
<button class="btn btn-outline-secondary"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#collapseBuscarBebidas"
        aria-expanded="false"
        aria-controls="collapseBuscarBebidas">
  <i class="bi bi-search"></i>
</button>

<%-- formulario de busqueda --%>
<div class="collapse mt-3" id="collapseBuscarBebidas">
  <div class="card mb-4">
    <div class="card-header bg-primary text-white">
      <i class="bi bi-search"></i> Buscar Bebidas
    </div>
    <div class="card-body">
      <form action="buscar-bebidas" method="get" class="row g-3">
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

        <div class="col-md-3">
          <label for="precioMin" class="form-label">Precio mínimo</label>
          <input type="number" class="form-control" id="precioMin" name="precioMin" min="0" step="0.01"
                 value="${requestScope.precioMinBusqueda != null ? requestScope.precioMinBusqueda : ''}">
        </div>
        <div class="col-md-3">
          <label for="precioMax" class="form-label">Precio máximo</label>
          <input type="number" class="form-control" id="precioMax" name="precioMax" min="0" step="0.01"
                 value="${requestScope.precioMaxBusqueda != null ? requestScope.precioMaxBusqueda : ''}">
        </div>
        <div class="col-md-2">
          <label for="esAlcoholica" class="form-label">Contiene alcohol</label>
          <select class="form-select" id="esAlcoholica" name="esAlcoholica">
            <option value="">- Cualquiera -</option>
            <option value="true"  ${requestScope.esAlcoholicaBusqueda==true  ? 'selected' : ''}>Sí</option>
            <option value="false" ${requestScope.esAlcoholicaBusqueda==false ? 'selected' : ''}>No</option>
          </select>
        </div>
        <div class="col-md-2">
          <label for="graduacionMin" class="form-label">Graduación mín.</label>
          <input type="number" class="form-control" id="graduacionMin" name="graduacionMin" min="0" step="0.1"
                 value="${requestScope.graduacionMinBusqueda != null ? requestScope.graduacionMinBusqueda : ''}">
        </div>
        <div class="col-md-2">
          <label for="graduacionMax" class="form-label">Graduación máx.</label>
          <input type="number" class="form-control" id="graduacionMax" name="graduacionMax" min="0" step="0.1"
                 value="${requestScope.graduacionMaxBusqueda != null ? requestScope.graduacionMaxBusqueda : ''}">
        </div>

        <div class="col-12 text-center mt-3">
          <button type="submit" class="btn btn-primary">
            <i class="bi bi-search"></i> Buscar
          </button>
          <a href="bebidas" class="btn btn-secondary">
            <i class="bi bi-x-circle"></i> Limpiar
          </a>
        </div>
      </form>
    </div>
  </div>
</div>

<%@ include file="mensajes-busqueda.jsp" %>
