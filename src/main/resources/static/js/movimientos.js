document.addEventListener('DOMContentLoaded', function() {
    $('#tablaMovimientos').DataTable({
        language: {
            url: '//cdn.datatables.net/plug-ins/1.10.25/i18n/Spanish.json'
        },
        responsive: true,
        order: [[3, 'desc']]
    });

    const formularioMovimiento = document.getElementById('formularioMovimiento');
    if (formularioMovimiento) {
        formularioMovimiento.addEventListener('submit', function(e) {
            let valido = true;
            
            const producto = document.getElementById('producto');
            if (producto.value === '') {
                alert('Debe seleccionar un producto');
                valido = false;
            }
            
            const cantidad = document.getElementById('cantidad');
            if (cantidad.value <= 0) {
                alert('La cantidad debe ser mayor que cero');
                valido = false;
            }
            
            if (!valido) {
                e.preventDefault();
            }
        });
    }

    const productoSelect = document.getElementById('producto');
    const stockDisponible = document.getElementById('stockDisponible');
    
    if (productoSelect && stockDisponible) {
        productoSelect.addEventListener('change', function() {
            const productoId = this.value;
            if (productoId) {
                fetch(`/api/productos/${productoId}`)
                    .then(response => response.json())
                    .then(data => {
                        stockDisponible.textContent = data.cantidad;
                    });
            } else {
                stockDisponible.textContent = '0';
            }
        });
    }
});