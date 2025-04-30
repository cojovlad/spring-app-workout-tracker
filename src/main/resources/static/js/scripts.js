document.addEventListener('DOMContentLoaded', function() {
    // Alert auto-dismiss
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => setTimeout(() => new bootstrap.Alert(alert).close(), 5000));

    // Form elements
    const newWorkoutBtn = document.getElementById('newWorkoutBtn');
    const workoutForm = document.getElementById('workoutForm');
    const cancelFormBtn = document.querySelector('.cancel-form');
    const muscleGroupsContainer = document.getElementById('muscleGroups');
    const template = document.querySelector('.muscle-group-template .muscle-group');

    // Toggle form visibility
    if (newWorkoutBtn && workoutForm) {
        newWorkoutBtn.addEventListener('click', () => {
            workoutForm.classList.remove('d-none');
            newWorkoutBtn.classList.add('d-none');
            addInitialMuscleGroup();
        });

        cancelFormBtn.addEventListener('click', () => {
            workoutForm.classList.add('d-none');
            newWorkoutBtn.classList.remove('d-none');
            muscleGroupsContainer.innerHTML = '';
        });
    }

    function addInitialMuscleGroup() {
        const newGroup = cloneMuscleGroup();
        muscleGroupsContainer.appendChild(newGroup);
        reindexForm();
    }

    // Event delegation for dynamic elements
    document.addEventListener('click', function(e) {
        // Add Muscle Group
        if (e.target.classList.contains('add-group')) {
            const newGroup = cloneMuscleGroup();
            muscleGroupsContainer.appendChild(newGroup);
            reindexForm();
        }

        // Remove Muscle Group
        if (e.target.classList.contains('remove-group')) {
            e.target.closest('.muscle-group').remove();
            reindexForm();
        }

        // Add Exercise
        if (e.target.classList.contains('add-exercise')) {
            const muscleGroup = e.target.closest('.muscle-group');
            const exercise = cloneExercise();
            muscleGroup.querySelector('.exercises').appendChild(exercise);
            reindexForm();
        }

        // Remove Exercise
        if (e.target.classList.contains('remove-exercise')) {
            e.target.closest('.exercise').remove();
            reindexForm();
        }

        // Add Set
        if (e.target.classList.contains('add-set')) {
            const exercise = e.target.closest('.exercise');
            const set = cloneSet();
            exercise.querySelector('.sets').appendChild(set);
            reindexForm();
        }

        // Remove Set
        if (e.target.classList.contains('remove-set')) {
            e.target.closest('.set').remove();
            reindexForm();
        }
    });

    // Clone helpers
    function cloneMuscleGroup() {
        const newGroup = template.cloneNode(true);
        newGroup.classList.remove('muscle-group-template');
        newGroup.querySelectorAll('input').forEach(input => input.value = '');
        return newGroup;
    }

    function cloneExercise() {
        const exercise = template.querySelector('.exercise').cloneNode(true);
        exercise.querySelectorAll('input').forEach(input => input.value = '');
        return exercise;
    }

    function cloneSet() {
        const set = template.querySelector('.set').cloneNode(true);
        set.querySelectorAll('input').forEach(input => input.value = '');
        return set;
    }

    // Reindex all form elements
    function reindexForm() {
        document.querySelectorAll('.muscle-group:not(.muscle-group-template)').forEach((group, groupIndex) => {
            // Update muscle group fields
            group.querySelectorAll('[name*="muscleGroups["]').forEach(el => {
                el.name = el.name.replace(/muscleGroups\[\d*\]/g, `muscleGroups[${groupIndex}]`);
            });

            // Update exercises
            group.querySelectorAll('.exercise').forEach((exercise, exerciseIndex) => {
                exercise.querySelectorAll('[name*="exercises["]').forEach(el => {
                    el.name = el.name.replace(/exercises\[\d*\]/g, `exercises[${exerciseIndex}]`);
                });

                // Update sets
                exercise.querySelectorAll('.set').forEach((set, setIndex) => {
                    set.querySelectorAll('[name*="sets["]').forEach(el => {
                        el.name = el.name.replace(/sets\[\d*\]/g, `sets[${setIndex}]`);
                    });
                });
            });
        });
    }
});

