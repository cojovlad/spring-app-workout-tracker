// General page behaviors
document.addEventListener('DOMContentLoaded', function() {
    // Alert auto-dismiss
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => setTimeout(() => new bootstrap.Alert(alert).close(), 5000));
});