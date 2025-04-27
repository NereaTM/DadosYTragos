<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%-- boton lupa --%>
<button class="btn btn-outline-secondary"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#collapseBuscarJuegos"
        aria-expanded="false"
        aria-controls="collapseBuscarJuegos">
  <i class="bi bi-search"></i>
</button>

<%-- formulario de busqueda --%>
<div class="collapse mt-3" id="collapseBuscarJuegos">
  <div class="card mb-4">
    <div class="card-header bg-primary text-white">
      <i class="bi bi-search"></i> Buscar Juegos
    </div>
    <div class="card-body">
      <form action="${pageContext.request.contextPath}/buscar-juegos" method="get" class="row g-3">
        <div class="col-md-6">
          <label for="nombre" class="form-label">Nombre</label>
          <input type="text" class="form-control" id="nombre" name="nombre"
                 value="${requestScope.nombreBusqueda != null ? requestScope.nombreBusqueda : ''}">
        </div>
        <div class="col-md-6">
          <label for="categoria" class="form-label">Categoría</label>
          <input type="text" class="form-control" id="categoria" name="categoria"
                 value="${param.categoria != null ? param.categoria : ''}" placeholder="Buscar por categoria">
        </div>

        <div class="col-md-3">
          <label for="minJugadores" class="form-label">Mínimo jugadores</label>
          <input type="number" class="form-control" id="minJugadores" name="minJugadores" min="1"
                 value="${requestScope.minJugadoresBusqueda != null ? requestScope.minJugadoresBusqueda : ''}">
        </div>
        <div class="col-md-3">
          <label for="maxJugadores" class="form-label">Máximo jugadores</label>
          <input type="number" class="form-control" id="maxJugadores" name="maxJugadores" min="1"
                 value="${requestScope.maxJugadoresBusqueda != null ? requestScope.maxJugadoresBusqueda : ''}">
        </div>
        <div class="col-md-3">
          <label for="edadMinima" class="form-label">Edad mínima</label>
          <input type="number" class="form-control" id="edadMinima" name="edadMinima" min="0"
                 value="${requestScope.edadMinimaBusqueda != null ? requestScope.edadMinimaBusqueda : ''}">
        </div>
        <div class="col-md-3">
          <label for="disponible" class="form-label">Disponibilidad</label>
          <select class="form-select" id="disponible" name="disponible">
            <option value="">- Cualquiera -</option>
            <option value="true"  ${requestScope.disponibleBusqueda==true  ? 'selected' : ''}>Disponible</option>
            <option value="false" ${requestScope.disponibleBusqueda==false ? 'selected' : ''}>No disponible</option>
          </select>
        </div>

        <div class="col-12 text-center mt-3">
          <button type="submit" class="btn btn-primary">
            <i class="bi bi-search"></i> Buscar
          </button>
          <a href="juegos" class="btn btn-secondary">
            <i class="bi bi-x-circle"></i> Limpiar
          </a>
        </div>
      </form>
    </div>
  </div>
</div>

<%@ include file="mensajes-busqueda.jsp" %>
