<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="includes/header.jsp"%>

<div class="container mt-5">
    <div class="row g-4">
        <div class="col-md-6">
            <div class="card h-100 border-primary">
                <div class="card-body text-center">
                    <i class="bi bi-dice-5-fill text-primary" style="font-size: 3rem;"></i>
                    <h3 class="card-title mt-3">Colección</h3>
                    <p class="card-text">Descubre los juegos de mesa para todas las edades y gustos.</p>
                    <a href="juegos" class="btn btn-primary mt-3">Ver Juegos</a>
                </div>
            </div>
        </div>

        <div class="col-md-6">
            <div class="card h-100 border-primary">
                <div class="card-body text-center">
                    <i class="bi bi-cup-hot-fill text-primary" style="font-size: 3rem;"></i>
                    <h3 class="card-title mt-3">Bebidas</h3>
                    <p class="card-text">Disfruta de nuestra selección de cervezas artesanales, cócteles y más.</p>
                    <a href="bebidas" class="btn btn-primary mt-3">Ver Bebidas</a>
                </div>
            </div>
        </div>

        <div class="col-md-6">
            <div class="card h-100 border-primary">
                <div class="card-body text-center">
                    <i class="bi bi-egg-fried text-primary" style="font-size: 3rem;"></i>
                    <h3 class="card-title mt-3">Comidas</h3>
                    <p class="card-text">Descubre nuestra selección de platos y tapas.</p>
                    <a href="comidas" class="btn btn-primary mt-3">Ver Comidas</a>
                </div>
            </div>
        </div>

        <%
            if (Boolean.TRUE.equals(isAdmin)) {
        %>
        <div class="col-md-6">
            <div class="card h-100 border-primary">
                <div class="card-body text-center">
                    <i class="bi bi-people-fill text-primary" style="font-size: 3rem;"></i>
                    <h3 class="card-title mt-3">Usuarios</h3>
                    <p class="card-text">Listado usuarios registrados en la web.</p>
                    <a href="usuarios" class="btn btn-primary mt-3">Ver Usuarios</a>
                </div>
            </div>
        </div>
        <%
            }
        %>
    </div>

    <div class="row mt-5 justify-content-center">
        <div class="col-md-6">
            <div class="card h-100" style="border-color: var(--color-doradoy);">
                <div class="card-header" style="background-color: var(--color-morado-oscuro); color: var(--color-dorado);">
                    <h3 class="mb-0 text-center"><i class="bi bi-geo-alt-fill me-2"></i>¿Dónde estamos?</h3>
                </div>
                <div class="card-body p-0">
                    <!-- Mapa del Maps -->
                    <iframe
                        src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d2981.947389679198!2d-0.8876504235640624!3d41.65178707126214!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0xd5914ce97ef7b31%3A0x1a1a6e7e1b5d0b1e!2sMonoNoke%20Zaragoza!5e0!3m2!1ses!2ses!4v1620000000000!5m2!1ses!2ses"
                        width="100%"
                        height="300"
                        style="border:0;"
                        allowfullscreen=""
                        loading="lazy">
                    </iframe>
                    <div class="p-3 text-center">
                        <p class="mb-1"><strong>Cam. de las Torres, 116, </strong></p>
                        <p class="mb-3">50007 Zaragoza, España</p>
                        <a href="https://www.google.es/maps/place/Mononoke+Board+Game+Caf%C3%A9/@41.6403106,-0.8882788,17z/data=!3m1!4b1!4m6!3m5!1s0xd5915b4db26b6dd:0xaecc85b00117864d!8m2!3d41.6403066!4d-0.8857039!16s%2Fg%2F11fnb04kd2?hl=es&entry=ttu&g_ep=EgoyMDI1MDQwOS4wIKXMDSoASAFQAw%3D%3D"
                           class="btn btn-primary mt-3">
                            <i class="bi bi-arrow-up-right-circle me-1"></i> Cómo llegar
                        </a>
                    </div>
                </div>
            </div>
        </div>
     </div>
</div>

<%@include file="includes/footer.jsp"%>