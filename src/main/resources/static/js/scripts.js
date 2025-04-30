document.addEventListener('DOMContentLoaded', function() {
    // Alert auto-dismiss
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => setTimeout(() => new bootstrap.Alert(alert).close(), 5000));

    // Form controls
    const newWorkoutBtn = document.getElementById('newWorkoutBtn');
    const workoutForm = document.getElementById('workoutForm');
    const cancelFormBtn = document.querySelector('.cancel-form');

    // Index counters
    let groupCounter = 0;
    let exerciseCounter = 0;
    let setCounter = 0;

    if (newWorkoutBtn && workoutForm) {
        newWorkoutBtn.addEventListener('click', () => {
            workoutForm.classList.remove('d-none');
            newWorkoutBtn.classList.add('d-none');
            addInitialMuscleGroup();
        });

        cancelFormBtn.addEventListener('click', () => {
            workoutForm.classList.add('d-none');
            newWorkoutBtn.classList.remove('d-none');
        });
    }

    function addInitialMuscleGroup() {
        const container = document.getElementById('muscleGroups');
        const newGroup = cloneMuscleGroup();
        container.appendChild(newGroup);
        reindexGroups();
    }

    document.addEventListener('click', function(e) {
        // Add Muscle Group
        if (e.target.classList.contains('add-group')) {
            const newGroup = cloneMuscleGroup();
            e.target.closest('.d-grid').before(newGroup);
            reindexGroups();
        }

        // Remove Muscle Group
        if (e.target.classList.contains('remove-group')) {
            e.target.closest('.muscle-group').remove();
            reindexGroups();
        }

        // Add Exercise
        if (e.target.classList.contains('add-exercise')) {
            const parentGroup = e.target.closest('.muscle-group');
            const newExercise = cloneExercise(parentGroup);
            e.target.before(newExercise);
            reindexExercises(parentGroup);
        }

        // Remove Exercise
        if (e.target.classList.contains('remove-exercise')) {
            const parentGroup = e.target.closest('.muscle-group');
            e.target.closest('.exercise').remove();
            reindexExercises(parentGroup);
        }

        // Add Set
        if (e.target.classList.contains('add-set')) {
            const parentExercise = e.target.closest('.exercise');
            const newSet = cloneSet(parentExercise);
            parentExercise.querySelector('.sets').appendChild(newSet);
            reindexSets(parentExercise);
        }

        // Remove Set
        if (e.target.classList.contains('remove-set')) {
            const parentExercise = e.target.closest('.exercise');
            e.target.closest('.set').remove();
            reindexSets(parentExercise);
        }
    });

    // Muscle Group Management
    function cloneMuscleGroup() {
        const template = document.querySelector('.muscle-group-template .muscle-group');
        const newGroup = template.cloneNode(true);
        newGroup.classList.remove('muscle-group-template');
        return newGroup;
    }

    function reindexGroups() {
        const groups = document.querySelectorAll('.muscle-group:not(.muscle-group-template)');
        groups.forEach((group, groupIndex) => {
            // Update group-level names
            group.querySelectorAll('[name*="muscleGroups"]').forEach(el => {
                el.name = el.name.replace(/muscleGroups\[\d+\]/g, `muscleGroups[${groupIndex}]`);
            });

            // Reindex exercises within group
            const exercises = group.querySelectorAll('.exercise');
            exercises.forEach((exercise, exerciseIndex) => {
                exercise.querySelectorAll('[name*="exercises"]').forEach(el => {
                    el.name = el.name.replace(/exercises\[\d+\]/g, `exercises[${exerciseIndex}]`);
                });

                // Reindex sets within exercise
                const sets = exercise.querySelectorAll('.set');
                sets.forEach((set, setIndex) => {
                    set.querySelectorAll('[name*="sets"]').forEach(el => {
                        el.name = el.name.replace(/sets\[\d+\]/g, `sets[${setIndex}]`);
                    });
                });
            });
        });
    }

    // Exercise Management
    function cloneExercise(parentGroup) {
        const template = parentGroup.querySelector('.exercise');
        const newExercise = template.cloneNode(true);
        newExercise.querySelectorAll('input').forEach(input => input.value = '');
        return newExercise;
    }

    function reindexExercises(group) {
        const exercises = group.querySelectorAll('.exercise');
        exercises.forEach((exercise, exerciseIndex) => {
            exercise.querySelectorAll('[name*="exercises"]').forEach(el => {
                el.name = el.name.replace(/exercises\[\d+\]/g, `exercises[${exerciseIndex}]`);
            });
            reindexSets(exercise);
        });
    }

    // Set Management
    function cloneSet(parentExercise) {
        const template = parentExercise.querySelector('.set');
        const newSet = template.cloneNode(true);
        newSet.querySelectorAll('input').forEach(input => input.value = '');
        return newSet;
    }

    function reindexSets(exercise) {
        const sets = exercise.querySelectorAll('.set');
        sets.forEach((set, setIndex) => {
            set.querySelectorAll('[name*="sets"]').forEach(el => {
                el.name = el.name.replace(/sets\[\d+\]/g, `sets[${setIndex}]`);
            });
        });
    }
});