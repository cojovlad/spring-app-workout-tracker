// Workout form specific functionality
document.addEventListener('DOMContentLoaded', function() {
    // Form elements
    const newWorkoutBtn = document.getElementById('newWorkoutBtn');
    const workoutForm = document.getElementById('workoutForm');
    const cancelFormBtn = document.querySelector('.cancel-form');
    const muscleGroupsContainer = document.getElementById('muscleGroups');

    // Templates
    const muscleGroupTemplate = document.querySelector('.muscle-group-template .muscle-group');
    const exerciseTemplate = document.querySelector('.exercise-template .exercise');
    const setTemplate = document.querySelector('.set-template .set');

    // Initialize form state
    let formInitialized = false;

    // Toggle form visibility
    if (newWorkoutBtn && workoutForm) {
        newWorkoutBtn.addEventListener('click', () => {
            if (!formInitialized) {
                workoutForm.classList.remove('d-none');
                newWorkoutBtn.classList.add('d-none');
                addInitialMuscleGroup();
                formInitialized = true;
            }
        });

        cancelFormBtn.addEventListener('click', () => {
            workoutForm.classList.add('d-none');
            newWorkoutBtn.classList.remove('d-none');
            muscleGroupsContainer.innerHTML = '';
            formInitialized = false;
        });
    }

    function addInitialMuscleGroup() {
        const newGroup = cloneMuscleGroup();
        muscleGroupsContainer.appendChild(newGroup);
        addExerciseToGroup(newGroup);
        reindexForm();
    }

    // Event delegation for dynamic elements
    document.addEventListener('click', function(e) {
        if (!formInitialized) return;

        // Add Muscle Group
        if (e.target.classList.contains('add-group')) {
            const newGroup = cloneMuscleGroup();
            muscleGroupsContainer.appendChild(newGroup);
            addExerciseToGroup(newGroup);
            reindexForm();
        }
        // Remove Muscle Group
        else if (e.target.classList.contains('remove-group')) {
            e.target.closest('.muscle-group').remove();
            reindexForm();
        }
        // Add Exercise
        else if (e.target.classList.contains('add-exercise')) {
            const group = e.target.closest('.muscle-group');
            addExerciseToGroup(group);
            reindexForm();
        }
        // Remove Exercise
        else if (e.target.classList.contains('remove-exercise')) {
            e.target.closest('.exercise').remove();
            reindexForm();
        }
        // Add Set
        else if (e.target.classList.contains('add-set')) {
            const exercise = e.target.closest('.exercise');
            addSetToExercise(exercise);
            reindexForm();
        }
        // Remove Set
        else if (e.target.classList.contains('remove-set')) {
            e.target.closest('.set').remove();
            reindexForm();
        }
    });

    // Cloning functions
    function cloneMuscleGroup() {
        const newGroup = muscleGroupTemplate.cloneNode(true);
        newGroup.querySelectorAll('input').forEach(input => input.value = '');
        return newGroup;
    }

    function addExerciseToGroup(group) {
        const exercise = exerciseTemplate.cloneNode(true);
        const exercisesContainer = group.querySelector('.exercises');
        exercisesContainer.appendChild(exercise);
        addSetToExercise(exercise);
    }

    function addSetToExercise(exercise) {
        const set = setTemplate.cloneNode(true);
        const setsContainer = exercise.querySelector('.sets');
        setsContainer.appendChild(set);
    }

    // Reindexing logic
    function reindexForm() {
        document.querySelectorAll('.muscle-group').forEach((group, groupIndex) => {
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