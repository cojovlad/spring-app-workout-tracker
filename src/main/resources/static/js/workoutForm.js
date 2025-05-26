// workoutForm.js
document.addEventListener('DOMContentLoaded', function () {
    const newBtn = document.getElementById('newWorkoutBtn');
    const formContainer = document.getElementById('workoutForm');
    const cancelBtn = document.querySelector('.cancel-form');
    const groupsContainer = document.getElementById('muscleGroups');
    const groupTpl = document.querySelector('.muscle-group-template .muscle-group');
    const exerTpl = document.querySelector('.exercise-template .exercise');
    const setTpl = document.querySelector('.set-template .set');

    let initialized = false;

    // Utility to update all input name indexes after DOM changes
    function reindex() {
        groupsContainer.querySelectorAll('.muscle-group').forEach((grp, gI) => {
            grp.querySelectorAll('[name*="muscleGroups["]').forEach(el =>
                el.name = el.name.replace(/muscleGroups\[\d*\]/, `muscleGroups[${gI}]`)
            );
            grp.querySelectorAll('.exercise').forEach((ex, eI) => {
                ex.querySelectorAll('[name*="exercises["]').forEach(el =>
                    el.name = el.name.replace(/exercises\[\d*\]/, `exercises[${eI}]`)
                );
                ex.querySelectorAll('.set').forEach((st, sI) => {
                    st.querySelectorAll('[name*="sets["]').forEach(el =>
                        el.name = el.name.replace(/sets\[\d*\]/, `sets[${sI}]`)
                    );
                });
            });
        });
    }

    function cloneGroup() {
        const node = groupTpl.cloneNode(true);
        node.querySelectorAll('input').forEach(i => i.value = '');
        return node;
    }

    function addGroup() {
        const grp = cloneGroup();
        groupsContainer.appendChild(grp);
        addExercise(grp);
        reindex();
    }

    function addExercise(grp) {
        const ex = exerTpl.cloneNode(true);
        grp.querySelector('.exercises').appendChild(ex);
        addSet(ex);
    }

    function addSet(exerciseElem) {
        const setsContainer = exerciseElem.querySelector('.sets');
        const existingSets = setsContainer.querySelectorAll('.set');
        let newSet;

        if (existingSets.length > 0) {
            // Clone the last set, preserving its input values
            const lastSet = existingSets[existingSets.length - 1];
            newSet = lastSet.cloneNode(true);
        } else {
            // Fallback to blank template if no sets exist yet
            newSet = setTpl.cloneNode(true);
        }

        // If you want to reset only certain fields (e.g. clear restSeconds), do it here:
        // newSet.querySelector('input[name$="[restSeconds]"]').value = '';

        setsContainer.appendChild(newSet);
        reindex();
    }


    // Show the workout form when "New Workout" button is clicked
    newBtn.addEventListener('click', () => {
        if (!initialized) {
            formContainer.classList.remove('d-none');
            newBtn.classList.add('d-none');
            addGroup();
            initialized = true;
        }
    });

    // Hide the workout form when "Cancel" button is clicked
    cancelBtn.addEventListener('click', () => {
        formContainer.classList.add('d-none');
        newBtn.classList.remove('d-none');
        groupsContainer.innerHTML = '';
        initialized = false;
    });

    // Handle dynamic form actions via event delegation
    document.addEventListener('click', e => {
        if (!initialized) return;
        if (e.target.matches('.add-group')) {
            addGroup();
        } else if (e.target.matches('.remove-group')) {
            e.stopImmediatePropagation();
            e.target.closest('.muscle-group').remove();
            reindex();
        } else if (e.target.matches('.add-exercise')) {
            addExercise(e.target.closest('.muscle-group'));
            reindex();
        } else if (e.target.matches('.remove-exercise')) {
            e.stopImmediatePropagation();
            e.target.closest('.exercise').remove();
            reindex();
        } else if (e.target.matches('.add-set')) {
            addSet(e.target.closest('.exercise'));
            reindex();
        } else if (e.target.matches('.remove-set')) {
            e.stopImmediatePropagation();
            e.target.closest('.set').remove();
            reindex();
        }
    });

    // Keep sidebar open or closed based on screen size
    window.addEventListener('resize', () => {
        const sidebar = document.getElementById('sidebar');
        sidebar.classList.toggle('mobile-sidebar-active', window.innerWidth >= 768);
    });
});