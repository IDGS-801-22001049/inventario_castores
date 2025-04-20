function validarNumeroPositivo(input) {
    if (input.value < 0) {
        input.value = 0;
        alert('El valor no puede ser negativo');
        return false;
    }
    return true;
}

function validarCampoRequerido(input) {
    if (input.value.trim() === '') {
        alert('Este campo es obligatorio');
        return false;
    }
    return true;
}

function validarFormulario(formulario) {
    let valido = true;
    const inputsRequeridos = formulario.querySelectorAll('[required]');
    
    inputsRequeridos.forEach(input => {
        if (!validarCampoRequerido(input)) {
            valido = false;
        }
    });
    
    return valido;
}

document.addEventListener('DOMContentLoaded', function() {
    const inputsNumero = document.querySelectorAll('input[type="number"]');
    inputsNumero.forEach(input => {
        input.addEventListener('change', function() {
            validarNumeroPositivo(this);
        });
    });
    
    const formularios = document.querySelectorAll('form');
    formularios.forEach(formulario => {
        formulario.addEventListener('submit', function(e) {
            if (!validarFormulario(this)) {
                e.preventDefault();
            }
        });
    });
});