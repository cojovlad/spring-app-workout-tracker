document.addEventListener('DOMContentLoaded', function() {
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => setTimeout(() => new bootstrap.Alert(alert).close(), 5000));
});