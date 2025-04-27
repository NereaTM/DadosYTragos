<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="includes/header.jsp"%>

<script type="text/javascript">
    $(document).ready(function() {
        $("form").on("submit", function(event) {
            event.preventDefault();
            const formValue = $(this).serialize();
            $.post("login", formValue, function(response) {
                if (response === "ok") {
                    window.location.href = "index.jsp";
                } else {
                    $("#result").html("<div class='alert alert-danger' role='alert'>" + response + "</div>");
                }
            });
        });
    });
</script>

<main>
    <div class="container d-flex justify-content-center">
        <form>
            <h1 class="h3 mb-3 fw-normal">Iniciar sesión</h1>
            <div class="input-group mb-3">
                <input type="text" name="username" class="form-control" placeholder="Usuario" required>
            </div>

            <div class="input-group mb-3">
                <input type="password" name="password" class="form-control" placeholder="Contraseña" required>
            </div>

            <button class="btn btn-primary w-100 py-2" type="submit">Iniciar sesión</button>

            <div class="input-group mb-3">
                ¿No tienes usuario?
                <a href="${pageContext.request.contextPath}/usuarios/nuevo"> Regístrate aquí</a>
            </div>

            <div id="result"></div>
        </form>
    </div>
</main>

<%@include file="includes/footer.jsp"%>