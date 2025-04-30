document.addEventListener('DOMContentLoaded', function () {
    var alerts = document.querySelectorAll('.alert');
    alerts.forEach(function (alert) {
        setTimeout(function () {
            var bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000);
    });
});
document.addEventListener('DOMContentLoaded', function() {
    const newWorkoutBtn = document.getElementById('newWorkoutBtn');
    const workoutForm = document.getElementById('workoutForm');
    const cancelFormBtn = document.querySelector('.cancel-form');

    // Toggle form visibility
    newWorkoutBtn.addEventListener('click', () => {
        workoutForm.classList.remove('d-none');
        newWorkoutBtn.classList.add('d-none');
    });

    cancelFormBtn.addEventListener('click', () => {
        workoutForm.classList.add('d-none');
        newWorkoutBtn.classList.remove('d-none');
    });

    // Dynamic form handling
    document.querySelector('#muscleGroups').addEventListener('click', function(e) {
        // Add Muscle Group
        if (e.target.classList.contains('add-group')) {
            const newGroup = document.querySelector('.muscle-group').cloneNode(true);
            resetIndexes(newGroup);
            this.insertBefore(newGroup, e.target.parentElement);
        }

        // Remove Muscle Group
        if (e.target.classList.contains('remove-group')) {
            e.target.closest('.muscle-group').remove();
        }

        // Add Exercise
        if (e.target.classList.contains('add-exercise')) {
            const exerciseTemplate = e.target.closest('.muscle-group').querySelector('.exercise').cloneNode(true);
            resetIndexes(exerciseTemplate);
            e.target.closest('.muscle-group').querySelector('.exercises').appendChild(exerciseTemplate);
        }

        // Remove Exercise
        if (e.target.classList.contains('remove-exercise')) {
            e.target.closest('.exercise').remove();
        }

        // Add Set
        if (e.target.classList.contains('add-set')) {
            const setTemplate = e.target.closest('.exercise').querySelector('.set').cloneNode(true);
            resetIndexes(setTemplate);
            e.target.closest('.exercise').querySelector('.sets').appendChild(setTemplate);
        }

        // Remove Set
        if (e.target.classList.contains('remove-set')) {
            e.target.closest('.set').remove();
        }
    });

    function resetIndexes(element) {
        // Reset all input indexes to maintain proper array structure
        const inputs = element.querySelectorAll('[name]');
        inputs.forEach(input => {
            const name = input.getAttribute('name')
                .replace(/\[\d+\]/g, (match) => `[${Date.now()}]`);
            input.setAttribute('name', name);
        });
    }
});