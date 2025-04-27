<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>DadosYTragos</title>
    <!-- necesario para bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <!-- necesario para jquery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <!-- css -->
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
</head>
<body>

<%@ include file="mensajes-alerta.jsp" %>

<header class="fixed-top">
    <nav class="navbar navbar-expand-lg navbar-dark">
        <div class="container">

            <a href="index.jsp" class="navbar-brand d-flex align-items-center">
                <strong class="ms-2">Dados y Tragos</strong>
            </a>

            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarMain">
                <span class="navbar-toggler-icon"></span>
            </button>

            <!-- navbar -->
            <div class="collapse navbar-collapse" id="navbarMain">
                <!-- menu principal -->
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/index.jsp" class="nav-link">Inicio</a>
                    </li>
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/juegos" class="nav-link">Juegos</a>
                    </li>
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/bebidas" class="nav-link">Bebidas</a>
                    </li>
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/comidas" class="nav-link">Comidas</a>
                    </li>
                </ul>

                <!-- Menu de usuario -->
                <div class="d-flex align-items-center">
                    <%
                        // obtenemos el role: true=admin, false=user, null=anonymous
                        HttpSession currentSession = request.getSession(false);
                        Boolean isAdmin = null;
                        if (currentSession != null) {
                            isAdmin = (Boolean) currentSession.getAttribute("role");
                        }

                        if (isAdmin == null) {
                    %>
                    <!-- Anonymous, no puede ver nada del menu -->
                    <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-link p-0 border-0">
                        <img
                                src="${pageContext.request.contextPath}/images/Login.png"
                                alt="Iniciar sesión"
                                style="height: 40px;">
                    </a>
                    <%
                    } else if (!isAdmin) {
                    %>
                    <!-- Usuario normal, puede ver el perfil y deslogear -->
                    <div class="btn-group me-3">
                        <a href="${pageContext.request.contextPath}/perfil" class="btn btn-outline-light me-3" title="Mi perfil">
                            <i class="bi bi-person"></i>
                        </a>
                    </div>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-light">
                        <i class="bi bi-box-arrow-right"></i>
                    </a>
                    <%
                    } else {
                    %>
                    <!-- Administrador, puede crear juego, bebida, comida y usuarios -->
                    <div class="btn-group me-3">
                        <a href="nuevoJuego.jsp" class="btn btn-outline-light" title="Nuevo Juego">
                            <i class="bi bi-plus-circle"></i>
                        </a>
                        <a href="nuevaBebida.jsp" class="btn btn-outline-light" title="Nueva Bebida">
                            <i class="bi bi-cup-hot"></i>
                        </a>
                        <a href="nuevaComida.jsp" class="btn btn-outline-light" title="Nueva Comida">
                            <i class="bi bi-egg-fried"></i>
                        </a>
                        <a href="nuevoUsuario.jsp" class="btn btn-outline-light" title="Nuevo Usuario">
                            <i class="bi bi-people-fill"></i>
                        </a>
                    </div>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-light">
                        <i class="bi bi-box-arrow-right"></i>
                    </a>
                    <%
                        }
                    %>
                </div>
            </div>
        </div>
    </nav>
</header>
