// dashboard.js
document.addEventListener('DOMContentLoaded', function () {
    const mobileMenuToggle = document.getElementById('mobileMenuToggle');
    const sidebar = document.getElementById('sidebar');

    if (mobileMenuToggle && sidebar) {
        // Toggle sidebar on mobile
        mobileMenuToggle.addEventListener('click', () =>
            sidebar.classList.toggle('mobile-sidebar-active')
        );

        // Click outside to close
        document.addEventListener('click', function (e) {
            if (window.innerWidth < 768 &&
                !e.target.closest('#sidebar') &&
                !e.target.closest('#mobileMenuToggle')) {
                sidebar.classList.remove('mobile-sidebar-active');
            }
        });
    }

    // “Are you sure?” on any delete form
    document.querySelectorAll('form[action*="/delete"]').forEach(form => {
        form.addEventListener('submit', function (e) {
            if (!confirm('Are you sure you want to delete this?')) {
                e.preventDefault();
            }
        });
    });

    // Dynamic “Add Set” for exercises outside the workout form
    let setIndex = 0;
    document.querySelectorAll('.add-exercise-set').forEach(button => {
        button.addEventListener('click', function () {
            const template = document.querySelector('.add-exercise-set-template');
            const clone = template.cloneNode(true);
            const html = clone.innerHTML.replace(/INDEX/g, setIndex++);
            const container = this.closest('form').querySelector('.sets-container');
            const wrapper = document.createElement('div');
            wrapper.innerHTML = html;
            container.appendChild(wrapper);

            wrapper.querySelector('.remove-set').addEventListener('click', function () {
                this.closest('.set').remove();
            });
        });
    });
});
