document.addEventListener('DOMContentLoaded', function() {
    $('#tablaProductos').DataTable({
        language: {
            url: '//cdn.datatables.net/plug-ins/1.10.25/i18n/Spanish.json'
        },
        responsive: true
    });

    const formularioProducto = document.getElementById('formularioProducto');
    if (formularioProducto) {
        formularioProducto.addEventListener('submit', function(e) {
            let valido = true;
            
            const nombre = document.getElementById('nombre');
            if (nombre.value.trim() === '') {
                alert('El nombre del producto es obligatorio');
                valido = false;
            }
            
            const cantidad = document.getElementById('cantidad');
            if (cantidad.value < 0) {
                alert('La cantidad no puede ser negativa');
                valido = false;
            }
            
            const precio = document.getElementById('precio');
            if (precio.value <= 0) {
                alert('El precio debe ser mayor que cero');
                valido = false;
            }
            
            if (!valido) {
                e.preventDefault();
            }
        });
    }

    const filtroEstado = document.getElementById('filtroEstado');
    if (filtroEstado) {
        filtroEstado.addEventListener('change', function() {
            const estado = this.value;
            if (estado) {
                window.location.href = `/inventario/lista?estado=${estado}`;
            } else {
                window.location.href = '/inventario/lista';
            }
        });
    }
});