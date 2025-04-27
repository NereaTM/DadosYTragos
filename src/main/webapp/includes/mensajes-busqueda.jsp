<%-- Muestra mensaje si existe --%>
<%
  Boolean busquedaRealizada = (Boolean) request.getAttribute("busquedaRealizada");
  String mensajeBusqueda      = (String)  request.getAttribute("mensajeBusqueda");
%>
<% if (Boolean.TRUE.equals(busquedaRealizada)) { %>
<div class="alert alert-info">
  <i class="bi bi-info-circle"></i>
  <%= mensajeBusqueda %>
</div>

<script>
  document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.collapse').forEach(function(el) {
      // sólo si no está abierto ya
      if (!el.classList.contains('show')) {
        new bootstrap.Collapse(el, { toggle: true });
      }
    });
  });
</script>

<% } else if (mensajeBusqueda != null && mensajeBusqueda.startsWith("Error")) { %>
<div class="alert alert-danger">
  <i class="bi bi-exclamation-triangle"></i>
  <%= mensajeBusqueda %>
</div>
<% } %>
