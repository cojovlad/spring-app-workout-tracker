package com.example.spring_app_workout_tracker.controller;

import com.example.spring_app_workout_tracker.dto.workout.WorkoutRequest;
import com.example.spring_app_workout_tracker.entity.User;
import com.example.spring_app_workout_tracker.entity.workout.MusclePart;
import com.example.spring_app_workout_tracker.entity.workout.Workout;
import com.example.spring_app_workout_tracker.entity.workout.WorkoutExercise;
import com.example.spring_app_workout_tracker.repository.workout.MusclePartRepository;
import com.example.spring_app_workout_tracker.repository.workout.WorkoutExerciseRepository;
import com.example.spring_app_workout_tracker.repository.workout.WorkoutRepository;
import com.example.spring_app_workout_tracker.service.ExerciseService;
import com.example.spring_app_workout_tracker.service.ExerciseSetService;
import com.example.spring_app_workout_tracker.service.WorkoutService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final WorkoutService workoutService;
    private final MusclePartRepository musclePartRepo;
    private final WorkoutRepository workoutRepo;
    private final ExerciseService exerciseService;
    private final ExerciseSetService setService;
    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final MusclePartRepository musclePartRepository;

    @GetMapping
    @Transactional
    public String showDashboard(Model m, @AuthenticationPrincipal User u) {
        m.addAttribute("muscleParts", musclePartRepo.findAll());
        m.addAttribute("workouts", workoutService.getWorkoutsByUser(u));
        m.addAttribute("workoutRequest", new WorkoutRequest());
        return "dashboard";
    }

    @PostMapping("/workouts")
    public String createWorkout(@Valid @ModelAttribute WorkoutRequest req,
                                BindingResult br,
                                @AuthenticationPrincipal User u,
                                RedirectAttributes ra) {
        if (br.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.workoutRequest", br);
            ra.addFlashAttribute("workoutRequest", req);
            return "redirect:/api/v1/dashboard";
        }
        workoutService.createWorkoutTemplate(req, u);
        ra.addFlashAttribute("success", "Created");
        return "redirect:/api/v1/dashboard";
    }

    @GetMapping("/workouts/{id}")
    @Transactional
    public String viewWorkout(@PathVariable Long id,
                              @AuthenticationPrincipal User u,
                              HttpSession s,
                              Model m) {
        s.setAttribute("currentWorkoutId", id);
        m.addAttribute("muscleParts", musclePartRepo.findAll());
        m.addAttribute("workouts", workoutService.getWorkoutsByUser(u));
        m.addAttribute("selectedWorkout", workoutService.getWorkoutById(id));
        m.addAttribute("workoutRequest", new WorkoutRequest());
        return "dashboard";
    }

    @PostMapping("/workouts/{id}/update")
    public String updateWorkoutName(@PathVariable Long id,
                                    @RequestParam String name,
                                    RedirectAttributes ra) {
        Workout w = workoutService.getWorkoutById(id);
        w.setName(name);
        workoutRepo.save(w);
        ra.addFlashAttribute("success", "Workout renamed");
        return "redirect:/api/v1/dashboard/workouts/" + id;
    }

    @PostMapping("/workouts/{id}/delete")
    public String deleteWorkout(@PathVariable Long id,
                                RedirectAttributes ra) {
        workoutService.deleteWorkout(id);
        ra.addFlashAttribute("success", "Workout deleted");
        return "redirect:/api/v1/dashboard";
    }

    @PostMapping("/exercises/{id}/update")
    public String updateExerciseName(@PathVariable Long id,
                                     @RequestParam String name,
                                     HttpSession s,
                                     RedirectAttributes ra) {
        exerciseService.updateExerciseName(id, name);
        ra.addFlashAttribute("success", "Exercise renamed");
        return "redirect:/api/v1/dashboard/workouts/" + s.getAttribute("currentWorkoutId");
    }

    @PostMapping("/exercises/{id}/delete")
    public String deleteExercise(@PathVariable Long id,
                                 HttpSession s,
                                 RedirectAttributes ra) {
        exerciseService.deleteExercise(id);
        ra.addFlashAttribute("success", "Exercise deleted");
        return "redirect:/api/v1/dashboard/workouts/" + s.getAttribute("currentWorkoutId");
    }

    @PostMapping("/exercises/{id}/add-muscle")
    public String addMuscle(@PathVariable Long id,
                            @RequestParam Long musclePartId,
                            HttpSession s,
                            RedirectAttributes ra) {
        exerciseService.addMuscleTarget(id, musclePartId);
        ra.addFlashAttribute("success", "Muscle added");
        return "redirect:/api/v1/dashboard/workouts/" + s.getAttribute("currentWorkoutId");
    }

    @PostMapping("/exercises/{id}/update-muscle")
    public String updateMuscle(@PathVariable Long id,
                               @RequestParam Long musclePartId,
                               HttpSession s,
                               RedirectAttributes ra) {
        exerciseService.updateExerciseMuscle(id, musclePartId);
        ra.addFlashAttribute("success", "Muscle updated");
        return "redirect:/api/v1/dashboard/workouts/" + s.getAttribute("currentWorkoutId");
    }

    @PostMapping("/exercises/{id}/remove-muscle")
    public String removeMuscle(@PathVariable Long id,
                               @RequestParam Long musclePartId,
                               HttpSession s,
                               RedirectAttributes ra) {
        exerciseService.removeMuscleTarget(id, musclePartId);
        ra.addFlashAttribute("success", "Muscle removed");
        return "redirect:/api/v1/dashboard/workouts/" + s.getAttribute("currentWorkoutId");
    }

    @PostMapping("/sets/{id}/update")
    public String updateSet(@PathVariable Long id,
                            @RequestParam Integer repetitions,
                            @RequestParam BigDecimal weightKg,
                            @RequestParam Integer restSeconds,
                            HttpSession s,
                            RedirectAttributes ra) {
        setService.updateSet(id, repetitions, weightKg, restSeconds);
        ra.addFlashAttribute("success", "Set updated");
        return "redirect:/api/v1/dashboard/workouts/" + s.getAttribute("currentWorkoutId");
    }

    @PostMapping("/sets/{id}/delete")
    public String deleteSet(@PathVariable Long id,
                            HttpSession s,
                            RedirectAttributes ra) {
        setService.deleteSet(id);
        ra.addFlashAttribute("success", "Set deleted");
        return "redirect:/api/v1/dashboard/workouts/" + s.getAttribute("currentWorkoutId");
    }

    @PostMapping("/workout-exercises/{id}/update-muscle")
    public String updateWorkoutExerciseMuscle(@PathVariable Long id,
                                              @RequestParam Long musclePartId,
                                              HttpSession s,
                                              RedirectAttributes ra) {
        WorkoutExercise we = workoutExerciseRepository.findById(id).orElseThrow();
        MusclePart m = musclePartRepository.findById(musclePartId).orElseThrow();
        we.setMusclePart(m);
        workoutExerciseRepository.save(we);
        ra.addFlashAttribute("success", "Muscle updated");
        return "redirect:/api/v1/dashboard/workouts/" + s.getAttribute("currentWorkoutId");
    }
}
