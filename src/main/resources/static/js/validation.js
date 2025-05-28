// validation.js

/**
 * Enables HTML5 + Bootstrap-style validation on the given <form> element.
 *
 * @param {HTMLFormElement} form - The form you want to validate.
 */
export function setupClientValidation(form) {
    if (!form) {
        console.warn('No form passed to setupClientValidation');
        return;
    }

    // Disable native browser tooltips
    form.setAttribute('novalidate', '');

    // Add Bootstrap “needs validation” marker
    form.classList.add('needs-validation');

    // On submit, check validity. If invalid, prevent submit & show feedback.
    form.addEventListener('submit', event => {
        if (!form.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
        }
        form.classList.add('was-validated');
    }, false);
}
