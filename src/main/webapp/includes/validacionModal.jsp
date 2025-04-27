<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Cuando cualquier modal vaya a mostrarse
        document.addEventListener('show.bs.modal', function(e) {
            const disparador = e.relatedTarget;
            if (!disparador) return;
            // Buscamos el formulario contenedor del botón que abrió el modal
            const form = disparador.closest('form');
            if (!form) return;
            // Si NO es válido, prevenimos el modal y mostramos errores
            if (!form.checkValidity()) {
                e.preventDefault();
                form.reportValidity();
            }
        });
    });
</script>
