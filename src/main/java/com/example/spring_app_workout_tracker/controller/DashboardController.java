package com.example.spring_app_workout_tracker.controller;

import com.example.spring_app_workout_tracker.dto.workout.AddExerciseRequest;
import com.example.spring_app_workout_tracker.dto.workout.SetRequest;
import com.example.spring_app_workout_tracker.dto.workout.WorkoutRequest;
import com.example.spring_app_workout_tracker.entity.User;
import com.example.spring_app_workout_tracker.service.workout.ExerciseService;
import com.example.spring_app_workout_tracker.service.workout.ExerciseSetService;
import com.example.spring_app_workout_tracker.service.workout.MusclePartService;
import com.example.spring_app_workout_tracker.service.workout.WorkoutService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Locale;

import static com.example.spring_app_workout_tracker.util.MessageKeys.*;

@Controller
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final WorkoutService workoutService;
    private final ExerciseService exerciseService;
    private final ExerciseSetService setService;
    private final MusclePartService musclePartService;
    private final MessageSource messageSource;

    @GetMapping
    @Transactional
    public String showDashboard(Model model, @AuthenticationPrincipal User user, Locale locale) {
        model.addAttribute("muscleParts", musclePartService.listAllMuscleParts());
        model.addAttribute("workouts", workoutService.getWorkoutsByUser(user));
        model.addAttribute("workoutRequest", new WorkoutRequest());
        return "dashboard";
    }

    @PostMapping("/workouts")
    public String createWorkout(@Valid @ModelAttribute WorkoutRequest request,
                                BindingResult bindingResult,
                                @AuthenticationPrincipal User user,
                                RedirectAttributes redirectAttributes, Locale locale) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.workoutRequest", bindingResult);
            redirectAttributes.addFlashAttribute(WORKOUT_REQUEST, request);
            return "redirect:/api/v1/dashboard";
        }
        workoutService.createWorkoutTemplate(request, user);
        redirectAttributes.addFlashAttribute(SUCCESS, messageSource.getMessage("success.workout.created", null, locale));
        return "redirect:/api/v1/dashboard";
    }

    @PostMapping("/workouts/{id}/update")
    public String updateWorkoutName(@PathVariable Long id,
                                    @RequestParam String name,
                                    RedirectAttributes redirectAttributes, Locale locale) {
        workoutService.updateWorkout(id, name);
        redirectAttributes.addFlashAttribute(SUCCESS, messageSource.getMessage("success.workout.renamed", null, locale));
        return "redirect:/api/v1/dashboard/workouts/" + id;
    }

    @PostMapping("/workouts/{id}/delete")
    public String deleteWorkout(@PathVariable Long id,
                                RedirectAttributes redirectAttributes, Locale locale) {
        workoutService.deleteWorkout(id);
        redirectAttributes.addFlashAttribute(SUCCESS, messageSource.getMessage("success.workout.deleted", null, locale));
        return "redirect:/api/v1/dashboard";
    }

    @GetMapping("/workouts/{id}")
    @Transactional
    public String viewWorkout(@PathVariable Long id,
                              @AuthenticationPrincipal User user,
                              HttpSession httpSession,
                              Model model) {
        httpSession.setAttribute(CURRENT_WORKOUT_ID, id);
        model.addAttribute(MUSCLE_PARTS, musclePartService.listAllMuscleParts());
        model.addAttribute(WORKOUTS, workoutService.getWorkoutsByUser(user));
        model.addAttribute(SELECTED_WORKOUT, workoutService.getWorkoutById(id));
        model.addAttribute(WORKOUT_REQUEST, new WorkoutRequest());
        return "dashboard";
    }

    @PostMapping("/workouts/{workoutId}/exercises")
    public String addExerciseToWorkout(@PathVariable Long workoutId,
                                       @ModelAttribute AddExerciseRequest request,
                                       @AuthenticationPrincipal User user,
                                       RedirectAttributes redirectAttributes, Locale locale) {
        exerciseService.createExerciseForWorkout(workoutId, request.getExerciseName(), request.getMusclePartId(), request.getSets(), user);
        redirectAttributes.addFlashAttribute(SUCCESS, messageSource.getMessage("success.exercise.added", null, locale));
        return "redirect:/api/v1/dashboard/workouts/" + workoutId;
    }

    @PostMapping("/exercises/{id}/update")
    public String updateExerciseName(@PathVariable Long id,
                                     @RequestParam String name,
                                     HttpSession httpSession,
                                     RedirectAttributes redirectAttributes, Locale locale) {
        exerciseService.updateExerciseName(id, name);
        redirectAttributes.addFlashAttribute(SUCCESS, messageSource.getMessage("success.exercise.renamed", null, locale));
        return "redirect:/api/v1/dashboard/workouts/" + httpSession.getAttribute(CURRENT_WORKOUT_ID);
    }

    @PostMapping("/exercises/{id}/delete")
    public String deleteExercise(@PathVariable Long id,
                                 HttpSession httpSession,
                                 RedirectAttributes redirectAttributes, Locale locale) {
        exerciseService.deleteExercise(id);
        redirectAttributes.addFlashAttribute(SUCCESS, messageSource.getMessage("success.exercise.deleted", null, locale));
        return "redirect:/api/v1/dashboard/workouts/" + httpSession.getAttribute(CURRENT_WORKOUT_ID);
    }

    @PostMapping("/exercises/{id}/add-muscle")
    public String addMuscle(@PathVariable Long id,
                            @RequestParam Long musclePartId,
                            HttpSession httpSession,
                            RedirectAttributes redirectAttributes, Locale locale) {
        exerciseService.addMuscleTarget(id, musclePartId);
        redirectAttributes.addFlashAttribute(SUCCESS, messageSource.getMessage("success.muscle.added", null, locale));
        return "redirect:/api/v1/dashboard/workouts/" + httpSession.getAttribute(CURRENT_WORKOUT_ID);
    }

    @PostMapping("/workout-exercises/{id}/update-muscle")
    public String updateWorkoutExerciseMuscle(@PathVariable Long id,
                                              @RequestParam Long musclePartId,
                                              HttpSession httpSession,
                                              RedirectAttributes redirectAttributes, Locale locale) {
        exerciseService.updateExerciseMuscle(id, musclePartId);
        redirectAttributes.addFlashAttribute(SUCCESS, messageSource.getMessage("success.muscle.updated", null, locale));
        return "redirect:/api/v1/dashboard/workouts/" + httpSession.getAttribute(CURRENT_WORKOUT_ID);

    }

    @PostMapping("/exercises/{id}/remove-muscle")
    public String removeMuscle(@PathVariable Long id,
                               @RequestParam Long musclePartId,
                               HttpSession httpSession,
                               RedirectAttributes redirectAttributes, Locale locale) {
        exerciseService.removeMuscleTarget(id, musclePartId);
        redirectAttributes.addFlashAttribute(SUCCESS, messageSource.getMessage("success.muscle.removed", null, locale));
        return "redirect:/api/v1/dashboard/workouts/" + httpSession.getAttribute(CURRENT_WORKOUT_ID);
    }

    @PostMapping("/workout-exercises/{workoutExerciseId}/sets")
    public String addSetToExercise(@PathVariable Long workoutExerciseId,
                                   @ModelAttribute SetRequest request,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes, Locale locale) {
        setService.createSet(workoutExerciseId, request);
        redirectAttributes.addFlashAttribute(SUCCESS, messageSource.getMessage("success.set.added", null, locale));
        return "redirect:/api/v1/dashboard/workouts/" + session.getAttribute(CURRENT_WORKOUT_ID);
    }

    @PostMapping("/sets/{id}/update")
    public String updateSet(@PathVariable Long id,
                            @RequestParam Integer repetitions,
                            @RequestParam BigDecimal weightKg,
                            @RequestParam Integer restSeconds,
                            HttpSession httpSession,
                            RedirectAttributes redirectAttributes, Locale locale) {
        setService.updateSet(id, repetitions, weightKg, restSeconds);
        redirectAttributes.addFlashAttribute(SUCCESS, messageSource.getMessage("success.set.updated", null, locale));
        return "redirect:/api/v1/dashboard/workouts/" + httpSession.getAttribute(CURRENT_WORKOUT_ID);
    }

    @PostMapping("/sets/{id}/delete")
    public String deleteSet(@PathVariable Long id,
                            HttpSession httpSession,
                            RedirectAttributes redirectAttributes, Locale locale) {
        setService.deleteSet(id);
        redirectAttributes.addFlashAttribute(SUCCESS, messageSource.getMessage("success.set.deleted", null, locale));
        return "redirect:/api/v1/dashboard/workouts/" + httpSession.getAttribute(CURRENT_WORKOUT_ID);
    }
}
