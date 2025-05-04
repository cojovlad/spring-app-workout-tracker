package com.example.spring_app_workout_tracker.controller;

import com.example.spring_app_workout_tracker.dto.workout.WorkoutRequest;
import com.example.spring_app_workout_tracker.entity.User;
import com.example.spring_app_workout_tracker.repository.workout.MusclePartRepository;
import com.example.spring_app_workout_tracker.repository.workout.WorkoutExerciseRepository;
import com.example.spring_app_workout_tracker.repository.workout.WorkoutRepository;
import com.example.spring_app_workout_tracker.service.workout.ExerciseService;
import com.example.spring_app_workout_tracker.service.workout.ExerciseSetService;
import com.example.spring_app_workout_tracker.service.workout.MusclePartService;
import com.example.spring_app_workout_tracker.service.workout.WorkoutService;
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
    private final ExerciseService exerciseService;
    private final ExerciseSetService setService;
    private final MusclePartService musclePartService;

    private final WorkoutRepository workoutRepo;
    private final MusclePartRepository musclePartRepo;
    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final MusclePartRepository musclePartRepository;

    @GetMapping
    @Transactional
    public String showDashboard(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("muscleParts", musclePartService.listAllMuscleParts());
        model.addAttribute("workouts", workoutService.getWorkoutsByUser(user));
        model.addAttribute("workoutRequest", new WorkoutRequest());
        return "dashboard";
    }

    @PostMapping("/workouts")
    public String createWorkout(@Valid @ModelAttribute WorkoutRequest request,
                                BindingResult bindingResult,
                                @AuthenticationPrincipal User user,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.workoutRequest", bindingResult);
            redirectAttributes.addFlashAttribute("workoutRequest", request);
            return "redirect:/api/v1/dashboard";
        }
        workoutService.createWorkoutTemplate(request, user);
        redirectAttributes.addFlashAttribute("success", "Created");
        return "redirect:/api/v1/dashboard";
    }

    @GetMapping("/workouts/{id}")
    @Transactional
    public String viewWorkout(@PathVariable Long id,
                              @AuthenticationPrincipal User user,
                              HttpSession httpSession,
                              Model model) {
        httpSession.setAttribute("currentWorkoutId", id);
        model.addAttribute("muscleParts", musclePartService.listAllMuscleParts());
        model.addAttribute("workouts", workoutService.getWorkoutsByUser(user));
        model.addAttribute("selectedWorkout", workoutService.getWorkoutById(id));
        model.addAttribute("workoutRequest", new WorkoutRequest());
        return "dashboard";
    }

    @PostMapping("/workouts/{id}/update")
    public String updateWorkoutName(@PathVariable Long id,
                                    @RequestParam String name,
                                    RedirectAttributes redirectAttributes) {
        workoutService.updateWorkout(id, name);
        redirectAttributes.addFlashAttribute("success", "Workout renamed");
        return "redirect:/api/v1/dashboard/workouts/" + id;
    }

    @PostMapping("/workouts/{id}/delete")
    public String deleteWorkout(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {
        workoutService.deleteWorkout(id);
        redirectAttributes.addFlashAttribute("success", "Workout deleted");
        return "redirect:/api/v1/dashboard";
    }

    @PostMapping("/exercises/{id}/update")
    public String updateExerciseName(@PathVariable Long id,
                                     @RequestParam String name,
                                     HttpSession httpSession,
                                     RedirectAttributes redirectAttributes) {
        exerciseService.updateExerciseName(id, name);
        redirectAttributes.addFlashAttribute("success", "Exercise renamed");
        return "redirect:/api/v1/dashboard/workouts/" + httpSession.getAttribute("currentWorkoutId");
    }

    @PostMapping("/exercises/{id}/delete")
    public String deleteExercise(@PathVariable Long id,
                                 HttpSession httpSession,
                                 RedirectAttributes redirectAttributes) {
        exerciseService.deleteExercise(id);
        redirectAttributes.addFlashAttribute("success", "Exercise deleted");
        return "redirect:/api/v1/dashboard/workouts/" + httpSession.getAttribute("currentWorkoutId");
    }

    @PostMapping("/exercises/{id}/add-muscle")
    public String addMuscle(@PathVariable Long id,
                            @RequestParam Long musclePartId,
                            HttpSession httpSession,
                            RedirectAttributes redirectAttributes) {
        exerciseService.addMuscleTarget(id, musclePartId);
        redirectAttributes.addFlashAttribute("success", "Muscle added");
        return "redirect:/api/v1/dashboard/workouts/" + httpSession.getAttribute("currentWorkoutId");
    }

    @PostMapping("/workout-exercises/{id}/update-muscle")
    public String updateWorkoutExerciseMuscle(@PathVariable Long id,
                                              @RequestParam Long musclePartId,
                                              HttpSession httpSession,
                                              RedirectAttributes redirectAttributes) {
        exerciseService.updateExerciseMuscle(id, musclePartId);
        redirectAttributes.addFlashAttribute("success", "Muscle updated");
        return "redirect:/api/v1/dashboard/workouts/" + httpSession.getAttribute("currentWorkoutId");

    }

    @PostMapping("/exercises/{id}/remove-muscle")
    public String removeMuscle(@PathVariable Long id,
                               @RequestParam Long musclePartId,
                               HttpSession httpSession,
                               RedirectAttributes redirectAttributes) {
        exerciseService.removeMuscleTarget(id, musclePartId);
        redirectAttributes.addFlashAttribute("success", "Muscle removed");
        return "redirect:/api/v1/dashboard/workouts/" + httpSession.getAttribute("currentWorkoutId");
    }

    @PostMapping("/sets/{id}/update")
    public String updateSet(@PathVariable Long id,
                            @RequestParam Integer repetitions,
                            @RequestParam BigDecimal weightKg,
                            @RequestParam Integer restSeconds,
                            HttpSession httpSession,
                            RedirectAttributes redirectAttributes) {
        setService.updateSet(id, repetitions, weightKg, restSeconds);
        redirectAttributes.addFlashAttribute("success", "Set updated");
        return "redirect:/api/v1/dashboard/workouts/" + httpSession.getAttribute("currentWorkoutId");
    }

    @PostMapping("/sets/{id}/delete")
    public String deleteSet(@PathVariable Long id,
                            HttpSession httpSession,
                            RedirectAttributes redirectAttributes) {
        setService.deleteSet(id);
        redirectAttributes.addFlashAttribute("success", "Set deleted");
        return "redirect:/api/v1/dashboard/workouts/" + httpSession.getAttribute("currentWorkoutId");
    }
}
