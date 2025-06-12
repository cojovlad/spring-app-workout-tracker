// validation.js
export function setupClientValidation(form) {
    if (!form) {
        console.warn('No form passed to setupClientValidation');
        return;
    }

    form.setAttribute('novalidate', '');

    form.classList.add('needs-validation');

    form.addEventListener('submit', event => {
        if (!form.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
        }
        form.classList.add('was-validated');
    }, false);
}
