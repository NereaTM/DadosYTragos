<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  // Mensaje de exito
  if (session.getAttribute("successMessage") != null) {
%>
<div class="alert alert-success alert-dismissible fade show mb-3" role="alert">
  <i class="bi bi-check-circle-fill me-2"></i>
  <%= session.getAttribute("successMessage") %>
  <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
</div>
<%
    session.removeAttribute("successMessage");
  }

  // Mensaje de error
  if (session.getAttribute("errorMessage") != null) {
%>
<div class="alert alert-danger alert-dismissible fade show mb-3" role="alert">
  <i class="bi bi-exclamation-triangle-fill me-2"></i>
  <%= session.getAttribute("errorMessage") %>
  <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
</div>
<%
    session.removeAttribute("errorMessage");
  }
%>