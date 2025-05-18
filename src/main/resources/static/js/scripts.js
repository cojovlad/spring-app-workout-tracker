// scripts.js
document.addEventListener('DOMContentLoaded', function () {
    // Auto-dismiss Bootstrap alerts after 5s
    document.querySelectorAll('.alert').forEach(alert =>
        setTimeout(() => new bootstrap.Alert(alert).close(), 5000)
    );

    // Prevent double-submit on all forms
    document.querySelectorAll('form').forEach(form => {
        let isSubmitting = false;
        form.addEventListener('submit', function (e) {
            if (isSubmitting) {
                e.preventDefault();
                return;
            }
            isSubmitting = true;
            setTimeout(() => isSubmitting = false, 2000);
        });
    });

    // Collapse sidebar on mobile when any sidebar link is clicked
    document.querySelectorAll('#sidebar a').forEach(link => {
        link.addEventListener('click', () => {
            if (window.innerWidth < 768) {
                document.getElementById('sidebar')
                    .classList.remove('mobile-sidebar-active');
            }
        });
    });
});
